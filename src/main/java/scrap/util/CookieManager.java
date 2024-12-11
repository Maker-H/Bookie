package scrap.util;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.Header;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieManager {

    public static Map<String, Cookie> getCookieMap(BasicCookieStore cookieStore) {
        List<Cookie> cookies = cookieStore.getCookies();
        Map<String, Cookie> cookieMap = new HashMap<>();

        for (Cookie c : cookies) {
            cookieMap.put(c.getName(), c);
        }

        return cookieMap;
    }

    public static Cookie addHomeTaxCookie(BasicClientCookie cookie) {
        cookie.setDomain("www.hometax.go.kr");
        cookie.setPath("/");
        return cookie;
    }

    public static String getCookieValue(BasicCookieStore cookieStore, String cookieName) {
        return cookieStore.getCookies().stream()
                .filter(cookie -> cookie.getName().equals(cookieName))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }

    public static BasicCookieStore parseHeaders(Header[] headers) {

        BasicCookieStore basicCookieStore = new BasicCookieStore();

        for (Header header : headers) {
            if (!header.getName().equals("Set-Cookie")) {
                throw new IllegalStateException("cookie manager got wrong header, have to be 'Set-Cookie' header");
            }

            String cookieValue = header.getValue();

            BasicClientCookie cookie = CookieManager.parseString(cookieValue);
            basicCookieStore.addCookie(cookie);
        }

        return basicCookieStore;
    }

    public static BasicClientCookie parseString(String cookieHeader) {

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
