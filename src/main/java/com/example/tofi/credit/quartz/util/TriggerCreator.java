package com.example.tofi.credit.quartz.util;

import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class TriggerCreator {

    @Autowired
    MessageUtil ms;

    public Trigger createTrigger(String jobName, Credit credit) {
        Date date = convertToDate(credit.getNextPayDate());
        int interval = 1;
        DateBuilder.IntervalUnit unit = DateBuilder.IntervalUnit.MONTH;
        return createTrigger(jobName, date, interval, unit);
    }

    private Trigger createTrigger(String jobName, Date date, int interval, DateBuilder.IntervalUnit unit) {
        ScheduleBuilder<CalendarIntervalTrigger> scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(interval, unit);
        return TriggerBuilder.newTrigger()
                .withIdentity(jobName, "credit-trigger")
                .startAt(date)
                .withSchedule(scheduleBuilder)
                .build();
    }

    private Date convertToDate(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
