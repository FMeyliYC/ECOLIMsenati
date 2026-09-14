package com.ecolim.app.data.remote;

import com.ecolim.app.data.remote.dto.LoginRequest;
import com.ecolim.app.data.remote.dto.LoginResponse;
import com.ecolim.app.data.remote.dto.RecoleccionDTO;
import com.ecolim.app.data.remote.dto.ReporteDTO;
import com.ecolim.app.data.remote.dto.RespuestaApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/recolecciones")
    Call<RespuestaApi> sincronizarRecoleccion(@Body RecoleccionDTO recoleccion);

    @GET("api/reportes")
    Call<List<ReporteDTO>> obtenerReportes(
            @Query("desde") String desde,
            @Query("hasta") String hasta,
            @Query("tipo") String tipo,
            @Query("zona") Integer zona
    );
}
