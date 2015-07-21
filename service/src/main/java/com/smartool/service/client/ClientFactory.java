package com.smartool.service.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartool.service.util.JsonUtil;
import com.smartool.service.util.SslUtil;

abstract public class ClientFactory implements Closeable {

    protected final URI baseUrl;
    protected final CloseableHttpClient httpClient;
    protected final ObjectMapper objectMapper;
    protected final AuthorizationProvider authorizationProvider;
    private final HttpClientConnectionManager connectionManager;
    private final boolean closeConnectionManager;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected ClientFactory(Builder<?> builder) {
        // Base url is optional and can be specified on a per-client basis
        baseUrl = builder.baseUrl;
        if (builder.connectionManager != null) {
            connectionManager = builder.connectionManager;
            closeConnectionManager = false;
        } else {
            connectionManager = newPoolingClientConnectionManager(builder);
            closeConnectionManager = true;
        }
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setRetryHandler(new StandardHttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (super.retryRequest(exception, executionCount, context)) {
                    log.warn("###Retrying http request... (executionCount: {}, exception: {})", executionCount, exception.getMessage());
                    return true;
                } else {
                    log.warn("###Cannot retry http request... (exception: {})", exception.getMessage());
                    return false;
                }
            }
        });
        clientBuilder.setConnectionManager(connectionManager);
        // Disable processing of cookies by default...
        clientBuilder.disableCookieManagement();
        if (builder.enableRequestCookies) {
           clientBuilder.addInterceptorFirst(new RequestAddCookies());
        }
        if (builder.requestInterceptor != null) {
            clientBuilder.addInterceptorFirst(builder.requestInterceptor);
        }
        if (builder.userAgent != null) {
            clientBuilder.setUserAgent(builder.userAgent);
        }
        RequestConfig.Builder requestConfig = RequestConfig.custom();
        if (builder.connectTimeout > 0) {
            requestConfig.setConnectTimeout((int) builder.connectTimeout);
            //Don't wait longer than our connectTimeout for a connection to become available in the pool.
            //The default is to wait indefinitely.
            requestConfig.setConnectionRequestTimeout((int) builder.connectTimeout);
        }
        if (builder.readTimeout > 0) {
            requestConfig.setSocketTimeout((int) builder.readTimeout);
        }
        if (builder.cookieSpec != null) {
            requestConfig.setCookieSpec(builder.cookieSpec);
        }
        if (builder.disableRedirects) {
            requestConfig.setRedirectsEnabled(false);
        }
        clientBuilder.setDefaultRequestConfig(requestConfig.build());
        if (builder.proxyHost != null && builder.proxyPort > 0) {
            clientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(new HttpHost(builder.proxyHost, builder.proxyPort, "http")));
        } else {
            // use the standard JRE proxy selector to obtain proxy information
            clientBuilder.setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()));
        }
        this.authorizationProvider = builder.authorizationProvider;
        if (builder.authScope != null && builder.credentials != null) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(builder.authScope, builder.credentials);
            clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }
        objectMapper = builder.objectMapper == null ? JsonUtil.getObjectMapper() : builder.objectMapper;
        httpClient = clientBuilder.build();
    }

    private static PoolingHttpClientConnectionManager newPoolingClientConnectionManager(Builder builder) {
        PoolingHttpClientConnectionManager cm;
        // SSL host name verification can be optionally disabled
        if (builder.disableSslChecks) {
            SSLContext ctx = SslUtil.permissiveContext();
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", ssf).build();
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        } else {
            cm = new PoolingHttpClientConnectionManager();
        }

        if (builder.maxConnections > 0) {
            cm.setMaxTotal(builder.maxConnections);
        }
        if (builder.maxConnectionsPerRoute > 0) {
            cm.setDefaultMaxPerRoute(builder.maxConnectionsPerRoute);
        }
        return cm;
    }

    public URI getBaseUrl() {
        return baseUrl;
    }

    @Override
    public void close() {
        if (closeConnectionManager) {
            log.info("Closing connection manager");
            connectionManager.shutdown();
        }
    }

    protected static abstract class Builder<T extends Builder<?>> {
        protected URI baseUrl;
        protected String userAgent;
        protected long readTimeout;
        protected long connectTimeout;
        protected int maxConnections;
        protected int maxConnectionsPerRoute;
        protected boolean disableSslChecks;
        protected boolean disableRedirects;
        protected boolean enableRequestCookies;
        protected String cookieSpec;
        protected ObjectMapper objectMapper;
        protected AuthorizationProvider authorizationProvider;
        protected AuthScope authScope;
        protected Credentials credentials;
        protected String proxyHost;
        protected int proxyPort;
        protected HttpClientConnectionManager connectionManager;
        protected HttpRequestInterceptor requestInterceptor;

        public T baseUrl(String url) {
            return baseUrl(URI.create(url));
        }

        public T baseUrl(URL url) {
            return baseUrl(url.toString());
        }

        public T baseUrl(URI url) {
            this.baseUrl = checkNotNull(url, "url");
            return builder();
        }

        public T userAgent(String userAgent) {
            this.userAgent = userAgent;
            return builder();
        }

        public T readTimeout(long millis) {
            this.readTimeout = millis;
            return builder();
        }

        public T connectTimeout(long millis) {
            this.connectTimeout = millis;
            return builder();
        }

        public T maxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
            return builder();
        }

        public T maxConnectionsPerRoute(int maxConnectionsPerRoute) {
            this.maxConnectionsPerRoute = maxConnectionsPerRoute;
            return builder();
        }

        public T connectionManager(HttpClientConnectionManager connectionManager) {
            this.connectionManager = connectionManager;
            return builder();
        }

        public T objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = checkNotNull(objectMapper, "objectMapper");
            return builder();
        }

        public T disableSSLChecks(boolean disableSslChecks) {
            this.disableSslChecks = disableSslChecks;
            return builder();
        }

        public T disableRedirects(boolean disableRedirects) {
            this.disableRedirects = disableRedirects;
            return builder();
        }

        public T enableRequestCookies(boolean enableRequestCookies) {
            this.enableRequestCookies = enableRequestCookies;
            return builder();
        }

        public T cookieSpec(String cookieSpec) {
            this.cookieSpec = cookieSpec;
            return builder();
        }

        public T authorizationProvider(AuthorizationProvider authorizationProvider) {
            this.authorizationProvider = authorizationProvider;
            return builder();
        }

        public T authScope(AuthScope authScope) {
            this.authScope = checkNotNull(authScope, "authScope");
            return builder();
        }

        public T credentials(Credentials credentials) {
            this.credentials = checkNotNull(credentials, "credentials");
            return builder();
        }

        public T proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return builder();
        }

        public T proxyPort(int proxyPort) {
            this.proxyPort = proxyPort;
            return builder();
        }

        public T requestInterceptor(HttpRequestInterceptor requestInterceptor) {
            this.requestInterceptor = requestInterceptor;
            return builder();
        }


        @SuppressWarnings("unchecked")
        private T builder() {
            return (T) this;
        }
    }

}
