package com.hitech.dms.web.repo.dao.pdi.checklist;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitech.dms.web.entity.pdi.PdiAggregate;

public interface OutwardPdiAggregateRepo extends JpaRepository<PdiAggregate, Long> {

	@Query(value = "exec sp_service_outwardpdi_autocomplete_chassisNo :userCode,:chassisNo", nativeQuery = true)
    List<Map<String, Object>> autoCompleteChassisNo(@Param("userCode") String userCode,
                                                    @Param("chassisNo") String chassisNo);
	
	@Query(value = "exec sp_service_outwardpdi_get_grnDetails_by_chassisNo :chassisNo, :userCode", nativeQuery = true)
    List<Map<String, Object>> grnDetailsByChassisNo(@Param("chassisNo") String chassisNo,@Param("userCode") String userCode);
}
