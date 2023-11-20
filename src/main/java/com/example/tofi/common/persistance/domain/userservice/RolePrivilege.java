package com.example.tofi.common.persistance.domain.userservice;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Table(name = "privilege_")
public class RolePrivilege extends AbstractPersistentObject {
    @NotBlank
    String name;
}