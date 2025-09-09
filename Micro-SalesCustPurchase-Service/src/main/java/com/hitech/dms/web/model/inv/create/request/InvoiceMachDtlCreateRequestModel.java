/**
 * 
 */
package com.hitech.dms.web.model.inv.create.request;

import java.math.BigInteger;

import com.hitech.dms.web.model.inv.dtl.model.InvoiceMachDtlModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class InvoiceMachDtlCreateRequestModel  extends InvoiceMachDtlModel {
	private Boolean isSelected;
	private BigInteger dcId;
	private BigInteger machineDcDtlId;
	private BigInteger vinId;
}
