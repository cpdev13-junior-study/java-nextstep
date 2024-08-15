package chapter3to6.webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import chapter3to6.db.DataBase;
import chapter3to6.model.User;
import chapter3to6.util.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            process(httpRequest, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void process(HttpRequest httpRequest, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String method = httpRequest.getMethod();
        String path = getDefaultPath(httpRequest.getPath());
        if (method.equals("GET")) {
            byte[] body = null;
            if (path.contains("/user/list")) {
                String isLogined = httpRequest.getCookie("logined");
                if (isLogined != null && isLogined.equals("true")) {
                    String html = createUserListHtml();
                    body = html.getBytes();
                    response200Header(dos, body.length, httpRequest);
                    responseBody(dos, body);
                } else {
                    response302Header(dos, httpRequest, "http://localhost:8080/index.html");
                }
            } else {
                body = Files.readAllBytes(new File("./src/main/java/chapter3to6/webapp" + path).toPath());
                String accept = httpRequest.getHeader("Accept");
                if (accept.contains("text/css")) {
                    response200HeaderWithCss(dos, body.length);
                } else {
                    response200Header(dos, body.length, httpRequest);
                }
                responseBody(dos, body);
            }

        }
        if (method.equals("POST")) {
            if (path.contains("/user/create")) {
                User user = new User(
                        httpRequest.getParameter("userId"),
                        httpRequest.getParameter("password"),
                        httpRequest.getParameter("name"),
                        httpRequest.getParameter("email")
                );
                log.info("회원가입 결과 = {}", user);
                DataBase.addUser(user);
                response302Header(dos, httpRequest, "http://localhost:8080/index.html");
            }
            if (path.contains("/user/login")) {
                String userId = httpRequest.getParameter("userId");
                String password = httpRequest.getParameter("password");

                User findUser = DataBase.findUserById(userId);
                String redirectUrl = "http://localhost:8080/user/login_failed.html";
                if (findUser != null && findUser.getPassword().equals(password)) {
                    httpRequest.addCookie("logined", "true");
                    redirectUrl = "http://localhost:8080/index.html";

                } else {
                    httpRequest.addCookie("logined", "false");
                }
                response302Header(dos, httpRequest, redirectUrl);
            }
        }
    }

    private String getDefaultPath(String path) {
        return path.equals("/") ? "/index.html" : path;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, HttpRequest request) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            setCookie(dos, request);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200HeaderWithCss(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, HttpRequest request, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl + " \r\n");
            setCookie(dos, request);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void setCookie(DataOutputStream dos, HttpRequest request) throws IOException {
        for (Map.Entry<String, String> entry : request.getCookies().entrySet()) {
            dos.writeBytes("Set-Cookie: " + entry.getKey() + "=" + entry.getValue() + "; Path=/ \r\n");
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String createUserListHtml() {
        StringBuilder sb = new StringBuilder();
        List<User> userList = DataBase.findAll().stream().toList();
        sb.append(getBefore());
        for (int i = 0; i < userList.size(); i++) {
            sb.append(addInfo(userList.get(i), i));
        }
        sb.append(getAfter());
        return sb.toString();
    }

    private String getBefore() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"kr\">\n" +
                "<head>\n" +
                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>SLiPP Java Web Programming</title>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n" +
                "    <link href=\"../css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "    <!--[if lt IE 9]>\n" +
                "    <script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n" +
                "    <![endif]-->\n" +
                "    <link href=\"../css/styles.css\" rel=\"stylesheet\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<nav class=\"navbar navbar-fixed-top header\">\n" +
                "    <div class=\"col-md-12\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "\n" +
                "            <a href=\"../index.html\" class=\"navbar-brand\">SLiPP</a>\n" +
                "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n" +
                "                <i class=\"glyphicon glyphicon-search\"></i>\n" +
                "            </button>\n" +
                "\n" +
                "        </div>\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n" +
                "            <form class=\"navbar-form pull-left\">\n" +
                "                <div class=\"input-group\" style=\"max-width:470px;\">\n" +
                "                    <input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n" +
                "                    <div class=\"input-group-btn\">\n" +
                "                        <button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </form>\n" +
                "            <ul class=\"nav navbar-nav navbar-right\">\n" +
                "                <li>\n" +
                "                    <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n" +
                "                    <ul class=\"dropdown-menu\">\n" +
                "                        <li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n" +
                "                        <li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n" +
                "                    </ul>\n" +
                "                </li>\n" +
                "                <li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</nav>\n" +
                "<div class=\"navbar navbar-default\" id=\"subnav\">\n" +
                "    <div class=\"col-md-12\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "            <a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n" +
                "            <ul class=\"nav dropdown-menu\">\n" +
                "                <li><a href=\"profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>\n" +
                "                <li class=\"nav-divider\"></li>\n" +
                "                <li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n" +
                "            </ul>\n" +
                "            \n" +
                "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n" +
                "            \t<span class=\"sr-only\">Toggle navigation</span>\n" +
                "            \t<span class=\"icon-bar\"></span>\n" +
                "            \t<span class=\"icon-bar\"></span>\n" +
                "            \t<span class=\"icon-bar\"></span>\n" +
                "            </button>            \n" +
                "        </div>\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n" +
                "            <ul class=\"nav navbar-nav navbar-right\">\n" +
                "                <li class=\"active\"><a href=\"../index.html\">Posts</a></li>\n" +
                "                <li><a href=\"login.html\" role=\"button\">로그인</a></li>\n" +
                "                <li><a href=\"form.html\" role=\"button\">회원가입</a></li>\n" +
                "                <li><a href=\"#\" role=\"button\">로그아웃</a></li>\n" +
                "                <li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"container\" id=\"main\">\n" +
                "   <div class=\"col-md-10 col-md-offset-1\">\n" +
                "      <div class=\"panel panel-default\">\n" +
                "          <table class=\"table table-hover\">\n" +
                "              <thead>\n" +
                "                <tr>\n" +
                "                    <th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>\n" +
                "                </tr>\n" +
                "              </thead>\n" +
                "              <tbody>";
    }

    private String addInfo(User user, int num) {
        return String.format("<tr>\n" +
                "   <th scope=\"row\">%s</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                "</tr> \n", num + 1, user.getUserId(), user.getName(), user.getEmail()
        );
    }

    private String getAfter() {
        return " </tbody>\n" +
                "          </table>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<!-- script references -->\n" +
                "<script src=\"../js/jquery-2.2.0.min.js\"></script>\n" +
                "<script src=\"../js/bootstrap.min.js\"></script>\n" +
                "<script src=\"../js/scripts.js\"></script>\n" +
                "\t</body>\n" +
                "</html>";
    }
}
