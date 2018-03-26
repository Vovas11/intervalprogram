package com.example.intervalprogram;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class IntervalTest {


    private static final Interval INTERVAL_CORRECT = new Interval(2, 100);
    private static final Interval INTERVAL_INCORRECT = new Interval(100, 2);

    @Test
    public void testConstructorLogic() {
        assertEquals(INTERVAL_CORRECT.getStart(), INTERVAL_INCORRECT.getStart());
        assertEquals(INTERVAL_CORRECT.getEnd(), INTERVAL_INCORRECT.getEnd());
    }

    @Test
    public void testEqualsNotInstances() {
        assertFalse(INTERVAL_CORRECT == INTERVAL_INCORRECT);
    }

    @Test
    public void testEqualsMethodEquals() {
        assertTrue(INTERVAL_CORRECT.equals(INTERVAL_INCORRECT));
    }

    @Test
    public void testEqualsMethodHashCode() {
        assertEquals(INTERVAL_CORRECT.hashCode(), INTERVAL_INCORRECT.hashCode());
    }


}
