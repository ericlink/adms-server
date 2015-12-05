
import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import play.Play;
import play.test.*;
import play.mvc.Http.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }

//    //TODO  move file tests here?
//    // still need to run over network, so ok for intermediate testing
////    @Test
//    public void testApplicationStartupMessage() {
//        Map<String, String> parameters = new HashMap<String, String>();
//        //Response response = POST("/data", parameters, "body");
//        Response response = POST("/ping", "text/html", "body");
//        assertIsOk(response);
//        assertContentMatch("ACK", response);
//    }
    @Test
    public void testPing() {
        Map<String, String> parameters = new HashMap<String, String>();
        Response response = GET("/ping");
        assertIsOk(response);
        assertContentMatch("ACK", response);
        assertContentMatch(Play.id, response);
        assertContentMatch(Play.configuration.getProperty("uom"), response);
    }
}
