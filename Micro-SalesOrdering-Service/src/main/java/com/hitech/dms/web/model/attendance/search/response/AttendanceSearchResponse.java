package com.hitech.dms.web.model.attendance.search.response;

import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceSearchResponse {
	//private List<AttendanceSearchResponseModel> searchList;
	private List<?> searchList;
	private Integer recordCount;
}
