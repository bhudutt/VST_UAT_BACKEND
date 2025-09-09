/**
 * 
 */
package com.hitech.dms.web.model.inv.dc.dtl.response;

import java.math.BigInteger;

import com.hitech.dms.web.model.inv.dtl.model.InvoiceItemDtlModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class DcItemDtlForInvResponseModel extends InvoiceItemDtlModel {
	private BigInteger dcId;
	private BigInteger dcItemId;
}
