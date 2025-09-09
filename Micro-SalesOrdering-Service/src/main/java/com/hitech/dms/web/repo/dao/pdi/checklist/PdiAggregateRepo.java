package com.hitech.dms.web.repo.dao.pdi.checklist;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitech.dms.web.controller.pdi.dto.CheckDraftModeDto;
import com.hitech.dms.web.entity.pdi.PdiAggregate;

public interface PdiAggregateRepo extends JpaRepository<PdiAggregate, Long> {
	
	@Query(value = "exec sp_service_pdi_autocomplete_chassisNo :userCode,:chassisNo", nativeQuery = true)
    List<Map<String, Object>> autoCompleteChassisNo(@Param("userCode") String userCode,
                                                    @Param("chassisNo") String chassisNo);
	
	
	@Query(value = "exec sp_service_pdi_check_chassis_no_draft_mode :chassisNo,:userCode ", nativeQuery = true)
    CheckDraftModeDto servicePdiDraftModeCheck(@Param("chassisNo") String chassisNo, @Param("userCode") String userCode);

	
	@Query(value = "exec sp_service_pdi_get_grnDetails_by_chassisNo :chassisNo", nativeQuery = true)
    Map<String, Object> grnDetailsByChassisNo(@Param("chassisNo") String chassisNo);
	
	
	@Query(value = "exec sp_service_pdi_autocomplete_InvoiceNumber :userCode,:invoiceNo", nativeQuery = true)
    List<Map<String, Object>> autoCompleteInvoiceNo(@Param("userCode") String userCode,
                                                    @Param("invoiceNo") String invoiceNo);
	
	
	@Query(value = "exec sp_service_pdi_search_autocomplete_chassis_number_by_invoice :userCode, :invoiceNo", nativeQuery = true)
	List<Map<String, Object>> chassisDetailsByInvoiceNo(@Param("userCode") String userCode,@Param("invoiceNo") String invoiceNo);
	
	@Query(value = "exec SP_GET_MECHANIC_DEALER_LIST :userCode", nativeQuery = true)
	List<Map<String, Object>>  machnicList(@Param("userCode") String userCode);
	
	@Query(value = "exec sp_service_pdiNumber_filter :pdiNo", nativeQuery = true)
    List<Map<String, Object>> autoCompletePdiNo(@Param("pdiNo") String pdiNo);
	
	@Query(value = "exec sp_service_invoiceNumber_filter :invoiceNo", nativeQuery = true)
    List<Map<String, Object>> autoCompleteInvoiceNo(@Param("invoiceNo") String invoiceNo);
	
}
