package com.example.intervalprogram;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class IntervalHandlerInheritTest {

    private IntervalHandler handler;

    private static final List<Interval> INCLUDES_NORMAL = Arrays.asList(new Interval(10, 19), new Interval(25, 35));
    private static final List<Interval> INCLUDES_OVERLAP = Arrays.asList(new Interval(10, 19), new Interval(5, 35));
    private static final List<Interval> INCLUDES_WRONG_ORDER = Arrays.asList(new Interval(19, 10), new Interval(35, 10));
    private static final List<Interval> INCLUDES_NEGATIVE = Arrays.asList(new Interval(-10, -5), new Interval(-7, 35));
    private static final List<Interval> INCLUDES_LONG = Arrays.asList(new Interval(100000, 500000),
                                                                      new Interval(50000, 80000),
                                                                      new Interval(500, 8000));

    private static final List<Interval> EXLUDES_NORMAL = Arrays.asList(new Interval(15, 27));
    private static final List<Interval> EXLUDES_OVERLAP = Arrays.asList(new Interval(15, 27), new Interval(22, 30));
    private static final List<Interval> EXLUDES_OUTER = Arrays.asList(new Interval(5, 40));
    private static final List<Interval> EXLUDES_NEGATIVE = Arrays.asList(new Interval(-8, -4), new Interval(-1, 10));
    private static final List<Interval> EXCLUDES_LONG = Arrays.asList(new Interval(6000, 70000),
                                                                      new Interval(200000, 300000));

    private static final List<Interval> RESULT_NORMAL = Arrays.asList(new Interval(10, 14), new Interval(28, 35));
    private static final List<Interval> RESULT_IN_OVERLAP = Arrays.asList(new Interval(5, 14), new Interval(28, 35));
    private static final List<Interval> RESULT_EX_OVERLAP = Arrays.asList(new Interval(10, 14), new Interval(31, 35));
    private static final List<Interval> RESULT_NEGATIVE = Arrays.asList(new Interval(-10, -9),
                                                                        new Interval(-3, -2), new Interval(11, 35));
    private static final List<Interval> RESULT_LONG = Arrays.asList(new Interval(500, 5999),
                                                                    new Interval(70001, 80000),
                                                                    new Interval(100000, 199999),
                                                                    new Interval(300001, 500000));


    public IntervalHandlerInheritTest(IntervalHandler handler) {
        this.handler = handler;
    }

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[] {
                //new IntervalHandlerByDirectSearch(),
                new IntervalHandlerBySet() };
    }


    @Test
    public void testProcessIntervalsNormal() {
        assertEquals(RESULT_NORMAL, handler.processIntervals(INCLUDES_NORMAL, EXLUDES_NORMAL));
    }

    @Test
    public void testProcessIntervalsIncludesOverlap() {
        assertEquals(RESULT_IN_OVERLAP, handler.processIntervals(INCLUDES_OVERLAP, EXLUDES_NORMAL));
    }

    @Test
    public void testProcessIntervalsExcludesOverlap() {
        assertEquals(RESULT_EX_OVERLAP, handler.processIntervals(INCLUDES_NORMAL, EXLUDES_OVERLAP));
    }

    @Test
    public void testProcessIntervalsEmptyResult() {
        assertEquals(Collections.emptyList(), handler.processIntervals(INCLUDES_NORMAL, EXLUDES_OUTER));
    }

    @Test
    public void testProcessIntervalsIncludesWrongOrder() {
        assertEquals(RESULT_NORMAL, handler.processIntervals(INCLUDES_WRONG_ORDER, EXLUDES_NORMAL));
    }

    @Test
    public void testProcessIntervalsNegative() {
        assertEquals(RESULT_NEGATIVE, handler.processIntervals(INCLUDES_NEGATIVE, EXLUDES_NEGATIVE));
    }

    @Test
    public void testProcessIntervalsLong() {
        assertEquals(RESULT_LONG, handler.processIntervals(INCLUDES_LONG, EXCLUDES_LONG));
    }
}
