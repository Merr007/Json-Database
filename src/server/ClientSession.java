package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ClientSession implements Callable<Boolean> {
    private Socket socket;
    private Database database;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public ClientSession(Socket socket, Database database) {
        this.socket = socket;
        this.database = database;

    }


    @Override
    public Boolean call() {
        boolean isShutdown = false;
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            JsonObject jsonObject = new Gson().fromJson(input.readUTF(), JsonObject.class);
            String result = "";
            switch (jsonObject.get("type").getAsString()) {
                case "get" -> {
                    readLock.lock();
                    result = database.get(jsonObject.get("key"));
                    readLock.unlock();
                }
                case "set" -> {
                    writeLock.lock();
                    result = database.set(jsonObject.get("key"), jsonObject.get("value"));
                    writeLock.unlock();
                }
                case "delete" -> {
                    writeLock.lock();
                    result = database.delete(jsonObject.get("key"));
                    writeLock.unlock();

                }
                case "exit" -> {
                    result = database.exit();
                    isShutdown = true;
                }
            }
            output.writeUTF(result);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isShutdown;
    }
}
