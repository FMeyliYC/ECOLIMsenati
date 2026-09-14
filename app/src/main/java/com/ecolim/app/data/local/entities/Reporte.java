package com.ecolim.app.data.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "reportes",
    foreignKeys = {
        @ForeignKey(
            entity = Usuario.class,
            parentColumns = "idUsuario",
            childColumns = "idUsuario",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index(value = "idUsuario")
    }
)
public class Reporte {

    @PrimaryKey(autoGenerate = true)
    private int idReporte;

    private int idUsuario;
    private String fechaGeneracion;
    private String rangoFechaInicio;
    private String rangoFechaFin;
    private double totalKg;

    public Reporte(int idUsuario, String fechaGeneracion, String rangoFechaInicio,
                   String rangoFechaFin, double totalKg) {
        this.idUsuario = idUsuario;
        this.fechaGeneracion = fechaGeneracion;
        this.rangoFechaInicio = rangoFechaInicio;
        this.rangoFechaFin = rangoFechaFin;
        this.totalKg = totalKg;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getRangoFechaInicio() {
        return rangoFechaInicio;
    }

    public void setRangoFechaInicio(String rangoFechaInicio) {
        this.rangoFechaInicio = rangoFechaInicio;
    }

    public String getRangoFechaFin() {
        return rangoFechaFin;
    }

    public void setRangoFechaFin(String rangoFechaFin) {
        this.rangoFechaFin = rangoFechaFin;
    }

    public double getTotalKg() {
        return totalKg;
    }

    public void setTotalKg(double totalKg) {
        this.totalKg = totalKg;
    }
}
