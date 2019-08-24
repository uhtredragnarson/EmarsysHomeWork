package duedatecalculator;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

public class DueDateCalculatorTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testSubmitTimeEarly() {
        LocalDateTime testTimeEarly = LocalDateTime.of(2019,8,22,7,14);
        //GIVEN
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Time of submit must be between 9AM and 5PM.");
        //WHEN
        new DueDateCalculator().calculateDueDate(testTimeEarly, 16);
    }

    @Test
    public void testSubmitTimeLate() {
        LocalDateTime testTimeLate = LocalDateTime.of(2019,8,22,19,14);
        //GIVEN
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Time of submit must be between 9AM and 5PM.");
        //WHEN
        new DueDateCalculator().calculateDueDate(testTimeLate, 16);
    }

    @Test
    public void testSubmitTimeNull() {
        //GIVEN
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The timeOfSubmit should not be null!");
        //WHEN
        new DueDateCalculator().calculateDueDate(null, 16);
    }

    @Test
    public void testZeroOffset() {
        LocalDateTime testTime = LocalDateTime.of(2019,8,22,14,14);

        assertThat("If offset is zero and all the parameters are all right, the returned time should be the timeOfSubmit.",
            new DueDateCalculator().calculateDueDate(testTime, 0), is(testTime));
    }

    @Test
    public void testWeekdaysOffset() {
        LocalDateTime testTime = LocalDateTime.of(2019,8,20, 11,2);

        assertThat(new DueDateCalculator().calculateDueDate(testTime, 8),
            is(LocalDateTime.of(2019,8,21, 11,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 16),
            is(LocalDateTime.of(2019,8,22, 11,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 24),
            is(LocalDateTime.of(2019,8,23, 11,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 3),
            is(LocalDateTime.of(2019,8,20, 14,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 4),
            is(LocalDateTime.of(2019,8,20, 15,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 5),
            is(LocalDateTime.of(2019,8,20, 16,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 6),
            is(LocalDateTime.of(2019,8,21, 9,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 7),
            is(LocalDateTime.of(2019,8,21, 10,2)));
    }


    @Test
    public void testWeekendJumps() {
        LocalDateTime testTime = LocalDateTime.of(2019,8,20, 11,2);

        assertThat(new DueDateCalculator().calculateDueDate(testTime, 32),
            is(LocalDateTime.of(2019,8,26, 11,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 72),
            is(LocalDateTime.of(2019,9,2, 11,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 352),
            is(LocalDateTime.of(2019,10,21, 11,2)));
        assertThat(new DueDateCalculator().calculateDueDate(testTime, 672),
            is(LocalDateTime.of(2019,12,16, 11,2)));
    }

}
