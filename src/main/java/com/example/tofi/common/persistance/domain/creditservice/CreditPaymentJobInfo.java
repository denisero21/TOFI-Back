package com.example.tofi.common.persistance.domain.creditservice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "credit_payment_job_info")
public class CreditPaymentJobInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    private String jobName;
    private String jobGroup;
    private String jobStatus;
    private String jobClass;

    @Column(name = "credit_payment_id")
    private Long creditId;
}
