/**
 * ECOLIM S.A.C. - Servidor Mock REST API
 * Implementación en Node.js nativo (sin dependencias requeridas)
 *
 * Contrato de endpoints:
 * - POST /api/auth/login
 * - POST /api/recolecciones
 * - GET  /api/reportes
 */

const http = require('http');
const url = require('url');

const PORT = 3000;
let recoleccionIdCounter = 1000;
const recoleccionesGuardadas = [];

const server = http.createServer((req, res) => {
    const parsedUrl = url.parse(req.url, true);
    const pathname = parsedUrl.pathname;
    const method = req.method;

    // Encabezados CORS y JSON
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
    res.setHeader('Content-Type', 'application/json; charset=utf-8');

    if (method === 'OPTIONS') {
        res.writeHead(204);
        res.end();
        return;
    }

    let body = '';
    req.on('data', chunk => {
        body += chunk.toString();
    });

    req.on('end', () => {
        console.log(`[${new Date().toISOString()}] ${method} ${pathname}`);

        // 1. POST /api/auth/login
        if (method === 'POST' && pathname === '/api/auth/login') {
            try {
                const data = JSON.parse(body || '{}');
                if ((data.usuario === 'operario' && data.password === '123456') ||
                    (data.usuario === 'supervisor' && data.password === 'admin123')) {
                    const rol = data.usuario === 'supervisor' ? 'supervisor' : 'operario';
                    res.writeHead(200);
                    res.end(JSON.stringify({
                        token: "mock-jwt-token-" + Date.now(),
                        rol: rol
                    }));
                } else {
                    res.writeHead(401);
                    res.end(JSON.stringify({ error: "Credenciales inválidas" }));
                }
            } catch (err) {
                res.writeHead(400);
                res.end(JSON.stringify({ error: "Cuerpo JSON inválido" }));
            }
            return;
        }

        // 2. POST /api/recolecciones
        if (method === 'POST' && pathname === '/api/recolecciones') {
            try {
                const data = JSON.parse(body || '{}');
                recoleccionIdCounter++;
                recoleccionesGuardadas.push({
                    idRemoto: recoleccionIdCounter,
                    ...data,
                    recibidoEn: new Date().toISOString()
                });

                console.log(`-> Recolección registrada exitosamente. ID Remoto: ${recoleccionIdCounter}`);
                res.writeHead(200);
                res.end(JSON.stringify({
                    status: "OK",
                    idRemoto: recoleccionIdCounter
                }));
            } catch (err) {
                res.writeHead(400);
                res.end(JSON.stringify({ status: "ERROR", mensaje: err.message }));
            }
            return;
        }

        // 3. GET /api/reportes
        if (method === 'GET' && pathname === '/api/reportes') {
            const desde = parsedUrl.query.desde || '2026-09-01';
            const hasta = parsedUrl.query.hasta || '2026-09-12';
            const tipo = parsedUrl.query.tipo;

            console.log(`-> Consulta de reportes: desde=${desde}, hasta=${hasta}, tipo=${tipo}`);

            // Datos consistentes con el wireframe de diseño
            const reportes = [
                { tipoResiduo: "Papel/cartón", totalKg: 120.0 },
                { tipoResiduo: "Plástico", totalKg: 85.0 },
                { tipoResiduo: "Orgánico", totalKg: 210.0 }
            ];

            const filtrados = tipo ? reportes.filter(r => r.tipoResiduo.toLowerCase().includes(tipo.toLowerCase())) : reportes;

            res.writeHead(200);
            res.end(JSON.stringify(filtrados));
            return;
        }

        // Ruta no encontrada
        res.writeHead(404);
        res.end(JSON.stringify({ error: "Ruta no encontrada" }));
    });
});

server.listen(PORT, () => {
    console.log(`=======================================================`);
    console.log(` ECOLIM S.A.C. - Servidor Mock REST API activo`);
    console.log(` Escuchando en http://localhost:${PORT}`);
    console.log(` Endpoints disponibles:`);
    console.log(`   POST /api/auth/login`);
    console.log(`   POST /api/recolecciones`);
    console.log(`   GET  /api/reportes`);
    console.log(`=======================================================`);
});
