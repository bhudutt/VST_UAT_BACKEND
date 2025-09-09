package com.hitech.dms.web.model.bajaj.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.web.model.bajaj.request.StockRequestDTO;
import com.hitech.dms.web.model.bajaj.response.StockResponseDTO;
import com.hitech.dms.web.model.bajaj.service.StockValidationService;

@RestController
@RequestMapping("/payments-partner")
public class StockValidationController {
	
	@Autowired
    private StockValidationService stockValidationService;
	private static final Logger log = LoggerFactory.getLogger(StockValidationController.class);
	
	/*
	 * @PostMapping("/authenticate") public ResponseEntity<List<StockResponseDTO>>
	 * authenticateStock(
	 * 
	 * @RequestHeader("api_key") String apiKey,
	 * 
	 * @RequestHeader("partner_code") String partnerCode,
	 * 
	 * @RequestBody StockRequestDTO requestDTO) {
	 * log.info("Received request from partner: {}", requestDTO);
	 * 
	 * // Validate headers (optional security check) if
	 * (!"123456789abc".equals(apiKey) ||
	 * !"BajajFinanceDemoabcde".equals(partnerCode)) {
	 * 
	 * List<StockResponseDTO> errorResponse = Collections.singletonList( new
	 * StockResponseDTO(401, "Unauthorized", null,null) ); return
	 * ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse); }
	 * 
	 * List<StockResponseDTO> response =
	 * stockValidationService.validateAndProcessStock(requestDTO); return
	 * ResponseEntity.ok(response); }
	 */
	
	@PostMapping("/authenticate")
    public ResponseEntity<StockResponseDTO> authenticateStock(
            @RequestHeader("api_key") String apiKey,
            @RequestHeader("partner_code") String partnerCode,
            @RequestBody StockRequestDTO requestDTO) {
		log.info("Received request from partner: {}", requestDTO);

        // Validate headers (optional security check)
        if (!"123456789abc".equals(apiKey) || !"BajajFinanceDemoabcde".equals(partnerCode)) {
        	
        	StockResponseDTO errorResponse = new StockResponseDTO(401, "Unauthorized", null,null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        StockResponseDTO response = stockValidationService.validateAndProcessStock(requestDTO);
        return ResponseEntity.ok(response);
    }

}
