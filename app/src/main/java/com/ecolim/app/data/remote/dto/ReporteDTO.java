package com.ecolim.app.data.remote.dto;

public class ReporteDTO {
    private String tipoResiduo;
    private double totalKg;

    public ReporteDTO(String tipoResiduo, double totalKg) {
        this.tipoResiduo = tipoResiduo;
        this.totalKg = totalKg;
    }

    public String getTipoResiduo() {
        return tipoResiduo;
    }

    public void setTipoResiduo(String tipoResiduo) {
        this.tipoResiduo = tipoResiduo;
    }

    public double getTotalKg() {
        return totalKg;
    }

    public void setTotalKg(double totalKg) {
        this.totalKg = totalKg;
    }
}
