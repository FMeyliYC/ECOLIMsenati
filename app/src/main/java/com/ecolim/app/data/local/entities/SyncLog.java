package com.ecolim.app.data.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "sync_logs",
    foreignKeys = {
        @ForeignKey(
            entity = Recoleccion.class,
            parentColumns = "idRecoleccion",
            childColumns = "idRecoleccion",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index(value = "idRecoleccion")
    }
)
public class SyncLog {

    @PrimaryKey(autoGenerate = true)
    private int idSync;

    private int idRecoleccion;
    private String fechaEnvio;
    private String resultadoApi; // "OK" | "ERROR: ..."

    public SyncLog(int idRecoleccion, String fechaEnvio, String resultadoApi) {
        this.idRecoleccion = idRecoleccion;
        this.fechaEnvio = fechaEnvio;
        this.resultadoApi = resultadoApi;
    }

    public int getIdSync() {
        return idSync;
    }

    public void setIdSync(int idSync) {
        this.idSync = idSync;
    }

    public int getIdRecoleccion() {
        return idRecoleccion;
    }

    public void setIdRecoleccion(int idRecoleccion) {
        this.idRecoleccion = idRecoleccion;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getResultadoApi() {
        return resultadoApi;
    }

    public void setResultadoApi(String resultadoApi) {
        this.resultadoApi = resultadoApi;
    }
}
