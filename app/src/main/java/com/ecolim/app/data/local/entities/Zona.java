package com.ecolim.app.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "zonas")
public class Zona {

    @PrimaryKey(autoGenerate = true)
    private int idZona;

    private String nombreZona;
    private String areaCliente;

    public Zona(String nombreZona, String areaCliente) {
        this.nombreZona = nombreZona;
        this.areaCliente = areaCliente;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getAreaCliente() {
        return areaCliente;
    }

    public void setAreaCliente(String areaCliente) {
        this.areaCliente = areaCliente;
    }

    @Override
    public String toString() {
        return nombreZona;
    }
}
