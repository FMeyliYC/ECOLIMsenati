package com.ecolim.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ecolim.app.data.local.entities.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(Usuario usuario);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodos(List<Usuario> usuarios);

    @Update
    void actualizar(Usuario usuario);

    @Delete
    void eliminar(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE (usuario = :identifier OR dni = :identifier) LIMIT 1")
    Usuario autenticar(String identifier);

    @Query("SELECT * FROM usuarios WHERE idUsuario = :idUsuario LIMIT 1")
    Usuario obtenerPorId(int idUsuario);

    @Query("SELECT * FROM usuarios")
    List<Usuario> obtenerTodos();

    @Query("SELECT COUNT(*) FROM usuarios")
    int contar();
}
