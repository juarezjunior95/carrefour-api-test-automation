package tests;

import clients.ApiClient;
import clients.Routes;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.BaseTest;
import utils.DataFactory;

import static org.junit.jupiter.api.Assertions.*;

@Epic("User Management")
@Feature("Negative Tests")
@DisplayName("User Negative Tests - Validation & Edge Cases")
@Tag("negative")
@Tag("validation")
public class UserNegativeTests extends BaseTest {
    
    @Test
    @Story("Email Validation")
    @DisplayName("Should return 400 when creating user with invalid email format")
    @Description("Validates that API rejects user creation with email without @")
    @Severity(SeverityLevel.CRITICAL)
    void shouldNotCreateUserWithInvalidEmailFormat() {
        System.out.println("\n>>> TEST: Create user with invalid email format");
        
        ApiClient client = getApiClient();
        UserRequest invalidUser = DataFactory.createUserWithInvalidEmail();
        
        System.out.println("→ Attempting to create user with email: " + invalidUser.getEmail());
        
        Response response = client.post(Routes.USERS, invalidUser);
        
        try {
            assertEquals(400, response.getStatusCode(),
                        "Should return 400 Bad Request for invalid email");
            
            String errorMessage = response.jsonPath().getString("email");
            assertEquals("email deve ser um email válido", errorMessage,
                        "Error message should indicate invalid email format");
            
            System.out.println("✓ Invalid email correctly rejected");
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
    @Story("Email Validation")
    @DisplayName("Should return 400 when creating user without email")
    @Description("Validates that API rejects user creation when email field is missing")
    @Severity(SeverityLevel.CRITICAL)
    void shouldNotCreateUserWithoutEmail() {
        System.out.println("\n>>> TEST: Create user without email");
        
        ApiClient client = getApiClient();
        UserRequest userWithoutEmail = DataFactory.createUserWithoutEmail();
        
        System.out.println("→ Attempting to create user without email field");
        
        Response response = client.post(Routes.USERS, userWithoutEmail);
        
        try {
            assertEquals(400, response.getStatusCode(),
                        "Should return 400 Bad Request when email is missing");
            
            String errorMessage = response.jsonPath().getString("email");
            assertEquals("email é obrigatório", errorMessage,
                        "Error message should indicate email is required");
            
            System.out.println("✓ Missing email correctly rejected");
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
    @Story("Duplicate Data")
    @DisplayName("Should return 400 when creating user with duplicate email")
    @Description("Validates that API prevents duplicate user emails")
    @Severity(SeverityLevel.CRITICAL)
    void shouldNotCreateUserWithDuplicateEmail() {
        System.out.println("\n>>> TEST: Create user with duplicate email");
        
        ApiClient client = getApiClient();
        
        UserRequest firstUser = DataFactory.createValidUser();
        
        System.out.println("→ Creating first user with email: " + firstUser.getEmail());
        Response firstResponse = client.post(Routes.USERS, firstUser);
        
        try {
            assertEquals(201, firstResponse.getStatusCode(),
                        "First user creation should succeed");
            
            String firstUserId = firstResponse.jsonPath().getString("_id");
            System.out.println("✓ First user created: " + firstUserId);
            
            System.out.println("→ Attempting to create second user with same email");
            Response duplicateResponse = client.post(Routes.USERS, firstUser);
            
            assertEquals(400, duplicateResponse.getStatusCode(),
                        "Should return 400 Bad Request for duplicate email");
            
            String errorMessage = duplicateResponse.jsonPath().getString("message");
            assertEquals("Este email já está sendo usado", errorMessage,
                        "Error message should indicate duplicate email");
            
            System.out.println("✓ Duplicate email correctly rejected");
            System.out.println("  Status: " + duplicateResponse.getStatusCode());
            System.out.println("  Error: " + errorMessage);
            
            client.delete(Routes.USERS, firstUserId);
            System.out.println("✓ Cleanup: Test user deleted");
            
        } catch (AssertionError e) {
            System.err.println("✗ TEST FAILED");
            throw e;
        }
    }
    
    @Test
    @Story("Non-existent Resource")
    @DisplayName("Should return 400 when getting user with invalid ID format")
    @Description("Validates that API returns error for invalid user ID format")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturn400ForNonExistentUser() {
        System.out.println("\n>>> TEST: Get user with invalid ID format");
        
        ApiClient client = getApiClient();
        String invalidId = "nonExistentId123456";
        
        System.out.println("→ Attempting to get user with ID: " + invalidId);
        
        Response response = client.getById(Routes.USERS, invalidId);
        
        try {
            assertEquals(400, response.getStatusCode(),
                        "Should return 400 for invalid ID format");
            
            String errorMessage = response.jsonPath().getString("id");
            assertNotNull(errorMessage, "Error message for 'id' field should exist");
            assertTrue(errorMessage.contains("caracteres alfanuméricos"),
                      "Error message should indicate ID format issue");
            
            System.out.println("✓ Invalid ID format correctly handled");
            System.out.println("  Status: " + response.getStatusCode());
            System.out.println("  Error: " + errorMessage);
            
        } catch (AssertionError e) {
            System.err.println("✗ TEST FAILED");
            System.err.println("Status: " + response.getStatusCode());
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
}
