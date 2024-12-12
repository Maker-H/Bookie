package scrap.util;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.Header;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieManager {

    public static Map<String, Cookie> extractCookiesToMap(BasicCookieStore cookieStore) {
        List<Cookie> cookies = cookieStore.getCookies();
        Map<String, Cookie> cookieMap = new HashMap<>();

        for (Cookie c : cookies) {
            cookieMap.put(c.getName(), c);
        }

        return cookieMap;
    }

    public static Cookie createHomeTaxCookie(BasicClientCookie cookie) {
        cookie.setDomain("www.hometax.go.kr");
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie createWatchaCookie(BasicClientCookie cookie) {
        cookie.setDomain("pedia.watcha.com");
        cookie.setPath("/");
        return cookie;
    }

    public static String findCookieValue(BasicCookieStore cookieStore, String cookieName) {
        return cookieStore.getCookies().stream()
                .filter(cookie -> cookie.getName().equals(cookieName))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }

    public static BasicCookieStore createCookieStoreFromHttpHeaders(Header[] headers) {

        BasicCookieStore basicCookieStore = new BasicCookieStore();

        Arrays.stream(headers)
                .filter(header -> header.getName().equals("Set-Cookie"))
                .map(header -> CookieManager.convertCookieHeaderToCookie(header.getValue()))
                .forEach(basicCookieStore::addCookie);

        return basicCookieStore;
    }

    public static BasicClientCookie convertCookieHeaderToCookie(String cookieHeader) {

        try {
            String[] cookieParts = cookieHeader.split(";");
            String[] nameValue = cookieParts[0].split("=", 2);

            if (nameValue.length < 2) {
                return null;
            }

            String name = nameValue[0].trim();
            String value = nameValue[1].trim();

            // BasicClientCookie 생성
            BasicClientCookie cookie = new BasicClientCookie(name, value);

            for (int i = 1; i < cookieParts.length; i++) {
                String[] attribute = cookieParts[i].split("=", 2);

                String attrName = attribute[0].trim().toLowerCase();
                String attrValue = (attribute.length > 1) ? attribute[1].trim() : null;

                switch (attrName) {
                    case "domain":
                        cookie.setDomain(attrValue);
                        break;
                    case "path":
                        cookie.setPath(attrValue);
                        break;
                    case "secure":
                        cookie.setSecure(true);
                        break;
                    case "httponly":
                        // HttpOnly는 BasicClientCookie에 직접 설정되지 않음
                        break;
                    default:
                        break;
                }
            }

            return cookie;
        } catch (Exception e) {
            return null;
        }
    }
}
