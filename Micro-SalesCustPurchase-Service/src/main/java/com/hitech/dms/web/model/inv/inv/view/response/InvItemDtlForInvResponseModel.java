/**
 * 
 */
package com.hitech.dms.web.model.inv.inv.view.response;

import java.math.BigInteger;

import com.hitech.dms.web.model.inv.dtl.model.InvoiceItemDtlModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class InvItemDtlForInvResponseModel extends InvoiceItemDtlModel {
	private BigInteger salesInvoiceItemId;
	private BigInteger dcId;
	private BigInteger dcItemId;
}
