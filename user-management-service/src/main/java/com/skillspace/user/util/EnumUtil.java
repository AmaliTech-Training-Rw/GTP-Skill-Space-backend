package com.skillspace.user.util;

public class EnumUtil {

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String value) {
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
