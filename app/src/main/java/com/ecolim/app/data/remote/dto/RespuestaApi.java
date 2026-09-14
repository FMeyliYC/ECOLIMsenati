package com.ecolim.app.data.remote.dto;

public class RespuestaApi {
    private String status;
    private int idRemoto;

    public RespuestaApi(String status, int idRemoto) {
        this.status = status;
        this.idRemoto = idRemoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdRemoto() {
        return idRemoto;
    }

    public void setIdRemoto(int idRemoto) {
        this.idRemoto = idRemoto;
    }
}
