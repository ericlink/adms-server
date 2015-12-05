package controllers;

import core.api.data.InboundMessage;
import core.api.data.QueueInboundMessage;
import helper.io.FileLogHelper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Monitoring;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http;

public class Application extends Controller {

    public static void index() {
        try {
            play.Logger.info("index");
            render();
        } catch (Exception ex) {
            Logger.error(ex, null);
        }
    }

    public static void ping() {
        play.Logger.info("ping");

        //touch monitoring row
        Date currentTime = new Date();
        Monitoring mi = null;
        List<Monitoring> m = Monitoring.findAll();
        if (null == m || m.isEmpty()) {
            mi = new Monitoring();
        } else {
            mi = m.get(0);
        }
        mi.setLastPing(currentTime);
        mi = mi.save();
        // all's ok if we made it this far w/o an exception, so return ok token "ACK"
        //String ack = "ACK"+z.getId()+" " + z.getCreated();
        String ack = "ACK";
        String deployDate = Play.configuration.getProperty("deploy.date");
        String uom = Play.configuration.getProperty("uom");
        render(ack, currentTime, deployDate, uom);
    }

    public static void data() throws Exception {
        try {
            play.Logger.info("data");
            // capture receipt timestamp as soon as possible,
            // since it's used in time setting algorithm
            long messageReceivedTimestampUtc = System.currentTimeMillis();

            Map<String, String> headers = getHttpHeaders(messageReceivedTimestampUtc);

            byte[] postData = getHttpPostData();

            String data = handleApplicationDataMessage(postData, headers);

            render(data);
        } catch (IOException ex) {
            Logger.error(ex, null);
        }
    }

    private static byte[] getHttpPostData() throws IOException {
        InputStream is = Http.Request.current().body;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //System.out.println(is.available());
        while (is.available() > 0) {
            int b = is.read();
            baos.write(b);
        }
        byte[] msg = baos.toByteArray();
        String logDir = Play.configuration.getProperty("log.data");
        FileLogHelper.createInstance().
            log(logDir + "/" + System.currentTimeMillis() + "." + "txt", msg);

        return msg;
    }

    private static Map<String, String> getHttpHeaders(long messageReceivedTimestampUtc) {
        Map<String, String> headers = new HashMap<String, String>();
        for (String key : Http.Request.current().headers.keySet()) {
            String value = Http.Request.current().headers.get(key).
                value();
            //logger.log(Level.FINE, "http header name={0},value={1}", new Object[]{key, value});
            //System.out.println(key);
            //System.out.println(value);
            headers.put(key.toUpperCase(), value);
        }
        headers.put("REQUEST_TIME", String.valueOf(messageReceivedTimestampUtc));
        return headers;
    }

    /**
     * Create and queue the InboundMessage and return a safe default response in case of exception.
     * @param msg
     * @param headers
     * @return
     * @throws Exception
     */
    private static String handleApplicationDataMessage(byte[] postData, Map<String, String> headers) throws Exception {
        //pull out meta-data/data for this message and package for processing
        try {
            // default should be 0, get from module (add targetfirmware version/last rep version)
            InboundMessage inboundMessage = createInboundMessage(headers, postData);

            // enqueue message for further processing and
            // return response needed for client application
            QueueInboundMessage q = new QueueInboundMessage();
            String data = q.enqueue(inboundMessage);
            Logger.info("Returning response [%s]", data);
            return data;
        } catch (Exception e) {
            Logger.error(e, "Exception processing document message=");
            // return no time info since something went wrong and we don't have it,
            // also otap may have been exceeded so return zero for the app version'
            return "0:0:0";
        } finally {
            Logger.trace("Done processing message");
        }
    }

    private static InboundMessage createInboundMessage(Map<String, String> headers, byte[] msg) throws IllegalArgumentException {
        Logger.info("Received message");
        String appVer = headers.get("APP_VER");
        String requestTimeInMillis = headers.get("REQUEST_TIME");
        String userAgent = headers.get("USER-AGENT");
        String moduleId = headers.get("IMEI");
        String messageId = headers.get("MSG_ID");
        InboundMessage.MessageType messageType = InboundMessage.MessageType.UNKNOWN;
        if (userAgent != null && userAgent.startsWith("TC65")) {
            messageType = InboundMessage.MessageType.OTAP_NOTIFICATION;
            //User agent format:"TC65/355632002468412 Profile/IMP-NG Configuration/CLDC-1.1"
            moduleId = userAgent.substring(userAgent.indexOf("/") + 1, userAgent.indexOf(" "));
        } else if (messageId == null) {
            messageType = InboundMessage.MessageType.DATA;
        } else if (messageId.equals("APP_STARTUP")) {
            messageType = InboundMessage.MessageType.APP_STARTUP;
        }
        Logger.info("appVer=%s," + "messageType=%s," + "messageId=%s," + "moduleId=%s," + "requestTimeInMillis=%s," + "userAgent=%s", appVer, messageType, messageId, moduleId, requestTimeInMillis, userAgent);
        if (messageType == InboundMessage.MessageType.UNKNOWN) {
            throw new IllegalArgumentException("Unknown message type");
        }
        return new InboundMessage(requestTimeInMillis, moduleId, messageType, appVer, new String(msg));
    }

}
