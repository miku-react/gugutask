package org.fengling.gugutask.service.impl;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        User user = userService.findByUsername(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> roles = userService.findRolesByUserId(user.getId());
        authorizationInfo.setRoles(roles);

        for (String role : roles) {
            Long roleId = userService.findRoleIdByRoleName(role);
            Set<String> permissions = userService.findPermissionsByRoleId(roleId);
            authorizationInfo.addStringPermissions(permissions);
        }

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException(); // 用户不存在
        }
        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
    }
}
