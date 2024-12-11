package scrap.http;

import lombok.Getter;

@Getter
public enum HttpMethod {
    GET("HttpGet"), POST("HttpPost");

    private String simpleName;
    private String className;

    private HttpMethod(String simpleName) {
        String pkg = "org.apache.hc.client5.http.classic.methods.";

        this.simpleName = simpleName;
        this.className = pkg + simpleName;
    }
}
