package util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    void request_GET() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        HttpRequest request = new HttpRequest(in);

        assertEquals(request.getMethod(), "GET");
        assertEquals(request.getPath(), "/user/create");
        assertEquals(request.getHeader("Connection"), "keep-alive");
        assertEquals(request.getParameter("userId"), "javajigi");
    }

    @Test
    void request_POST() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        HttpRequest request = new HttpRequest(in);

        assertEquals(request.getMethod(), "POST");
        assertEquals(request.getPath(), "/user/create");
        assertEquals(request.getHeader("Connection"), "keep-alive");
        assertEquals(request.getParameter("userId"), "javajigi");
    }
}
