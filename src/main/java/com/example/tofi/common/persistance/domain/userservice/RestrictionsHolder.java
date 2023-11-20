package com.example.tofi.common.persistance.domain.userservice;

import java.util.Set;

import static com.example.tofi.common.persistance.domain.userservice.DefaultRoles.*;


public class RestrictionsHolder {
    public static final Set<String> ROLES_PREVENTED_FOR_MODIFICATION = Set.of(ADMIN_PS.getName(), AGENT.getName(), AGENT_ADMIN.getName(), AGENT_DIRECTOR.getName(), AGENT_ACCOUNTANT.getName(), PAYMENT_REGISTRATION.getName());
}