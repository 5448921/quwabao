package com.gedoumi.asset.dao;

import com.gedoumi.asset.entity.UserTeam;
import com.gedoumi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamDao extends JpaRepository<UserTeam, Long> {

     UserTeam findByUser(User user);

}
