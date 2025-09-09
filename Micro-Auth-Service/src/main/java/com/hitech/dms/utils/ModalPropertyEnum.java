/**
 * 
 */
package com.hitech.dms.utils;

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

	ASSEMBLYNO("ASSEMBLY NO.") {
		@Override
		public String getPropertyName() {
			return "assemblyNo";
		}
	},

	BASE_FERT("BASE FERT") {
		@Override
		public String getPropertyName() {
			return "baseFert";
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

	FERTDESC("FERT DESC.") {
		@Override
		public String getPropertyName() {
			return "fertDesc";
		}
	},

	CATALOG_AVAILABLE("SCOPE FERT") {
		@Override
		public String getPropertyName() {
			return "catalogAvailable";
		}
	},

	NEWFERT_AVAILABLE("NEWFERT AVAILABLE") {
		@Override
		public String getPropertyName() {
			return "newFertAvailable";
		}
	},

	VEHICLE_AVAILABLE("VEHICLE AVAILABLE") {
		@Override
		public String getPropertyName() {
			return "vehicleAvailable";
		}
	},

	FERTNO("FERT NO.") {
		@Override
		public String getPropertyName() {
			return "fertNo";
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

	SERVICEABILITY("SERVICEABILITY") {
		@Override
		public String getPropertyName() {
			return "serviceability";
		}
	},

	REVISION("REVISION") {
		@Override
		public String getPropertyName() {
			return "revision";
		}
	},

	MODELNO("MODEL NO") {
		@Override
		public String getPropertyName() {
			return "modelNo";
		}
	},

	MAPNAME("GROUP NO.") {
		@Override
		public String getPropertyName() {
			return "mapName";
		}
	},

	GROUPNO("GROUP NO.") {
		@Override
		public String getPropertyName() {
			return "groupNo";
		}
	},

	GROUPDESC("GROUP DESC.") {
		@Override
		public String getPropertyName() {
			return "groupDesc";
		}
	},

	SEQUENCE("SEQUENCE") {
		@Override
		public String getPropertyName() {
			return "sequence";
		}
	},

	SEQUENCE_POS("POS.") {
		@Override
		public String getPropertyName() {
			return "sequence";
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

	OBJECTID("OBJECT ID") {
		@Override
		public String getPropertyName() {
			return "objectID";
		}
	},

	OBJECTDESC("OBJECT DESC.") {
		@Override
		public String getPropertyName() {
			return "objectDesc";
		}
	},

	G_ALT("G_ALT") {
		@Override
		public String getPropertyName() {
			return "galt";
		}
	},

	MATERIALREV("MATERIAL REV") {
		@Override
		public String getPropertyName() {
			return "materialRev";
		}
	},

	MPART_REPLACED_FOR_DESC("MPART REPLACED FOR DESC.") {
		@Override
		public String getPropertyName() {
			return "mpartReplacedForDesc";
		}
	},

	AGGREGATEDESC("AGGREGATE DESC.") {
		@Override
		public String getPropertyName() {
			return "aggregateDesc";
		}
	},

	COMPONENT("PART NO.") {
		@Override
		public String getPropertyName() {
			return "component";
		}
	},

	REFNO("POS.") {
		@Override
		public String getPropertyName() {
			return "refNo";
		}
	},

	QTYVALUE("QTY.") {
		@Override
		public String getPropertyName() {
			return "qtyValue";
		}
	},

	TOPICNAME("TOPIC NAME") {
		@Override
		public String getPropertyName() {
			return "topicName";
		}
	},

	SUBTOPICNAME("SUB TOPIC NAME") {
		@Override
		public String getPropertyName() {
			return "subTopicName";
		}
	},

	FBDATE("FEEDBACK DATE") {
		@Override
		public String getPropertyName() {
			return "fbdate";
		}
	},

	FBDESC("FEEDBACK DESCRIPTION") {
		@Override
		public String getPropertyName() {
			return "fbDesc";
		}
	},

	FBGROUP("FEEDBACK GROUP") {
		@Override
		public String getPropertyName() {
			return "fbGroup";
		}
	},

	FBLOGGEDBY("FEEDBACK LOGGED BY") {
		@Override
		public String getPropertyName() {
			return "fbLoggedBy";
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

	MRPAVAILABLE("MRP AVAILABLE") {
		@Override
		public String getPropertyName() {
			return "mrpAvailable";
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

	SERIAL_ID("DESCRIPTION") {
		@Override
		public String getPropertyName() {
			return "serialId";
		}
	},

	DRIVERLINENO("PLANT") {
		@Override
		public String getPropertyName() {
			return "driveLineNo";
		}
	},

	MFGDATE("MFG. DATE") {
		@Override
		public String getPropertyName() {
			return "mfgDate";
		}
	},

	BUCKLE_UP_DATE("BUCKLE UP DATE") {
		@Override
		public String getPropertyName() {
			return "buckleUPDate";
		}
	},

	OLD_PART_NO("OLD PART NO.") {
		@Override
		public String getPropertyName() {
			return "oldPartNo";
		}
	},

	OLD_PART_DESC("OLD PART DESC.") {
		@Override
		public String getPropertyName() {
			return "oldPartDesc";
		}
	},

	NEW_PART_NO("NEW PART NO.") {
		@Override
		public String getPropertyName() {
			return "newPartNo";
		}
	},

	NEW_PART_DESC("NEW PART DESC.") {
		@Override
		public String getPropertyName() {
			return "newPartDesc";
		}
	},

	OBS_PART_NO("OBS PART NO.") {
		@Override
		public String getPropertyName() {
			return "obsPartNo";
		}
	},

	OBS_PART_DESC("OBS PART DESC.") {
		@Override
		public String getPropertyName() {
			return "obsPartDesc";
		}
	},

	VALID_FROM("VALID FROM") {
		@Override
		public String getPropertyName() {
			return "validFrom";
		}
	},

	OBS_REMARKS("OBS REMARKS") {
		@Override
		public String getPropertyName() {
			return "obsRemarks";
		}
	},

	CL_CODE("CL CODE") {
		@Override
		public String getPropertyName() {
			return "clCode";
		}
	},

	CL_DESC("CL DESC") {
		@Override
		public String getPropertyName() {
			return "clDesc";
		}
	},

	CL_DATE("CL DATE") {
		@Override
		public String getPropertyName() {
			return "clDate";
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

	EFF_DATE("EFF. DATE") {
		@Override
		public String getPropertyName() {
			return "effDate";
		}
	},

	CH_TYPE("CHANGE TYPE") {
		@Override
		public String getPropertyName() {
			return "chType";
		}
	},

	SAP_CH_NO("SAP CHANGE NO.") {
		@Override
		public String getPropertyName() {
			return "sapChNo";
		}
	},

	SAP_CHANGE_PART("SAP CHANGE PART") {
		@Override
		public String getPropertyName() {
			return "sapChPart";
		}
	},

	SAP_CHANGE_DESC("SAP CHANGE DESC.") {
		@Override
		public String getPropertyName() {
			return "sapChDesc";
		}
	},

	MATNR("MATNR") {
		@Override
		public String getPropertyName() {
			return "matnr";
		}
	},

	POS("POS") {
		@Override
		public String getPropertyName() {
			return "pos";
		}
	},

	FERT_AVAILABLE("FERT AVAILABLE") {
		@Override
		public String getPropertyName() {
			return "fertAvailable";
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

	LAST_CHANGED("LAST CHANGED") {
		@Override
		public String getPropertyName() {
			return "lastChanged";
		}
	},

	STLNR("STLNR") {
		@Override
		public String getPropertyName() {
			return "stlnr";
		}
	},

	WERK_PLANT("WERK") {
		@Override
		public String getPropertyName() {
			return "werk";
		}
	},

	MATR("MATERIAL") {
		@Override
		public String getPropertyName() {
			return "matr";
		}
	},

	MAT_DESC("MAT_DESC") {
		@Override
		public String getPropertyName() {
			return "matDesc";
		}
	},

	REV_LVL("REV_LVL") {
		@Override
		public String getPropertyName() {
			return "revLevel";
		}
	},

	GROUP_NO("GROUP NO.") {
		@Override
		public String getPropertyName() {
			return "groupNo";
		}
	},

	GROUP_DESC("GROUP DESC.") {
		@Override
		public String getPropertyName() {
			return "groupDesc";
		}
	},

	FERT("FERT NO.") {
		@Override
		public String getPropertyName() {
			return "fert";
		}
	},

	LEV("LEV") {
		@Override
		public String getPropertyName() {
			return "lev";
		}
	},

	Object_ID("Object ID") {
		@Override
		public String getPropertyName() {
			return "objectID";
		}
	},

	ObjectDescription("Object description") {
		@Override
		public String getPropertyName() {
			return "objectDescription";
		}
	},

	RevLev("RevLev") {
		@Override
		public String getPropertyName() {
			return "revLev";
		}
	},

	Quantity("Quantity") {
		@Override
		public String getPropertyName() {
			return "quantity";
		}
	},

	BUn("BUn") {
		@Override
		public String getPropertyName() {
			return "bun";
		}
	},

	MTyp("MTyp") {
		@Override
		public String getPropertyName() {
			return "mtyp";
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

	ValidFrom("Valid From") {
		@Override
		public String getPropertyName() {
			return "validFrom";
		}
	},

	Pr("Pr") {
		@Override
		public String getPropertyName() {
			return "pr";
		}
	},

	BuMat_IM("BuMat_IM") {
		@Override
		public String getPropertyName() {
			return "buMatIM";
		}
	},

	By1("By") {
		@Override
		public String getPropertyName() {
			return "by1";
		}
	},

	By2("By") {
		@Override
		public String getPropertyName() {
			return "by2";
		}
	},

	AltItm("AltItm") {
		@Override
		public String getPropertyName() {
			return "altItm";
		}
	},

	ChangeNo("Change No.") {
		@Override
		public String getPropertyName() {
			return "changeNo";
		}
	},

	PO_AVAIL("PO STATUS") {
		@Override
		public String getPropertyName() {
			return "poAvail";
		}
	},

	PRICE_AVL("PRICE STATUS") {
		@Override
		public String getPropertyName() {
			return "priceAVL";
		}
	},

	PO_AVL("PO STATUS") {
		@Override
		public String getPropertyName() {
			return "poAvl";
		}
	},

	HIGHER_LEVEL_ASSY("HIGHER LEVEL ASSY") {
		@Override
		public String getPropertyName() {
			return "higherLevelAssy";
		}
	},

	PART_PUBLISHED("PART PUBLISHED") {
		@Override
		public String getPropertyName() {
			return "partPublished";
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

	PRICE_REQ_DATE("PRICE REQ DATE") {
		@Override
		public String getPropertyName() {
			return "priceReqDate";
		}
	},

	SLA("SLA") {
		@Override
		public String getPropertyName() {
			return "sla";
		}
	},

	PENDING_SINCE("PENDING SINCE") {
		@Override
		public String getPropertyName() {
			return "pendingSince";
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

	PO_REQ_DATE("PO REQ DATE") {
		@Override
		public String getPropertyName() {
			return "poReqDate";
		}
	},

	PART_CATEGORY("PART CATEGORY") {
		@Override
		public String getPropertyName() {
			return "partCategory";
		}
	},

	LINE_COLOR("LINE_COLOR") {
		@Override
		public String getPropertyName() {
			return "lineColor";
		}
	},

	DONE_UNDERSLA_COUNT("DONE UNDERSLA COUNT") {
		@Override
		public String getPropertyName() {
			return "doneUnderSALCount";
		}
	},

	DONE_SLABREACHED_COUNT("DONE SLABREACHED COUNT") {
		@Override
		public String getPropertyName() {
			return "doneSLABreachedCount";
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

	HAlB_NONHALB("HALB_NON-HALB") {
		@Override
		public String getPropertyName() {
			return "halbNONHALB";
		}
	},

	DISPLAY_GRPNO("DISPLAY GROUP NO.") {
		@Override
		public String getPropertyName() {
			return "displayGroupNo";
		}
	},

	PLATFORM_NAME("PLATFORM NAME") {
		@Override
		public String getPropertyName() {
			return "platFormName";
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

	PLANT_CODE("PLANT CODE") {
		@Override
		public String getPropertyName() {
			return "plantCode";
		}
	},

	FERT_CODE("FERT CODE") {
		@Override
		public String getPropertyName() {
			return "fcode";
		}
	},

	MBOM_FECTH_DATE("MBOM FECTH DATE") {
		@Override
		public String getPropertyName() {
			return "mbomFetchDate";
		}
	},

	SR_NO("SR NO") {
		@Override
		public String getPropertyName() {
			return "sr_No";
		}
	},

	BOM_LEVEL("LEVEL") {
		@Override
		public String getPropertyName() {
			return "bomLVL";
		}
	},

	REVLVL("REV LVL") {
		@Override
		public String getPropertyName() {
			return "revLVL";
		}
	},

	GRP_ALT("GRP ALT") {
		@Override
		public String getPropertyName() {
			return "grpALT";
		}
	},

	CHANGE_NO("CHANGE NO") {
		@Override
		public String getPropertyName() {
			return "changeNo";
		}
	},

	PRIORITY_ALT("PRIORITY ALT") {
		@Override
		public String getPropertyName() {
			return "priorityALT";
		}
	},

	TYPE("TYPE") {
		@Override
		public String getPropertyName() {
			return "type";
		}
	},

	CHANGETYPE("CHANGE TYPE") {
		@Override
		public String getPropertyName() {
			return "changeType";
		}
	},

	EFFECTIVE_DATE("EFFECTIVE DATE") {
		@Override
		public String getPropertyName() {
			return "effectiveDate";
		}
	},

	CL_Manufacturing_Date("CL Manufacturing Date") {
		@Override
		public String getPropertyName() {
			return "clmanuDate";
		}
	},

	SBOM_download_date("SBOM download date") {
		@Override
		public String getPropertyName() {
			return "sbomDownloadDate";
		}
	},

	Material_Description("Material Description") {
		@Override
		public String getPropertyName() {
			return "mdescription";
		}
	},

	DOMESTIC_EXPORT("DOMESTIC-EXPORT") {
		@Override
		public String getPropertyName() {
			return "fexportdomestic";
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

	Drive_Type("Drive Type") {
		@Override
		public String getPropertyName() {
			return "driveType";
		}
	},

	wheel_Type("Wheel Type") {
		@Override
		public String getPropertyName() {
			return "wheelType";
		}
	},

	Body_Type("Body Type") {
		@Override
		public String getPropertyName() {
			return "bodyType";
		}
	},

	PublishStatus("Publish Status") {
		@Override
		public String getPropertyName() {
			return "publishStatus";
		}
	},

	SAP_CHANGE_PART_("SAP CHANGE PART") {
		@Override
		public String getPropertyName() {
			return "sapChangePart";
		}
	},

	SAP_CHANGE_NO__("SAP CHANGE NO.") {
		@Override
		public String getPropertyName() {
			return "sapChangeNo";
		}
	},

	SAP_CHANGE_DESC_("SAP CHANGE DESC.") {
		@Override
		public String getPropertyName() {
			return "sapChangeDesc";
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

	ECN_STATUS("ECN STATUS") {
		@Override
		public String getPropertyName() {
			return "ecnStatus";
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
	lEVEL("MODEL") {
		@Override
		public String getPropertyName() {
			return "level4";
		}
	},
	APPLICATION_TYPE("SERIES") {
		@Override
		public String getPropertyName() {
			return "applicationType";
		}
	},
	BULLETIN_TYPE("BULLETIN TYPE") {
		@Override
		public String getPropertyName() {
			return "bulletinType";
		}
	},
	SUBJECT("SUBJECT") {
		@Override
		public String getPropertyName() {
			return "heading";
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
	BULLETIN_NUMBER("BULLETIN NUMBER") {
		@Override
		public String getPropertyName() {
			return "bulletinNumber";
		}
	},
	DOCUMENT_NUMBER("DOCUMENT") {
		@Override
		public String getPropertyName() {
			return "docName";
		}
	},
	LEVEL_4("MODEL") {
		@Override
		public String getPropertyName() {
			return "level4";
		}
	},
	REG_NO("REGISTRATION NO.") {
		@Override
		public String getPropertyName() {
			return "registrationNo";
		}
	},
	EMISSION_NORMS("EMISSION NORMS") {
		@Override
		public String getPropertyName() {
			return "emissionNorms";
		}
	},
	EMISSION_FUEL_TYPE("EMISSION FUEL TYPE") {
		@Override
		public String getPropertyName() {
			return "emissionFuelType";
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
	SBOM_RequestDate("S-BOM Request Date") {
		@Override
		public String getPropertyName() {
			return "sbomRequestDate";
		}
	},
	ReusedGroupCount("Reused Group Count") {
		@Override
		public String getPropertyName() {
			return "reusedGroupCount";
		}
	},
	CatalogueStatus("Catalogue Status") {
		@Override
		public String getPropertyName() {
			return "catalogueStatus";
		}
	},
	FERT_DESCRIPTION("FERT DESCRIPTION") {
		@Override
		public String getPropertyName() {
			return "description";
		}
	},
	AVAIL_IN_SBOM("AVAIL IN SBOM") {
		@Override
		public String getPropertyName() {
			return "availInSbom";
		}
	},
	Part_Publish_Status("Part Publish Status") {
		@Override
		public String getPropertyName() {
			return "partPublishStatus";
		}
	},
	INPUT_PART("Input Part") {
		@Override
		public String getPropertyName() {
			return "inputPart";
		}
	},
	AUTHOR_REMARKS("AUTHOR REMARKS") {
		@Override
		public String getPropertyName() {
			return "authorRemarks";
		}
	},
	LEVEL_VALUE("LEVEL VALUE") {
		@Override
		public String getPropertyName() {
			return "levelValue";
		}
	},
	SBOM_SERVICEABILITY("SBOM SERVICEABILITY") {
		@Override
		public String getPropertyName() {
			return "sbomServiceability";
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
