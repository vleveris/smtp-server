package smtp.server;

import smtp.server.core.CommandManager;
import smtp.server.core.Reply;
import smtp.server.core.constants.Code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Client extends Thread {
    private final Socket clientSocket;

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        String hostname;
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            hostname = "localhost";
        }
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            Class.forName("org.postgresql.Driver").newInstance();
            Connection sqlConnection = DriverManager.getConnection("jdbc:postgresql://localhost/emaildb", "postgres", "admin");
            String greeting = new Reply(Code.READY.getCode(), hostname + " SMTP Service Ready").toString();
            output.println(greeting);
            System.out.println("Greeting: " + greeting);
            CommandManager cmd = new CommandManager(hostname, sqlConnection);
            while (cmd.isKeepAlive()) {
                String command = input.readLine();
                if (command != null) {
                    System.out.println("Command: " + command);
                    String reply = cmd.execute(command);
                    if (!reply.isEmpty()) {
                        output.println(reply);
                        System.out.println("Reply: " + reply);
                    }
                }
                if (!cmd.isKeepAlive())
                    clientSocket.close();
            }
        } catch (IOException | SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}