/**
 * 
 */
package com.hitech.dms.web.model.dynamicColumns;

/**
 * @author dinesh.jakhar
 *
 */
public class ReportModel {
	private String header;
	private String row;
	private String value;

	public ReportModel(String header, String row, String value) {
		this.row = row;
		this.header = header;
		this.value = value;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ReportModel [header=" + header + ", row=" + row + ", value=" + value + "]";
	}
}