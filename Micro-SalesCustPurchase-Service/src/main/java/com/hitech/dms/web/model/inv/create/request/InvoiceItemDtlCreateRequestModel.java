/**
 * 
 */
package com.hitech.dms.web.model.inv.create.request;

import java.math.BigInteger;

import com.hitech.dms.web.model.inv.dtl.model.InvoiceItemDtlModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class InvoiceItemDtlCreateRequestModel extends InvoiceItemDtlModel {
	private BigInteger dcId;
	private BigInteger dcItemId;
}
