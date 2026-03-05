package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("administrador")
    private String administrador;
    
    public UserRequest() {
    }
    
    public UserRequest(String nome, String email, String password, String administrador) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.administrador = administrador;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAdministrador() {
        return administrador;
    }
    
    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }
    
    @Override
    public String toString() {
        return "UserRequest{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + (password != null ? "***" : null) + '\'' +
                ", administrador='" + administrador + '\'' +
                '}';
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String nome;
        private String email;
        private String password;
        private String administrador;
        
        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        public Builder administrador(String administrador) {
            this.administrador = administrador;
            return this;
        }
        
        public Builder admin(boolean isAdmin) {
            this.administrador = isAdmin ? "true" : "false";
            return this;
        }
        
        public UserRequest build() {
            return new UserRequest(nome, email, password, administrador);
        }
    }
}
