package com.ecolim.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.ecolim.app.data.local.entities.ResumenTipoResiduo;
import com.ecolim.app.data.local.entities.TipoResiduo;
import com.ecolim.app.repository.RecoleccionRepository;

import java.util.List;

public class ReportesViewModel extends AndroidViewModel {

    private final RecoleccionRepository repository;
    private final LiveData<List<TipoResiduo>> tiposResiduo;

    private static class FiltroParams {
        final String fechaInicio;
        final String fechaFin;
        final int idTipo;

        FiltroParams(String fechaInicio, String fechaFin, int idTipo) {
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.idTipo = idTipo;
        }
    }

    private final MutableLiveData<FiltroParams> filtroTrigger = new MutableLiveData<>();
    private final LiveData<List<ResumenTipoResiduo>> resumenReporte;
    private final LiveData<Double> totalKgPeriodo;

    public ReportesViewModel(@NonNull Application application) {
        super(application);
        repository = RecoleccionRepository.getInstance(application);
        tiposResiduo = repository.getTiposResiduo();

        resumenReporte = Transformations.switchMap(filtroTrigger, params ->
                repository.getResumenReporte(params.fechaInicio, params.fechaFin, params.idTipo)
        );

        totalKgPeriodo = Transformations.switchMap(filtroTrigger, params ->
                repository.getTotalKgPeriodo(params.fechaInicio, params.fechaFin, params.idTipo)
        );
    }

    public LiveData<List<TipoResiduo>> getTiposResiduo() {
        return tiposResiduo;
    }

    public LiveData<List<ResumenTipoResiduo>> getResumenReporte() {
        return resumenReporte;
    }

    public LiveData<Double> getTotalKgPeriodo() {
        return totalKgPeriodo;
    }

    public void aplicarFiltros(String fechaInicio, String fechaFin, int idTipo) {
        filtroTrigger.setValue(new FiltroParams(fechaInicio, fechaFin, idTipo));
    }
}
