package ua.isolutions.bot.commons.utils;

public class PhoneFormatter {

    public static String getPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Phone is absent.");
        }

        String validPhone = phone.replaceAll("[\\s+\\-()]", "");

        if (phone.length() <= 1) {
            throw new IllegalArgumentException("Phone number is in wrong format." + phone);
        }

        return validPhone;
    }
}
