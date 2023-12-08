package com.example.tofi.credit.quartz.job;

import com.example.tofi.auth.service.EmailService;
import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.persistance.domain.creditservice.CreditStatus;
import com.example.tofi.common.persistance.repository.AccountRepository;
import com.example.tofi.common.persistance.repository.CreditRepository;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class CreditJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        ApplicationContext applicationContext = null;
        try {
            applicationContext = (ApplicationContext)
                    context.getScheduler().getContext().get("applicationContext");
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        CreditRepository creditRepository = applicationContext.getBean(CreditRepository.class);
        AccountRepository accountRepository = applicationContext.getBean(AccountRepository.class);
        EmailService emailService = applicationContext.getBean(EmailService.class);
        Long creditId =(Long) context.getMergedJobDataMap().get("creditId");
        Credit credit = creditRepository
                .findById(creditId)
                .orElseThrow(()-> new RuntimeException("Credit not found"));
        Account account = accountRepository
                .findById(credit.getAccountId())
                .orElseThrow(()->new RuntimeException("Account not found"));
        //Если счет заблочен, уведомляем пользователя и даем ему возможность еще раз заплатить
        if (account.getIsBlocked()) {
            emailService.sendSimpleMessage("billingsystemgroup@gmail.com",
                    credit.getEmailForNotification(),
                    "Credit in TOFIBANK",
                    "Ваш счет заблокирован.Обратитесь в банк для разблокировки счета и проведите платеж по кредиту вручную" );
            credit.setIsNeedManualPayment(true);
        }
        //Проверка оплатил ли пользователь кредит вручную после неудачной прошлой автооплаты
        else if(credit.getIsNeedManualPayment()){
            emailService.sendSimpleMessage("billingsystemgroup@gmail.com",
                    credit.getEmailForNotification(),
                    "Credit in TOFIBANK",
                    "Ваш счет заблокирован из-за неоплаты прошлого платежа.");
            account.setIsBlocked(true);
        }
        if (account.getBalance() >= credit.getPerMonthPaySum()) {
            account.setBalance(BigDecimal.valueOf(account.getBalance()).subtract(BigDecimal.valueOf(credit.getPerMonthPaySum())).doubleValue());
            credit.setDebt(BigDecimal.valueOf(credit.getDebt()).subtract(BigDecimal.valueOf(credit.getPerMonthPaySum())).doubleValue());
        } else {
            emailService.sendSimpleMessage("billingsystemgroup@gmail.com",
                    credit.getEmailForNotification(),
                    "Credit in TOFIBANK",
                    "На вашем счет недостаточно средств, во изюежании блокировки, пополните баланс счета и проведите платеж по кредиту вручную");
            credit.setIsNeedManualPayment(true);
        }
        if (credit.getDebt().equals(0.0)) {
            credit.setStatus(CreditStatus.PAID);
        } else {
            credit.setNextPayDate(credit.getNextPayDate().plusMinutes(1));
        }

        accountRepository.save(account);
        creditRepository.save(credit);
        System.out.println("Next fire time : " + context.getNextFireTime());
        System.out.println("Payment made");
    }
}