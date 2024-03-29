package com.gedoumi.sys.dao;

import com.gedoumi.sys.entity.SysLog;
import com.gedoumi.sys.entity.SysRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRentDao extends JpaRepository<SysRent, Long> {

    SysRent findByCode(String code);

    List<SysRent> findByRentStatus(Integer rentStatus);
}
