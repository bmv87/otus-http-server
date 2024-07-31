package ru.otus.java.basic.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer implements AutoCloseable {
    private final int port;
    private final ExecutorService clientPool = Executors.newCachedThreadPool();

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            while (!serverSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                clientPool.submit(new RequestHandler(socket));
            }
        } catch (IOException e) {
            //todo: logger
            e.printStackTrace();
            clientPool.shutdown();
        }
    }

    @Override
    public void close() throws Exception {
        clientPool.shutdown();
    }
}
