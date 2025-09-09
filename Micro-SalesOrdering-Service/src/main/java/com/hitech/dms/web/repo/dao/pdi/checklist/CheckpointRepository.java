package com.hitech.dms.web.repo.dao.pdi.checklist;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hitech.dms.web.entity.pdi.ServiceMtCheckPoint;

public interface CheckpointRepository extends JpaRepository<ServiceMtCheckPoint,Long>  {
	
	@Query(value = "exec sp_mt_service_getChecklist :transType,:chassis,:model",nativeQuery = true)
    List<Map<String,Object>> getAllCheckpoint(String transType, String chassis, String model);

}
