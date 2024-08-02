package ru.otus.java.basic.http.server;

public class Application {
    public static void main(String[] args) throws Exception {
        //run multiple curl requests asynchronously in Bash: curl http://localhost:8189 & curl http://localhost:8189 & curl http://localhost:8189
        try (var server = new HttpServer(8189)) {
            server.start();
        }
    }
}
