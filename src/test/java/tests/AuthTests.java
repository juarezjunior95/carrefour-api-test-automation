package tests;

import clients.ApiClient;
import clients.Routes;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.LoginRequest;
import models.UserRequest;
import org.junit.jupiter.api.*;
import utils.BaseTest;
import utils.DataFactory;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Authentication")
@Feature("JWT Authentication")
@DisplayName("Authentication Tests")
@Tag("auth")
@Tag("smoke")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTests extends BaseTest {
    
    private static String testUserId;
    private static String testUserEmail;
    private static String testUserPassword;
    
    @BeforeAll
    public static void setupAuthTests() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           AUTHENTICATION TESTS - SETUP                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        // Criar usuário para testes de autenticação
        ApiClient client = new ApiClient(requestSpec);
        UserRequest testUser = DataFactory.createValidAdminUser();
        testUserEmail = testUser.getEmail();
        testUserPassword = testUser.getPassword();
        
        Response response = client.post(Routes.USERS, testUser);
        testUserId = response.jsonPath().getString("_id");
        
        System.out.println("✓ Test user created for auth tests");
        System.out.println("  Email: " + testUserEmail);
        System.out.println("  ID: " + testUserId);
    }
    
    @AfterAll
    public static void cleanupAuthTests() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║          AUTHENTICATION TESTS - CLEANUP                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        if (testUserId != null) {
            try {
                ApiClient client = new ApiClient(requestSpec);
                client.delete(Routes.USERS, testUserId);
                System.out.println("✓ Test user deleted");
            } catch (Exception e) {
                System.err.println("⚠ Failed to delete test user: " + e.getMessage());
            }
        }
    }
    
    @Test
    @Order(1)
    @Story("Login Success")
    @DisplayName("Should login successfully and return valid JWT token")
    @Description("Validates that login with valid credentials returns status 200 and a non-empty JWT token")
    @Severity(SeverityLevel.BLOCKER)
    void shouldLoginSuccessfullyAndReturnToken() {
        System.out.println("\n>>> TEST: Login with valid credentials");
        
        ApiClient client = getApiClient();
        
        LoginRequest loginRequest = LoginRequest.builder()
                .email(testUserEmail)
                .password(testUserPassword)
                .build();
        
        System.out.println("→ Attempting login with:");
        System.out.println("  Email: " + testUserEmail);
        
        Response response = client.post(Routes.LOGIN, loginRequest);
        
        try {
            // Valida status code
            assertEquals(200, response.getStatusCode(),
                        "Login should return status 200");
            
            // Valida mensagem de sucesso
            String message = response.jsonPath().getString("message");
            assertEquals("Login realizado com sucesso", message,
                        "Success message should match");
            
            // Valida token não vazio
            String token = response.jsonPath().getString("authorization");
            assertNotNull(token, "Authorization token should not be null");
            assertFalse(token.isEmpty(), "Authorization token should not be empty");
            assertTrue(token.length() > 20, "Token should have reasonable length");
            
            System.out.println("✓ Login successful");
            System.out.println("  Status: " + response.getStatusCode());
            System.out.println("  Message: " + message);
            System.out.println("  Token length: " + token.length());
            System.out.println("  Token preview: " + token.substring(0, Math.min(30, token.length())) + "...");
            
        } catch (AssertionError e) {
            System.err.println("✗ TEST FAILED");
            System.err.println("Status: " + response.getStatusCode());
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
    
    @Test
    @Order(2)
    @Story("Login Failure")
    @DisplayName("Should return 401 when login with invalid credentials")
    @Description("Validates that login with wrong password returns status 401 Unauthorized")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturn401WithInvalidCredentials() {
        System.out.println("\n>>> TEST: Login with invalid credentials");
        
        ApiClient client = getApiClient();
        
        LoginRequest invalidLoginRequest = LoginRequest.builder()
                .email(testUserEmail)
                .password("wrongpassword123")
                .build();
        
        System.out.println("→ Attempting login with:");
        System.out.println("  Email: " + testUserEmail);
        System.out.println("  Password: <wrong password>");
        
        Response response = client.post(Routes.LOGIN, invalidLoginRequest);
        
        try {
            // Valida status code
            assertEquals(401, response.getStatusCode(),
                        "Login with wrong password should return 401 Unauthorized");
            
            // Valida mensagem de erro
            String errorMessage = response.jsonPath().getString("message");
            assertEquals("Email e/ou senha inválidos", errorMessage,
                        "Error message should indicate invalid credentials");
            
            System.out.println("✓ Invalid credentials correctly rejected");
            System.out.println("  Status: " + response.getStatusCode());
            System.out.println("  Error: " + errorMessage);
            
        } catch (AssertionError e) {
            System.err.println("✗ TEST FAILED");
            System.err.println("Status: " + response.getStatusCode());
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
    
    @Test
    @Order(3)
    @Story("Token Required")
    @DisplayName("Should validate that endpoints work with or without token as per API design")
    @Description("Tests API behavior when token is not provided (note: Serverest allows GET without auth)")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleRequestWithoutToken() {
        System.out.println("\n>>> TEST: Request without authentication token");
        
        ApiClient client = getApiClient();
        
        System.out.println("→ Attempting GET /usuarios without token");
        
        Response response = client.get(Routes.USERS);
        
        // Serverest permite GET /usuarios sem autenticação
        // Este teste valida o comportamento atual da API
        System.out.println("✓ Request completed");
        System.out.println("  Status: " + response.getStatusCode());
        System.out.println("  Note: Serverest allows GET /usuarios without authentication");
        
        // Validar que a resposta é válida (200 ou 401, dependendo da API)
        assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 401,
                  "API should return either 200 (allowed) or 401 (requires auth)");
        
        if (response.getStatusCode() == 200) {
            System.out.println("  Result: API allows this endpoint without authentication");
        } else if (response.getStatusCode() == 401) {
            System.out.println("  Result: API requires authentication for this endpoint");
        }
    }
}
