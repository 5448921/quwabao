package com.gedoumi.sys.service;

import com.gedoumi.asset.entity.UserTeam;
import com.gedoumi.asset.vo.SysRentVO;
import com.gedoumi.asset.vo.UserTeamVO;
import com.gedoumi.common.Constants;
import com.gedoumi.common.base.DataGrid;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.common.enums.RentStatus;
import com.gedoumi.common.enums.TeamStatus;
import com.gedoumi.sys.dao.SysLogDao;
import com.gedoumi.sys.dao.SysRentDao;
import com.gedoumi.sys.entity.SysLog;
import com.gedoumi.sys.entity.SysRent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class SysRentService {
	
	@Autowired
	private SysRentDao rentDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void add(SysRent sysRent){
		Date now = new Date();
		sysRent.setCreateTime(now);
		sysRent.setUpdateTime(now);
		BigDecimal rateExt = sysRent.getRate().add(BigDecimal.ONE);
		sysRent.setProfitMoney(sysRent.getMoney().multiply(sysRent.getRate()).setScale(Constants.SCALE_PROFIT,BigDecimal.ROUND_HALF_UP));
		sysRent.setProfitMoneyExt(sysRent.getMoney().multiply(rateExt).setScale(Constants.SCALE_PROFIT, BigDecimal.ROUND_HALF_UP));
		rentDao.save(sysRent);
	}

	@Transactional
	public void updateInfo(SysRent sysRent){
		Date now = new Date();
		sysRent.setUpdateTime(now);
		BigDecimal rateExt = sysRent.getRate().add(BigDecimal.ONE);
		sysRent.setProfitMoney(sysRent.getMoney().multiply(sysRent.getRate()).setScale(Constants.SCALE_PROFIT,BigDecimal.ROUND_HALF_UP));
		sysRent.setProfitMoneyExt(sysRent.getMoney().multiply(rateExt).setScale(Constants.SCALE_PROFIT, BigDecimal.ROUND_HALF_UP));
		rentDao.save(sysRent);
	}

	@Transactional
	public void updateRentNumber(SysRent sysRent){
		Date now = new Date();
		SysRent org = rentDao.findOne(sysRent.getId());
		org.setUpdateTime(now);
		org.setRentNumber(sysRent.getRentNumber());
		rentDao.save(org);
	}

	public SysRent getById(Long id){
		return  rentDao.findOne(id);
	}

	public SysRent getByCode(String code){
		return  rentDao.findByCode(code);
	}

	public List<SysRent> getAcviteList(){
		return rentDao.findByRentStatus(RentStatus.Active.getValue());
	}


	public DataGrid getList(PageParam param, SysRentVO sysRentVO){
		DataGrid data=new DataGrid();
		String prefSql = " from SysRent t where 1=1 ";
		StringBuffer sqlCount = new StringBuffer("select count(t.id)  ").append(prefSql);
		StringBuffer sqlData = new StringBuffer("select t ").append(prefSql);
		String name = sysRentVO.getName();

		if(StringUtils.isNotEmpty(name)){
			sqlCount.append("and t.name like :name ");
			sqlData.append("and t.name like :name ");
		}
		Query queryCount = entityManager.createQuery(sqlCount.toString());
		Query queryData = entityManager.createQuery(sqlData.toString());
		if(StringUtils.isNotEmpty(name)){
			queryCount.setParameter("name", "%"+name+"%");
			queryData.setParameter("name", "%"+name+"%");
		}
		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());

		List<UserTeam> list = (List<UserTeam>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();

		data.setTotal(count);
		data.setRows(list);
		return data;
	}



}
