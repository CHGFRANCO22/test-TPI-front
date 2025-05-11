package entidades;

public class Persona {
    
    private int id;
    private String nombre;
    private String dni;
    private char sexo; // 'M' o 'F'
    
    // Constructor vacío
    public Persona() {
    }
    
    // Constructor completo
    public Persona(int id, String nombre, String dni, char sexo) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.sexo = sexo;
    }
    
    // Getters y setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public char getSexo() {
        return sexo;
    }
    public void setSexo(char sexo) {
        this.sexo = sexo;
    }
    
    @Override
    public String toString() {
        return "Persona{" +
                    "ID=" + id +
                    ", Nombre='" + nombre + '\'' +
                    ", DNI='" + dni + '\'' +
                    ", Sexo=" + sexo +
                    '}';
    }
}