package com.hitech.dms.web.repo.dao.pdi.checklist;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitech.dms.web.entity.pdi.CheckpointSpecification;

public interface CheckpointSpecificationRepository extends JpaRepository<CheckpointSpecification,Long> {

	@Query(value = "{call sp_service_checkpoint_specification_by_checkpointId(:checkpointId)}",nativeQuery = true)
    List<Map<String,Object>> getSpecificationList(@Param("checkpointId") Integer checkpontId);
}
