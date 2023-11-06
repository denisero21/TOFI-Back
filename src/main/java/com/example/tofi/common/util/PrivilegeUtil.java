package com.example.tofi.common.util;

import com.example.tofi.common.persistance.domain.user.RolePrivilege;
import org.springframework.util.Assert;

import java.util.Set;

public class PrivilegeUtil {
    public static boolean arePrivilegeSetsEqual(Set<RolePrivilege> set1, Set<RolePrivilege> set2) {
        Assert.notNull(set1, "Privileges set1 may not be null");
        Assert.notNull(set2, "Privileges set2 may not be null");
        if (set1.size() == set2.size()) {
            return set1.containsAll(set2);
        }
        return false;
    }
}