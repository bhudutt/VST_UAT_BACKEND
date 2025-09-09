package com.hitech.dms.web.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitech.dms.web.entity.user.SessionHistory;

public interface SessionHistoryRepository extends JpaRepository<SessionHistory,Long>
{
}
