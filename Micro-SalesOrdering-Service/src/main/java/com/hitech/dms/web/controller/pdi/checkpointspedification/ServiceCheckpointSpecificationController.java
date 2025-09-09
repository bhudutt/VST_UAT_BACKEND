package com.hitech.dms.web.controller.pdi.checkpointspedification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.repo.dao.pdi.checklist.CheckpointSpecificationRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/pdi")
@SecurityRequirement(name = "hitechApis")
public class ServiceCheckpointSpecificationController {

	@Autowired
	private CheckpointSpecificationRepository checkpointSpecificationRepository;

	@GetMapping(value = "/specificationDropdown")
	public ResponseEntity<?> getCheckpointSpecification(@RequestParam Integer checkpointId) {
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Specification List.");
		apiResponse.setStatus(HttpStatus.OK.value());
		apiResponse.setResult(checkpointSpecificationRepository.getSpecificationList(checkpointId));
		return ResponseEntity.ok(apiResponse);
	}

}
