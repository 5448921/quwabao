package com.gedoumi.sys.dao;

import com.gedoumi.sys.entity.SysLog;
import com.gedoumi.sys.entity.SysSms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogDao extends JpaRepository<SysLog, Long> {

}
