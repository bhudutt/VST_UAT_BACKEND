package com.hitech.dms.web.model.advancereport;

import lombok.Data;

@Data
public class ListPreference {
	Integer id;
	String columnName;
	Boolean columnVisibility;
	Integer order;
}
