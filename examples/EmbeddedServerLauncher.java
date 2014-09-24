package com.example.opensongbook;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Class is used to initialize Jetty servlet container.
 * <p>
 * Using this, ArhiXML can be started just as any regular Java application.
 * Using <code>java -jar ArhiXML.jar</code> will initialize embedded servlet
 * container. After that, user can just point his web browser to the
 * <code>http://localhost:8080/ArhiXML</code> and use ArhiXML as usual.
 * <p>
 * This is primarily used for demonstration purposes of ArhiXML application.
 * Many users don't have the technical knowledge to install his own Servlet
 * Container, configure it and install ArhiXML application. Or maybe user just
 * want to run the ArhiXML application and evaluate it before installing of
 * dedicated Servlet Container.  
 */
public class EmbeddedServerLauncher {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Server server = new Server(PORT);

        ProtectionDomain domain = EmbeddedServerLauncher.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();

        WebAppContext webApp = new WebAppContext();
        webApp.setContextPath("/");
        webApp.setWar(location.toExternalForm());
        webApp.setServer(server);

        server.setHandler(webApp);

        try {
            server.start();
            server.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}