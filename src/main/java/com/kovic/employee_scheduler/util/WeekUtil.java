package com.kovic.employee_scheduler.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

public class WeekUtil {

    public static LocalDate getStartOfIsoWeek(int year, int weekNumber) {
        WeekFields weekFields = WeekFields.ISO;
        return LocalDate
                .now()
                .withYear(year)
                .with(weekFields.weekOfYear(), weekNumber)
                .with(TemporalAdjusters.previousOrSame(weekFields.getFirstDayOfWeek()));
    }

    public static int getCurrentIsoWeek() {
        return LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

}
