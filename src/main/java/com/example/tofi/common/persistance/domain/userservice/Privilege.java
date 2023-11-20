package com.example.tofi.common.persistance.domain.userservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.compress.utils.Sets;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.tofi.common.persistance.domain.userservice.Privilege.PrivilegeObject.*;
import static com.example.tofi.common.persistance.domain.userservice.Privilege.PrivilegeType.*;

public enum Privilege implements GrantedAuthority {
    @JsonProperty("information_view")
    INFORMATION_VIEW(INFORMATION, VIEW),
    @JsonProperty("information_edit")
    INFORMATION_EDIT(INFORMATION, EDIT),

    @JsonProperty("terminals_view")
    TERMINALS_VIEW(TERMINALS, VIEW),
    @JsonProperty("terminals_commission_view")
    TERMINALS_COMMISSION_VIEW(TERMINALS, VIEW),

    @JsonProperty("users_view")
    USERS_VIEW(USERS, VIEW),
    @JsonProperty("users_add")
    USERS_ADD(USERS, ADD),
    @JsonProperty("users_edit")
    USERS_EDIT(USERS, EDIT),
    @JsonProperty("users_block")
    USERS_BLOCK(USERS, BLOCK),
    @JsonProperty("users_delete")
    USERS_DELETE(USERS, DELETE),
    @JsonProperty("users_reset")
    USERS_RESET(USERS, RESET),

    @JsonProperty("roles_view")
    ROLES_VIEW(ROLES, VIEW),
    @JsonProperty("roles_add")
    ROLES_ADD(ROLES, ADD),
    @JsonProperty("roles_edit")
    ROLES_EDIT(ROLES, EDIT),
    @JsonProperty("roles_delete")
    ROLES_DELETE(ROLES, DELETE),

    @JsonProperty("payment_view")
    PAYMENTS_VIEW(PAYMENTS, VIEW),
    @JsonProperty("payment_list")
    PAYMENTS_LIST(PAYMENTS, LIST),
    @JsonProperty("payment_refund")
    PAYMENTS_REFUND(PAYMENTS, REFUND),

    @JsonProperty("refund_view")
    REFUNDS_VIEW(REFUNDS, VIEW),
    @JsonProperty("refund_list")
    REFUNDS_LIST(REFUNDS, LIST),

    @JsonProperty("recurring_payment_view")
    RECURRING_PAYMENTS_VIEW(REC_PAYMENTS, VIEW),
    @JsonProperty("recurring_payment_list")
    RECURRING_PAYMENTS_LIST(REC_PAYMENTS, LIST),
    @JsonProperty("recurring_payment_block")
    RECURRING_PAYMENTS_BLOCK(REC_PAYMENTS, BLOCK),

    @JsonProperty("virtual_pos_view")
    VIRTUAL_POS_VIEW(VIRTUAL_CASH_DESKS, VIEW),
    @JsonProperty("virtual_pos_add")
    VIRTUAL_POS_ADD(VIRTUAL_CASH_DESKS, ADD),
    @JsonProperty("virtual_pos_edit")
    VIRTUAL_POS_EDIT(VIRTUAL_CASH_DESKS, EDIT),
    @JsonProperty("virtual_pos_block")
    VIRTUAL_POS_BLOCK(VIRTUAL_CASH_DESKS, BLOCK),
    @JsonProperty("virtual_pos_delete")
    VIRTUAL_POS_DELETE(VIRTUAL_CASH_DESKS, DELETE),

    @JsonProperty("cards_view")
    CARDS_VIEW(CARDS, VIEW),
    @JsonProperty("cards_add")
    CARDS_ADD(CARDS, ADD),
    @JsonProperty("cards_delete")
    CARDS_BLOCK(CARDS, DELETE),

    @JsonProperty("links_view")
    PAYMENT_LINKS_VIEW(PAYMENT_LINKS, VIEW),
    @JsonProperty("links_add")
    PAYMENT_LINKS_ADD(PAYMENT_LINKS, ADD),
    @JsonProperty("links_edit")
    PAYMENT_LINKS_EDIT(PAYMENT_LINKS, EDIT),
    @JsonProperty("links_delete")
    PAYMENT_LINKS_DELETE(PAYMENT_LINKS, DELETE),
    @JsonProperty("links_send")
    PAYMENT_LINKS_SEND(PAYMENT_LINKS, SEND),
    @JsonProperty("links_list")
    PAYMENT_LINKS_LIST(PAYMENT_LINKS, LIST),

    @JsonProperty("reports_view")
    REPORTS_VIEW(REPORTS, VIEW);

    private PrivilegeType privilegeType;
    private PrivilegeObject privilegeObject;

    Privilege(PrivilegeObject privilegeObject, PrivilegeType privilegeType) {
        this.privilegeObject = privilegeObject;
        this.privilegeType = privilegeType;
    }

    public static Set<Privilege> getAllValues() {
        return Sets.newHashSet(Privilege.values());
    }

    public static Set<Privilege> get(PrivilegeObject privilegeObject) {
        return getAllValues().stream().filter(p -> p.getObject() == privilegeObject).collect(Collectors.toSet());
    }

    public static Set<Privilege> get(PrivilegeType privilegeType) {
        return getAllValues().stream().filter(p -> p.getType() == privilegeType).collect(Collectors.toSet());
    }

    public static Optional<Privilege> get(PrivilegeObject privilegeObject, PrivilegeType privilegeType) {
        return getAllValues()
                .stream()
                .filter(p -> p.getObject() == privilegeObject && p.getType() == privilegeType)
                .findFirst();
    }

    @Override
    public String getAuthority() {
        return this.name();
    }

    public PrivilegeType getType() {
        return this.privilegeType;
    }

    public String getTypeName() {
        return this.privilegeType.name();
    }

    public PrivilegeObject getObject() {
        return this.privilegeObject;
    }

    public enum PrivilegeObject {
        USERS, ROLES, PAYMENTS, REFUNDS, REC_PAYMENTS, VIRTUAL_CASH_DESKS, CARDS, CLIENT_CARDS, PAYMENT_LINKS, REPORTS, INFORMATION, TERMINALS
    }

    public enum PrivilegeType {
        VIEW, ADD, EDIT, BLOCK, DELETE, RESET, REGISTER, SETTINGS, SEND, LIST, REFUND
    }
}