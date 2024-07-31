package ru.otus.java.basic.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.basic.http.server.app.ItemsRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer implements AutoCloseable {
    private final int port;
    private final ExecutorService clientPool = Executors.newCachedThreadPool();
    private final Logger logger;

    public HttpServer(int port) {
        this.port = port;
        this.logger = LoggerFactory.getLogger(HttpServer.class);
    }

    public void start() {
       var dispatcher = new Dispatcher(new ItemsRepository()) ;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту: " + port);
            while (!serverSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                clientPool.submit(new RequestHandler(socket, dispatcher));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            clientPool.shutdown();
        }
    }

    @Override
    public void close() throws Exception {
        clientPool.shutdown();
    }
}
