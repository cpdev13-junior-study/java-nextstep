import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;


public class WebServerLauncher {

    private static final Logger logger = LoggerFactory.getLogger(WebServerLauncher.class);
//    public static void main(String[] args) throws Exception {
//        String webappDirLocation = "webapp/";
//        Tomcat tomcat = new Tomcat();
//        String webPort = System.getenv("PORT");
//        if(webPort == null || webPort.isEmpty()) {
//            webPort = "8080";
//        }
//
//        tomcat.setPort(Integer.valueOf(webPort));
//        Connector connector = tomcat.getConnector();
//        connector.setURIEncoding("UTF-8");
//        Context ctx = tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
//
//        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());
//        tomcat.start();
//        tomcat.getServer().await();
//    }

    public static void main(String[] args) throws ServletException, LifecycleException {
        String webappPath = "webapp/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        StandardContext context = (StandardContext) tomcat.addWebapp("/",
                new File(webappPath).getAbsolutePath());
        File additionWebInfClasses = new File("out/production/classes");
        StandardRoot resources = new StandardRoot(context);
        resources.addPreResources(
                new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }
}
