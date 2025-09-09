package com.hitech.dms.web.dao.pdi.create;

import java.math.BigInteger;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.entity.pdi.PdiEntity;
import com.hitech.dms.web.repo.dao.pdi.checklist.PdiRepository;
import com.hitech.dms.web.repo.dao.pdi.checklist.ServiceCheckpointRepo;

//@Service
public class ServicePdiImpl {
	/*
	 * 
	 * @Autowired private PdiRepository pdiRepository;
	 * 
	 * @Autowired private ServiceCheckpointRepo serviceCheckpointRepo;
	 * 
	 * @Transactional public ApiResponse savePdi(PdiEntity servicePdi,String
	 * usercode) { ApiResponse apiResponse = new ApiResponse(); if
	 * (servicePdi.getDraftFlag()) { Long servicePdi1 = pdiRepository.
	 * findByMachineMasterAndUsercode(usercode,servicePdi.getMachineInventory().
	 * getMachineInventoryId()); if (servicePdi1 != null) {
	 * apiResponse.setMessage("PDI Already Done for the selected Chassis No.");
	 * apiResponse.setStatus(HttpStatus.OK.value()); return apiResponse; } }
	 * 
	 * servicePdi.setPdiDate(new Date());
	 * servicePdi.setCreatedBy(BigInteger.valueOf(1)); servicePdi.setCreatedDate(new
	 * Date()); if(servicePdi.getId()!=null){
	 * servicePdi.setModifiedBy(BigInteger.valueOf(1));
	 * servicePdi.setModifiedDate(new Date()); } PdiEntity servicePdi1 =
	 * pdiRepository.save(servicePdi);
	 * 
	 * 
	 * servicePdi.getPdiDeliveryDetailList() .forEach(s -> {
	 * s.getChassisCheckpointId().setServicePdi(servicePdi1);
	 * serviceCheckpointRepo.save(s); } );
	 * 
	 * apiResponse.setMessage(servicePdi.getDraftFlag() ? "PDI saved successfully."
	 * :"PDI submitted successfully.");
	 * apiResponse.setStatus(HttpStatus.OK.value()); return apiResponse; }
	 */}
