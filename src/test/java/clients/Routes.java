package clients;

public class Routes {
    
    public static final String USERS = "/usuarios";
    public static final String LOGIN = "/login";
    
    private Routes() {
        throw new IllegalStateException("Utility class - should not be instantiated");
    }
}
