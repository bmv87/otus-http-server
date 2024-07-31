package ru.otus.java.basic.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.basic.http.server.app.ItemsRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private final Dispatcher dispatcher;
    private final Logger logger;

    public RequestHandler(Socket connection, Dispatcher dispatcher) throws IOException {
        this.socket = connection;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.dispatcher = dispatcher;
        this.logger = LoggerFactory.getLogger(RequestHandler.class);
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[8192];
            int n = in.read(buffer);
            if (n < 1) {
                return;
            }
            String rawRequest = new String(buffer, 0, n);
            HttpRequest request = new HttpRequest(rawRequest);
            request.printInfo(true);
            dispatcher.execute(request, out);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
