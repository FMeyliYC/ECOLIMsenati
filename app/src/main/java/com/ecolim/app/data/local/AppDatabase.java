package com.ecolim.app.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ecolim.app.data.local.dao.RecoleccionDao;
import com.ecolim.app.data.local.dao.ReporteDao;
import com.ecolim.app.data.local.dao.SyncLogDao;
import com.ecolim.app.data.local.dao.TipoResiduoDao;
import com.ecolim.app.data.local.dao.UsuarioDao;
import com.ecolim.app.data.local.dao.ZonaDao;
import com.ecolim.app.data.local.entities.Recoleccion;
import com.ecolim.app.data.local.entities.Reporte;
import com.ecolim.app.data.local.entities.SyncLog;
import com.ecolim.app.data.local.entities.TipoResiduo;
import com.ecolim.app.data.local.entities.Usuario;
import com.ecolim.app.data.local.entities.Zona;
import com.ecolim.app.utils.Constants;
import com.ecolim.app.utils.SecurityUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {
        Usuario.class,
        TipoResiduo.class,
        Zona.class,
        Recoleccion.class,
        Reporte.class,
        SyncLog.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract UsuarioDao usuarioDao();
    public abstract TipoResiduoDao tipoResiduoDao();
    public abstract ZonaDao zonaDao();
    public abstract RecoleccionDao recoleccionDao();
    public abstract ReporteDao reporteDao();
    public abstract SyncLogDao syncLogDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            Constants.DATABASE_NAME
                    )
                    .addCallback(sRoomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                if (INSTANCE == null) return;

                // 1. Precargar Usuarios de prueba
                UsuarioDao usuarioDao = INSTANCE.usuarioDao();
                usuarioDao.insertar(new Usuario(
                        "Juan Pérez Huamán",
                        "72345678",
                        "operario",
                        "operario",
                        SecurityUtils.sha256("123456")
                ));
                usuarioDao.insertar(new Usuario(
                        "María Gómez Santos",
                        "45678912",
                        "supervisor",
                        "supervisor",
                        SecurityUtils.sha256("admin123")
                ));

                // 2. Precargar Tipos de Residuo
                TipoResiduoDao tipoResiduoDao = INSTANCE.tipoResiduoDao();
                long idPapel = tipoResiduoDao.insertar(new TipoResiduo("Papel/cartón", "Aprovechable", "kg"));
                long idPlastico = tipoResiduoDao.insertar(new TipoResiduo("Plástico", "Aprovechable", "kg"));
                long idOrganico = tipoResiduoDao.insertar(new TipoResiduo("Orgánico", "Orgánico", "kg"));
                tipoResiduoDao.insertar(new TipoResiduo("Vidrio", "Aprovechable", "kg"));
                tipoResiduoDao.insertar(new TipoResiduo("Metal", "Aprovechable", "kg"));
                tipoResiduoDao.insertar(new TipoResiduo("Peligroso", "Peligroso", "kg"));

                // 3. Precargar Zonas de recolección
                ZonaDao zonaDao = INSTANCE.zonaDao();
                long idZonaAlmacen = zonaDao.insertar(new Zona("Zona Norte - Almacén Central", "Almacén"));
                long idZonaPlanta = zonaDao.insertar(new Zona("Zona Sur - Planta Industrial", "Planta"));
                zonaDao.insertar(new Zona("Zona Este - Oficinas Administrativas", "Oficinas"));
                zonaDao.insertar(new Zona("Patio de Maniobras", "Exteriores"));

                // 4. Precargar datos de muestra que coinciden exactamente con los totales del wireframe:
                // Papel/cartón: 120 kg, Plástico: 85 kg, Orgánico: 210 kg
                RecoleccionDao recoleccionDao = INSTANCE.recoleccionDao();
                recoleccionDao.insertar(new Recoleccion(
                        1, (int) idPapel, (int) idZonaAlmacen, 120.0,
                        "2026-09-02 09:30:00", "-12.0531,-77.0345", "", true
                ));
                recoleccionDao.insertar(new Recoleccion(
                        1, (int) idPlastico, (int) idZonaPlanta, 85.0,
                        "2026-09-05 14:15:00", "-12.0540,-77.0350", "", true
                ));
                recoleccionDao.insertar(new Recoleccion(
                        1, (int) idOrganico, (int) idZonaPlanta, 210.0,
                        "2026-09-10 11:00:00", "-12.0520,-77.0330", "", true
                ));
            });
        }
    };
}
