package ru.otus.java.basic.http.server.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.http.server.exceptions.BadRequestException;
import ru.otus.java.basic.http.server.app.DefaultErrorDto;
import ru.otus.java.basic.http.server.HttpRequest;
import ru.otus.java.basic.http.server.exceptions.NotFoundException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ServerErrorRequestProcessor implements ErrorProcessor {
    @Override
    public void execute(HttpRequest request, Exception e, OutputStream out) throws IOException {

        String response = "";
        if (e instanceof BadRequestException) {
            DefaultErrorDto defaultErrorDto = new DefaultErrorDto("CLIENT_DEFAULT_ERROR", e.getMessage());
            String jsonError = new Gson().toJson(defaultErrorDto);
            response = "HTTP/1.1 400 Bad Request\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    jsonError;
        } else if (e instanceof NotFoundException) {
            DefaultErrorDto defaultErrorDto = new DefaultErrorDto("NOT_FOUND", e.getMessage());
            String jsonError = new Gson().toJson(defaultErrorDto);
            response = "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    jsonError;
        } else {
            DefaultErrorDto defaultErrorDto = new DefaultErrorDto("INTERNAL SERVER ERROR", e.getMessage());
            String jsonError = new Gson().toJson(defaultErrorDto);
            response = "HTTP/1.1 500 Internal Server Error\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    jsonError;
        }

        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
