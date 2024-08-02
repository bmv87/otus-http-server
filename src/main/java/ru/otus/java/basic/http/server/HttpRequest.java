package ru.otus.java.basic.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String body;
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parse();
    }

    private void parse() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        this.parameters = new HashMap<>();
        this.headers = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                this.parameters.put(keyValue[0], keyValue[1]);
            }
        }
        if (method == HttpMethod.POST) {
            this.body = rawRequest.substring(
                    rawRequest.indexOf("\r\n\r\n") + 4
            );
        }

        int startHeadersIndex = rawRequest.indexOf('\n') + 1;
        int endHeadersIndex = rawRequest.indexOf("\r\n\r\n", startHeadersIndex);

        var rawHeaders = rawRequest.substring(startHeadersIndex, endHeadersIndex).split("\r\n");
        for (String header : rawHeaders) {
            var keyValue = header.split(": ", 2);
            headers.put(keyValue[0], keyValue[1]);
        }
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public void printInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator())
                .append("uri: ").append(uri).append(System.lineSeparator())
                .append("method: ").append(method).append(System.lineSeparator())
                .append("body: ").append(body).append(System.lineSeparator());

        logger.info(sb.toString());
        logger.debug("{}{}", System.lineSeparator(), rawRequest);
    }
}
