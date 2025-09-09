package com.hitech.dms.web.entity.partrequisition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "PA_STOCK_ISSUE")
@Data
public class SparePartIssueEntity implements Serializable{

	private static final long serialVersionUID = 1867514944801926147L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="issue_id")
	private BigInteger issueId;
	@Column(name="Issue_Type_Id")
	private Integer issueTypeId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name="issue_date")
	private Date issueDate =new Date();
	@Column(name="branch_id")
	private Integer branchId;
	@Column(name="Issue_By")
	private Integer issueBy;
	@Column(name="IssueNumber")
	private String issueNumber;
	@Column(name="TransferTypeflag")
	private String transferTypeFlag;
	@Column(name="PriceTypeFlag")
	private BigInteger priceTypeFlag;
	@Column(name="From_branch_store_id")
	private BigInteger frombranchstoreId;
	@Column(name="To_branch_store_id")
	private BigInteger tobranchstoreId;
	@Column(name="ref_to_branch_id")
	private BigInteger reftobranchId;
	@Column(name="ref_indentNumber")
	private String refindentNumber;
	@Column(name="ref_indentDate")
	private Date refindentDate;
	@Column(name="ref_indentValue")
	private BigDecimal refindentValue;
	@Column(name="issue_against_indent_id")
	private BigInteger issueagainstindentId;
	@Column(name="TotalQty")
	private BigDecimal totalQty;
	@Column(name="TotalItems")
	private Integer totalItems;
	@Column(name="TotalAmount")
	private BigDecimal totalAmount;
	@Column(name="Remarks")
	private String remarks;
	@Column(name="CreatedBy")
	private String createdBy;
	@Column(name="CreatedDate")
	private Date createdDate =new Date();
	@Column(name="ModifiedBy")
	private String modifiedBy;
	@Column(name="ModifiedDate")
	private Date modifiedDate =new Date();
	@Column(name="status")
	private String status;
	@Column(name="View_Doc_No")
	private String viewDocNo;
	@Column(name="Version_No")
	private Integer versionNo;
	@Column(name="ro_Id")
	private BigInteger roId;
	@Column(name="requisition_Id")
	private Integer requisitionId;
}
