import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread{

    private PrintWriter writer;
    private Socket socket;

    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        //Console console = System.console();
        //String userName = console.readLine("\nEnter your name: ");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username : ");
        String userName = scanner.next();
        client.setUserName(userName);
        writer.println(userName);

        String text;

        do {
            Scanner scanne = new Scanner(System.in);
            System.out.println(("[" + userName + "]: "));
            text = scanne.next();
            writer.println(text);

        } while (!text.equals("quit"));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }

}
