package entidades;

import java.time.LocalDateTime;

public class Paciente extends Persona {

    private String email;
    private String password;
    private LocalDateTime createdAt; // Representa el campo created_at de la BD
    private LocalDateTime updatedAt; // Representa el campo updated_at de la BD

    // Constructor vacío
    public Paciente() {
        super();
    }

    // Constructor completo, incluyendo atributos heredados de Persona
    public Paciente(int id, String nombre, String dni, char sexo,
                    String email, String password,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, nombre, dni, sexo);
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters para los atributos del Paciente
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
  
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
  
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
  
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
  
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
  
    @Override
    public String toString() {
        return "Paciente{" +
               "id=" + getId() + ", " +
               "nombre='" + getNombre() + "', " +
               "dni='" + getDni() + "', " +
               "sexo=" + getSexo() + ", " +
               "email='" + email + "', " +
               "password='" + password + "', " +
               "createdAt=" + createdAt + ", " +
               "updatedAt=" + updatedAt +
               '}';
    }
}