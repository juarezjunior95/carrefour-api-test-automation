package tests;

import clients.ApiClient;
import clients.Routes;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.UserRequest;
import org.junit.jupiter.api.*;
import utils.BaseTest;
import utils.DataFactory;
import utils.TestContext;

import static org.junit.jupiter.api.Assertions.*;

@Epic("User Management")
@Feature("CRUD Operations")
@DisplayName("User CRUD - Happy Path Suite")
@Tag("crud")
@Tag("happy-path")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserCrudHappyPathTest extends BaseTest {
    
    private static final String CONTEXT_USER_ID = "createdUserId";
    private static final String CONTEXT_USER_EMAIL = "createdUserEmail";
    private static final String CONTEXT_USER_NAME = "createdUserName";
    private static final String CONTEXT_USER_PASSWORD = "createdUserPassword";
    private static final String CONTEXT_UPDATED_NAME = "updatedUserName";
    
    @BeforeAll
    public static void setupCrudSuite() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        USER CRUD HAPPY PATH - TEST SUITE START          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        TestContext.clear();
    }
    
    @AfterAll
    public static void tearDownCrudSuite() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         USER CRUD HAPPY PATH - TEST SUITE END           ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        TestContext.printAll();
        TestContext.clearAll();
    }
    
    @Test
    @Order(1)
    @Story("Create User")
    @DisplayName("1️⃣ Should create valid user successfully")
    @Description("Creates a new user with valid data and stores ID in TestContext")
    @Severity(SeverityLevel.BLOCKER)
    void shouldCreateUserSuccessfully() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("TEST 1: CREATE USER");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        ApiClient client = getApiClient();
        
        UserRequest user = DataFactory.createValidAdminUser();
        
        System.out.println("→ Creating user with data:");
        System.out.println("  Nome: " + user.getNome());
        System.out.println("  Email: " + user.getEmail());
        System.out.println("  Admin: " + user.getAdministrador());
        
        Response response = client.post(Routes.USERS, user);
        
        try {
            assertEquals(201, response.getStatusCode(), 
                        "Status code should be 201 Created");
            
            String message = response.jsonPath().getString("message");
            assertEquals("Cadastro realizado com sucesso", message,
                        "Success message should match");
            
            String userId = response.jsonPath().getString("_id");
            assertNotNull(userId, "User ID should not be null");
            assertFalse(userId.isEmpty(), "User ID should not be empty");
            assertTrue(userId.length() > 10, "User ID should have reasonable length");
            
            TestContext.set(CONTEXT_USER_ID, userId);
            TestContext.set(CONTEXT_USER_EMAIL, user.getEmail());
            TestContext.set(CONTEXT_USER_NAME, user.getNome());
            TestContext.set(CONTEXT_USER_PASSWORD, user.getPassword());
            
            System.out.println("\n✓ USER CREATED SUCCESSFULLY");
            System.out.println("  ID: " + userId);
            System.out.println("  Email: " + user.getEmail());
            System.out.println("  Nome: " + user.getNome());
            System.out.println("  Status: " + response.getStatusCode());
            System.out.println("  Message: " + message);
            
        } catch (AssertionError e) {
            System.err.println("\n✗ TEST FAILED - Logging response:");
            System.err.println("Status: " + response.getStatusCode());
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
    
    @Test
    @Order(2)
    @Story("Get User")
    @DisplayName("2️⃣ Should get user by ID and validate fields")
    @Description("Retrieves user by ID from TestContext and validates all fields")
    @Severity(SeverityLevel.CRITICAL)
    void shouldGetUserByIdSuccessfully() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("TEST 2: GET USER BY ID");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        assertTrue(TestContext.has(CONTEXT_USER_ID), 
                  "User ID must exist in TestContext (Test 1 must run first)");
        
        String userId = TestContext.getString(CONTEXT_USER_ID);
        String expectedEmail = TestContext.getString(CONTEXT_USER_EMAIL);
        String expectedName = TestContext.getString(CONTEXT_USER_NAME);
        
        System.out.println("→ Getting user with ID: " + userId);
        
        ApiClient client = getApiClient();
        Response response = client.getById(Routes.USERS, userId);
        
        try {
            assertEquals(200, response.getStatusCode(),
                        "Status code should be 200 OK");
            
            String actualId = response.jsonPath().getString("_id");
            String actualEmail = response.jsonPath().getString("email");
            String actualName = response.jsonPath().getString("nome");
            String actualPassword = response.jsonPath().getString("password");
            String actualAdmin = response.jsonPath().getString("administrador");
            
            assertEquals(userId, actualId, "User ID should match");
            assertEquals(expectedEmail, actualEmail, "Email should match");
            assertEquals(expectedName, actualName, "Name should match");
            assertNotNull(actualPassword, "Password should be present");
            assertNotNull(actualAdmin, "Admin flag should be present");
            assertTrue(actualAdmin.equals("true") || actualAdmin.equals("false"),
                      "Admin flag should be 'true' or 'false'");
            
            System.out.println("\n✓ USER RETRIEVED SUCCESSFULLY");
            System.out.println("  ID: " + actualId);
            System.out.println("  Nome: " + actualName);
            System.out.println("  Email: " + actualEmail);
            System.out.println("  Admin: " + actualAdmin);
            System.out.println("  All fields validated ✓");
            
        } catch (AssertionError e) {
            System.err.println("\n✗ TEST FAILED - Logging response:");
            System.err.println("Status: " + response.getStatusCode());
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
    
    @Test
    @Order(3)
    @Story("Update User")
    @DisplayName("3️⃣ Should update user name successfully")
    @Description("Updates user name and validates the change")
    @Severity(SeverityLevel.CRITICAL)
    void shouldUpdateUserNameSuccessfully() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("TEST 3: UPDATE USER");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        assertTrue(TestContext.has(CONTEXT_USER_ID),
                  "User ID must exist in TestContext (Test 1 must run first)");
        
        String userId = TestContext.getString(CONTEXT_USER_ID);
        String currentEmail = TestContext.getString(CONTEXT_USER_EMAIL);
        String currentPassword = TestContext.getString(CONTEXT_USER_PASSWORD);
        
        String updatedName = "Updated Name " + System.currentTimeMillis();
        
        System.out.println("→ Updating user ID: " + userId);
        System.out.println("  New name: " + updatedName);
        
        UserRequest updateRequest = UserRequest.builder()
                .nome(updatedName)
                .email(currentEmail)
                .password(currentPassword)
                .administrador("true")
                .build();
        
        ApiClient client = getApiClient();
        Response response = client.put(Routes.USERS, userId, updateRequest);
        
        try {
            assertEquals(200, response.getStatusCode(),
                        "Status code should be 200 OK");
            
            String message = response.jsonPath().getString("message");
            assertEquals("Registro alterado com sucesso", message,
                        "Update message should match");
            
            TestContext.set(CONTEXT_UPDATED_NAME, updatedName);
            
            System.out.println("\n✓ USER UPDATED SUCCESSFULLY");
            System.out.println("  Updated name: " + updatedName);
            System.out.println("  Status: " + response.getStatusCode());
            System.out.println("  Message: " + message);
            
            Response verifyResponse = client.getById(Routes.USERS, userId);
            String verifiedName = verifyResponse.jsonPath().getString("nome");
            
            assertEquals(updatedName, verifiedName,
                        "Name should be updated when retrieved again");
            
            System.out.println("  ✓ Update verified by GET request");
            System.out.println("  Verified name: " + verifiedName);
            
        } catch (AssertionError e) {
            System.err.println("\n✗ TEST FAILED - Logging response:");
            System.err.println("Status: " + response.getStatusCode());
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
    
    @Test
    @Order(4)
    @Story("Delete User")
    @DisplayName("4️⃣ Should delete user successfully")
    @Description("Deletes user and validates success response")
    @Severity(SeverityLevel.CRITICAL)
    void shouldDeleteUserSuccessfully() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("TEST 4: DELETE USER");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        assertTrue(TestContext.has(CONTEXT_USER_ID),
                  "User ID must exist in TestContext (Test 1 must run first)");
        
        String userId = TestContext.getString(CONTEXT_USER_ID);
        
        System.out.println("→ Deleting user ID: " + userId);
        
        ApiClient client = getApiClient();
        Response response = client.delete(Routes.USERS, userId);
        
        try {
            assertEquals(200, response.getStatusCode(),
                        "Status code should be 200 OK");
            
            String message = response.jsonPath().getString("message");
            assertEquals("Registro excluído com sucesso", message,
                        "Delete message should match");
            
            System.out.println("\n✓ USER DELETED SUCCESSFULLY");
            System.out.println("  Deleted ID: " + userId);
            System.out.println("  Status: " + response.getStatusCode());
            System.out.println("  Message: " + message);
            
        } catch (AssertionError e) {
            System.err.println("\n✗ TEST FAILED - Logging response:");
            System.err.println("Status: " + response.getStatusCode());
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
    
    @Test
    @Order(5)
    @Story("Get Deleted User")
    @DisplayName("5️⃣ Should return 400 when getting deleted user")
    @Description("Attempts to retrieve deleted user and validates error response")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnErrorWhenGettingDeletedUser() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("TEST 5: GET DELETED USER (NEGATIVE TEST)");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        assertTrue(TestContext.has(CONTEXT_USER_ID),
                  "User ID must exist in TestContext (Test 1 must run first)");
        
        String deletedUserId = TestContext.getString(CONTEXT_USER_ID);
        
        System.out.println("→ Attempting to get deleted user ID: " + deletedUserId);
        
        ApiClient client = getApiClient();
        Response response = client.getById(Routes.USERS, deletedUserId);
        
        try {
            assertEquals(400, response.getStatusCode(),
                        "Status code should be 400 Bad Request for deleted user");
            
            String message = response.jsonPath().getString("message");
            assertEquals("Usuário não encontrado", message,
                        "Error message should indicate user not found");
            
            System.out.println("\n✓ DELETED USER NOT FOUND (AS EXPECTED)");
            System.out.println("  Deleted ID: " + deletedUserId);
            System.out.println("  Status: " + response.getStatusCode());
            System.out.println("  Message: " + message);
            System.out.println("  ✓ Negative test passed - user correctly not found");
            
        } catch (AssertionError e) {
            System.err.println("\n✗ TEST FAILED - Logging response:");
            System.err.println("Status: " + response.getStatusCode());
            System.err.println("Body: " + response.getBody().asString());
            throw e;
        }
    }
    
    @Test
    @Order(6)
    @Story("CRUD Summary")
    @DisplayName("6️⃣ CRUD Suite Summary")
    @Description("Prints summary of all CRUD operations performed")
    @Severity(SeverityLevel.TRIVIAL)
    void printCrudSummary() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║              CRUD HAPPY PATH - SUMMARY                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        System.out.println("\n✓ Test Suite Completed Successfully!");
        System.out.println("\nOperations performed:");
        System.out.println("  1. ✓ CREATE - User created with ID: " + 
                         TestContext.getString(CONTEXT_USER_ID));
        System.out.println("  2. ✓ READ   - User retrieved and validated");
        System.out.println("  3. ✓ UPDATE - Name updated to: " + 
                         TestContext.getString(CONTEXT_UPDATED_NAME));
        System.out.println("  4. ✓ DELETE - User deleted successfully");
        System.out.println("  5. ✓ VERIFY - Deleted user returns 400 (not found)");
        
        System.out.println("\nContext Data:");
        System.out.println("  Email: " + TestContext.getString(CONTEXT_USER_EMAIL));
        System.out.println("  Original Name: " + TestContext.getString(CONTEXT_USER_NAME));
        System.out.println("  Updated Name: " + TestContext.getString(CONTEXT_UPDATED_NAME));
        
        System.out.println("\n✓ All CRUD operations completed successfully!");
        System.out.println("════════════════════════════════════════════════════════════\n");
    }
}
