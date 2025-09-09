/**
 * 
 */
package com.hitech.dms.web.model.allot.deallot.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;
import com.hitech.dms.web.model.allot.view.response.MachineAllotMachDtlViewResponseModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachineDeAllotRequestModel {
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger machineAllotmentId;
	private String allotNumber;
	@JsonDeserialize(using = DateHandler.class)
	private Date deAllotDate;
	private String deAllotReason;
	private List<MachineAllotMachDtlViewResponseModel> machineAllotDtlList;
}
