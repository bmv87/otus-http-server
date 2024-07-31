package ru.otus.java.basic.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private final Dispatcher dispatcher;

    public RequestHandler(Socket connection) throws IOException {
        this.socket = connection;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.dispatcher = new Dispatcher();

    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[8192];
            int n = in.read(buffer);
            String rawRequest = new String(buffer, 0, n);
            HttpRequest request = new HttpRequest(rawRequest);
            request.printInfo(true);
            dispatcher.execute(request, out);
        } catch (IOException e) {
            //todo: logger
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                //todo: logger
                e.printStackTrace();
            }
        }
    }
}
