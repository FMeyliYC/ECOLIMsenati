package com.ecolim.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.ecolim.app.data.local.entities.Recoleccion;
import com.ecolim.app.data.local.entities.TipoResiduo;
import com.ecolim.app.data.local.entities.Usuario;
import com.ecolim.app.data.local.entities.Zona;

import org.junit.Test;

public class DatabaseEntitiesTest {

    @Test
    public void testCrearUsuario() {
        Usuario usuario = new Usuario("Carlos Ruiz", "12345678", "operario", "cruiz", "hash123");
        assertEquals("Carlos Ruiz", usuario.getNombre());
        assertEquals("12345678", usuario.getDni());
        assertEquals("operario", usuario.getRol());
        assertEquals("cruiz", usuario.getUsuario());
    }

    @Test
    public void testCrearTipoResiduoYZona() {
        TipoResiduo tipo = new TipoResiduo("Plástico", "Aprovechable", "kg");
        assertEquals("Plástico", tipo.getNombreResiduo());
        assertEquals("kg", tipo.getUnidadMedida());

        Zona zona = new Zona("Zona Norte", "Almacén");
        assertEquals("Zona Norte", zona.getNombreZona());
        assertEquals("Almacén", zona.getAreaCliente());
    }

    @Test
    public void testCrearRecoleccion() {
        Recoleccion recoleccion = new Recoleccion(1, 2, 3, 25.5, "2026-09-12 10:00:00", "-12.05,-77.03", "foto.jpg", false);
        assertEquals(25.5, recoleccion.getCantidadKg(), 0.001);
        assertFalse(recoleccion.isSincronizado());
        assertNotNull(recoleccion.getFechaHora());
    }
}
