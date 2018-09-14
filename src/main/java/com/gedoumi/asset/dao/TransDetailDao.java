package com.gedoumi.asset.dao;

import com.gedoumi.asset.entity.TransDetail;
import com.gedoumi.asset.entity.UserAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransDetailDao extends JpaRepository<TransDetail, Long> {


}
