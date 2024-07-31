package ru.otus.java.basic.http.server;

import ru.otus.java.basic.http.server.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, RequestProcessor> processors;
    private final RequestProcessor defaultNotFoundRequestProcessor;
    private final RequestProcessor defaultInternalServerErrorProcessor;

    public Dispatcher() {
        this.processors = new HashMap<>();
        this.processors.put("/", new HelloWorldRequestProcessor());
        this.processors.put("/another", new AnotherHelloWorldRequestProcessor());
        this.processors.put("/calculator", new CalculatorRequestProcessor());

        this.defaultNotFoundRequestProcessor = new DefaultNotFoundRequestProcessor();
        this.defaultInternalServerErrorProcessor = new DefaultInternalServerErrorRequestProcessor();
    }

    public void execute(HttpRequest request, OutputStream out) throws IOException {
        try {
            if (!processors.containsKey(request.getUri())) {
                defaultNotFoundRequestProcessor.execute(request, out);
                return;
            }
            processors.get(request.getUri()).execute(request, out);
        } catch (BadRequestException e) {
            //todo: logger
            e.printStackTrace();
            String response = "HTTP/1.1 400 Bad Request\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<html><body><h1>" + e.getMessage() + "</h1></body></html>";
            out.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            //todo: logger
            e.printStackTrace();
            defaultInternalServerErrorProcessor.execute(request, out);
        }
    }
}
