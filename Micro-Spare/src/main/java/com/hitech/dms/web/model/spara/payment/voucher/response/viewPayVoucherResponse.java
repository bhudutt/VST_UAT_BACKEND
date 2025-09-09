package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spara.payment.voucher.request.RefDocListRequest;

import lombok.Data;

@Data
public class viewPayVoucherResponse {
	
	
	private String	branchName;
	private String	voucherType;
	private Date	createdDate;
	private String	docNo;
	private Date docDate;
	private String partyType;
	private String	partyCode;
	private String	partyName;
	private String	partyAddLine1;
	private String	partyAddLine2;
	private String	partyAddLine3;
	private String	mobileNumber;
	private String	districtDesc;
	private String	recepitMode;
	private String	tehsilDesc;
	private String	cityDesc;
	private String	pinCode;
	private String	stateDesc;
	private String	countryDesc;
	private String	 gstNumber;
	private String	panOrTan;
	private String	referenceDocument;
	private BigDecimal	amount;
	private BigDecimal	advAmt;
	private Integer saleInvoiceId;
	private String counterSalePartyName;
	
	
//	private Integer id;
//	
//	private Integer voucherTypeId;
//	
//	private String pvTypeValueCode;
//	
//	private String docNo;
//	
//	private BigDecimal amount;
//	
//	private Integer receiptModeId;
//	
//	private BigDecimal advAmt;
//	
//	private String remark;
//
//	private Boolean adjustAginAdv;
//	
//	private Boolean isActive;
//	
//	private Date createdDate;
//	
//	private BigInteger createdBy;
//
//	private Date modifiedDate;
//	
//	private BigInteger modifiedBy;
//	
	private List<RefDocListRequest> refDocList = new ArrayList<>();
	
	private CardResponse cardResponse;
	
	private EWalletResponse ewalletResponse;
	
	private RtgsNeftImpsResponse rtgsNeftImpsResponse;
	
	private ChequeDdResponse chequeDdResponse;
//	
//	private Integer  branchId;
//	
//	private String branchName;
//	
//	private Date   docDate;
//	
//	private Integer partyId;
//	
//	private Integer partyCatgId;
//	
//	private String partyCode;
//	
//	private Integer refDocId;
//
	

}
