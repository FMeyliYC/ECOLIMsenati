# Brief de desarrollo — App ECOLIM (Android)

> Instrucciones para un agente de codificación (Codex, Antigravity, Claude Code, etc.).
> Objetivo: construir el proyecto Android Studio completo en el repositorio de GitHub
> `https://github.com/FMeyliYC/ECOLIMsenati.git`, actualmente **vacío**, siguiendo
> exactamente la arquitectura y el modelo de datos definidos en este documento
> (coinciden con el Trabajo Final de SENATI ya entregado, para que el código y el
> informe sean consistentes entre sí).

---

## 1. Contexto del proyecto

La empresa ficticia **ECOLIM S.A.C.** (servicios de limpieza industrial) necesita una
app móvil Android que digitalice el registro de recolección de residuos sólidos,
actualmente hecho en papel y hojas de cálculo. La app debe:

- Registrar cada recolección (tipo de residuo, cantidad, zona, fecha/hora, foto).
- Funcionar sin conexión (offline-first) y sincronizar luego con un backend REST.
- Generar reportes filtrables por fecha, tipo de residuo y zona.
- Cumplir buenas prácticas de permisos, privacidad y usabilidad.

## 2. Repositorio

- URL: `https://github.com/FMeyliYC/ECOLIMsenati.git`
- Estado actual: **vacío** (sin commits).
- Acciones esperadas del agente:
  1. Clonar el repositorio.
  2. Crear la estructura completa de un proyecto Android Studio (Gradle, `app/`, etc.).
  3. Configurar `.gitignore` estándar de Android Studio.
  4. Hacer commits incrementales y descriptivos por módulo (no un solo commit gigante).
  5. Incluir un `README.md` con: descripción del proyecto, stack técnico, cómo compilar
     y ejecutar, estructura de carpetas y capturas (si se generan).
  6. Rama principal: `main`. Usar ramas `feature/...` si se trabaja por módulos y luego
     mergear a `main`.

## 3. Stack técnico

| Capa | Tecnología |
|---|---|
| Lenguaje | Java (Kotlin es aceptable como alternativa moderna; mantener consistencia en todo el proyecto) |
| UI | Android Views + Material Design 3, layouts en XML |
| Arquitectura | MVVM (Model-View-ViewModel) por capas |
| Persistencia local | Room (sobre SQLite) |
| Red / API remota | Retrofit2 + OkHttp + Gson/Moshi |
| Asincronía | Coroutines (Kotlin) o LiveData/Executors (Java) |
| Escaneo de código | CameraX + ML Kit Barcode Scanning |
| Control de versiones | Git / GitHub |
| Gestión de dependencias | Gradle (Kotlin DSL o Groovy, cualquiera es válido) |

## 4. Arquitectura y estructura de paquetes

Replicar esta estructura (ya definida en el informe entregado; **no cambiar los nombres
de paquetes ni de clases principales**, para mantener trazabilidad con el diagrama de
arquitectura del informe):

```
app/
 └─ src/main/java/com/ecolim/app/
     ├─ ui/
     │   ├─ LoginActivity
     │   ├─ MainActivity                (contenedora, patrón Single-Activity)
     │   ├─ fragments/
     │   │    ├─ RegistroFragment
     │   │    ├─ HistorialFragment
     │   │    └─ ReportesFragment
     │   └─ adapters/
     │        └─ RecoleccionAdapter     (RecyclerView)
     ├─ viewmodel/
     │   ├─ RegistroViewModel
     │   └─ ReportesViewModel
     ├─ data/
     │   ├─ local/
     │   │    ├─ AppDatabase            (Room)
     │   │    ├─ entities/              (Usuario, TipoResiduo, Recoleccion, Zona, Reporte, SyncLog)
     │   │    └─ dao/                   (UsuarioDao, RecoleccionDao, ReporteDao, ...)
     │   └─ remote/
     │        ├─ ApiService             (Retrofit)
     │        └─ dto/                   (RecoleccionDTO, ReporteDTO, RespuestaApi)
     ├─ repository/
     │   └─ RecoleccionRepository       (fuente única de verdad: combina local + remoto)
     └─ utils/                          (validaciones, constantes, formateo de fechas)

app/src/main/res/
 ├─ layout/         (activity_login.xml, activity_main.xml, fragment_registro.xml,
 │                   fragment_historial.xml, fragment_reportes.xml, item_residuo.xml)
 ├─ values/         (strings.xml, colors.xml, styles.xml, dimens.xml)
 ├─ drawable/
 └─ menu/ (si se usa bottom navigation)
```

Comunicación entre pantallas: Intents (Activities) y Bundle/Safe Args (Fragments dentro
de `MainActivity`).

## 5. Modelo de datos (Room / SQLite)

Implementar exactamente estas entidades y relaciones (diagrama entidad-relación ya
definido en el informe):

### `Usuario`
| Campo | Tipo | Notas |
|---|---|---|
| idUsuario | int (PK, autogenerate) | |
| nombre | String | |
| dni | String | |
| rol | String | "operario" \| "supervisor" |
| usuario | String | login |
| password | String | almacenar con hash (no texto plano) |

### `TipoResiduo`
| Campo | Tipo | Notas |
|---|---|---|
| idTipo | int (PK, autogenerate) | |
| nombreResiduo | String | ej. "Plástico", "Orgánico" |
| categoria | String | |
| unidadMedida | String | ej. "kg" |

### `Zona`
| Campo | Tipo | Notas |
|---|---|---|
| idZona | int (PK, autogenerate) | |
| nombreZona | String | |
| areaCliente | String | |

### `Recoleccion`
| Campo | Tipo | Notas |
|---|---|---|
| idRecoleccion | int (PK, autogenerate) | |
| idUsuario | int (FK → Usuario) | |
| idTipo | int (FK → TipoResiduo) | |
| idZona | int (FK → Zona) | |
| cantidadKg | double | |
| fechaHora | String (ISO 8601) | |
| ubicacion | String | "lat,long" |
| fotoEvidencia | String | ruta local del archivo |
| sincronizado | boolean | default false |

### `Reporte`
| Campo | Tipo | Notas |
|---|---|---|
| idReporte | int (PK, autogenerate) | |
| idUsuario | int (FK → Usuario) | |
| fechaGeneracion | String | |
| rangoFechaInicio | String | |
| rangoFechaFin | String | |
| totalKg | double | |

### `SyncLog`
| Campo | Tipo | Notas |
|---|---|---|
| idSync | int (PK, autogenerate) | |
| idRecoleccion | int (FK → Recoleccion) | |
| fechaEnvio | String | |
| resultadoApi | String | "OK" \| "ERROR: ..." |

Generar los DAOs con, como mínimo:
- `insertar()`, `actualizar()`, `eliminar()` por entidad.
- `RecoleccionDao.obtenerPendientes()` → registros con `sincronizado = 0`.
- `RecoleccionDao.filtrarPorFecha(inicio, fin)`.
- `RecoleccionDao.filtrarPorTipoYZona(idTipo, idZona)`.

## 6. API RESTful (contrato esperado, para el backend simulado o real)

```
POST /api/recolecciones
Body:  { "idUsuario": 1, "idTipo": 3, "idZona": 2, "cantidadKg": 5.5,
         "fechaHora": "2026-09-12T10:30:00Z", "ubicacion": "-12.05,-77.03" }
Resp:  { "status": "OK", "idRemoto": 1024 }

GET /api/reportes?desde=2026-09-01&hasta=2026-09-12&tipo=plastico&zona=1
Resp:  [ { "tipoResiduo": "Plástico", "totalKg": 85.0 }, ... ]

POST /api/auth/login
Body:  { "usuario": "jperez", "password": "..." }
Resp:  { "token": "...", "rol": "operario" }
```

Si no hay backend real, crear un **mock server** (json-server, Mockoon, o un módulo
Node/Express mínimo) documentado en el README para que el proyecto sea ejecutable
end-to-end en modo demo.

## 7. Pantallas (ya prototipadas como wireframes en el informe)

1. **Login** — usuario/DNI + contraseña, botón "Ingresar", checkbox "Recordar sesión".
2. **Registro de residuo** — spinner tipo de residuo, cantidad (kg), spinner zona,
   botón "Escanear código/Foto" (CameraX + ML Kit), fecha/hora automática, botones
   "Guardar registro" y "Sincronizar ahora".
3. **Historial** — lista (RecyclerView) de recolecciones del operario, con estado de
   sincronización visible (ícono ✓ / pendiente).
4. **Reportes** — filtros por fecha/tipo/zona, botón "Filtrar", lista de resultados con
   totales, botón "Exportar/Compartir" (PDF o CSV).

Usar Material Design 3, contraste alto (uso en exteriores), tamaño de toque ≥ 48dp.

## 8. Requerimientos no funcionales

- **Offline-first**: toda escritura se guarda primero en Room; la sincronización es un
  proceso aparte (WorkManager recomendado) que reintenta cuando hay conexión.
- **Seguridad**: comunicación por HTTPS; no guardar contraseñas en texto plano;
  permisos declarados en `AndroidManifest.xml` (`CAMERA`, `ACCESS_FINE_LOCATION`,
  `INTERNET`, `READ/WRITE_EXTERNAL_STORAGE`) con justificación en el README.
- **Privacidad**: incluir un texto de política de privacidad simulada (uso de ubicación
  y fotos solo con fines de trazabilidad de residuos).
- **Publicación (simulada)**: dejar preparado `versionCode`/`versionName`, ícono
  adaptativo, y un `PRIVACY.md` con los puntos que pediría Google Play Console.

## 9. Plan de trabajo sugerido (orden de ejecución para el agente)

1. Inicializar proyecto Android Studio (Empty Views Activity), configurar Gradle,
   dependencias (Room, Retrofit, CameraX/ML Kit, Material) y `.gitignore`. Commit inicial.
2. Crear entidades Room + DAOs + `AppDatabase`. Commit.
3. Crear capa `data/remote` (Retrofit, DTOs, `ApiService`) + mock server o backend real. Commit.
4. Crear `RecoleccionRepository` (combina local/remoto, maneja `sincronizado`). Commit.
5. Implementar `LoginActivity` + autenticación básica contra Room/API. Commit.
6. Implementar `MainActivity` + navegación entre `RegistroFragment`, `HistorialFragment`,
   `ReportesFragment` (bottom navigation o drawer). Commit.
7. Implementar `RegistroFragment` (formulario + escaneo de código + guardado local). Commit.
8. Implementar `HistorialFragment` (RecyclerView + adapter). Commit.
9. Implementar `ReportesFragment` (filtros + consulta + exportación). Commit.
10. Implementar sincronización en segundo plano (WorkManager) que envía pendientes a
    la API y marca `sincronizado = true`. Commit.
11. Pruebas unitarias (DAOs con Room in-memory) y, si es posible, una prueba de UI
    (Espresso) del flujo de registro. Commit.
12. Completar `README.md`, `PRIVACY.md`, capturas de pantalla y limpieza final. Commit.

## 10. Entregables esperados en el repositorio

- Código fuente completo y compilable (`./gradlew assembleDebug` sin errores).
- `README.md` con instrucciones de instalación/ejecución y descripción de arquitectura.
- Diagrama de arquitectura y ER (puede reutilizar/mejorar los del informe SENATI).
- Historial de commits claro y incremental (no un solo commit "proyecto completo").

## 11. Restricciones importantes

- Mantener nombres de clases/paquetes/entidades tal como se listan aquí, porque deben
  coincidir con el diagrama entidad-relación y el diagrama de arquitectura ya entregados
  en el Trabajo Final académico (evaluación cruzada informe ↔ código).
- No usar librerías con licencias restrictivas o de pago.
- Priorizar código legible y comentado por encima de "trucos" de una sola línea.
