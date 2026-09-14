package com.ecolim.app.data.remote.dto;

public class RecoleccionDTO {
    private int idUsuario;
    private int idTipo;
    private int idZona;
    private double cantidadKg;
    private String fechaHora;
    private String ubicacion;

    public RecoleccionDTO(int idUsuario, int idTipo, int idZona, double cantidadKg, String fechaHora, String ubicacion) {
        this.idUsuario = idUsuario;
        this.idTipo = idTipo;
        this.idZona = idZona;
        this.cantidadKg = cantidadKg;
        this.fechaHora = fechaHora;
        this.ubicacion = ubicacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public double getCantidadKg() {
        return cantidadKg;
    }

    public void setCantidadKg(double cantidadKg) {
        this.cantidadKg = cantidadKg;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
