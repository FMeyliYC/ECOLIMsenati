package com.ecolim.app.data.sync;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ecolim.app.repository.RecoleccionRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SyncWorker extends Worker {

    private static final String TAG = "SyncWorker";

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Iniciando tarea periódica de sincronización en segundo plano...");
        RecoleccionRepository repository = RecoleccionRepository.getInstance(getApplicationContext());
        CountDownLatch latch = new CountDownLatch(1);

        repository.sincronizarPendientes((sincronizados, fallidos) -> {
            Log.d(TAG, "Sincronización finalizada: " + sincronizados + " enviados, " + fallidos + " fallidos.");
            latch.countDown();
        });

        try {
            boolean completed = latch.await(30, TimeUnit.SECONDS);
            if (!completed) {
                Log.w(TAG, "Tiempo de espera agotado en la sincronización.");
                return Result.retry();
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupción durante sincronización", e);
            return Result.retry();
        }

        return Result.success();
    }
}
