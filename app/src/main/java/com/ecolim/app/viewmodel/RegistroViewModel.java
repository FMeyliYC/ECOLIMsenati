package com.ecolim.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ecolim.app.data.local.entities.Recoleccion;
import com.ecolim.app.data.local.entities.TipoResiduo;
import com.ecolim.app.data.local.entities.Zona;
import com.ecolim.app.repository.RecoleccionRepository;

import java.util.List;

public class RegistroViewModel extends AndroidViewModel {

    private final RecoleccionRepository repository;
    private final LiveData<List<TipoResiduo>> tiposResiduo;
    private final LiveData<List<Zona>> zonas;
    private final LiveData<Integer> cantidadPendientes;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> mensajeExito = new MutableLiveData<>();
    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();

    public RegistroViewModel(@NonNull Application application) {
        super(application);
        repository = RecoleccionRepository.getInstance(application);
        tiposResiduo = repository.getTiposResiduo();
        zonas = repository.getZonas();
        cantidadPendientes = repository.getCantidadPendientes();
    }

    public LiveData<List<TipoResiduo>> getTiposResiduo() {
        return tiposResiduo;
    }

    public LiveData<List<Zona>> getZonas() {
        return zonas;
    }

    public LiveData<Integer> getCantidadPendientes() {
        return cantidadPendientes;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMensajeExito() {
        return mensajeExito;
    }

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    public void guardarRegistro(int idUsuario, int idTipo, int idZona, double cantidadKg,
                                String fechaHora, String ubicacion, String fotoEvidencia) {
        if (idTipo <= 0) {
            mensajeError.setValue("Seleccione un tipo de residuo válido.");
            return;
        }
        if (idZona <= 0) {
            mensajeError.setValue("Seleccione una zona válida.");
            return;
        }
        if (cantidadKg <= 0.0) {
            mensajeError.setValue("Ingrese una cantidad en kg mayor a 0.");
            return;
        }

        isLoading.setValue(true);
        Recoleccion recoleccion = new Recoleccion(
                idUsuario, idTipo, idZona, cantidadKg,
                fechaHora, ubicacion, fotoEvidencia, false
        );

        repository.registrarRecoleccion(recoleccion, new RecoleccionRepository.OnOperacionListener() {
            @Override
            public void onSuccess(long idGenerado) {
                isLoading.postValue(false);
                mensajeExito.postValue("¡Recolección guardada exitosamente en modo local!");
            }

            @Override
            public void onError(String mensaje) {
                isLoading.postValue(false);
                mensajeError.postValue("Error al guardar: " + mensaje);
            }
        });
    }

    public void sincronizarAhora() {
        isLoading.setValue(true);
        repository.sincronizarPendientes((sincronizados, fallidos) -> {
            isLoading.postValue(false);
            if (sincronizados > 0 && fallidos == 0) {
                mensajeExito.postValue("Sincronización completada: " + sincronizados + " registros subidos.");
            } else if (sincronizados > 0) {
                mensajeExito.postValue("Sincronizados " + sincronizados + " registros. " + fallidos + " pendientes.");
            } else if (fallidos > 0) {
                mensajeError.postValue("No se pudo conectar al servidor. Los registros permanecen guardados localmente.");
            } else {
                mensajeExito.postValue("Todos los registros ya están sincronizados.");
            }
        });
    }
}
