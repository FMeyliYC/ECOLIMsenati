package com.ecolim.app.data.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "recolecciones",
    foreignKeys = {
        @ForeignKey(
            entity = Usuario.class,
            parentColumns = "idUsuario",
            childColumns = "idUsuario",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = TipoResiduo.class,
            parentColumns = "idTipo",
            childColumns = "idTipo",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Zona.class,
            parentColumns = "idZona",
            childColumns = "idZona",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index(value = "idUsuario"),
        @Index(value = "idTipo"),
        @Index(value = "idZona")
    }
)
public class Recoleccion {

    @PrimaryKey(autoGenerate = true)
    private int idRecoleccion;

    private int idUsuario;
    private int idTipo;
    private int idZona;
    private double cantidadKg;
    private String fechaHora; // ISO 8601
    private String ubicacion; // "lat,long"
    private String fotoEvidencia; // ruta local
    private boolean sincronizado; // default false

    public Recoleccion(int idUsuario, int idTipo, int idZona, double cantidadKg,
                       String fechaHora, String ubicacion, String fotoEvidencia, boolean sincronizado) {
        this.idUsuario = idUsuario;
        this.idTipo = idTipo;
        this.idZona = idZona;
        this.cantidadKg = cantidadKg;
        this.fechaHora = fechaHora;
        this.ubicacion = ubicacion;
        this.fotoEvidencia = fotoEvidencia;
        this.sincronizado = sincronizado;
    }

    public int getIdRecoleccion() {
        return idRecoleccion;
    }

    public void setIdRecoleccion(int idRecoleccion) {
        this.idRecoleccion = idRecoleccion;
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

    public String getFotoEvidencia() {
        return fotoEvidencia;
    }

    public void setFotoEvidencia(String fotoEvidencia) {
        this.fotoEvidencia = fotoEvidencia;
    }

    public boolean isSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }
}
