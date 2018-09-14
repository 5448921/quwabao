package com.gedoumi.sys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gedoumi.sys.entity.SysRoleAuthority;

@Repository
public interface SysRoleAuthorityDao extends JpaRepository<SysRoleAuthority, Long> {

	public SysRoleAuthority findByAuthIdAndRoleId(Long authId, Long roleId);
	
	public List<SysRoleAuthority> findByRoleId(Long roleId);
	
	@Query(value = "select ra from SysRoleAuthority ra left join SysAuthority a on a.id = ra.authId where a.menuId = ?")
	public List<SysRoleAuthority> findByMenuId(Long menuId);
}
