package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.beust.jcommander.*;
import com.google.gson.Gson;

public class Main {
    private static Args clientArgs;
    public static final String FILE_REQUEST_PATH_TEST_ENVIRONMENT = System.getProperty("user.dir") + "/src/client/data/";
    public static final String FILE_REQUEST_PATH_LOCAL_ENVIRONMENT = System.getProperty("user.dir") + "\\JSON Database\\task\\src\\client\\data\\";
    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 23456;

    public static void main(String[] args) {
        clientArgs = new Args();
        Main main = new Main();
        JCommander.newBuilder().
                addObject(clientArgs).
                build().
                parse(args);
        main.run();
    }

    public void run() {
        System.out.println("Client started!");
        String send;
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            if (clientArgs.getType() == null) {
                send = Files.readString(Paths.get(FILE_REQUEST_PATH_TEST_ENVIRONMENT + clientArgs.getFileName()));
            } else {
                send = new Gson().toJson(clientArgs);
            }
            output.writeUTF(send);
            System.out.printf("Sent: %s%n", send);
            String received = input.readUTF();
            System.out.printf("Received: %s%n", received);
        } catch (IOException e) {
            System.out.println("Client exception");
        }
    }
}
