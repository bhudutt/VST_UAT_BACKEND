/**
 * 
 */
package com.hitech.dms.web.model.inv.inv.view.response;

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
public class InvMachDtlForInvResponseModel extends InvoiceMachDtlModel {
	private BigInteger salesInvoiceDtlId;
	private BigInteger dcId;
	private BigInteger machineDcDtlId;
}
