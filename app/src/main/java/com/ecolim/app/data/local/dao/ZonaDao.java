package com.ecolim.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ecolim.app.data.local.entities.Zona;

import java.util.List;

@Dao
public interface ZonaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(Zona zona);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodos(List<Zona> zonas);

    @Update
    void actualizar(Zona zona);

    @Delete
    void eliminar(Zona zona);

    @Query("SELECT * FROM zonas ORDER BY nombreZona ASC")
    List<Zona> obtenerTodosSync();

    @Query("SELECT * FROM zonas ORDER BY nombreZona ASC")
    LiveData<List<Zona>> obtenerTodos();

    @Query("SELECT * FROM zonas WHERE idZona = :idZona LIMIT 1")
    Zona obtenerPorId(int idZona);

    @Query("SELECT COUNT(*) FROM zonas")
    int contar();
}
