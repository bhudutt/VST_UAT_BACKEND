package com.hitech.dms.web.entity.spare.inventorymanagement.deadstockupload;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mahesh.kumar
 */
@Getter
@Setter
@Entity
@Table(name = "SP_DEAD_STOCK")
public class DeadStockUploadEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name = "user_code")
	private String userCode;
	
	@Column(name = "part_id")
	private BigInteger partId;
	
	@Column(name = "product_category_id")
	private BigInteger productCategoryId;
	
	@Column(name = "prod_sub_category_id")
	private BigInteger prodSubCategoryId;
	
	@Column(name = "dead_stock_qty")
	private BigDecimal deadStockQty;
	
	
	@Column(name = "date_of_packing")
	private Date dateOfPacking;
	
	@Column(name = "file_content_type")
	private String fileContentType;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "created_by")
	private BigInteger createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "modified_by")
	private BigInteger modifiedBy;
	
	@Column(name = "modified_date")
	private Date modifiedDate;

}
