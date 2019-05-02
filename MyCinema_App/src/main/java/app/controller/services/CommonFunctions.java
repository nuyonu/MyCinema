package app.controller.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CommonFunctions {

    private CommonFunctions(){}

    private static Logger logger = LoggerFactory.getLogger(CommonFunctions.class);

    public static boolean lengthBetween(String string, Integer left, Integer right) {
        if (left > right)
            return string.length() > right && string.length() < left;
        return string.length() > left && string.length() < right;
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("[0-9]+") && (phoneNumber.length() > 8) && (phoneNumber.length() < 14);
    }

    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }

    public static byte[] imageFromPath(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            String error = "Image not found" + e;
            logger.error(error);
        }
        return new byte[0];
    }
}
