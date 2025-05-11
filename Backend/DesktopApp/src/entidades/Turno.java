package entidades;

import java.time.LocalDateTime;

public class Turno {
    private int id;
    private int idPaciente;
    private int idProfesional;
    private int idEspecialidad;
    private LocalDateTime fechaTurno;        // Fecha y hora programada de la cita
    private LocalDateTime fechaSolicitud;    // Momento en que se solicita el turno
    private LocalDateTime fechaConfirmacion; // Se actualizará cuando se confirme el turno
    private String estado;                   // 'confirmado', 'cancelado', 'rechazado', 'reprogramado'
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Turno() {
    }

    // Constructor completo (opcional)
    public Turno(int id, int idPaciente, int idProfesional, int idEspecialidad, 
                 LocalDateTime fechaTurno, LocalDateTime fechaSolicitud,
                 LocalDateTime fechaConfirmacion, String estado,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idProfesional = idProfesional;
        this.idEspecialidad = idEspecialidad;
        this.fechaTurno = fechaTurno;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaConfirmacion = fechaConfirmacion;
        this.estado = estado;
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

    public int getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
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

    public LocalDateTime getFechaTurno() {
        return fechaTurno;
    }
    public void setFechaTurno(LocalDateTime fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public LocalDateTime getFechaConfirmacion() {
        return fechaConfirmacion;
    }
    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
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
        return "Turno{" +
                "id=" + id +
                ", idPaciente=" + idPaciente +
                ", idProfesional=" + idProfesional +
                ", idEspecialidad=" + idEspecialidad +
                ", fechaTurno=" + fechaTurno +
                ", fechaSolicitud=" + fechaSolicitud +
                ", fechaConfirmacion=" + fechaConfirmacion +
                ", estado='" + estado + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}