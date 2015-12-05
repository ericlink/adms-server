package controllers;

import core.datapoint.DataPoint;
import core.medicaldevice.MedicalDevice;
import core.module.Module;
import core.module.NetworkType;
import core.report.dayoverday.DayOverDayDataSource;
import helper.io.FileLogHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Future;
import models.User;
import models.UserType;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import play.Play;
import play.libs.Mail;
import play.mvc.Controller;

public class Test extends Controller {
    /*
     * Module x = new Module(); x.setDisplayKey("disp key"); String pin = new
     * Date().toString(); x.setPin(new Date().toString());
     * x.setNetworkType(NetworkType.SKYTEL); x = x.save(); Module z =
     * x.findByPin(pin);
     */

    private static void testDependentPersistence() {
        User u = User.findById(12L);
        u.setHandle("handle-updated");
        u.setType(UserType.OTHER);
        u = u.save();
        Module m = new Module();
        m.setDisplayKey("disp key");
        m.setPin("pin");
        m.setNetworkType(NetworkType.SKYTEL);
        m = m.save();
        MedicalDevice md = new MedicalDevice();
        md.setUser(u);
        md.setSerialNumber("SN");
        md = md.save();
        DataPoint p = new DataPoint();
        p.setOriginated(new Date());
        p.setValue(10);
        p.setTimestamp(new Date());
        p.setModule(m);
        p.setMedicalDevice(md);
        u.addDataPoint(p);
        //
        u.save();
    }

    private static void testEmail() throws EmailException {
        //email
        Email email = new SimpleEmail();
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("username", "password"));
        email.setDebug(false);
        //email.setHostName("smtp.gmail.com");
        email.setFrom("user@gmail.com");
        email.setSubject("TestMail");
        email.setMsg("This is a test mail ... :-)");
        email.addTo("foo@bar.com");
        Future<Boolean> send = Mail.send(email);
        //attachment
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        //write to mailerbean dir then read from there, that is fine
        attachment.setPath("/home/elink/Desktop/image002.png");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Picture of John");
        attachment.setName("John");
        // Create the email message
        MultiPartEmail email2 = new MultiPartEmail();
        email2.addTo("jdoe@somewhere.org", "John Doe");
        email2.setFrom("me@apache.org", "Me");
        email2.setSubject("The picture");
        email2.setMsg("Here is the picture you wanted");
        // add the attachment
        email2.attach(attachment);
        Future<Boolean> send2 = Mail.send(email2);
    }

    private static void testReport() throws Exception {
        User u = new User();
        u.setHandle("REPORT HANDLE");
        u.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar reportDate = Calendar.getInstance(u.getTimeZone());
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        Calendar dataPointCal = Calendar.getInstance(u.getTimeZone());
        dataPointCal.add(Calendar.DATE, -1);
        DataPoint dp = new DataPoint();
        dp.setValue(100);
        dp.setTimestamp(dataPointCal.getTime());
        dataPoints.add(dp);
        dataPointCal.add(Calendar.DATE, -1);
        dp = new DataPoint();
        dp.setValue(30);
        dp.setTimestamp(dataPointCal.getTime());
        dataPoints.add(dp);
        dataPointCal.add(Calendar.DATE, -1);
        dp = new DataPoint();
        dp.setValue(200);
        dp.setTimestamp(dataPointCal.getTime());
        dataPoints.add(dp);
        JRDataSource dataSource = new DayOverDayDataSource(u, reportDate, dataPoints);
        byte[] pdf = JasperRunManager.runReportToPdf(Play.configuration.getProperty("jasper.dayoverday"), null, dataSource);

        String logDir = Play.configuration.getProperty("log.reports");
        FileLogHelper.createInstance().
            log(logDir + "/" + System.currentTimeMillis() + "." + "pdf", pdf);
    }
}
