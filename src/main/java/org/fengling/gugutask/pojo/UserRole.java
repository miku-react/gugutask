package org.fengling.gugutask.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserRole {
    private Long userId;
    private Long roleId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

}

