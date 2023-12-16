package com.nomad.socialspring.util;

import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BDate extends Date {

    private final static Calendar calender = new GregorianCalendar();

    public BDate(long date) {
        super(date);
    }

    public static BDate currentDate() {
        return new BDate(Instant.now().toEpochMilli());
    }

    public BDate zeroTime() {
        calender.setTime(this);
        calender.set(Calendar.SECOND, 0);
        calender.set(Calendar.MINUTE, 0);
        calender.set(Calendar.HOUR_OF_DAY, 0);
        calender.set(Calendar.MILLISECOND, 0);
        setTime(calender.getTimeInMillis());
        return this;
    }

    public BDate addSecond(int amount) {
        return add(Calendar.SECOND, amount);
    }

    public BDate addMinute(int amount) {
        return add(Calendar.MINUTE, amount);
    }

    public BDate addHour(int amount) {
        return add(Calendar.HOUR_OF_DAY, amount);
    }

    public BDate addDay(int amount) {
        return add(Calendar.DATE, amount);
    }

    public BDate addMonth(int amount) {
        return add(Calendar.MONTH, amount);
    }

    public BDate addYear(int amount) {
        return add(Calendar.YEAR, amount);
    }

    public BDate add(int field, int amount) {
        calender.setTime(this);
        calender.add(field, amount);
        setTime(calender.getTimeInMillis());
        return this;
    }
}
