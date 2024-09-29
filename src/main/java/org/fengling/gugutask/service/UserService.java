package org.fengling.gugutask.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fengling.gugutask.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserService extends IService<User> {
    User findByUsername(String username);

    User findByUserId(Long userId);

    List<String> findRolesByUserId(Long userId);

    // 新增方法，获取用户的角色和权限，并转换为Spring Security的GrantedAuthority对象
    Set<GrantedAuthority> getUserAuthorities(Long userId);

    void deleteUserById(Long userId);
}
