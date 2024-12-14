package scrap.http;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

public class HttpResponseWrapper {

    @Getter private final int code;
    @Getter private final String responseBody;
    @Getter private JsonNode jsonNode;
    private final Header[] headers;

    ObjectMapper objectMapper = new ObjectMapper();

    public HttpResponseWrapper(int code, Header[] headers, HttpEntity entity) {

        this.code = code;
        this.headers = headers;
        try {
            this.responseBody = EntityUtils.toString(entity);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("error while parsing entity" + e.getMessage());
        }

        try {
            this.jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            System.out.println("no json involved");
        }

    }

    public Header[] getHeaders() {
        return headers;
    }

    public Header[] getHeaders(String name) {
        return Arrays.stream(headers)
                .filter(header -> name.equalsIgnoreCase(header.getName()))
                .toArray(Header[]::new);
    }


    public void printLog() {

        System.out.println("\n================================ HTTP RESPONSE ================================");
        System.out.println("[Response Status Code]: " + code);

        System.out.println("[Response Headers]");
        Arrays.stream(headers)
                .forEach(header -> System.out.println("   " + header.getName() + ": " + header.getValue()));


        try {
            System.out.println("[Response Body]");
            if (jsonNode != null) {
                String prettyString = jsonNode.toPrettyString();
                System.out.println(prettyString);

                int jsonEndIndex = responseBody.indexOf(prettyString.trim()) + prettyString.trim().length();
                if (jsonEndIndex <= responseBody.length()) {
                    String extraContent = responseBody.substring(jsonEndIndex).trim();
                    if (!extraContent.isEmpty()) {
                        System.out.println("[Extra Content]");
                        System.out.println("   " + extraContent);
                    }
                }

            } else {
                System.out.println("   " + responseBody);
            }

        }catch (Exception e) {
            System.err.println("   Failed to log request body: " + e.getMessage());
        }
        System.out.println("\n================================ END ================================");

    }

}

