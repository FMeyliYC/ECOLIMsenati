package com.ecolim.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.ecolim.app.utils.DateUtils;

import org.junit.Test;

public class DateUtilsTest {

    @Test
    public void testGetFechaHoraActualNoEsNulo() {
        String fechaHora = DateUtils.getFechaHoraActual();
        assertNotNull(fechaHora);
        assertTrue(fechaHora.length() >= 19);
    }

    @Test
    public void testFormatoVisualAFechaSql() {
        String visual = "12/09/2026";
        String sql = DateUtils.formatoVisualAFechaSql(visual);
        assertEquals("2026-09-12", sql);
    }

    @Test
    public void testGetFechaHoraActualISO() {
        String iso = DateUtils.getFechaHoraActualISO();
        assertNotNull(iso);
        assertTrue(iso.endsWith("Z"));
    }
}
