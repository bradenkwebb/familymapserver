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

    // The maximum number of waiting incoming connections to queue.
    // While this value is necessary, for our purposes it is unimportant.
    // Take CS 460 for a deeper understanding of what it means.
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

        // Create and install the HTTP handler for the "/games/list" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/games/list" URL path, it will forward the request to ListGamesHandler
        // for processing.
//        server.createContext("/games/list", new ListGamesHandler());

        // Create and install the HTTP handler for the "/routes/claim" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/routes/claim" URL path, it will forward the request to ClaimRouteHandler
        // for processing.
//        server.createContext("/routes/claim", new ClaimRouteHandler());

        // Create and install the "default" (or "file") HTTP handler.
        // All requests that do not match the other handler URLs
        // will be passed to this handle.
        // These are requests to download a file from the server
        // (e.g., web site files)
        server.createContext("/", new FileHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("person", new PersonHandler());

        logger.info("Starting server");

        server.start();

        logger.info("Server started");

        /* MY STUFF FOR TESTING
        Database db = new Database();

        Connection connection = null;
        try(Connection c = db.getConnection()){
            connection = c;

            // Start a transaction
            connection.setAutoCommit(false);
            EventDAO eventDAO = new EventDAO(c);
            eventDAO.clear();
            eventDAO.insert(new Event("1", "bkwebb23", "2", 42.24f, 23.23f,
                            "Slovakia", "Nitra", "Mission", 2018));

            UserDAO userDAO = new UserDAO(c);
            userDAO.clear();
            userDAO.insert(new User("bkwebb23", "mypass", "bw@gmail.com",
                    "Braden", "Webb", "m", "2"));

            connection.commit();

            Event myEvent = eventDAO.find("1");
            System.out.println(myEvent.getAssociatedUsername());

            User myUser = userDAO.find("bkwebb23");
            System.out.println(myUser.getFirstName() + " " + myUser.getLastName());

        }
        catch(SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (java.sql.SQLException ex1) {
                System.out.println("This is the line that threw the exception!");
                System.out.println(ex1.getMessage());
            }

            ex.printStackTrace();
            System.out.println(ex.getMessage());

        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        */
    }

    // "main" method for the server program
    // "args" should contain one command-line argument, which is the port number
    // on which the server should accept incoming client connections.
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
