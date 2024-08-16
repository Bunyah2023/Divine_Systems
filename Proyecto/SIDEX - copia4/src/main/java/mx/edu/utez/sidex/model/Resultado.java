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
        if (aciertos >= 0 && aciertos <= totalPreguntas) {
            this.aciertos = aciertos;
        } else {
            throw new IllegalArgumentException("Aciertos no pueden ser negativos o mayores que el total de preguntas.");
        }
    }

    public int getTotalPreguntas() {
        return totalPreguntas;
    }

    public void setTotalPreguntas(int totalPreguntas) {
        if (totalPreguntas >= 0) {
            this.totalPreguntas = totalPreguntas;
        } else {
            throw new IllegalArgumentException("El total de preguntas no puede ser negativo.");
        }
    }

    public int getRespuestasIncorrectas() {
        return respuestasIncorrectas;
    }

    public void setRespuestasIncorrectas(int respuestasIncorrectas) {
        if (respuestasIncorrectas >= 0) {
            this.respuestasIncorrectas = respuestasIncorrectas;
        } else {
            throw new IllegalArgumentException("Las respuestas incorrectas no pueden ser negativas.");
        }
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
        if (rangosCalificacion == null) {
            throw new IllegalArgumentException("Los rangos de calificación no pueden ser nulos.");
        }

        Double minAU = rangosCalificacion.get("minAU");
        Double maxAU = rangosCalificacion.get("maxAU");
        Double minDE = rangosCalificacion.get("minDE");
        Double maxDE = rangosCalificacion.get("maxDE");
        Double minSA = rangosCalificacion.get("minSA");
        Double maxSA = rangosCalificacion.get("maxSA");

        if (minAU == null || maxAU == null || minDE == null || maxDE == null || minSA == null || maxSA == null) {
            throw new IllegalStateException("Los rangos de calificación no están completamente definidos.");
        }

        if (calificacion >= minAU && calificacion <= maxAU) {
            return "AU";
        } else if (calificacion >= minDE && calificacion <= maxDE) {
            return "DE";
        } else if (calificacion >= minSA && calificacion <= maxSA) {
            return "SA";
        } else {
            return "NA";
        }
    }

    public boolean getAprobado() {
        return aprobado;
    }

}
