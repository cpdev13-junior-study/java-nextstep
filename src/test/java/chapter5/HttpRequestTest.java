package chapter5;

import chapter3to6.util.HttpRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class HttpRequestTest {

    private String testDirectory = "./src/test/resources/chapter5/";

    @Test
    void request_GET() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        HttpRequest request = new HttpRequest(in);

        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/user/create", request.getPath());
        Assertions.assertEquals("keep-alive", request.getHeader("Connection"));
        Assertions.assertEquals("javajigi", request.getParameter("userId"));
    }

    @Test
    void request_POST() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        HttpRequest request = new HttpRequest(in);

        Assertions.assertEquals("POST", request.getMethod());
        Assertions.assertEquals("/user/create", request.getPath());
        Assertions.assertEquals("keep-alive", request.getHeader("Connection"));
        Assertions.assertEquals("javajigi", request.getParameter("userId"));
    }
}
