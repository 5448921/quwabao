package com.gedoumi.sys.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gedoumi.common.base.DataGrid;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.sys.dao.SysAuthorityDao;
import com.gedoumi.sys.entity.SysAuthority;

@Service
public class SysAuthorityService {

	@Autowired
	private SysAuthorityDao sysAuthorityDao;
	
	@PersistenceContext  
    private EntityManager entityManager; 
	
	public List<SysAuthority> getAll(){
		return sysAuthorityDao.findAll();
	}
	
	@SuppressWarnings("unchecked")
	public DataGrid getListByMenu(PageParam param, Long menuId){
		DataGrid data=new DataGrid();
		StringBuffer sqlCount = new StringBuffer("select count(id) from SysAuthority");
		StringBuffer sqlData = new StringBuffer("select r from SysAuthority r");
		if(menuId != null){
			sqlCount.append(" where menuId = ").append(menuId);
			sqlData.append(" where r.menuId = ").append(menuId);
		}
			
		Query queryCount = entityManager.createQuery(sqlCount.toString());
		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());
		Query queryData = entityManager.createQuery(sqlData.toString());
		List<SysAuthority> list = (List<SysAuthority>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();
		data.setTotal(count);
		data.setRows(list);
		return data;
	}
	
	public SysAuthority getById(Long id){
		return sysAuthorityDao.findOne(id);
	}
	
	public SysAuthority create(SysAuthority sysAuthority){
		return sysAuthorityDao.save(sysAuthority);
	}
	
	public SysAuthority update(SysAuthority sysAuthority){
		return sysAuthorityDao.save(sysAuthority);
	}
	
	public Boolean delete(String[] ids){
		List<SysAuthority> list =new ArrayList<SysAuthority>();
		for(String id : ids){
			SysAuthority authority = sysAuthorityDao.findOne(Long.valueOf(id));
			if(authority!=null){
				list.add(authority);
			}
		}
		sysAuthorityDao.deleteInBatch(list);
		return true;
	}
	
	public Boolean delete(Long id){
		sysAuthorityDao.delete(id);
		return true;
	}
	
	public Boolean deleteByMenuId(Long menuId){
		List<SysAuthority> list = sysAuthorityDao.findByMenuId(menuId);
		sysAuthorityDao.deleteInBatch(list);
		return true;
	}
}
