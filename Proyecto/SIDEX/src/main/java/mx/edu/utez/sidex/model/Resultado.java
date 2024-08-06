package mx.edu.utez.sidex.model;

import java.util.Map;

public class Resultado {
    private int estudianteId;
    private String estudianteNombre;
    private double calificacion;
    private int aciertos;
    private int totalPreguntas;
    private int respuestasIncorrectas;
    private int examenId;
    private boolean aprobado;

    public Resultado(int estudianteId, String estudianteNombre, double calificacion, int aciertos, int totalPreguntas, int respuestasIncorrectas, int examenId, boolean aprobado) {
        this.estudianteId = estudianteId;
        this.estudianteNombre = estudianteNombre;
        this.calificacion = calificacion;
        this.aciertos = aciertos;
        this.totalPreguntas = totalPreguntas;
        this.respuestasIncorrectas = respuestasIncorrectas;
        this.examenId = examenId;
        this.aprobado = aprobado;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(int estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public int getAciertos() {
        return aciertos;
    }

    public void setAciertos(int aciertos) {
        this.aciertos = aciertos;
    }

    public int getTotalPreguntas() {
        return totalPreguntas;
    }

    public void setTotalPreguntas(int totalPreguntas) {
        this.totalPreguntas = totalPreguntas;
    }

    public int getRespuestasIncorrectas() {
        return respuestasIncorrectas;
    }

    public void setRespuestasIncorrectas(int respuestasIncorrectas) {
        this.respuestasIncorrectas = respuestasIncorrectas;
    }

    public int getExamenId() {
        return examenId;
    }

    public void setExamenId(int examenId) {
        this.examenId = examenId;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    /**
     * Determina el rango de calificación basado en los rangos dados.
     *
     * @param rangosCalificacion Mapa de rangos de calificación con llaves como "minAU", "maxAU", etc.
     * @return String representando el rango de calificación ("AU", "DE", "SA", "NA").
     */
    public String getRango(Map<String, Double> rangosCalificacion) {
        if (calificacion >= rangosCalificacion.get("minAU") && calificacion <= rangosCalificacion.get("maxAU")) {
            return "AU";
        } else if (calificacion >= rangosCalificacion.get("minDE") && calificacion <= rangosCalificacion.get("maxDE")) {
            return "DE";
        } else if (calificacion >= rangosCalificacion.get("minSA") && calificacion <= rangosCalificacion.get("maxSA")) {
            return "SA";
        } else {
            return "NA";
        }
    }
}
