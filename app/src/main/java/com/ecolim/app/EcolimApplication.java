package com.ecolim.app;

import android.app.Application;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.ecolim.app.data.local.AppDatabase;
import com.ecolim.app.data.sync.SyncWorker;

import java.util.concurrent.TimeUnit;

public class EcolimApplication extends Application {

    private static EcolimApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Inicializar base de datos Room y precargar datos
        AppDatabase.getInstance(this);

        // Programar sincronización periódica en segundo plano con WorkManager
        setupPeriodicSync();
    }

    public static EcolimApplication getInstance() {
        return instance;
    }

    private void setupPeriodicSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest syncWorkRequest = new PeriodicWorkRequest.Builder(
                SyncWorker.class,
                15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "EcolimSyncWork",
                ExistingPeriodicWorkPolicy.KEEP,
                syncWorkRequest
        );
    }
}
