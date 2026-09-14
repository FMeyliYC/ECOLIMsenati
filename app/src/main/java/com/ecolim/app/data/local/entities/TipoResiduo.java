package com.ecolim.app.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tipos_residuo")
public class TipoResiduo {

    @PrimaryKey(autoGenerate = true)
    private int idTipo;

    private String nombreResiduo; // ej. "Plástico", "Papel/cartón", "Orgánico"
    private String categoria;
    private String unidadMedida; // ej. "kg"

    public TipoResiduo(String nombreResiduo, String categoria, String unidadMedida) {
        this.nombreResiduo = nombreResiduo;
        this.categoria = categoria;
        this.unidadMedida = unidadMedida;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombreResiduo() {
        return nombreResiduo;
    }

    public void setNombreResiduo(String nombreResiduo) {
        this.nombreResiduo = nombreResiduo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    @Override
    public String toString() {
        return nombreResiduo;
    }
}
