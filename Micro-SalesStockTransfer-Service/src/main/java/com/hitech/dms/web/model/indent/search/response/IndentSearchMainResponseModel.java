/**
 * 
 */
package com.hitech.dms.web.model.indent.search.response;

import lombok.ToString;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class IndentSearchMainResponseModel {
	private List<IndentSearchResponseModel> searchList;
	private Integer recordCount;
}
