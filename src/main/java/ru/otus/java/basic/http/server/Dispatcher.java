package ru.otus.java.basic.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.basic.http.server.app.ItemsRepository;
import ru.otus.java.basic.http.server.exceptions.NotFoundException;
import ru.otus.java.basic.http.server.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, RequestProcessor> processors;
    private final ErrorProcessor serverErrorProcessor;
    private ItemsRepository itemsRepository;
    private final Logger logger;

    public Dispatcher(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
        this.processors = new HashMap<>();
        this.processors.put("GET /", new HelloWorldRequestProcessor());
        this.processors.put("GET /another", new AnotherHelloWorldRequestProcessor());
        this.processors.put("GET /calculator", new CalculatorRequestProcessor());
        this.processors.put("GET /items", new GetAllItemsProcessor(itemsRepository));
        this.processors.put("GET /item", new GetItemProcessor(itemsRepository));
        this.processors.put("POST /items", new CreateNewItemProcessor(itemsRepository));
        this.processors.put("DELETE /items", new DeleteItemProcessor(itemsRepository));

        this.serverErrorProcessor = new ServerErrorRequestProcessor();
        this.logger = LoggerFactory.getLogger(Dispatcher.class);
    }

    public void execute(HttpRequest request, OutputStream out) throws IOException {
        try {
            if (!processors.containsKey(request.getRoutingKey())) {
                throw new NotFoundException("Route not exists");
            }
            processors.get(request.getRoutingKey()).execute(request, out);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            serverErrorProcessor.execute(request, e, out);
        }
    }
}
