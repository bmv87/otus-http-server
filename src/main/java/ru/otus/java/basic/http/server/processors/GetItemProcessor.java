package ru.otus.java.basic.http.server.processors;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import ru.otus.java.basic.http.server.HttpRequest;
import ru.otus.java.basic.http.server.app.ItemsRepository;
import ru.otus.java.basic.http.server.exceptions.BadRequestException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GetItemProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;

    public GetItemProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        var params = request.getParameters();
        if (params.isEmpty() || !params.containsKey("id")) {
            throw new BadRequestException("Не указан id.");
        }
        var item = itemsRepository.get(Long.parseLong(params.get("id")));
        Gson gson = new Gson();
        String itemsJson = gson.toJson(item);
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "\r\n" +
                itemsJson;
        out.write(response.getBytes(StandardCharsets.UTF_8));

    }
}