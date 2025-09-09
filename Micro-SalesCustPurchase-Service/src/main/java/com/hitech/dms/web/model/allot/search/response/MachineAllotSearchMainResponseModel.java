/**
 * 
 */
package com.hitech.dms.web.model.allot.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */

@Getter
@Setter
public class MachineAllotSearchMainResponseModel {
	private List<MachineAllotSearchResponseModel> searchList;
	private Integer recordCount;
}
