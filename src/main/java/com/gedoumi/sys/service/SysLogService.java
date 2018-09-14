package com.gedoumi.sys.service;

import com.gedoumi.common.Constants;
import com.gedoumi.sys.dao.SysLogDao;
import com.gedoumi.sys.dao.SysSmsDao;
import com.gedoumi.sys.entity.SysLog;
import com.gedoumi.sys.entity.SysSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class SysLogService {
	
	@Autowired
	private SysLogDao logDao;

	@Transactional
	public void add(SysLog sysLog){
		logDao.save(sysLog);
	}

	@Transactional
	public void update(SysLog sysLog){
		logDao.save(sysLog);
	}



}
