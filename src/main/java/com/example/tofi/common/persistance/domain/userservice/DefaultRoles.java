package com.example.tofi.common.persistance.domain.userservice;

import lombok.Getter;

@Getter
public enum DefaultRoles {

    ADMIN_PS("Admin PS"),
    AGENT("Agent"),
    AGENT_ADMIN("Agent Admin"),
    AGENT_DIRECTOR("Agent Director"),
    AGENT_ACCOUNTANT("Agent Accountant"),
    PAYMENT_REGISTRATION("Payment Registration");

    private final String name;

    DefaultRoles(String name) {
        this.name = name;
    }

}