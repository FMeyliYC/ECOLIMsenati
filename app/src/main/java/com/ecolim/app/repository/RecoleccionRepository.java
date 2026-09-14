package com.ecolim.app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.ecolim.app.data.local.AppDatabase;
import com.ecolim.app.data.local.dao.RecoleccionDao;
import com.ecolim.app.data.local.dao.SyncLogDao;
import com.ecolim.app.data.local.dao.TipoResiduoDao;
import com.ecolim.app.data.local.dao.ZonaDao;
import com.ecolim.app.data.local.entities.Recoleccion;
import com.ecolim.app.data.local.entities.RecoleccionConDetalle;
import com.ecolim.app.data.local.entities.ResumenTipoResiduo;
import com.ecolim.app.data.local.entities.SyncLog;
import com.ecolim.app.data.local.entities.TipoResiduo;
import com.ecolim.app.data.local.entities.Zona;
import com.ecolim.app.data.remote.ApiService;
import com.ecolim.app.data.remote.RetrofitClient;
import com.ecolim.app.data.remote.dto.RecoleccionDTO;
import com.ecolim.app.data.remote.dto.RespuestaApi;
import com.ecolim.app.utils.DateUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class RecoleccionRepository {

    private static final String TAG = "RecoleccionRepository";
    private static volatile RecoleccionRepository INSTANCE;

    private final RecoleccionDao recoleccionDao;
    private final TipoResiduoDao tipoResiduoDao;
    private final ZonaDao zonaDao;
    private final SyncLogDao syncLogDao;
    private final ApiService apiService;
    private final ExecutorService executor;

    public interface OnOperacionListener {
        void onSuccess(long idGenerado);
        void onError(String mensaje);
    }

    public interface OnSyncListener {
        void onSyncComplete(int sincronizados, int fallidos);
    }

    private RecoleccionRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        recoleccionDao = db.recoleccionDao();
        tipoResiduoDao = db.tipoResiduoDao();
        zonaDao = db.zonaDao();
        syncLogDao = db.syncLogDao();
        apiService = RetrofitClient.getInstance().getApiService();
        executor = Executors.newSingleThreadExecutor();
    }

    public static synchronized RecoleccionRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RecoleccionRepository(context);
        }
        return INSTANCE;
    }

    public LiveData<List<TipoResiduo>> getTiposResiduo() {
        return tipoResiduoDao.obtenerTodos();
    }

    public LiveData<List<Zona>> getZonas() {
        return zonaDao.obtenerTodos();
    }

    public LiveData<List<RecoleccionConDetalle>> getTodasLasRecolecciones() {
        return recoleccionDao.obtenerTodasConDetalle();
    }

    public LiveData<Integer> getCantidadPendientes() {
        return recoleccionDao.contarPendientes();
    }

    public LiveData<List<ResumenTipoResiduo>> getResumenReporte(String fechaInicio, String fechaFin, int idTipo) {
        return recoleccionDao.obtenerResumenReporte(fechaInicio, fechaFin, idTipo);
    }

    public LiveData<Double> getTotalKgPeriodo(String fechaInicio, String fechaFin, int idTipo) {
        return recoleccionDao.obtenerTotalKgPeriodo(fechaInicio, fechaFin, idTipo);
    }

    public void registrarRecoleccion(Recoleccion recoleccion, OnOperacionListener listener) {
        executor.execute(() -> {
            try {
                long id = recoleccionDao.insertar(recoleccion);
                if (listener != null) {
                    listener.onSuccess(id);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error al registrar recolección localmente", e);
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Proceso de sincronización offline-first: consulta pendientes en Room y los envía al backend.
     */
    public void sincronizarPendientes(OnSyncListener listener) {
        executor.execute(() -> {
            List<Recoleccion> pendientes = recoleccionDao.obtenerPendientes();
            int sincronizados = 0;
            int fallidos = 0;

            for (Recoleccion r : pendientes) {
                RecoleccionDTO dto = new RecoleccionDTO(
                        r.getIdUsuario(),
                        r.getIdTipo(),
                        r.getIdZona(),
                        r.getCantidadKg(),
                        r.getFechaHora(),
                        r.getUbicacion()
                );

                try {
                    Response<RespuestaApi> response = apiService.sincronizarRecoleccion(dto).execute();
                    String ahora = DateUtils.getFechaHoraActual();

                    if (response.isSuccessful() && response.body() != null) {
                        // Sincronización exitosa
                        recoleccionDao.marcarSincronizado(r.getIdRecoleccion());
                        syncLogDao.insertar(new SyncLog(r.getIdRecoleccion(), ahora, "OK"));
                        sincronizados++;
                    } else {
                        // Respuesta no exitosa del servidor
                        String errorMsg = "HTTP " + response.code();
                        syncLogDao.insertar(new SyncLog(r.getIdRecoleccion(), ahora, "ERROR: " + errorMsg));
                        fallidos++;
                    }
                } catch (Exception e) {
                    // Error de conexión / offline
                    Log.w(TAG, "Fallo al sincronizar recolección ID: " + r.getIdRecoleccion(), e);
                    syncLogDao.insertar(new SyncLog(r.getIdRecoleccion(), DateUtils.getFechaHoraActual(), "ERROR: " + e.getMessage()));
                    fallidos++;
                }
            }

            if (listener != null) {
                listener.onSyncComplete(sincronizados, fallidos);
            }
        });
    }
}
