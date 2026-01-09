package com.sonda.dto;

public class OperacionDto {
    private long numeroRegistro;
    private int operacion;

    // Constructores
    public OperacionDto() {}

    public OperacionDto(long numeroRegistro, int operacion) {
        this.numeroRegistro = numeroRegistro;
        this.operacion = operacion;
    }

    // Getters y Setters
    public long getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(long numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public int getOperacion() { return operacion; }
    public void setOperacion(int operacion) { this.operacion = operacion; }
}
