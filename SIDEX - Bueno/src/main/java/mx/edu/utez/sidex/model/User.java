package mx.edu.utez.sidex.model;

public class User {
    private int id;
    private String nombre;
    private String apellido;
    private String apellido2;
    private String contra;
    private  String correo;
    private  String codigo;
    private boolean estado;

    public User(){}
    public User(int id, String nombre, String contra,String apellido, String apellido2, String correo, String codigo, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.apellido2 = apellido2;
        this.contra = contra;
        this.correo = correo;
        this.codigo = codigo;
        this.estado = estado;
    }

    public User(String nombre, String apellido, String apellido2, String correo, String contra) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.apellido2 = apellido2;
        this.contra = contra;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public int getId() {
        return id;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCodigo() {
        return codigo;
    }

    public boolean isEstado() {
        return estado;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getApellido() {
        return apellido;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }


}
