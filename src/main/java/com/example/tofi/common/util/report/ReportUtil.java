package com.example.tofi.common.util.report;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportUtil {
    public static Pair<String, Resource> generateReportNameWithResourceReport(String entityName, SXSSFWorkbook wb) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh_mm_ss");
        String formattedDateTime = formatter.format(LocalDateTime.now());
        String excelFileName = entityName + "_" + formattedDateTime + ".xlsx";
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            wb.write(outByteStream);
            wb.dispose();
            wb.close();
            return new ImmutablePair<>(excelFileName, new ByteArrayResource(outByteStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImmutablePair<>("", new ByteArrayResource(new byte[0]));
    }

    public static final List<String> REPORT_PAYLINK_HEADERS = List.of(
            "paylinkHeader.id",
            "paylinkHeader.agentId",
            "paylinkHeader.isBlocked",
            "paylinkHeader.amount",
            "paylinkHeader.currency",
            "paylinkHeader.dateLinkFrom",
            "paylinkHeader.dateLinkTo",
            "paylinkHeader.fullName",
            "paylinkHeader.virtualPosName",
            "paylinkHeader.paylinkType",
            "paylinkHeader.isSendByEmail",
            "paylinkHeader.email",
            "paylinkHeader.isSinglePayment",
            "paylinkHeader.invoiceId",
            "paylinkHeader.serviceProductName"
    );

    public static final List<String> REPORT_PAYMENT_HEADERS = List.of(
            "paymentHeader.id",
            "paymentHeader.billId",
            "paymentHeader.payerId",
            "paymentHeader.agentClientId",
            "paymentHeader.bankPaymentId",
            "paymentHeader.fullName",
            "paymentHeader.address",
            "paymentHeader.date",
            "paymentHeader.paymentSum",
            "paymentHeader.currency",
            "paymentHeader.transferSum",
            "paymentHeader.commissionSum",
            "paymentHeader.commissionPayerSum",
            "paymentHeader.commissionAgentSum",
            "paymentHeader.terminalId",
            "paymentHeader.invoiceId",
            "paymentHeader.payerCard",
            "paymentHeader.payeeCard",
            "paymentHeader.refundAmount",
            "paymentHeader.refundAvailableSum",
            "paymentHeader.virtualPosId",
            "paymentHeader.paylinkId",
            "paymentHeader.paymentType",
            "paymentHeader.paymentStatus",
            "paymentHeader.bankName",
            "paymentHeader.recPaymentId",
            "paymentHeader.paymentDirection"
    );

    public static final List<String> REPORT_REC_PAYMENT_HEADERS = List.of(
            "recPaymentHeader.id",
            "recPaymentHeader.periodType",
            "recPaymentHeader.paymentSum",
            "recPaymentHeader.currency",
            "recPaymentHeader.name",
            "recPaymentHeader.maxPaymentSum",
            "recPaymentHeader.startDate",
            "recPaymentHeader.endDate",
            "recPaymentHeader.registerDate",
            "recPaymentHeader.isBlocked",
            "recPaymentHeader.phoneNumber",
            "recPaymentHeader.notificationType",
            "recPaymentHeader.payerId",
            "recPaymentHeader.fullName",
            "recPaymentHeader.cardNumMask"
    );

    public static final List<String> REPORT_REFUND_HEADERS = List.of(
            "refundHeader.bankPaymentId",
            "refundHeader.id",
            "refundHeader.bankName",
            "refundHeader.amount",
            "refundHeader.currency",
            "refundHeader.date",
            "refundHeader.paymentPurpose",
            "refundHeader.payerId",
            "refundHeader.payerAgentId",
            "refundHeader.fullName",
            "refundHeader.paymentId"
    );

    public static final List<String> REPORT_USER_HEADERS = List.of(
            "userHeader.fullName",
            "userHeader.email",
            "userHeader.phoneNumber",
            "userHeader.roleName",
            "userHeader.twoFactorAuth",
            "userHeader.date",
            "userHeader.isBlocked"
    );

    public static String generateFileName(String entityName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh_mm_ss");
        String formattedDateTime = formatter.format(LocalDateTime.now());
        return "qazaqpay_merchant_" + entityName + "_" + formattedDateTime + ".txt";
    }
}