package com.manumafe.players.data.playersdata.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ApiKeyService {

    @Value("${API_KEY}")
    private String expectedApiKey;

    public Optional<String> extract(HttpServletRequest request) {
        String providedApiKey = request.getHeader("api-key");
        if (providedApiKey != null && validateApiKey(providedApiKey)) {
            return Optional.of(providedApiKey);
        } else {
            return Optional.empty();
        }
    }

    public boolean validateApiKey(String receivedApiKey) {
        return receivedApiKey.equals(expectedApiKey);
    }
}
