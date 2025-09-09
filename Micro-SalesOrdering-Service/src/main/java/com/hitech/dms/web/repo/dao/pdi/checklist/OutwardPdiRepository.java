package com.hitech.dms.web.repo.dao.pdi.checklist;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitech.dms.web.controller.pdi.dto.PdiSearchResponse;
import com.hitech.dms.web.controller.pdi.dto.PdiViewHeaderResponse;
import com.hitech.dms.web.entity.pdi.OutwardPdiEntity;


public interface OutwardPdiRepository extends JpaRepository<OutwardPdiEntity, Long> {

	@Query(value = "exec sp_service_pdi_findByDealerIdAndMachineId :usercode,:machineId", nativeQuery = true)
    Long findByMachineMasterAndUsercode(@Param("usercode") String usercode, @Param("machineId") BigInteger machineId);
	
	@Query(value = "exec sp_service_outwardpdi_search_autocomplete_chassis_number :searchString,:userCode", nativeQuery = true)
    List<Map<String, Object>> searchAutocompleteChassisNumber(@Param("searchString") String searchString, @Param("userCode") String userCode);
	
	 @Query(value = "exec sp_service_outwardpdi_search :pdiFromDate,:pdiToDate,:chassisNo,:engineNo,:dmsGrnFromDate,:dmsGrnToDate,:pdiNumber,:page,:size,:invoiceNumber,:userCode", nativeQuery = true)
	    List<PdiSearchResponse> pdiSearch(@Param("pdiFromDate") String pdiFromDate,
	                                      @Param("pdiToDate") String pdiToDate,
	                                      @Param("chassisNo") String chassisNo,
	                                      @Param("engineNo") String engineNo,
	                                      @Param("dmsGrnFromDate") String dmsGrnFromDate,
	                                      @Param("dmsGrnToDate") String dmsGrnToDate,
	                                      @Param("pdiNumber") String pdiNumber,
	                                      @Param("page") Integer page,
	                                      @Param("size") Integer size,
	                                      @Param("invoiceNumber") String invoiceNumber,
	                                      @Param("userCode") String userCode
	                                      
	                                    );
	 
	 @Query(value = "exec sp_service_pdi_get_view_header_data :pdiId", nativeQuery = true)
	 PdiViewHeaderResponse pdiViewGetHeaderData(@Param("pdiId") Long pdiId);
	 
	 @Query(value = "exec sp_service_pdi_get_aggergate_checkpoint_list_by_pdiId :pdiId", nativeQuery = true)
	    List<Map<String, Object>> pdiViewGetCheckpointListByPdiId(@Param("pdiId") Long pdiId);
}
