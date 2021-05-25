package smtp.server;

import java.net.ServerSocket;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        System.out.println("Server started.");
        try (ServerSocket sock = new ServerSocket(10000)) {
            while (true) {
                new Client(sock.accept()).start();
            }
        }
    }
}