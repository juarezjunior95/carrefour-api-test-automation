package utils;

import models.LoginRequest;
import models.UserRequest;

import java.util.UUID;

public class DataFactory {
    
    private static final String DEFAULT_PASSWORD = "senha123";
    private static final String DEFAULT_NAME = "Usuario Teste";
    
    public static UserRequest createValidUser() {
        return UserRequest.builder()
                .nome("Usuario Teste " + System.currentTimeMillis())
                .email(generateUniqueEmail())
                .password(DEFAULT_PASSWORD)
                .administrador("true")
                .build();
    }
    
    public static UserRequest createValidAdminUser() {
        return UserRequest.builder()
                .nome("Admin " + System.currentTimeMillis())
                .email(generateUniqueEmail())
                .password(DEFAULT_PASSWORD)
                .admin(true)
                .build();
    }
    
    public static UserRequest createValidNonAdminUser() {
        return UserRequest.builder()
                .nome("User " + System.currentTimeMillis())
                .email(generateUniqueEmail())
                .password(DEFAULT_PASSWORD)
                .admin(false)
                .build();
    }
    
    public static UserRequest createUserWithoutEmail() {
        return UserRequest.builder()
                .nome("Usuario Sem Email")
                .email(null)
                .password(DEFAULT_PASSWORD)
                .administrador("true")
                .build();
    }
    
    public static UserRequest createUserWithInvalidEmail() {
        return UserRequest.builder()
                .nome("Usuario Email Invalido")
                .email("email-invalido-sem-arroba")
                .password(DEFAULT_PASSWORD)
                .administrador("true")
                .build();
    }
    
    public static UserRequest createUserWithoutName() {
        return UserRequest.builder()
                .nome(null)
                .email(generateUniqueEmail())
                .password(DEFAULT_PASSWORD)
                .administrador("true")
                .build();
    }
    
    public static UserRequest createUserWithEmptyName() {
        return UserRequest.builder()
                .nome("")
                .email(generateUniqueEmail())
                .password(DEFAULT_PASSWORD)
                .administrador("true")
                .build();
    }
    
    public static UserRequest createUserWithoutPassword() {
        return UserRequest.builder()
                .nome("Usuario Sem Senha")
                .email(generateUniqueEmail())
                .password(null)
                .administrador("true")
                .build();
    }
    
    public static UserRequest createUserWithEmptyPassword() {
        return UserRequest.builder()
                .nome("Usuario Senha Vazia")
                .email(generateUniqueEmail())
                .password("")
                .administrador("true")
                .build();
    }
    
    public static UserRequest createUserWithShortPassword() {
        return UserRequest.builder()
                .nome("Usuario Senha Curta")
                .email(generateUniqueEmail())
                .password("12")
                .administrador("true")
                .build();
    }
    
    public static UserRequest createUserWithoutAdministrador() {
        return UserRequest.builder()
                .nome("Usuario Sem Flag Admin")
                .email(generateUniqueEmail())
                .password(DEFAULT_PASSWORD)
                .administrador(null)
                .build();
    }
    
    public static UserRequest createUserWithInvalidAdministrador() {
        return UserRequest.builder()
                .nome("Usuario Flag Admin Invalida")
                .email(generateUniqueEmail())
                .password(DEFAULT_PASSWORD)
                .administrador("invalido")
                .build();
    }
    
    public static UserRequest createCustomUser(String nome, String email, String password, boolean isAdmin) {
        return UserRequest.builder()
                .nome(nome)
                .email(email)
                .password(password)
                .admin(isAdmin)
                .build();
    }
    
    public static LoginRequest createValidLogin(String email, String password) {
        return LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
    
    public static LoginRequest createLoginWithoutEmail() {
        return LoginRequest.builder()
                .email(null)
                .password(DEFAULT_PASSWORD)
                .build();
    }
    
    public static LoginRequest createLoginWithEmptyEmail() {
        return LoginRequest.builder()
                .email("")
                .password(DEFAULT_PASSWORD)
                .build();
    }
    
    public static LoginRequest createLoginWithInvalidEmail() {
        return LoginRequest.builder()
                .email("email-invalido")
                .password(DEFAULT_PASSWORD)
                .build();
    }
    
    public static LoginRequest createLoginWithoutPassword() {
        return LoginRequest.builder()
                .email(generateUniqueEmail())
                .password(null)
                .build();
    }
    
    public static LoginRequest createLoginWithEmptyPassword() {
        return LoginRequest.builder()
                .email(generateUniqueEmail())
                .password("")
                .build();
    }
    
    public static LoginRequest createLoginWithWrongPassword() {
        return LoginRequest.builder()
                .email(generateUniqueEmail())
                .password("senhaerrada123")
                .build();
    }
    
    public static String generateUniqueEmail() {
        return "qa.test." + UUID.randomUUID().toString().substring(0, 8) + "@carrefour.com";
    }
    
    public static String generateUniqueEmailWithTimestamp() {
        return "qa.test." + System.currentTimeMillis() + "@carrefour.com";
    }
    
    public static String generateRandomName() {
        return "Usuario " + UUID.randomUUID().toString().substring(0, 8);
    }
    
    public static String getDefaultPassword() {
        return DEFAULT_PASSWORD;
    }
}
