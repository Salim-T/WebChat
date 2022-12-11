import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.print("Chat Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("\nNew user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }
        }
        catch(IOException ex){
            System.out.println("Error in the server" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Delivers message to all users (broadcasting)
     */

    void broadcast(String message, UserThread excludeUser){
        for(UserThread oneUser : userThreads){
            if(oneUser != excludeUser){
                oneUser.sendMessage(message);
            }
        }
    }

    /**
     * Stores username in a list of the connected client.
     */
    void addUserName(String userName){
        userNames.add(userName);
    }

    /**
     * When a client disconnet, removes his associated username and UserThread
     */

    void removeUser(String userName, UserThread oneUser){
        boolean userNameRemoved = userNames.remove(userName);
        if(userNameRemoved){
            userThreads.remove(oneUser);
            System.out.println("The user " + userName + " is gone");
        }
    }

    /**
     * Return userNames list
     * @return
     */
    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Return true if there are others user connected
     * @return
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    public static void main(String[] args) {
        /*
        if(args.length <1){
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }
         */

        int port = 8080;

        ChatServer server = new ChatServer(port);
        server.execute();
    }
}

