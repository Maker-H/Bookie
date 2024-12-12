package scrap.config;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import scrap.http.HttpMethod;
import scrap.util.CookieManager;

import java.util.function.Function;

@Getter
public class WatchaConfig extends BaseRequestConfig<JsonNode> {
    private String HTTP_PROTOCOL = "https";
    private String HTTP_HOST = "pedia.watcha.com";
    private String HTTP_ENDPOINT = "/api/home/book?page=3&size=3&is_new_comments=false";
    private int HTTP_PORT = 443;
    private HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        initializeHttpHost();
        initializeHttpMethodWithHeader();
        initializeClientContext();
        initializeJsonNodeResponseHandler();
    }

    private void initializeClientContext() {

        BasicCookieStore cookieStore = new BasicCookieStore();

        BasicClientCookie cookie = new BasicClientCookie("_guinness_session", "FbXYgfw3nfjJJF6qpptiiJ%2B01p7YRWMD6%2BSegKv2OdZ%2Bjl7M3ckIMGuxiY8wLUTc4U%2FZ%2FE95%2BT%2F%2B6vDUsznAT13aWhy%2B8j9AYJdpAhAm1QYPhmw44kSrvX38y7Z%2FWHRBdn1%2BSLgYABtUS3aDGMssSpCzCO%2FnteqWWyFlAJ1BJb6yIL2IF2rpK%2F%2BTh4KyM0bQ5yjZLM%2B1Lg0r4mRSMA%3D%3D--ZHKEovqTj2Sc5U8K--fh4H0EHpQRAiE1EJ7xvuLQ%3D%3D");
        CookieManager.createWatchaCookie(cookie);
        cookieStore.addCookie(cookie);

        HttpClientContext httpClientContext = new HttpClientContext();
        httpClientContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        setClientContext(httpClientContext);

    }

    private void initializeHttpMethodWithHeader() {
        Function<ClassicHttpRequest, ClassicHttpRequest> headerFunction = httpMethod -> {
            httpMethod.addHeader("Referer", "https://pedia.watcha.com");
            httpMethod.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");
            return httpMethod;
        };
        initializeHttpMethod(headerFunction);
    }


}
