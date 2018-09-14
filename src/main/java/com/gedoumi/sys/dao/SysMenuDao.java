package com.gedoumi.sys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gedoumi.sys.entity.SysMenu;

@Repository
public interface SysMenuDao extends JpaRepository<SysMenu, Long> {

}
