package com.hitech.dms.web.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name = "STOCK_UPLOAD_TEMP")
@Data
public class StockUploadStagingEntity {
		private static final long serialVersionUID = 926870347841281722L;

		@Id
		@Column(name = "id")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;
		@Column(name = "partNo")
		private String partNo;
		@Column(name = "store")
		private String store;
		@Column(name = "storeBinLocation")
		private String storeBinLocation;
		@Column(name = "quantity")
		private Integer quantity;
		@Column(name = "mrp")
		private float mrp;
		@Column(name = "ndp")
		private float ndp;
		@Column(name = "branch")
		private Integer branch;
		@Column(name = "dealer")
		private Integer dealer;

}
