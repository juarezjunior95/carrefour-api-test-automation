package utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.LoginRequest;
import clients.Routes;

import static io.restassured.RestAssured.given;

/**
 * Helper para obter token JWT de autenticação.
 * Simplificado para uso em testes - faz login e retorna o token.
 */
public class TokenManager {
    
    /**
     * Obtém token JWT fazendo login com credenciais do TestConfig.
     * 
     * @return Token JWT para usar em Authorization header
     * @throws RuntimeException se falhar ao obter token
     */
    public static String getToken() {
        LoginRequest loginRequest = new LoginRequest(
            TestConfig.getAdminEmail(),
            TestConfig.getAdminPassword()
        );
        
        Response response = given()
            .contentType(ContentType.JSON)
            .baseUri(TestConfig.getBaseUrl())
            .body(loginRequest)
            .when()
            .post(Routes.LOGIN);
        
        if (response.getStatusCode() == 200) {
            String token = response.jsonPath().getString("authorization");
            if (token != null && !token.isEmpty()) {
                return token;
            }
        }
        
        throw new RuntimeException(
            "Failed to get authentication token. Status: " + response.getStatusCode() +
            ", Body: " + response.getBody().asString()
        );
    }
}
