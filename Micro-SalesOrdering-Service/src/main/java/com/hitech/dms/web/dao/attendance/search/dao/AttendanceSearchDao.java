package com.hitech.dms.web.dao.attendance.search.dao;

import com.hitech.dms.web.model.attendance.search.request.AttendanceSearchRequestModel;
import com.hitech.dms.web.model.attendance.search.response.AttendanceSearchResponse;
import com.hitech.dms.web.model.attendance.search.response.AttendanceSearchResponseModel;

public interface AttendanceSearchDao {

	public AttendanceSearchResponse attendanceSearch(String userCode,
			AttendanceSearchRequestModel requestModel);
}
