
package com.hitech.dms.web.dao.masterdata.search;

import java.util.List;
import java.util.Map;

import com.hitech.dms.web.entity.master.servicesourcemaster.request.LabourGroupMasterEntity;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.PartyMasterEntity;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.ServiceLabourMasterEntity;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.ServiceSourceEntity;
import com.hitech.dms.web.entity.partmaster.create.request.AdminPartMasterEntity;
import com.hitech.dms.web.model.masterdata.request.MasterDataModelRequest;
import com.hitech.dms.web.model.masterdata.request.TaxDTO;
import com.hitech.dms.web.model.masterdata.request.TaxDetailsDTO;
import com.hitech.dms.web.model.masterdata.response.MasterDataModelResponse;
import com.hitech.dms.web.model.partmaster.create.request.PartDetailsDTO;



public interface MasterDataDao {

	public List<ServiceSourceEntity> searchSourceList(String userCode);

	public List<?> searchRepairCategoryList(String userCode);

	public List<PartyMasterEntity> searchinsuranceList(String userCode, String partyCatgCode);

	public List<?> serviceCategoryList(String userCode);

	public Map<Integer, String> fetchCustomerVoice(String userCode);

	public List<MasterDataModelResponse> chassisSearchList(String userCode, MasterDataModelRequest request);

	public MasterDataModelResponse getChassisDetails(String userCode, String chassisNo);

	public Map<String, Object> getWarrantyAndAMC(String userCode, MasterDataModelRequest request);

	List<MasterDataModelResponse> getServiceType(Integer serviceCategory, Integer modelFamilyId);

	public List<LabourGroupMasterEntity> getLabouGroupDescList(Integer vinId,String userCode);
	public Map<String, String> validateServiceType(MasterDataModelRequest request, String userCode);
	public List<ServiceLabourMasterEntity> fetchLabourCode(String userCode,MasterDataModelRequest request);
	public Map<String, Object> fetchLbrDetails(String labourCode,Integer labourGroup,String chassisNo,String userCode,String branchId,String doctype);
	public TaxDetailsDTO fetchTaxBreakInSingleRow(String userCode,TaxDTO request);
	public List<AdminPartMasterEntity> searchPartNumberDetails(String userCode,MasterDataModelRequest request);
	public PartDetailsDTO getPartDetails(Integer branchID,String partNo,String docType,String jobCardId,String userCode);
	
	

}
