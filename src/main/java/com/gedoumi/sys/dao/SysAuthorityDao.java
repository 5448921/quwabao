package com.gedoumi.sys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gedoumi.sys.entity.SysAuthority;

@Repository
public interface SysAuthorityDao extends JpaRepository<SysAuthority, Long> {

	public List<SysAuthority> findByMenuId(Long menuId);
}
