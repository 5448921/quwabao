package com.gedoumi.asset.dao;

import com.gedoumi.asset.entity.UserTeamReward;
import com.gedoumi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserTeamRewardDao extends JpaRepository<UserTeamReward, Long> {

     List<UserTeamReward> findByUser(User user);

     List<UserTeamReward> findByRemainFrozenAfter(BigDecimal remainAsset);


}
