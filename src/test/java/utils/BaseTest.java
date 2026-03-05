package utils;

import clients.ApiClient;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class BaseTest {
    
    protected static RequestSpecification requestSpec;
    protected static ApiClient apiClient;
    
    private ByteArrayOutputStream requestCaptor;
    private ByteArrayOutputStream responseCaptor;
    private PrintStream requestStream;
    private PrintStream responseStream;
    
    @BeforeAll
    public static void globalSetup() {
        RestAssured.baseURI = TestConfig.getBaseUrl();
        
        // Configure SSL to accept all certificates (for test environments)
        RestAssured.config = RestAssuredConfig.config().sslConfig(
            SSLConfig.sslConfig().relaxedHTTPSValidation()
        );
        
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(TestConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setConfig(RestAssured.config)
                .addFilter(new AllureRestAssured())
                .build();
        
        apiClient = new ApiClient(requestSpec);
        
        TestConfig.printConfig();
    }
    
    @BeforeEach
    public void setUp(TestInfo testInfo) {
        requestCaptor = new ByteArrayOutputStream();
        responseCaptor = new ByteArrayOutputStream();
        requestStream = new PrintStream(requestCaptor, true);
        responseStream = new PrintStream(responseCaptor, true);
        
        System.out.println("\n>>> Starting test: " + testInfo.getDisplayName());
    }
    
    protected RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL, requestStream))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL, responseStream))
                .build();
    }
    
    protected RequestSpecification getAuthenticatedRequestSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getRequestSpec())
                .addHeader("Authorization", "Bearer " + getAuthToken())
                .build();
    }
    
    protected ApiClient getApiClient() {
        return new ApiClient(getRequestSpec());
    }
    
    protected ApiClient getAuthenticatedApiClient(String token) {
        return new ApiClient(getRequestSpec())
                .addAuthorizationHeader(token);
    }
    
    protected ApiClient getAuthenticatedApiClient() {
        String token = TokenManager.getToken();
        return new ApiClient(getRequestSpec())
                .withJwtToken(token);
    }
    
    protected String getAuthToken() {
        return TokenManager.getToken();
    }
    
    protected void logRequestResponseOnFailure() {
        String requestLog = requestCaptor.toString();
        String responseLog = responseCaptor.toString();
        
        if (!requestLog.isEmpty()) {
            System.out.println("\n===== REQUEST (Logged due to test failure) =====");
            System.out.println(requestLog);
        }
        
        if (!responseLog.isEmpty()) {
            System.out.println("\n===== RESPONSE (Logged due to test failure) =====");
            System.out.println(responseLog);
        }
    }
    
    protected void setupTest() {
    }
    
    protected void cleanupTest() {
    }
}
