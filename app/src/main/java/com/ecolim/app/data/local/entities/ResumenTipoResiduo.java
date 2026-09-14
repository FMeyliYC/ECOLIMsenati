package com.ecolim.app.data.local.entities;

public class ResumenTipoResiduo {
    private String nombreResiduo;
    private double totalKg;

    public ResumenTipoResiduo(String nombreResiduo, double totalKg) {
        this.nombreResiduo = nombreResiduo;
        this.totalKg = totalKg;
    }

    public String getNombreResiduo() {
        return nombreResiduo;
    }

    public void setNombreResiduo(String nombreResiduo) {
        this.nombreResiduo = nombreResiduo;
    }

    public double getTotalKg() {
        return totalKg;
    }

    public void setTotalKg(double totalKg) {
        this.totalKg = totalKg;
    }
}
