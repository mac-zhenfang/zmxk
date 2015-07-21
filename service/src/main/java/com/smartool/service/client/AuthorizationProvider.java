package com.smartool.service.client;

public interface AuthorizationProvider {
    String getAuthorization();
    boolean refreshToken();
}
