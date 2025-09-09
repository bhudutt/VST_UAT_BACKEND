/**
 * 
 */
package com.hitech.dms.web.model.inv.dc.dtl.response;

import java.math.BigInteger;

import com.hitech.dms.web.model.inv.dtl.model.InvoiceMachDtlModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcMachDtlForInvResponseModel extends InvoiceMachDtlModel {
	private BigInteger dcId;
	private BigInteger machineDcDtlId;
}
