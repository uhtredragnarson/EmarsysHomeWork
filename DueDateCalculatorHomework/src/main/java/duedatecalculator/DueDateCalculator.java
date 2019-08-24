package duedatecalculator;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DueDateCalculator {

    /**
     * Calculates and returns the date and time when the issue is to be resolved based on the offset that was given.
     *
     * @author nagypa
     * @param timeOfSubmit the date and time the problem was submitted.
     * @param offsetInHours the turnaround time given in working hours.
     * @return a {@code LocalDateTime} based on this date-time with the requested hour, not null.
     */
    public LocalDateTime calculateDueDate(LocalDateTime timeOfSubmit, long offsetInHours) {

        if (timeOfSubmit == null) {
            throw new IllegalArgumentException("The timeOfSubmit should not be null!");
        }

        if (offsetInHours == 0) {
            return timeOfSubmit;
        }

        int workingHoursStart = 9;
        int workingHoursEnd = 17;
        int hourOfSubmit = timeOfSubmit.getHour();
        int jumpToMonday;
        LocalDateTime result;

        if (hourOfSubmit >= workingHoursEnd || hourOfSubmit < workingHoursStart) {
            throw new IllegalArgumentException("Time of submit must be between 9AM and 5PM.");
        } else if (timeOfSubmit.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            jumpToMonday = 2;
            result = new DueDateCalculator().calculateDueDate(timeOfSubmit.plusDays(jumpToMonday), offsetInHours);
        } else if (timeOfSubmit.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            jumpToMonday = 1;
            result = new DueDateCalculator().calculateDueDate(timeOfSubmit.plusDays(jumpToMonday), offsetInHours);
        } else {
            if (hourOfSubmit + offsetInHours < workingHoursEnd) {
                result = timeOfSubmit.plusHours(offsetInHours);
                return result;
            } else {
                long remainingHours = offsetInHours - (workingHoursEnd - hourOfSubmit);
                result = new DueDateCalculator().calculateDueDate(timeOfSubmit.plusDays(1).withHour(workingHoursStart), remainingHours);
            }
        }

        return result;
    }
}
