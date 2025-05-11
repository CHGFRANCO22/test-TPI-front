package entidades;

import java.time.LocalDateTime;

public class ProfesionalEspecialidad {
    private int id;
    private int idProfesional;
    private int idEspecialidad;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public ProfesionalEspecialidad() {
    }

    // Constructor completo
    public ProfesionalEspecialidad(int id, int idProfesional, int idEspecialidad, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.idProfesional = idProfesional;
        this.idEspecialidad = idEspecialidad;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(int idProfesional) {
        this.idProfesional = idProfesional;
    }

    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
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
        return "ProfesionalEspecialidad{" +
                "id=" + id +
                ", idProfesional=" + idProfesional +
                ", idEspecialidad=" + idEspecialidad +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}