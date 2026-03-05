package utils;

public class TestConfig {
    
    private static final String DEFAULT_BASE_URL = "https://serverest.dev";
    private static final String DEFAULT_ADMIN_EMAIL = "admin@carrefour.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";
    
    public static String getBaseUrl() {
        String baseUrl = System.getenv("BASE_URL");
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = DEFAULT_BASE_URL;
        }
        return baseUrl;
    }
    
    public static String getAdminEmail() {
        String email = System.getenv("ADMIN_EMAIL");
        if (email == null || email.trim().isEmpty()) {
            email = DEFAULT_ADMIN_EMAIL;
        }
        return email;
    }
    
    public static String getAdminPassword() {
        String password = System.getenv("ADMIN_PASSWORD");
        if (password == null || password.trim().isEmpty()) {
            password = DEFAULT_ADMIN_PASSWORD;
        }
        return password;
    }
    
    public static void printConfig() {
        System.out.println("===========================================");
        System.out.println("Test Configuration:");
        System.out.println("BASE_URL: " + getBaseUrl());
        System.out.println("ADMIN_EMAIL: " + getAdminEmail());
        System.out.println("ADMIN_PASSWORD: " + maskPassword(getAdminPassword()));
        System.out.println("===========================================");
    }
    
    private static String maskPassword(String password) {
        if (password == null || password.length() < 3) {
            return "***";
        }
        return password.substring(0, 2) + "*".repeat(password.length() - 2);
    }
}
