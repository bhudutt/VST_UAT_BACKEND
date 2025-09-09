package com.hitech.dms.web.repo.dao.pdi.checklist;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitech.dms.web.entity.pdi.PdiDetailsEntity;

public interface ServiceCheckpointRepo extends JpaRepository<PdiDetailsEntity,Object> {

}
