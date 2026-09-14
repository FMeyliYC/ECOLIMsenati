package com.ecolim.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.ecolim.app.utils.SecurityUtils;

import org.junit.Test;

public class SecurityUtilsTest {

    @Test
    public void testSha256GeneraHashValido() {
        String password = "mypassword123";
        String hash = SecurityUtils.sha256(password);
        assertNotEquals(password, hash);
        assertEquals(64, hash.length()); // SHA-256 genera 64 caracteres hexadecimales
    }

    @Test
    public void testVerificarPasswordValidaCorrectamente() {
        String password = "supersecreto";
        String hash = SecurityUtils.sha256(password);

        assertTrue(SecurityUtils.verificarPassword(password, hash));
        assertFalse(SecurityUtils.verificarPassword("password_equivocado", hash));
    }
}
