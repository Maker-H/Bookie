package scrap.http;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

public class HttpClientProvider {

    private static final Timeout CONNECT_TIMEOUT = Timeout.ofSeconds(30);
    private static final Timeout SOCKET_TIMEOUT = Timeout.ofMinutes(1);
    private static final TimeValue CONNECTION_TTL = TimeValue.ofMinutes(10);
    private static final TimeValue CONNECTION_INACTIVITY_VALIDATE = TimeValue.ofSeconds(30);

    private static final PoolConcurrencyPolicy CONCURRENCY_POLICY = PoolConcurrencyPolicy.STRICT;
    private static final PoolReusePolicy REUSE_POLICY = PoolReusePolicy.LIFO;
    private static final int MAX_CONN_PERROUTE = 10; // 특정 호스트(Route)에 대한 최대 연결 수
    private static final int MAX_CONN_TOTAL = 50; // 전체 연결 수

    private static final CloseableHttpClient HTTP_CLIENT;

    static {
        // 커넥션에 대한 설정
        PoolingHttpClientConnectionManagerBuilder httpClientConnectionBuilder = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(createSSLConfig())
                .setDefaultConnectionConfig(createConnectionConfig())
                .setDefaultSocketConfig(createSocketConfig());

        // 커넥션 풀에 대한 설정
        PoolingHttpClientConnectionManager poolingConnectionManager = httpClientConnectionBuilder
                .setPoolConcurrencyPolicy(CONCURRENCY_POLICY)
                .setConnPoolPolicy(REUSE_POLICY)
                .setMaxConnPerRoute(MAX_CONN_PERROUTE)
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .build();

        HTTP_CLIENT = HttpClients.custom()
                .setConnectionManager(poolingConnectionManager)
                .build();
    }

    static CloseableHttpClient getHttpClient() {
        return HttpClientProvider.HTTP_CLIENT;
    }

    private static ConnectionConfig createConnectionConfig() {
        return ConnectionConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)

                // 서버에 연결할 수 없으면 이 시간 이후 예외 발생
                .setConnectTimeout(CONNECT_TIMEOUT)

                // 이 시간이 지나면 연결은 커넥션 풀에서 더 이상 사용되지 않고 폐기됨.
                .setTimeToLive(CONNECTION_TTL)

                // 일정 시간이 지난 이후 커넥션 유효성 검사
                .setValidateAfterInactivity(CONNECTION_INACTIVITY_VALIDATE)
                .build();
    }

    private static SocketConfig createSocketConfig() {
        return SocketConfig.custom()
                // 서버로부터 응답을 기다리는 최대 시간을 지정 - 1분간 응답이 없으면 타임아웃 발생
                .setSoTimeout(SOCKET_TIMEOUT)
                .build();
    }

    private static SSLConnectionSocketFactory createSSLConfig() {
        return SSLConnectionSocketFactoryBuilder.create()
                //시스템 기본 SSL/TLS 설정을 사용하여 보안 연결을 생성 (시스템에 설치된 인증서 사용)
                .setSslContext(SSLContexts.createSystemDefault())
                .build();
    }
}
