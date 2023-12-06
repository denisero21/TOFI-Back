package com.example.tofi.credit.service;

import com.example.tofi.common.exception.SchedulerJobException;
import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.persistance.domain.creditservice.CreditPaymentJobInfo;
import com.example.tofi.common.persistance.repository.CreditPaymentJobInfoRepository;
import com.example.tofi.common.util.MessageUtil;
import com.example.tofi.credit.quartz.job.CreditJob;
import com.example.tofi.credit.quartz.util.TriggerCreator;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class SchedulerJobService {

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final CreditPaymentJobInfoRepository schedulerRepository;
    private final TriggerCreator triggerCreator;
    private final MessageUtil ms;

    public void scheduleNewCredit(Long creditId, Credit credit)  {

        CreditPaymentJobInfo jobInfo = new CreditPaymentJobInfo();
        jobInfo.setJobName("job_credit_payment_id_" + creditId);
        jobInfo.setJobClass("CreditJob");
        jobInfo.setJobGroup("Credits");
        jobInfo.setCreditId(creditId);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("creditId",creditId);

        String jobName = jobInfo.getJobName();

        JobDetail jobDetail = JobBuilder.newJob(CreditJob.class)
                .withIdentity(jobName, jobInfo.getJobGroup())
                .usingJobData(jobDataMap)
                .build();

        Trigger trigger = triggerCreator.createTrigger(jobName, credit);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new SchedulerJobException(ms.getMessage("error.scheduler"));
        }
        jobInfo.setJobStatus("SCHEDULED");
        schedulerRepository.save(jobInfo);
    }

    public void deleteJob(Long creditId) {
        try {
            CreditPaymentJobInfo getJobInfo = schedulerRepository
                    .findByCreditId(creditId)
                    .orElseThrow(()->new RuntimeException("Scheduled credit not found"));
            schedulerRepository.delete(getJobInfo);
            schedulerFactoryBean.getScheduler().deleteJob(new JobKey(getJobInfo.getJobName(), getJobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            throw new SchedulerJobException(ms.getMessage("error.scheduler"));
        }
    }

    public void updateScheduledJob(Long creditId, Credit credit) {
        CreditPaymentJobInfo jobInfo = schedulerRepository
                .findByCreditId(creditId)
                .orElseThrow(()->new RuntimeException("Scheduled credit not found"));
        Trigger newTrigger = triggerCreator.createTrigger(jobInfo.getJobName(), credit);
        try {
            schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
        } catch (SchedulerException e) {
            throw new SchedulerJobException(ms.getMessage("error.scheduler"));
        }

    }

    public void pauseScheduledJob(Long creditId) {
        CreditPaymentJobInfo jobInfo = schedulerRepository
                .findByCreditId(creditId)
                .orElseThrow(()->new RuntimeException("Scheduled credit not found"));
        try {
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            throw new SchedulerJobException(ms.getMessage("error.scheduler"));
        }
    }

    public void resumeScheduledJob(Long creditId) {
        CreditPaymentJobInfo jobInfo = schedulerRepository
                .findByCreditId(creditId)
                .orElseThrow(()->new RuntimeException("Scheduled credit not found"));
        try {
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            throw new SchedulerJobException(ms.getMessage("error.scheduler"));
        }
    }
}
