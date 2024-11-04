package com.open.cmt.enumeration;

import java.util.Arrays;

public enum TimePeriod {
    LAST_HOUR,
    LAST_24_HOURS,
    LAST_WEEK,
    LAST_MONTH,
    LAST_YEAR,
    ALL_TIME;

    public static TimePeriod fromString(String timePeriodStr) {
        return Arrays.stream(TimePeriod.values())
                .filter(c -> c.name().equalsIgnoreCase(timePeriodStr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Periodo no válido: " + timePeriodStr));
    }
}
