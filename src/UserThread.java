import java.io.*;
import java.net.Socket;

public class UserThread extends Thread{
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server){
        this.server = server;
        this.socket = socket;
    }

    public void run(){
        try{
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printConnectedUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected : " + userName;
            server.broadcast(serverMessage,this);

            String clientMessage;
            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "] : " + clientMessage;
                server.broadcast(serverMessage,this);
            }while(!clientMessage.equals("quit"));

            server.removeUser(userName,this);
            socket.close();

            serverMessage = userName + "has left the chat";
            server.broadcast(serverMessage, this);


        }catch(IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sends a list of differents online users to the new connected user
     */
    void printConnectedUsers() {
        if (server.hasUsers())
            writer.println("Connected users : " + server.getUserNames());
        else {
            writer.println(("You're the only user :("));
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
