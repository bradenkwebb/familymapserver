import com.sun.net.httpserver.HttpServer;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
    The core Server class from which everything runs.

	For this server, the only command-line argument is the port number on which
		the server should accept incoming client connections.
*/
public class Server {

    private static final Logger logger = Logger.getLogger("Server");

    /**
     * The maximum number of waiting incoming connections to queue
     */
    private static final int MAX_WAITING_CONNECTIONS = 12;

    // Java provides an HttpServer class that can be used to embed
    // an HTTP server in any Java program.
    // Using the HttpServer class, you can easily make a Java
    // program that can receive incoming HTTP requests, and respond
    // with appropriate HTTP responses.
    // HttpServer is the class that actually implements the HTTP network
    // protocol (be glad you don't have to).
    // The "server" field contains the HttpServer instance for this program,
    // which is initialized in the "run" method below.
    private HttpServer server;

    /**
     * Initializes and runs the server.
     *
     * @param portNumber the port number on which the server should accept incoming client connections
     */
    private void run(String portNumber) {

        logger.info("Initializing HTTP Server");

        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        // This line is necessary, but its function is unimportant for our purposes.
        server.setExecutor(null);

        logger.info("Creating contexts");

        server.createContext("/", new FileHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/person", new PersonHandler());

        logger.info("Starting server");

        server.start();

        logger.info("Server started");
    }

    /**
     * The main method for the server program.
     *
     * @param args should contain one command-line argument, which is the port number on which the server should
     *             accept incoming client connections
     */
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
