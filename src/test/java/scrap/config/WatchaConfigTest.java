package scrap.config;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import scrap.http.HttpRequestExecutor;


class WatchaConfigTest {

    @Test
    public void test() {
        JsonNode execute = HttpRequestExecutor.execute(new WatchaConfig());
    }
}