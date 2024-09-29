package org.fengling.gugutask.dto;

import lombok.Data;

@Data
public class RoleD {
    private String roleName;

    public RoleD() {
    }

    public RoleD(String roleName) {
        this.roleName = roleName;
    }
}
