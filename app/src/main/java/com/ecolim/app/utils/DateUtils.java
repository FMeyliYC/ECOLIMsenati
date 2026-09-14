package com.ecolim.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DISPLAY_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FILTER_FORMAT = "dd/MM/yyyy";
    private static final String SQLITE_DATE_FORMAT = "yyyy-MM-dd";

    public static String getFechaHoraActual() {
        SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getFechaHoraActualISO() {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_FORMAT, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    public static String formatoVisualAFechaSql(String fechaVisual) {
        try {
            SimpleDateFormat parseSdf = new SimpleDateFormat(DATE_FILTER_FORMAT, Locale.getDefault());
            Date date = parseSdf.parse(fechaVisual);
            SimpleDateFormat outSdf = new SimpleDateFormat(SQLITE_DATE_FORMAT, Locale.getDefault());
            return outSdf.format(date);
        } catch (ParseException e) {
            return fechaVisual;
        }
    }

    public static String formatearFechaVisual(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FILTER_FORMAT, Locale.getDefault());
        return sdf.format(date);
    }
}
