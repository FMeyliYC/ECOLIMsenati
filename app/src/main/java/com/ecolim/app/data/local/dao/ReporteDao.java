package com.ecolim.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ecolim.app.data.local.entities.Reporte;

import java.util.List;

@Dao
public interface ReporteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(Reporte reporte);

    @Update
    void actualizar(Reporte reporte);

    @Delete
    void eliminar(Reporte reporte);

    @Query("SELECT * FROM reportes ORDER BY idReporte DESC")
    LiveData<List<Reporte>> obtenerTodos();

    @Query("SELECT * FROM reportes WHERE idReporte = :idReporte LIMIT 1")
    Reporte obtenerPorId(int idReporte);
}
