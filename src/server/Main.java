package server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {
    private static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 23456;
    public static final int THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        Database database = new Database();
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            System.out.println("Server started!");
            while (true) {
                Socket socket = server.accept();
                Future<Boolean> future = executor.submit(new ClientSession(socket, database));
                if(future.get()) {
                    executor.shutdown();
                    server.close();
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Server error!");
        } 
    }
}
