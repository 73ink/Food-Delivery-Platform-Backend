package com.fooddelivery.Utils;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.Random;

// Utility class for helper methods required in the project.
// This class also demonstrates method overloading.
public class HelperUtils {

    private static final Random RANDOM = new Random();

    // Private constructor because this class should not be used to create objects.
    private HelperUtils() {
    }

    // Generates a code with default length of 4 digits.
    // Example: CUST-8492
    public static String generateCode(String prefix) {
        return generateCode(prefix, 4);
    }

    // Generates a code with custom digit length.
    // Example: REST-384920
    public static String generateCode(String prefix, int length) {
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < length; i++) {
            number.append(RANDOM.nextInt(10));
        }

        return prefix.toUpperCase() + "-" + number;
    }

    // Calculates distance between two map coordinates using the Haversine formula.
    // The result is returned in kilometers.
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int EARTH_RADIUS_KM = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2)
                * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    // Calculates total without discount.
    public static double calculateTotal(double subtotal, double fee) {
        return subtotal + fee;
    }

    // Calculates total with discount.
    public static double calculateTotal(double subtotal, double fee, double discount) {
        return subtotal + fee - discount;
    }

    // Formats amount using default currency OMR.
    public static String formatCurrency(double amount) {
        return formatCurrency(amount, "OMR");
    }

    // Formats amount using custom currency code.
    public static String formatCurrency(double amount, String currencyCode) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        return decimalFormat.format(amount) + " " + currencyCode;
    }

    // Checks if a business is open based on opening and closing time.
    // Time format should be HH:mm, for example: 09:00 or 23:30.
    public static boolean isBusinessOpen(String openTime, String closeTime) {
        LocalTime now = LocalTime.now();
        LocalTime opening = LocalTime.parse(openTime);
        LocalTime closing = LocalTime.parse(closeTime);

        // Normal case: opens 09:00 and closes 22:00
        if (opening.isBefore(closing)) {
            return !now.isBefore(opening) && !now.isAfter(closing);
        }

        // Overnight case: opens 18:00 and closes 02:00
        return !now.isBefore(opening) || !now.isAfter(closing);
    }
}