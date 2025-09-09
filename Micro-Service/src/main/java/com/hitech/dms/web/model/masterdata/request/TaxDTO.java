package com.hitech.dms.web.model.masterdata.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class TaxDTO {

	private transient Integer branchId;
	private transient String calcualtionFor;
	private transient Integer partOrLaborBranchId;
	private transient Integer paratyBranchId;
	private transient Integer customerId;
	private transient Integer stateId;
	private transient BigDecimal saleAmount;
	private transient BigDecimal discount;
	private transient Boolean fromGui; // set true if request coming from controller other wise dont set
	private transient List<TaxDTO> taxDTOList;
	private transient String isFor;
	private Integer hsnTaxMappingId;

	private String hsnCode;

	private String chargeDesc;

	private BigDecimal chargeRatePerc;

	private Integer displayOrder;

	private BigDecimal chargeamt;

	private BigDecimal totalChargeAmnt;
	private Character isPrimaryTax;
}
