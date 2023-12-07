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
        Date endDate = convertToDate(credit.getNextPayDate().plusMinutes(credit.getTerm().getTerm()));
        int interval = 1;
        DateBuilder.IntervalUnit unit = DateBuilder.IntervalUnit.MINUTE;
        return createTrigger(jobName, date, endDate, interval, unit);
    }

    private Trigger createTrigger(String jobName, Date date, Date endDate, int interval, DateBuilder.IntervalUnit unit) {
        ScheduleBuilder<CalendarIntervalTrigger> scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(interval, unit);
        return TriggerBuilder.newTrigger()
                .withIdentity(jobName, "credit-trigger")
                .startAt(date)
                .endAt(endDate)
                .withSchedule(scheduleBuilder)
                .build();
    }

    private Date convertToDate(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
