package com.ecolim.app.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    /**
     * Calcula el hash SHA-256 de una cadena de texto para almacenamiento seguro de contraseñas.
     * @param input Texto plano a hashear.
     * @return Cadena hexadecimal con el hash SHA-256.
     */
    public static String sha256(String input) {
        if (input == null) return "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular hash SHA-256", e);
        }
    }

    /**
     * Valida si una contraseña ingresada coincide con el hash almacenado.
     */
    public static boolean verificarPassword(String passwordIngresada, String hashAlmacenado) {
        if (passwordIngresada == null || hashAlmacenado == null) return false;
        return sha256(passwordIngresada).equalsIgnoreCase(hashAlmacenado);
    }
}
