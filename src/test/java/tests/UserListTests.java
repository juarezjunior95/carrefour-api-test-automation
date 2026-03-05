package tests;

import clients.ApiClient;
import clients.Routes;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

@Epic("User Management")
@Feature("User Listing")
@DisplayName("User List Tests")
@Tag("list")
@Tag("smoke")
public class UserListTests extends BaseTest {
    
    @Test
    @Story("List All Users")
    @DisplayName("Should list all users successfully")
    @Description("Validates GET /usuarios endpoint returns a list with proper structure")
    @Severity(SeverityLevel.CRITICAL)
    void shouldListAllUsersSuccessfully() {
        System.out.println("\n>>> TEST: List all users");
        
        ApiClient client = getApiClient();
        
        System.out.println("→ Getting all users from GET /usuarios");
        
        Response response = client.get(Routes.USERS);
        
        try {
            assertEquals(200, response.getStatusCode(),
                        "GET /usuarios should return 200 OK");
            
            // Validate response structure
            assertNotNull(response.jsonPath().getList("usuarios"),
                         "Response should contain 'usuarios' array");
            
            int totalUsers = response.jsonPath().getInt("quantidade");
            assertTrue(totalUsers >= 0,
                      "Total users count should be >= 0");
            
            System.out.println("✓ List all users successful");
            System.out.println("  Status: " + response.getStatusCode());
            System.out.println("  Total users: " + totalUsers);
            System.out.println("  Response structure validated ✓");
            
        } catch (AssertionError e) {
            System.err.println("✗ TEST FAILED");
            System.err.println("Status: " + response.getStatusCode());
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
    
    @Test
    @Story("List All Users")
    @DisplayName("Should validate usuarios array structure")
    @Description("Validates that each user in the list has required fields")
    @Severity(SeverityLevel.NORMAL)
    void shouldValidateUsersArrayStructure() {
        System.out.println("\n>>> TEST: Validate users array structure");
        
        ApiClient client = getApiClient();
        Response response = client.get(Routes.USERS);
        
        try {
            assertEquals(200, response.getStatusCode());
            
            var usuarios = response.jsonPath().getList("usuarios");
            assertNotNull(usuarios, "usuarios array should not be null");
            
            if (!usuarios.isEmpty()) {
                // Validate first user has expected fields
                assertNotNull(response.jsonPath().getString("usuarios[0]._id"),
                             "User should have _id field");
                assertNotNull(response.jsonPath().getString("usuarios[0].nome"),
                             "User should have nome field");
                assertNotNull(response.jsonPath().getString("usuarios[0].email"),
                             "User should have email field");
                assertNotNull(response.jsonPath().getString("usuarios[0].password"),
                             "User should have password field");
                assertNotNull(response.jsonPath().getString("usuarios[0].administrador"),
                             "User should have administrador field");
                
                System.out.println("✓ Users array structure validated");
                System.out.println("  First user has all required fields ✓");
            } else {
                System.out.println("⚠️ No users found in the list (empty array is valid)");
            }
            
        } catch (AssertionError e) {
            System.err.println("✗ TEST FAILED");
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
}
