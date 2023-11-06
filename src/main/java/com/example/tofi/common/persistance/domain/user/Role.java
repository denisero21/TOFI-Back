package com.example.tofi.common.persistance.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Table(
        name = "role_",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "users_role_idx")})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends AbstractPersistentObject {
    @NotBlank
    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_privilege",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    @ToString.Exclude
    Set<RolePrivilege> rolePrivileges;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role role = (Role) obj;
        return getName().equals(role.getName());
    }
}