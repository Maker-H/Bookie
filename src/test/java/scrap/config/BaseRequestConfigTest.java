package scrap.config;

import org.junit.jupiter.api.Test;
import scrap.http.HttpRequestExecutor;
import scrap.response.BookDetail;

class BaseRequestConfigTest {


    @Test
    public void bookTest() {
        BookDetail book = HttpRequestExecutor.execute(new WatchaBookConfig("byLKj8M"));

        System.out.println(book);
    }

    @Test
    public void contentTest() {
        HttpRequestExecutor.execute(new WatchaCommentConfig("byLKj8M", 1, 10));
    }


    @Test
    public void deckTest() {
        HttpRequestExecutor.execute(new WatchaDeckConfig("byLKj8M", 1, 10));
    }

    @Test
    public void externalTest() {
        String response = HttpRequestExecutor.execute(new WatchaExternalUrlConfig("aHR0cHM6Ly93d3cueWVzMjQuY29tL1Byb2R1Y3QvR29vZHMvMTA1NTI2MDQ3"));
        System.out.println(response + "!!!!!!!!!!!!!!!!!");
    }

}