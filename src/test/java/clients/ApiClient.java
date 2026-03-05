package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.AllureManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {
    
    private final RequestSpecification baseSpec;
    private final Map<String, String> headers;
    private ByteArrayOutputStream requestCaptor;
    private ByteArrayOutputStream responseCaptor;
    
    public ApiClient(RequestSpecification baseSpec) {
        this.baseSpec = baseSpec;
        this.headers = new HashMap<>();
    }
    
    public ApiClient addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
    
    public ApiClient addAuthorizationHeader(String token) {
        return addHeader("Authorization", token);
    }
    
    public ApiClient addBearerToken(String token) {
        return addHeader("Authorization", "Bearer " + token);
    }
    
    public ApiClient withAuthentication(String token) {
        if (token != null && !token.trim().isEmpty()) {
            return addAuthorizationHeader(token);
        }
        return this;
    }
    
    public ApiClient withBearerAuthentication(String token) {
        if (token != null && !token.trim().isEmpty()) {
            return addBearerToken(token);
        }
        return this;
    }
    
    public ApiClient withJwtToken(String token) {
        return withAuthentication(token);
    }
    
    public ApiClient clearHeaders() {
        this.headers.clear();
        return this;
    }
    
    public boolean hasAuthorizationHeader() {
        return headers.containsKey("Authorization");
    }
    
    public String getAuthorizationHeader() {
        return headers.get("Authorization");
    }
    
    @Step("GET {endpoint}")
    public Response get(String endpoint) {
        setupLogCapture();
        
        try {
            Response response = given()
                    .spec(baseSpec)
                    .headers(headers)
                    .when()
                    .get(endpoint);
            
            if (response.getStatusCode() >= 400) {
                AllureManager.attachOnFailure("GET", endpoint, null, response);
            }
            
            return response;
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Step("GET {endpoint}/{id}")
    public Response getById(String endpoint, String id) {
        setupLogCapture();
        
        try {
            String fullPath = endpoint + "/" + id;
            Response response = given()
                    .spec(baseSpec)
                    .headers(headers)
                    .when()
                    .get(fullPath);
            
            if (response.getStatusCode() >= 400) {
                AllureManager.attachOnFailure("GET", fullPath, null, response);
            }
            
            return response;
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Step("POST {endpoint}")
    public Response post(String endpoint, Object body) {
        setupLogCapture();
        
        try {
            Response response = given()
                    .spec(baseSpec)
                    .headers(headers)
                    .body(body)
                    .when()
                    .post(endpoint);
            
            if (response.getStatusCode() >= 400) {
                AllureManager.attachOnFailure("POST", endpoint, body, response);
            }
            
            return response;
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Step("PUT {endpoint}/{id}")
    public Response put(String endpoint, String id, Object body) {
        setupLogCapture();
        
        try {
            String fullPath = endpoint + "/" + id;
            Response response = given()
                    .spec(baseSpec)
                    .headers(headers)
                    .body(body)
                    .when()
                    .put(fullPath);
            
            if (response.getStatusCode() >= 400) {
                AllureManager.attachOnFailure("PUT", fullPath, body, response);
            }
            
            return response;
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Step("DELETE {endpoint}/{id}")
    public Response delete(String endpoint, String id) {
        setupLogCapture();
        
        try {
            String fullPath = endpoint + "/" + id;
            Response response = given()
                    .spec(baseSpec)
                    .headers(headers)
                    .when()
                    .delete(fullPath);
            
            if (response.getStatusCode() >= 400) {
                AllureManager.attachOnFailure("DELETE", fullPath, null, response);
            }
            
            return response;
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Step("PATCH {endpoint}/{id}")
    public Response patch(String endpoint, String id, Object body) {
        setupLogCapture();
        
        try {
            String fullPath = endpoint + "/" + id;
            Response response = given()
                    .spec(baseSpec)
                    .headers(headers)
                    .body(body)
                    .when()
                    .patch(fullPath);
            
            if (response.getStatusCode() >= 400) {
                AllureManager.attachOnFailure("PATCH", fullPath, body, response);
            }
            
            return response;
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    private void setupLogCapture() {
        requestCaptor = new ByteArrayOutputStream();
        responseCaptor = new ByteArrayOutputStream();
    }
}
