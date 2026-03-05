package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class AllureManager {
    
    @Step("Attach request details to report")
    public static void attachRequest(String method, String endpoint, Object body) {
        StringBuilder requestDetails = new StringBuilder();
        requestDetails.append("Method: ").append(method).append("\n");
        requestDetails.append("Endpoint: ").append(endpoint).append("\n");
        
        if (body != null) {
            requestDetails.append("\nRequest Body:\n");
            requestDetails.append(body.toString());
        }
        
        Allure.addAttachment("Request Details", "text/plain", requestDetails.toString());
    }
    
    @Step("Attach response details to report")
    public static void attachResponse(Response response) {
        if (response == null) {
            return;
        }
        
        StringBuilder responseDetails = new StringBuilder();
        responseDetails.append("Status Code: ").append(response.getStatusCode()).append("\n");
        responseDetails.append("Status Line: ").append(response.getStatusLine()).append("\n");
        responseDetails.append("Response Time: ").append(response.getTime()).append(" ms\n");
        responseDetails.append("\nResponse Headers:\n");
        response.getHeaders().forEach(header -> 
            responseDetails.append(header.getName()).append(": ").append(header.getValue()).append("\n")
        );
        responseDetails.append("\nResponse Body:\n");
        responseDetails.append(response.getBody().asPrettyString());
        
        Allure.addAttachment("Response Details", "text/plain", responseDetails.toString());
    }
    
    @Step("Attach request and response on failure")
    public static void attachOnFailure(String method, String endpoint, Object body, Response response) {
        Allure.addAttachment("❌ FAILED REQUEST - " + method + " " + endpoint, "text/plain",
            buildFailureDetails(method, endpoint, body, response));
    }
    
    private static String buildFailureDetails(String method, String endpoint, Object body, Response response) {
        StringBuilder details = new StringBuilder();
        
        details.append("═══════════════════════════════════════════════════════════\n");
        details.append("                    FAILED REQUEST DETAILS\n");
        details.append("═══════════════════════════════════════════════════════════\n\n");
        
        details.append("REQUEST:\n");
        details.append("  Method: ").append(method).append("\n");
        details.append("  Endpoint: ").append(endpoint).append("\n");
        
        if (body != null) {
            details.append("  Body:\n");
            details.append("    ").append(body.toString()).append("\n");
        }
        
        details.append("\n");
        details.append("RESPONSE:\n");
        details.append("  Status: ").append(response.getStatusCode()).append("\n");
        details.append("  Time: ").append(response.getTime()).append(" ms\n");
        details.append("  Body:\n");
        String prettyBody = response.getBody().asPrettyString();
        for (String line : prettyBody.split("\n")) {
            details.append("    ").append(line).append("\n");
        }
        
        details.append("\n═══════════════════════════════════════════════════════════\n");
        
        return details.toString();
    }
    
    @Step("{stepDescription}")
    public static void step(String stepDescription) {
        // Método vazio - apenas para criar step no relatório
    }
    
    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }
    
    public static void attachJson(String name, String jsonContent) {
        Allure.addAttachment(name, "application/json", jsonContent);
    }
}
