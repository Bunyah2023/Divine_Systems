package mx.edu.utez.sidex.model;

public class Pregunta {
    private int id;
    private String texto;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private int respuestaCorrecta;
    private int examenId;

    // Constructor completo
    public Pregunta(int id, String texto, String opcion1, String opcion2, String opcion3, String opcion4, int respuestaCorrecta, int examenId) {
        this.id = id;
        this.texto = texto;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.opcion4 = opcion4;
        this.respuestaCorrecta = respuestaCorrecta;
        this.examenId = examenId;
    }

    // Constructor sin ID (útil para la creación de nuevas preguntas)
    public Pregunta(String texto, String opcion1, String opcion2, String opcion3, String opcion4, int respuestaCorrecta, int examenId) {
        this(0, texto, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta, examenId);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public void setOpcion1(String opcion1) {
        this.opcion1 = opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public void setOpcion3(String opcion3) {
        this.opcion3 = opcion3;
    }

    public String getOpcion4() {
        return opcion4;
    }

    public void setOpcion4(String opcion4) {
        this.opcion4 = opcion4;
    }

    public int getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(int respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public int getExamenId() {
        return examenId;
    }

    public void setExamenId(int examenId) {
        this.examenId = examenId;
    }

    // Método para obtener la opción correcta
    public String getOpcion(int index) {
        switch (index) {
            case 1:
                return opcion1;
            case 2:
                return opcion2;
            case 3:
                return opcion3;
            case 4:
                return opcion4;
            default:
                throw new IllegalArgumentException("Índice de opción no válido: " + index);
        }
    }

    // Método toString para mostrar información de la pregunta
    @Override
    public String toString() {
        return "Pregunta{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", opcion1='" + opcion1 + '\'' +
                ", opcion2='" + opcion2 + '\'' +
                ", opcion3='" + opcion3 + '\'' +
                ", opcion4='" + opcion4 + '\'' +
                ", respuestaCorrecta=" + respuestaCorrecta +
                ", examenId=" + examenId +
                '}';
    }
}
