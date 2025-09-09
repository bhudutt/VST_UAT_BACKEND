package com.hitech.dms.web.model.bajaj.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockResponseDTO {
	private Integer responsecode;
    private String message;
    private String validationItem;
    private String transactionID;
}
