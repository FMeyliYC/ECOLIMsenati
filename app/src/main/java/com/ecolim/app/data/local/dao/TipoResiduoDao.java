package com.ecolim.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ecolim.app.data.local.entities.TipoResiduo;

import java.util.List;

@Dao
public interface TipoResiduoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(TipoResiduo tipoResiduo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodos(List<TipoResiduo> tipos);

    @Update
    void actualizar(TipoResiduo tipoResiduo);

    @Delete
    void eliminar(TipoResiduo tipoResiduo);

    @Query("SELECT * FROM tipos_residuo ORDER BY nombreResiduo ASC")
    List<TipoResiduo> obtenerTodosSync();

    @Query("SELECT * FROM tipos_residuo ORDER BY nombreResiduo ASC")
    LiveData<List<TipoResiduo>> obtenerTodos();

    @Query("SELECT * FROM tipos_residuo WHERE idTipo = :idTipo LIMIT 1")
    TipoResiduo obtenerPorId(int idTipo);

    @Query("SELECT COUNT(*) FROM tipos_residuo")
    int contar();
}
