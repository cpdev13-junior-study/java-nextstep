package chapter5;

import chapter3to6.util.HttpResponse;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class HttpResponseTest {

    private String testDirectory = "./src/test/resources/chapter5/";

    @Test
    void responseForward() throws Exception{
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
        response.forward("/index.html");
    }

    @Test
    void responseRedirect() throws Exception{
        HttpResponse response = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        response.sendRedirect("/index.html");
    }

    @Test
    void responseCookies() throws Exception{
        HttpResponse response = new HttpResponse(createOutputStream("Http_Cookie.txt"));
        response.addHeader("Set-Cookie","logined=true");
        response.sendRedirect("/index.html");
    }

    private OutputStream createOutputStream(String filename) throws Exception {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
