package com.smartool.service.client;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Formatter;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.OutputSupplier;
import com.smartool.service.util.HttpUtil;

public abstract class Client {
    protected final HttpClient httpClient;
    protected final HttpContext httpContext;
    protected final ObjectMapper objectMapper;
    protected URI baseUrl;
    protected AuthorizationProvider authorizationProvider;
    private String apiToken;

    private static final String X_SERVICE_API_TOKEN = "X-Service-Api-Token";
    public static final String TRACKING_ID = "TrackingID";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected Client(ClientFactory factory) {
        this(factory, factory.baseUrl);
    }

    protected Client(ClientFactory factory, URI baseUrl) {
        httpClient = factory.httpClient;
        objectMapper = factory.objectMapper;
        authorizationProvider = factory.authorizationProvider;
        this.baseUrl = baseUrl;
        httpContext = new BasicHttpContext();
    }

    protected void setBaseUrl(URI baseUrl) {
        this.baseUrl = checkNotNull(baseUrl, "baseUrl");
    }

    public URI getBaseUrl() {
        return baseUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String sharedToken) {
        this.apiToken = sharedToken;
    }

    public void setAuthorization(final String authorization) {
        checkNotNull(authorization, "authorization");
        setAuthorizationProvider(new AuthorizationProvider() {
            @Override
            public String getAuthorization() {
                return authorization;
            }

            @Override
            public boolean refreshToken() {
                return false;
            }
        });
    }

    public String getAuthorization() {
        return authorizationProvider != null ? authorizationProvider.getAuthorization() : null;
    }

    public void setAuthorizationProvider(AuthorizationProvider authorizationProvider) {
        this.authorizationProvider = authorizationProvider;
    }

    public Request<HttpGet> get(Object... paths) {
        return newRequest(new HttpGet(), paths);
    }

    public Request<HttpGet> get(URI uri, Object... paths) {
        return newRequest(new HttpGet(), uri, paths);
    }

    public Request<HttpHead> head(Object... paths) {
        return newRequest(new HttpHead(), paths);
    }

    public Request<HttpHead> head(URI uri, Object... paths) {
        return newRequest(new HttpHead(), uri, paths);
    }

    public Request<HttpPost> post(Object... paths) {
        return newRequest(new HttpPost(), paths);
    }

    public Request<HttpPost> post(URI uri, Object... paths) {
        return newRequest(new HttpPost(), uri, paths);
    }

    public Request<HttpPut> put(Object... paths) {
        return newRequest(new HttpPut(), paths);
    }

    public Request<HttpPut> put(URI uri, Object... paths) {
        return newRequest(new HttpPut(), uri, paths);
    }

    public Request<HttpPatch> patch(Object... paths) {
        return newRequest(new HttpPatch(), paths);
    }

    public Request<HttpPatch> patch(URI uri, Object... paths) {
        return newRequest(new HttpPatch(), uri, paths);
    }

    public Request<HttpDelete> delete(Object... paths) {
        return newRequest(new HttpDelete(), paths);
    }

    public Request<HttpDelete> delete(URI uri, Object... paths) {
        return newRequest(new HttpDelete(), uri, paths);
    }

    protected <T extends HttpRequestBase> Request<T> newRequest(T requestBase, Object... paths) {
        checkState(baseUrl != null, "baseUrl is not set");
        return newRequest(requestBase, baseUrl, paths);
    }

    protected <T extends HttpRequestBase> Request<T> newRequest(T requestBase, URI baseUrl, Object... paths) {
        checkNotNull(requestBase, "requestBase");
        checkNotNull(baseUrl, "baseUrl");
        return new Request<>(requestBase, this, baseUrl).path(paths);
    }

    protected HttpEntity jsonEntity(Object value) {
        checkNotNull(value, "value");
        try {
            return new ByteArrayEntity(objectMapper.writeValueAsBytes(value), ContentType.APPLICATION_JSON);
        } catch (IOException e) {
            throw new ClientException("Error serializing JSON payload", e);
        }
    }

    protected HttpEntity urlEncodedFormEntity(Iterable<? extends NameValuePair> params) {
        checkNotNull(params, "params");
        return new UrlEncodedFormEntity(params, Charsets.UTF_8);
    }
    
    protected HttpResponse execute(final HttpUriRequest request) {
        checkNotNull(request, "request");
        return execute(request, new ResponseHandler<HttpResponse>() {
            @Override
            public HttpResponse handleResponse(HttpResponse response) {
                checkResponse(request, response);
                return response;
            }
        });
    }

    protected long execute(final HttpUriRequest request, final OutputStream out) {
        checkNotNull(out, "out");
        return execute(request, new ResponseHandler<Long>() {
            @Override
            public Long handleResponse(HttpResponse response) throws IOException {
                checkResponse(request, response);
                return ByteStreams.copy(response.getEntity().getContent(), out);
            }
        });
    }

    protected <T> T execute(HttpUriRequest req, TypeReference<T> typeReference) {
        return execute(req, typeFactory().constructType(typeReference));
    }

    protected <T> T execute(HttpUriRequest req, Class<T> type) {
        return execute(req, typeFactory().constructType(type));
    }

    protected <T> T execute(final HttpUriRequest request, final JavaType type) {
        checkNotNull(type, "type");
        return execute(request, new ResponseHandler<T>() {
            @Override
            public T handleResponse(HttpResponse response) throws IOException {
                checkResponse(request, response);
                if (response.getEntity() == null) {
                    throw new ClientException("Unexpected empty response body: response = " + response.getStatusLine());
                }
                InputStream is = new BufferedInputStream(response.getEntity().getContent());
                return objectMapper.readValue(is, type);
            }
        });
    }

    protected <T> T readJsonEntity(HttpResponse response, Class<T> type) throws IOException {
        HttpEntity entity = response.getEntity();
        return entity != null && isApplicationJson(entity) ? objectMapper.readValue(entity.getContent(), type) : null;
    }

    protected <T> T readJsonEntity(HttpResponse response, TypeReference<T> typeRef) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null && isApplicationJson(entity)) {
            return objectMapper.readValue(entity.getContent(), typeRef);
        }
        return null;
    }

    protected <T> T execute(HttpUriRequest request, ResponseHandler<T> handler) {
        checkNotNull(request, "request");
        checkNotNull(handler, "handler");
        prepareRequest(request);
        HttpResponse response;
        try {
            // clear out any prior redirect history
            httpContext.removeAttribute(DefaultRedirectStrategy.REDIRECT_LOCATIONS);
            response = httpClient.execute(request, httpContext);
        } catch (IOException e) {
            throw new ClientException("Error executing request (url = " + request.getURI() + ")", e);
        }
        try {
            return handler.handleResponse(response);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new ClientException("Error in response handler (url = " + request.getURI() + ")", e);
        } finally {
            postHandleResponse(response);
            EntityUtils.consumeQuietly(response.getEntity());
        }
    }

    protected void postHandleResponse(HttpResponse response) {
        // Default implementation does nothing...
    }

    protected void prepareRequest(HttpUriRequest request) {
        // Accept JSON responses by default
        if (!request.containsHeader(HttpHeaders.ACCEPT)) {
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        }
        // Add authorization header if specified
        if (!request.containsHeader(HttpHeaders.AUTHORIZATION)) {
            String authorization = getAuthorization();
            if (authorization != null) {
                request.setHeader(HttpHeaders.AUTHORIZATION, authorization);
            }
        }
        if (apiToken != null) {
            request.setHeader(X_SERVICE_API_TOKEN, apiToken);
        }
        if (!request.containsHeader(TRACKING_ID)) {
            String trackingId = MDC.get("WEBEX_TRACKINGID");
            if (trackingId != null) {
                request.setHeader(TRACKING_ID, trackingId);
            }
        }
    }

    protected void checkResponse(HttpUriRequest request, HttpResponse response) {
        if (!isSuccessful(response)) {
            StatusLine status = response.getStatusLine();
            Formatter fmt = new Formatter().format("%s failed: %s", request.getMethod(), status);
            String message = extractErrorMessage(response);
            if (message != null) {
                fmt.format(" (%s)", message);
            }
            fmt.format(" (url = %s)", toLogSafeString(request.getURI()));
            throw new ClientException(status.getStatusCode(), fmt.toString());
        }
        // TODO Consider removing this as this prevents Client instances from being shared
        Header header = response.getFirstHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
           setAuthorization(header.getValue());
        }
    }

    private static String toLogSafeString(URI uri) {
        try {
            return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), null, null).toString();
        } catch (URISyntaxException e) {
            throw new AssertionError(); // Should never happen...
        }
    }


    protected String extractErrorMessage(HttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null && entity.getContentLength() != 0 && isApplicationJson(entity)) {
                String message = EntityUtils.toString(entity, Charsets.UTF_8);
                JsonNode node = objectMapper.readTree(message);
                if (node.has("message")) {
                    return node.get("message").asText();
                } else if (node.has("error")) {
                    return node.get("error").asText();
                }
            }
        } catch (IOException ex) {
            // ignore -- assume it's a json parse exception
        }
        return null;
    }

    protected static boolean isApplicationJson(HttpEntity entity) {
        ContentType type = ContentType.get(entity);
        return type != null && ContentType.APPLICATION_JSON.getMimeType().equals(type.getMimeType());
    }

    protected static boolean isInformational(HttpResponse response) {
        return HttpUtil.isInformational(response.getStatusLine().getStatusCode());
    }

    protected static boolean isSuccessful(HttpResponse response) {
        return HttpUtil.isSuccessful(response.getStatusLine().getStatusCode());
    }

    protected static boolean isRedirection(HttpResponse response) {
        return HttpUtil.isRedirection(response.getStatusLine().getStatusCode());
    }

    protected static boolean isClientError(HttpResponse response) {
        return HttpUtil.isClientError(response.getStatusLine().getStatusCode());
    }

    protected static boolean isServerError(HttpResponse response) {
        return HttpUtil.isServerError(response.getStatusLine().getStatusCode());
    }

    protected TypeFactory typeFactory() {
        return objectMapper.getTypeFactory();
    }
}