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

	PARTNO("PART NO.") {
		@Override
		public String getPropertyName() {
			return "partNo";
		}
	},

	PARTTYPE("PART TYPE") {
		@Override
		public String getPropertyName() {
			return "partType";
		}
	},

	PARTNAME("PART NAME") {
		@Override
		public String getPropertyName() {
			return "partName";
		}
	},

	QUANTITY("QTY.") {
		@Override
		public String getPropertyName() {
			return "quantity";
		}
	},

	QTY("QTY.") {
		@Override
		public String getPropertyName() {
			return "qty";
		}
	},

	MODELNO("MODEL NO") {
		@Override
		public String getPropertyName() {
			return "modelNo";
		}
	},

	PARTDESC("PART DESC.") {
		@Override
		public String getPropertyName() {
			return "partDesc";
		}
	},

	PARTREMARK("PART REMARK") {
		@Override
		public String getPropertyName() {
			return "partRemark";
		}
	},

	REMARKS("REMARKS") {
		@Override
		public String getPropertyName() {
			return "remarks";
		}
	},

	QTYVALUE("QTY.") {
		@Override
		public String getPropertyName() {
			return "qtyValue";
		}
	},

	USERNAME("USER NAME") {
		@Override
		public String getPropertyName() {
			return "fbLoggedBy";
		}
	},

	USER_NAME("USER NAME") {
		@Override
		public String getPropertyName() {
			return "userName";
		}
	},

	CITY("CITY") {
		@Override
		public String getPropertyName() {
			return "city";
		}
	},

	EMAIL("EMAIL") {
		@Override
		public String getPropertyName() {
			return "email";
		}
	},

	MOBILENO("MOBILE NO.") {
		@Override
		public String getPropertyName() {
			return "mobileNo";
		}
	},

	MRP("MRP") {
		@Override
		public String getPropertyName() {
			return "MRP";
		}
	},

	VEHICLE_NO("CHASSIS NO") {
		@Override
		public String getPropertyName() {
			return "vehicleNo";
		}
	},

	SO_NO("FERT NO") {
		@Override
		public String getPropertyName() {
			return "soNo";
		}
	},

	MFGDATE("MFG. DATE") {
		@Override
		public String getPropertyName() {
			return "mfgDate";
		}
	},

	VALID_FROM("VALID FROM") {
		@Override
		public String getPropertyName() {
			return "validFrom";
		}
	},

	OLD_PART("OLD PART") {
		@Override
		public String getPropertyName() {
			return "oldPart";
		}
	},

	OLD_PARTDESC("OLD PART DESC.") {
		@Override
		public String getPropertyName() {
			return "oldDesc";
		}
	},

	OLD_QTY("OLD QTY.") {
		@Override
		public String getPropertyName() {
			return "oldQty";
		}
	},

	NEW_PART("NEW PART") {
		@Override
		public String getPropertyName() {
			return "newPart";
		}
	},

	NEW_PARTDESC("NEW PART DESC.") {
		@Override
		public String getPropertyName() {
			return "newDesc";
		}
	},

	NEW_QTY("NEW QTY.") {
		@Override
		public String getPropertyName() {
			return "newQty";
		}
	},

	USER_ID("USER CODE") {
		@Override
		public String getPropertyName() {
			return "user_id";
		}
	},

	DESCRIPTION("DESCRIPTION") {
		@Override
		public String getPropertyName() {
			return "description";
		}
	},

	ADDRESS1("ADDRESS1") {
		@Override
		public String getPropertyName() {
			return "address1";
		}
	},

	ADDRESS2("ADDRESS2") {
		@Override
		public String getPropertyName() {
			return "address2";
		}
	},

	STATE("STATE") {
		@Override
		public String getPropertyName() {
			return "state";
		}
	},

	COUNTRY("COUNTRY") {
		@Override
		public String getPropertyName() {
			return "country";
		}
	},

	PINCODE("PINCODE") {
		@Override
		public String getPropertyName() {
			return "pincode";
		}
	},

	CONTACT_NO("CONTACT NO.") {
		@Override
		public String getPropertyName() {
			return "contactNo";
		}
	},

	MOBILE_NO("MOBILE NO.") {
		@Override
		public String getPropertyName() {
			return "mobileNo";
		}
	},

	REG_DATE("REG DATE") {
		@Override
		public String getPropertyName() {
			return "regDate";
		}
	},

	STATUS("STATUS") {
		@Override
		public String getPropertyName() {
			return "status";
		}
	},

	USER_FULL_NAME("USER NAME") {
		@Override
		public String getPropertyName() {
			return "userFullName";
		}
	},

	Quantity("Quantity") {
		@Override
		public String getPropertyName() {
			return "quantity";
		}
	},
	ChangedOn("Changed On") {
		@Override
		public String getPropertyName() {
			return "changedOn";
		}
	},

	CreatedOn("Created On") {
		@Override
		public String getPropertyName() {
			return "createdOn";
		}
	},

	PART_NO("PART NO.") {
		@Override
		public String getPropertyName() {
			return "partNo";
		}
	},

	PART_DESC("PART DESC.") {
		@Override
		public String getPropertyName() {
			return "partDesc";
		}
	},

	RESPONSIBILITY("RESPONSIBILITY") {
		@Override
		public String getPropertyName() {
			return "responsibility";
		}
	},

	PO_STATUS("STATUS") {
		@Override
		public String getPropertyName() {
			return "poStatus";
		}
	},
	FROM_USER_CODE("FROM_USER_CODE") {
		@Override
		public String getPropertyName() {
			return "fromUserCode";
		}
	},

	TO_USER_CODE("TO_USER_CODE") {
		@Override
		public String getPropertyName() {
			return "toUserCode";
		}
	},

	TO_USER_NAME("TO_USER_NAME") {
		@Override
		public String getPropertyName() {
			return "toUserName";
		}
	},

	MODEL_NAME("MODEL NAME") {
		@Override
		public String getPropertyName() {
			return "modelName";
		}
	},

	UPLOADED_BY("UPLOADED BY") {
		@Override
		public String getPropertyName() {
			return "uploadedBy";
		}
	},

	SR_NO("SR NO") {
		@Override
		public String getPropertyName() {
			return "sr_No";
		}
	},
	TYPE("TYPE") {
		@Override
		public String getPropertyName() {
			return "type";
		}
	},

	EFFECTIVE_DATE("EFFECTIVE DATE") {
		@Override
		public String getPropertyName() {
			return "effectiveDate";
		}
	},

	PLATFORM("Platform") {
		@Override
		public String getPropertyName() {
			return "platForm";
		}
	},

	Series("Series") {
		@Override
		public String getPropertyName() {
			return "series";
		}
	},

	MODEL("MODEL") {
		@Override
		public String getPropertyName() {
			return "model";
		}
	},

	SERIES("SERIES") {
		@Override
		public String getPropertyName() {
			return "series";
		}
	},

	REMARK("REMARK") {
		@Override
		public String getPropertyName() {
			return "remark";
		}
	},

	ENGINE_SERIES("COUNTRY") {
		@Override
		public String getPropertyName() {
			return "engineSeries";
		}
	},
	ENGINE_MODEL("PLATFORM") {
		@Override
		public String getPropertyName() {
			return "engineModel";
		}
	},
	APPLICATION_TYPE("SERIES") {
		@Override
		public String getPropertyName() {
			return "applicationType";
		}
	},
	ISSUE_DATE("ISSUE DATE") {
		@Override
		public String getPropertyName() {
			return "issueDate";
		}
	},
	ISSUE_DATE_STR("ISSUE DATE") {
		@Override
		public String getPropertyName() {
			return "issueDateValue";
		}
	},
	DOCUMENT_NUMBER("DOCUMENT") {
		@Override
		public String getPropertyName() {
			return "docName";
		}
	},
	REG_NO("REGISTRATION NO.") {
		@Override
		public String getPropertyName() {
			return "registrationNo";
		}
	},
	MARKET("MARKET") {
		@Override
		public String getPropertyName() {
			return "market";
		}
	},
	TotalVinCount("Total Vin Count") {
		@Override
		public String getPropertyName() {
			return "totalVinCount";
		}
	},
	LEVEL_VALUE("LEVEL VALUE") {
		@Override
		public String getPropertyName() {
			return "levelValue";
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
