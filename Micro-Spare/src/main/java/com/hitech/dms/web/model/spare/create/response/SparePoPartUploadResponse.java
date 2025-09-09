/**
 * 
 */
package com.hitech.dms.web.model.spare.create.response;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.hitech.dms.web.model.SpareModel.SparePoPartDltForExcel;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoPartUploadResponse {
//	private String partMasterNumber;
//	private int orderQty;
	private Map<String, String> errorPartData;
	private Map<String, Integer>partAndQty;
	private List<SparePoPartDltForExcel> partForExcelList;
	private String msg;
	private Integer statusCode;

}
