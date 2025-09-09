/**
 * 
 */
package com.hitech.dms.app.utils;

/**
 * @author dinesh.jakhar
 *
 */
public enum ModalPropertyEnum {

	SRLNO("SRL NO.") {
		@Override
		public String getPropertyName() {
			return "srNo";
		}
	},

	SRNO("SRL NO.") {
		@Override
		public String getPropertyName() {
			return "srlNo";
		}
	},

	LEVEL("Level") {
		@Override
		public String getPropertyName() {
			return "Level";
		}
	},

	CHASSISNO("CHASSIS NO.") {
		@Override
		public String getPropertyName() {
			return "chassisNo";
		}
	},

	VINNO("VIN NO.") {
		@Override
		public String getPropertyName() {
			return "vinNo";
		}
	},

	ENGINENO("ENGINE NO") {
		@Override
		public String getPropertyName() {
			return "engineNo";
		}
	},

	MFGDATE("MFG. DATE") {
		@Override
		public String getPropertyName() {
			return "mfgDate";
		}
	},

	MFGINVOICENO("MFGINVOICE NO.") {
		@Override
		public String getPropertyName() {
			return "mfgInvoiceNo";
		}
	},

	MFGINVOICEDATE("MFGINVOIVE DATE") {
		@Override
		public String getPropertyName() {
			return "mfgInvoiceDate";
		}
	},

	SELLINGDEALERCODE("SELLINGDEALER CODE") {
		@Override
		public String getPropertyName() {
			return "sellingDealerCode";
		}
	},

	CSBNUMBER("CSB. NUMBER") {
		@Override
		public String getPropertyName() {
			return "csbNumber";
		}
	},

	REGISTRATIONNUMBER("REGISTRATION NUMBER") {
		@Override
		public String getPropertyName() {
			return "registrationNumber";
		}
	},

	INSTALLATIONDATE("INSTALLATION DATE") {
		@Override
		public String getPropertyName() {
			return "installationDate";
		}
	},

	UNITPRICE("UNIT PRICE") {
		@Override
		public String getPropertyName() {
			return "unitPrice";
		}
	},

	DELIVERYDATE("DELIVERY DATE") {
		@Override
		public String getPropertyName() {
			return "deliveryDate";
		}
	},

	ORIGINALCUSTOMERMASTERID("ORIGINAL CUSTOMERMASTERID") {
		@Override
		public String getPropertyName() {
			return "originalCustomerMasterId";
		}
	},

	LATESTCUSTOMERMASTERID("LATEST CUSTOMERMASTERID") {
		@Override
		public String getPropertyName() {
			return "latestCustomerMasterId";
		}
	},

	PRODUCTGROUP("PRODUCT GROUP") {
		@Override
		public String getPropertyName() {
			return "productGroup";
		}
	},

	ITEMNO("ITEM NO.") {
		@Override
		public String getPropertyName() {
			return "itemNo";
		}
	},
	ITEM_DESC("ITEM DESC.") {
		@Override
		public String getPropertyName() {
			return "itemDesc";
		}
	},

	VARIANT("VARIANT") {
		@Override
		public String getPropertyName() {
			return "variant";
		}
	},
	

	STOCKQTY("STOCK QTY.") {
		@Override
		public String getPropertyName() {
			return "stockQty";
		}
	},

	ZONE("ZONE") {
		@Override
		public String getPropertyName() {
			return "zone";
					
		}
	},
	
	STATE("STATE") {
		@Override
		public String getPropertyName() {
			return "state";
		}
	},
	
	DEALERSHIP("DEALERSHIP") {
		@Override
		public String getPropertyName() {
			return "dealership";
		}
	},
	
	TERRITORY("TERRITORY") {
		@Override
		public String getPropertyName() {
			return "territory";
		}
	},

	BRANCH("BRANCH") {
		@Override
		public String getPropertyName() {
			return "branch";
		}
	},

	PRODUCTDIVISION("PRODUCT DIVISION") {
		@Override
		public String getPropertyName() {
			return "productdivision";
		}
	},

	MODEL("MODEL") {
		@Override
		public String getPropertyName() {
			return "modelName";
		}
	},

	PDIINWARDDATE("PDI INWARDDATE") {
		@Override
		public String getPropertyName() {
			return "pdiInwardDate";
		}
	},

	PONO("PO NO.") {
		@Override
		public String getPropertyName() {
			return "poNo";
		}
	},

	PODATE("PO DATE") {
		@Override
		public String getPropertyName() {
			return "PODATE";
		}
	},

	LRNO("LR NO") {
		@Override
		public String getPropertyName() {
			return "lrNo";
		}
	},

	LRDATE("LR DATE") {
		@Override
		public String getPropertyName() {
			return "lrDate";
		}
	},

	GRNNO("GRNNO") {
		@Override
		public String getPropertyName() {
			return "grnNo";
		}
	},

	GRNDATE("GRNDATE") {
		@Override
		public String getPropertyName() {
			return "grnDate";
		}
	},
	
	NOOFDATEINSTOCK("NOOFDATEINSTOCK") {
		@Override
		public String getPropertyName() {
			return "noofdateinStock";
		}
	},
	
	TRANSPORTER("TRANSPORTER") {
		@Override
		public String getPropertyName() {
			return "transporter";
		}
	},
	
	PROFITCENTER("PROFITCENTER") {
		@Override
		public String getPropertyName() {
			return "profitCenter";
		}
	},
	
	STATUS("STATUS") {
		@Override
		public String getPropertyName() {
			return "status";
		}
	},
	
	DELIVERYCHALLLANNO("DELIVERYCHALLLAN NO.") {
		@Override
		public String getPropertyName() {
			return "deliverychallanno";
		}
	},
	DELIVERYCHALLANDATE("DELIVERYCHALLAN DATE") {
		@Override
		public String getPropertyName() {
			return "deliverychallanDate";
		}
	},
	CUSTOMERINVOICENO("CUSTOMERINVOICE NO.") {
		@Override
		public String getPropertyName() {
			return "customerinvoiceNo";
		}
	},
	
	CUSTOMERINVOICEDATE("CUSTOMERINVOICE DATE") {
		@Override
		public String getPropertyName() {
			return "customerinvoiceDate";
		}
	},
	
	CUSTOMERNAME("CUSTOMER NAME") {
		@Override
		public String getPropertyName() {
			return "customerName";
		}
	},
	STOCKQUANTITY("STOCK QUANTITY") {
		@Override
		public String getPropertyName() {
			return "stockQuantity";
		}
	};
	
	
	private String descp;

	private ModalPropertyEnum(String descp) {
		this.setDescp(descp);
	}

	public abstract String getPropertyName();

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}
}
