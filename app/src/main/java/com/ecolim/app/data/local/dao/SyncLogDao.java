package com.ecolim.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ecolim.app.data.local.entities.SyncLog;

import java.util.List;

@Dao
public interface SyncLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(SyncLog syncLog);

    @Delete
    void eliminar(SyncLog syncLog);

    @Query("SELECT * FROM sync_logs ORDER BY idSync DESC")
    LiveData<List<SyncLog>> obtenerTodos();

    @Query("SELECT * FROM sync_logs WHERE idRecoleccion = :idRecoleccion ORDER BY idSync DESC LIMIT 1")
    SyncLog obtenerUltimoPorRecoleccion(int idRecoleccion);
}
