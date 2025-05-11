package entidades;

import java.time.LocalDateTime;

public class Profesional extends Persona {

    // Atributos propios de la entidad Profesional
    private int idProfesional; // corresponde al campo id_profesional
    private String email;
    private String password;
    private String rol;        // Valores posibles: "profesional" o "admin"
    private String matricula;
    private String telefono;
    private LocalDateTime createdAt; // Representa el campo created_at de la BD
    private LocalDateTime updatedAt; // Representa el campo updated_at de la BD

    // Constructor vacío
    public Profesional() {
        super();
    }

    // Constructor completo (recuerda que el primer parámetro id es el id de la tabla persona)
    public Profesional(int id, String nombre, String dni, char sexo,
                       int idProfesional, String email, String password, String rol,
                       String matricula, String telefono,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, nombre, dni, sexo);
        this.idProfesional = idProfesional;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.matricula = matricula;
        this.telefono = telefono;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y setters

    public int getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(int idProfesional) {
        this.idProfesional = idProfesional;
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

    public String getRol() {
        return rol;
    }
 
    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMatricula() {
        return matricula;
    }
 
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTelefono() {
        return telefono;
    }
 
    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
        return "Profesional{" +
                "idProfesional=" + idProfesional + ", " +
                "personaId=" + getId() + ", " +
                "nombre='" + getNombre() + "', " +
                "dni='" + getDni() + "', " +
                "sexo=" + getSexo() + ", " +
                "email='" + email + "', " +
                "password='" + password + "', " +
                "rol='" + rol + "', " +
                "matricula='" + matricula + "', " +
                "telefono='" + telefono + "', " +
                "createdAt=" + createdAt + ", " +
                "updatedAt=" + updatedAt +
                '}';
    }
}