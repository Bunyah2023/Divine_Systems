package mx.edu.utez.sidex.model;

public class User {
    private int id;
    private String nombres;
    private String apellido;
    private String apellidoMaterno;
    private String correo;
    private String contra;
    private int rolId;  // Usamos rolId para relacionarlo con la tabla roles
    private boolean estado;

    // Constructor completo
    public User(int id, String nombres, String apellido, String apellidoMaterno, String correo, String contra, int rolId, boolean estado) {
        this.id = id;
        this.nombres = nombres;
        this.apellido = apellido;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.contra = contra;
        this.rolId = rolId;
        this.estado = estado;
    }

    // Constructor sin id y estado (útil para crear nuevos usuarios)
    public User(String nombres, String apellido, String apellidoMaterno, String correo, String contra, int rolId) {
        this.nombres = nombres;
        this.apellido = apellido;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.contra = contra;
        this.rolId = rolId;
        this.estado = true; // Por defecto, asumimos que el usuario es activo
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
