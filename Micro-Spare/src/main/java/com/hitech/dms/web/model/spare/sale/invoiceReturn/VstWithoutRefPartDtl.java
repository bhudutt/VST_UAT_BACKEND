package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class VstWithoutRefPartDtl {
	
	 private Integer id;
	 private Integer partId;
	 private BigInteger branchId;
	 private BigInteger dealerId;
	 private Integer partBranchId;
     private String partNo;
     private String partDescription;
     private String productSubCat;
     private Integer currentStock;
     private Integer returnQty;
     private String store;
     private Integer individualBin;
     private String binLocation;
     private String binId;
     private String stockStoreId;
     private String branchStoreId;
     private BigDecimal basicUnitPrice;
     private BigDecimal price;
     private BigInteger gstPer;
     private BigDecimal gst;
     private BigDecimal total;

}
