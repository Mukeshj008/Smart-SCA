package com.example.app.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * String utility class.
 * Uses: commons-lang3, log4j
 */
public class StringUtil {
    private static final Logger logger = LogManager.getLogger(StringUtil.class);

    public static String capitalize(String input) {
        if (StringUtils.isEmpty(input)) return input;
        return WordUtils.capitalizeFully(input);
    }

    public static boolean isBlank(String input) {
        return StringUtils.isBlank(input);
    }

    public static String abbreviate(String input, int maxWidth) {
        return StringUtils.abbreviate(input, maxWidth);
    }

    public static String[] split(String input, char separator) {
        return StringUtils.split(input, separator);
    }
}
