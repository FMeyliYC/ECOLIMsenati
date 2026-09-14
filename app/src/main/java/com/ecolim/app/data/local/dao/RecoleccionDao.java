package com.ecolim.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ecolim.app.data.local.entities.Recoleccion;
import com.ecolim.app.data.local.entities.RecoleccionConDetalle;
import com.ecolim.app.data.local.entities.ResumenTipoResiduo;

import java.util.List;

@Dao
public interface RecoleccionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(Recoleccion recoleccion);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodas(List<Recoleccion> recolecciones);

    @Update
    void actualizar(Recoleccion recoleccion);

    @Delete
    void eliminar(Recoleccion recoleccion);

    @Query("SELECT * FROM recolecciones WHERE idRecoleccion = :id LIMIT 1")
    Recoleccion obtenerPorId(int id);

    @Query("SELECT * FROM recolecciones ORDER BY fechaHora DESC")
    LiveData<List<Recoleccion>> obtenerTodas();

    @Query("SELECT r.idRecoleccion, r.idUsuario, r.idTipo, r.idZona, r.cantidadKg, " +
           "r.fechaHora, r.ubicacion, r.fotoEvidencia, r.sincronizado, " +
           "t.nombreResiduo, z.nombreZona, u.nombre AS nombreUsuario " +
           "FROM recolecciones r " +
           "LEFT JOIN tipos_residuo t ON r.idTipo = t.idTipo " +
           "LEFT JOIN zonas z ON r.idZona = z.idZona " +
           "LEFT JOIN usuarios u ON r.idUsuario = u.idUsuario " +
           "ORDER BY r.fechaHora DESC")
    LiveData<List<RecoleccionConDetalle>> obtenerTodasConDetalle();

    @Query("SELECT * FROM recolecciones WHERE sincronizado = 0")
    List<Recoleccion> obtenerPendientes();

    @Query("SELECT * FROM recolecciones WHERE fechaHora BETWEEN :inicio AND :fin ORDER BY fechaHora DESC")
    List<Recoleccion> filtrarPorFecha(String inicio, String fin);

    @Query("SELECT * FROM recolecciones WHERE (:idTipo = 0 OR idTipo = :idTipo) AND (:idZona = 0 OR idZona = :idZona) ORDER BY fechaHora DESC")
    List<Recoleccion> filtrarPorTipoYZona(int idTipo, int idZona);

    @Query("UPDATE recolecciones SET sincronizado = 1 WHERE idRecoleccion = :idRecoleccion")
    void marcarSincronizado(int idRecoleccion);

    // Consulta para la pantalla de Reportes agrupada por tipo de residuo
    @Query("SELECT t.nombreResiduo, SUM(r.cantidadKg) AS totalKg " +
           "FROM recolecciones r " +
           "INNER JOIN tipos_residuo t ON r.idTipo = t.idTipo " +
           "WHERE (date(r.fechaHora) >= date(:fechaInicio) AND date(r.fechaHora) <= date(:fechaFin)) " +
           "AND (:idTipo = 0 OR r.idTipo = :idTipo) " +
           "GROUP BY t.idTipo, t.nombreResiduo " +
           "ORDER BY totalKg DESC")
    LiveData<List<ResumenTipoResiduo>> obtenerResumenReporte(String fechaInicio, String fechaFin, int idTipo);

    @Query("SELECT SUM(cantidadKg) FROM recolecciones " +
           "WHERE (date(fechaHora) >= date(:fechaInicio) AND date(fechaHora) <= date(:fechaFin)) " +
           "AND (:idTipo = 0 OR idTipo = :idTipo)")
    LiveData<Double> obtenerTotalKgPeriodo(String fechaInicio, String fechaFin, int idTipo);

    @Query("SELECT COUNT(*) FROM recolecciones WHERE sincronizado = 0")
    LiveData<Integer> contarPendientes();

    @Query("SELECT COUNT(*) FROM recolecciones")
    int contar();
}
