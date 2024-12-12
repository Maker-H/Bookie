package scrap.http;

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
    private final Header[] headers;

    public HttpResponseWrapper(int code, Header[] headers, HttpEntity entity) {
        this.code = code;
        this.headers = headers;
        try {
            this.responseBody = EntityUtils.toString(entity);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("error while parsing entity" + e.getMessage());
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
        int statusCode = code;
        System.out.println("================================ HTTP RESPONSE ================================");
        System.out.println("[Response Status Code]: " + statusCode);

        // 로그 출력: 헤더 정보
        System.out.println("[Response Headers]");
        Arrays.stream(headers)
                .forEach(header -> System.out.println("   " + header.getName() + ": " + header.getValue()));

        // 로그 출력: 본문 정보
        try {
            System.out.println("[Response Body]");
            System.out.println("   " + responseBody);
        } catch (Exception e) {
            System.err.println("[Response Body]: Failed to retrieve body - " + e.getMessage());
        }
        System.out.println("===============================================================================");
    }

}

