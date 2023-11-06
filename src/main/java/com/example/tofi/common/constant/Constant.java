package com.example.tofi.common.constant;

public class Constant {
    // Topics
    public static final String TOPIC_NAME_BIN = "bin-topic";
    public static final String TOPIC_NAME_BANK = "bank-topic";
    public static final String TOPIC_NAME_COMMISSION_BANK = "bankCommission-topic";
    public static final String TOPIC_NAME_COMMISSION_PAYER = "payerCommission-topic";
    public static final String TOPIC_NAME_COMMISSION_VIRTUAL_POS = "virtualPosCommission-topic";
    public static final String TOPIC_NAME_VIRTUAL_POS = "virtualPos-topic";
    public static final String TOPIC_NAME_PAYLINK = "paylink-topic";
    public static final String TOPIC_NAME_PAYLINK_SAVED = "paylink-saved-topic";
    public static final String TOPIC_NAME_AUDIT = "audit-topic";
    public static final String TOPIC_NAME_PAYMENT = "payment-topic";
    public static final String TOPIC_NAME_PAYLINK_USED = "paylink-used-topic";

    // Common
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String CHECK_DATE = "yyyy-MM-dd HH:mm:ss";
    public static final Integer CARD_TOKEN_ERROR_CODE = 444;
    public static final String QP_MAIL = "payments@qazaqpay.kz";
    public static final String CALL_URL = "http://BANK-ROUTER-SERVICE/api/banks/call";

    public static final String P2P_REFUND_URL = "http://192.168.166.76:85/payment-to-card";
    public static final Integer PAYMENT_EXPIRATION_TIME = 10;
    public static final String CHECK_IS_USED_URL = "http://TRANSACTION-SERVICE/api/payments/check_is_used";


}
