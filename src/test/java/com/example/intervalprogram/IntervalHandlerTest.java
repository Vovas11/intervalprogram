package com.example.intervalprogram;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class IntervalHandlerTest {

    private IntervalHandler handlerAbstract;
    private IntervalHandler handlerInherited;

    private static final String INTERVAL_NORMAL = "10-19, 31-100";
    private static final String INTERVAL_OVERLAP = "10-19, 5-35";
    private static final String INTERVAL_WRONG_ORDER = "19-10, 31-100";
    private static final String INTERVAL_NEGATIVE = "-10--5, -7-35";
    private static final String INTERVAL_ILLEGAL = "10-19, 7.5-35, 31-100";

    private static final List<Interval> INTERVALS_NORMAL = Arrays.asList(new Interval(10, 19), new Interval(31, 100));
    private static final List<Interval> INTERVALS_OVERLAP = Arrays.asList(new Interval(10, 19), new Interval(5, 35));

    private static final List<Interval> INCLUDES_NORMAL = Arrays.asList(new Interval(10, 19), new Interval(25, 35));
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


    public IntervalHandlerTest(IntervalHandler handlerInherited) {
        this.handlerInherited = handlerInherited;
    }

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[] {
                //new IntervalHandlerByDirectSearch(),
                new IntervalHandlerBySet() };
    }

    @Before
    public void start() {
        handlerAbstract = new IntervalHandler() {
            @Override
            public List<Interval> processIntervals(List<Interval> includes, List<Interval> excludes) {
                return null;
            }
        };
    }

    /* ---- test parseIntervals ---- */

    @Test
    public void testParseIntervalsOk() {
        assertEquals(INTERVALS_NORMAL, handlerAbstract.parseIntervals(INTERVAL_NORMAL));
    }

    @Test
    public void testParseIntervalsOverlap() {
        assertEquals(INTERVALS_OVERLAP, handlerAbstract.parseIntervals(INTERVAL_OVERLAP));
    }

    @Test
    public void testParseIntervalsWrongStart() {
        assertEquals(INTERVALS_NORMAL, handlerAbstract.parseIntervals(INTERVAL_WRONG_ORDER));
    }

    @Test
    public void testParseIntervalsNegative() {
        assertEquals(Collections.emptyList(), handlerAbstract.parseIntervals(INTERVAL_NEGATIVE));
    }

    @Test
    public void testParseIntervalsIllegal() {
        assertEquals(INTERVALS_NORMAL, handlerAbstract.parseIntervals(INTERVAL_ILLEGAL));
    }

    /* ---- test processIntervals ---- */

    @Test
    public void testProcessIntervalsNormal() {
        assertEquals(RESULT_NORMAL, handlerInherited.processIntervals(INCLUDES_NORMAL, EXLUDES_NORMAL));
    }

    @Test
    public void testProcessIntervalsIncludesOverlap() {
        assertEquals(RESULT_IN_OVERLAP, handlerInherited.processIntervals(INTERVALS_OVERLAP, EXLUDES_NORMAL));
    }

    @Test
    public void testProcessIntervalsExcludesOverlap() {
        assertEquals(RESULT_EX_OVERLAP, handlerInherited.processIntervals(INCLUDES_NORMAL, EXLUDES_OVERLAP));
    }

    @Test
    public void testProcessIntervalsEmptyResult() {
        assertEquals(Collections.emptyList(), handlerInherited.processIntervals(INCLUDES_NORMAL, EXLUDES_OUTER));
    }

    @Test
    public void testProcessIntervalsIncludesWrongOrder() {
        assertEquals(RESULT_NORMAL, handlerInherited.processIntervals(INCLUDES_WRONG_ORDER, EXLUDES_NORMAL));
    }

    @Test
    public void testProcessIntervalsNegative() {
        assertEquals(RESULT_NEGATIVE, handlerInherited.processIntervals(INCLUDES_NEGATIVE, EXLUDES_NEGATIVE));
    }

    @Test
    public void testProcessIntervalsLong() {
        assertEquals(RESULT_LONG, handlerInherited.processIntervals(INCLUDES_LONG, EXCLUDES_LONG));
    }
}
