package com.lweynant.wastecalendar.unitTest.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.lweynant.wastecalendar.Clock;
import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IDate;

import junit.framework.TestCase;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class DateTester extends TestCase{

    private IDate anyDay() {
        return new Date(2013, Calendar.JANUARY, 23);
    }

    private IDate sameDay(IDate day) {
        return new Date(day.year(), day.month(), day.dayOfMonth());
    }

    private IDate sameDayDifferentMonth(IDate day) {
        return addAMonth(day);
    }

    private IDate addAMonth(IDate day) {
        Calendar c = calendar(day);
        c.add(Calendar.MONTH, 1);
        return day(c);
    }

    private IDate addDays(IDate day, int n) {
        Calendar c = calendar(day);
        c.add(Calendar.DAY_OF_MONTH, n);
        return day(c);
    }

    private IDate addADay(IDate day) {
        return addDays(day, 1);
    }

    private IDate subtractADay(IDate day) {
        Calendar c = calendar(day);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return day(c);
    }

    private IDate addAYear(IDate day) {
        Calendar c = calendar(day);
        c.add(Calendar.YEAR, 1);
        return day(c);
    }

    private IDate day(Calendar c) {
        return new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    private Calendar calendar(IDate day) {
        return day.calendar();
    }

    private IDate sameDayDifferentDayOfMonth(IDate day) {
        return addADay(day);
    }

    private IDate sameDayDifferentYear(IDate day) {
        return addAYear(day);
    }

    @SmallTest
    public void testIsSameAs_Null_IsFalse() {
        assertThat(anyDay().isSameAs(null), is(false));
    }

    @SmallTest
    public void testIsSameAs_DifferentDayOfMonth_IsFalse() {
        IDate d1 = anyDay();
        IDate d2 = sameDayDifferentDayOfMonth(d1);
        assertThat(d1.isSameAs(d2), is(false));
    }

    @SmallTest
    public void testIsSameAs_DifferentMonth_IsFalse() {
        IDate d1 = anyDay();
        IDate d2 = sameDayDifferentMonth(d1);
        assertThat(d1.isSameAs(d2), is(false));
    }

    @SmallTest
    public void testIsSameAs_DifferentYear_IsFalse() {
        IDate d1 = anyDay();
        IDate d2 = sameDayDifferentYear(d1);
        assertThat(d1.isSameAs(d2), is(false));
    }

    @SmallTest
    public void testIsSameAs_SameDay_IsTrue() {
        IDate d1 = anyDay();
        IDate d2 = sameDay(d1);
        assertThat(d1.isSameAs(d2), is(true));
    }

    @SmallTest
    public void testIsYesterdayFor_Yesterday_IsTrue() {
        IDate today = anyDay();
        IDate yesterday = subtractADay(today);
        assertThat(yesterday.isYesterdayFor(today), is(true));

    }

    @SmallTest
    public void testIsYesterdayFor_OtherDay_IsFalse() {
        IDate today = anyDay();
        IDate other = sameDayDifferentMonth(today);
        assertThat(other.isYesterdayFor(today), is(false));

    }

    @SmallTest
    public void testIsYesterdayFor_Tomorrow_IsFalse() {
        IDate today = anyDay();
        IDate tomorrow = addADay(today);
        assertThat(tomorrow.isYesterdayFor(today), is(false));
    }

    @SmallTest
    public void testIsTomorrowFor_Tomorrow_IsTrue() {
        IDate today = anyDay();
        IDate tomorrow = addADay(today);
        assertThat(tomorrow.isTomorrowFor(today), is(true));
    }

    @SmallTest
    public void testIsTomorrowFor_OtherDay_IsFalse() {
        IDate today = anyDay();
        IDate other = sameDayDifferentMonth(today);
        assertThat(today.isTomorrowFor(other), is(false));
    }

    @SmallTest
    public void testIsDayAfterTomorrowFor_DayAfterTomorrow_IsTrue() {
        IDate today = anyDay();
        IDate dayAftertomorrow = addDays(today, 2);
        assertThat(dayAftertomorrow.isDayAfterTomorrowFor(today), is(true));
    }

    @SmallTest
    public void testIsDayAfterTomorrowFor_OtherDay_IsFalse() {
        IDate today = anyDay();
        IDate other = sameDayDifferentMonth(today);
        assertThat(other.isDayAfterTomorrowFor(today), is(false));
    }

    @SmallTest
    public void testBefore_OneDayBefore_IsTrue() {
        IDate today = anyDay();
        IDate yesterday = subtractADay(today);
        assertThat(yesterday.before(today), is(true));
    }

    @SmallTest
    public void testBefore_OneDayAfter_IsFalse() {
        IDate today = anyDay();
        IDate tomorrow = addADay(today);
        assertThat(tomorrow.before(today), is(false));
    }

    @SmallTest
    public void testBefore_SameDay_IsFalse() {
        IDate day = anyDay();
        IDate sameDay = sameDay(day);
        assertThat(day.before(sameDay), is(false));
    }

    @SmallTest
    public void testAfter_OneDayAfter_IsTrue() {
        IDate today = anyDay();
        IDate tomorrow = addADay(today);
        assertThat(tomorrow.after(today), is(true));
    }

    @SmallTest
    public void testAfter_OneDayBefore_IsFalse() {
        IDate today = anyDay();
        IDate yesterday = subtractADay(today);
        assertThat(yesterday.after(today), is(false));
    }

    @SmallTest
    public void testAfter_SameDay_IsFalse() {
        IDate day = anyDay();
        IDate sameDay = sameDay(day);
        assertThat(day.after(sameDay), is(false));
    }

    @SmallTest
    public void testToday() {
        IDate today = new Clock().today();
        Calendar c = Calendar.getInstance();
        Date t = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        assertThat(today.isSameAs(t), is(true));
    }

    @SmallTest
    public void testDayBefore() {
        IDate today = anyDay();
        IDate yesterday = subtractADay(today);
        assertThat(today.dayBefore().isSameAs(yesterday), is(true));
    }

    @SmallTest
    public void testDayAfter() {
        IDate today = anyDay();
        IDate tomorrow = addADay(today);
        assertThat(today.dayAfter().isSameAs(tomorrow), is(true));
    }

    @SmallTest
    public void testCompareToSameDay() {
        IDate day = anyDay();
        assertThat(day.compareTo(day), is(0));
    }

    @SmallTest
    public void testCompareToDayAfter() {
        IDate today = anyDay();
        IDate tomorrow = today.dayAfter();
        assertThat(today.compareTo(tomorrow), is(-1));
    }

    @SmallTest
    public void testCompareToDayBefore() {
        IDate today = anyDay();
        IDate yesterday = today.dayBefore();
        assertThat(today.compareTo(yesterday), is(1));
    }

    @SmallTest
    public void testEqualsToSameDay() {
        IDate day = anyDay();
        assertThat(day.equals(day), is(true));
    }

    @SmallTest
    public void testEqualsOnOtherDay() {
        IDate today = anyDay();
        IDate tomorrow = today.dayAfter();
        assertThat(today.equals(tomorrow), is(false));
    }

    @SmallTest
    public void testEqualsOnOtherType() {
        IDate today = anyDay();
        assertThat(today.equals("string"), is(false));
    }

}
