package com.nomad.socialspring.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BDate extends Date {

  private final static Calendar calender = new GregorianCalendar();

  public BDate(long time) {
    super(time);
  }

  public BDate(@NotNull Date date) {
    this(date.getTime());
  }

  public BDate(@NotNull Timestamp timestamp) {
    this(timestamp.getTime());
  }

  @NotNull
  @Contract("_ -> new")
  public static BDate valueOf(Date date) {
    return new BDate(date.getTime());
  }

  @NotNull
  @Contract("_ -> new")
  public static BDate valueOf(Timestamp timestamp) {
    return new BDate(timestamp.getTime());
  }

  @NotNull
  @Contract(" -> new")
  public static BDate currentDate() {
    return new BDate(System.currentTimeMillis());
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

  public boolean before(Date when) {
    return this.compareTo(when) < 0;
  }

  public boolean beforeEquals(Date when) {
    return this.compareTo(when) <= 0;
  }

  public boolean after(Date when) {
    return this.compareTo(when) > 0;
  }

  public boolean afterEquals(Date when) {
    return this.compareTo(when) >= 0;
  }

  public int differenceInMinutes(@NotNull BDate date) {
    long milliseconds1 = getTime();
    long milliseconds2 = date.getTime();
    return (int) ((milliseconds2 - milliseconds1) / (60 * 1000));
  }
}
