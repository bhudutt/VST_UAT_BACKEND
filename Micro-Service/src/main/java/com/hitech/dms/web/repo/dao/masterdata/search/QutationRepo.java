package com.hitech.dms.web.repo.dao.masterdata.search;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitech.dms.web.entity.quotation.create.request.ServiceQuotationEntity;
import com.hitech.dms.web.model.masterdata.response.QuotationSearchResponse;

public interface QutationRepo extends JpaRepository<ServiceQuotationEntity,Long> {

	 @Query(value = "exec sp_quotation_search :quotationNo,:page,:size,:fromDate,:toDate,:includeInActive,:segment,:series,:variant,:userCode,:status",nativeQuery = true)
	 List<QuotationSearchResponse> rcSearch(
	                                      @Param("quotationNo") String quotationNo,
	                                      @Param("page") Integer page,
	                                      @Param("size") Integer size,
	                                      @Param("fromDate") String fromDate,
	                                      @Param("toDate") String toDate,
	                                      @Param("includeInActive") String includeInActive,
	                                      @Param("segment") String segment,
	                                      @Param("series") String series,
	                                      @Param("variant") String variant,
	                                      @Param("userCode") String userCode,
	                                      @Param("status") String status
	                                    );
	 
	
	
}
