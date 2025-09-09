package com.hitech.dms.web.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitech.dms.web.entity.user.FailedLogin;

public interface FailedLoginRepository extends JpaRepository<FailedLogin,Long>
{
}
