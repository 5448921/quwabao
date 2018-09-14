package com.gedoumi.sys.dao;

import com.gedoumi.sys.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface SysUserDao extends JpaRepository<SysUser, Long> {
	 SysUser findByUsername(String username);

	 SysUser findByUsernameAndUserStatus(String username, Integer userStatus);

}
