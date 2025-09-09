package com.hitech.dms.web.model.spara.customer.order.picklist.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;

import lombok.Data;

@Data
public class PartDetailRequest {
	
	private BigInteger customerOrderDtlId;
	private BigInteger customerOrderHdrId;
	private BigInteger dcDtlId;
	private BigInteger pickListDtlId;
	private BigInteger dcHdrId;
	private BigInteger invoiceSaleId;
	private BigInteger poDtlId;
	private BigInteger partId;
	private String partNumber;
	private String partDesc;
	private String partSubCategory;
	private BigInteger partBranchId;
	private Integer totalStock;
	private Integer branchStoreId;
	private String branchStore;
	private String binLocation;
	private BigInteger binId;
	private BigDecimal orderQty;
	private BigDecimal balanceQty;
	private BigDecimal issueQty;
	private BigDecimal invoiceQty;
	private BigDecimal ndp;
	private BigDecimal mrp;
	private BigDecimal basicUnitPrice;
	private Boolean isIndividualBin;
	private String discountType;
	private BigDecimal discountRate;
	private BigDecimal discountAmount;
	private Integer cgstPercent;
	private BigDecimal cgst;
	private Integer sgstPercent;
	private BigDecimal sgst;
	private Integer igstPercent;
	private BigDecimal igst;
	private BigDecimal taxValue;
	private BigDecimal taxableValue;
	private BigDecimal billValue;
	private String splDiscountType;
	private BigDecimal splDiscountRate;
	private BigDecimal splDiscountAmount;
	private String hsnCode;
	private List<BranchSpareIssueBinStockResponse> binRequest;
}
