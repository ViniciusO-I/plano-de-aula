package br.com.fiap.skill_hub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false",
        "security.jwt.secret=MDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUY="
})
class ApiErrorHandlingIntegrationTest {

    @LocalServerPort
    private int port;

    private HttpResponse<String> postJson(String path, String body, String language, String traceId) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(body));

        if (language != null) {
            builder.header("Accept-Language", language);
        }
        if (traceId != null) {
            builder.header("X-Trace-Id", traceId);
        }

        return HttpClient.newHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void shouldReturnValidationErrorInEnglishAndPreserveIncomingTraceId() throws Exception {
        String traceId = "trace-login-validation-001";

        HttpResponse<String> response = postJson("/api/login", "{}", "en-US", traceId);

        assertEquals(400, response.statusCode());
        assertEquals(traceId, response.headers().firstValue("X-Trace-Id").orElse(""));
        assertTrue(response.body().contains("\"code\":\"VALIDATION_ERROR\""));
        assertTrue(response.body().contains("\"message\":\"Data validation failed\""));
        assertTrue(response.body().contains("\"traceId\":\"" + traceId + "\""));
        assertTrue(response.body().contains("email: Email is required"));
        assertTrue(response.body().contains("password: Password is required"));
    }

    @Test
    void shouldReturnUnauthorizedForMalformedRefreshTokenWithTraceId() throws Exception {
        String traceId = "trace-refresh-malformed-001";

        HttpResponse<String> response = postJson(
                "/api/login/refresh",
                "{\"refreshToken\":\"malformed-refresh-token-1234567890\"}",
                "en-US",
                traceId
        );

        assertEquals(401, response.statusCode());
        assertEquals(traceId, response.headers().firstValue("X-Trace-Id").orElse(""));
        assertTrue(response.body().contains("\"code\":\"INVALID_REFRESH_TOKEN\""));
        assertTrue(response.body().contains("\"message\":\"Invalid refresh token\""));
        assertTrue(response.body().contains("\"traceId\":\"" + traceId + "\""));
    }

    @Test
    void shouldReturnEnglishValidationMessageForUserCreateAndGenerateTraceIdWhenMissing() throws Exception {
        HttpResponse<String> response = postJson(
                "/api/users",
                "{\"email\":\"email-invalido\",\"password\":\"12345678\"}",
                "en-US",
                null
        );

        String generatedTraceId = response.headers().firstValue("X-Trace-Id").orElse("");

        assertEquals(400, response.statusCode());
        assertFalse(generatedTraceId.isBlank());
        assertTrue(response.body().contains("\"code\":\"VALIDATION_ERROR\""));
        assertTrue(response.body().contains("\"message\":\"Data validation failed\""));
        assertTrue(response.body().contains("name: Name is required"));
        assertTrue(response.body().contains("\"traceId\":\"" + generatedTraceId + "\""));
    }
}



