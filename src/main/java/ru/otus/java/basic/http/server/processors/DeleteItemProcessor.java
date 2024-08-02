package ru.otus.java.basic.http.server.processors;

import com.google.gson.JsonParseException;
import ru.otus.java.basic.http.server.HttpRequest;
import ru.otus.java.basic.http.server.app.ItemsRepository;
import ru.otus.java.basic.http.server.exceptions.BadRequestException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DeleteItemProcessor implements RequestProcessor {
    private final ItemsRepository itemsRepository;

    public DeleteItemProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        try {
            var params = request.getParameters();
            if (!params.containsKey("id")) {
                throw new BadRequestException("Не указан id.");
            }
            itemsRepository.delete(Long.parseLong(params.get("id")));

            String response = "HTTP/1.1 204 Deleted\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n";
            out.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (JsonParseException e) {
            throw new BadRequestException("Некорректный формат входящего JSON объекта");
        }
    }
}