package com.hitech.dms.web.service.mobile.attandence;

import java.util.List;

import com.hitech.dms.web.model.mobile.attendance.request.AttendanceClockList;
import com.hitech.dms.web.model.mobile.attendance.request.AttendanceClockRequest;
import com.hitech.dms.web.model.mobile.attendance.request.LeaveClockRequest;
import com.hitech.dms.web.model.mobile.attendance.response.AttendanceClockRes;
import com.hitech.dms.web.model.mobile.attendance.response.AttendanceResponse;
import com.hitech.dms.web.model.mobile.attendance.response.ClockTypeResponse;
import com.hitech.dms.web.model.mobile.attendance.response.TodayStatusResponse;

public interface MobileAttandenceService {

	List<ClockTypeResponse> getClockTypeList();

	AttendanceResponse saveAttendanceClock(AttendanceClockRequest bean, String userCode);

	AttendanceClockRes getAttendanceClockList(AttendanceClockList bean, String userCode);

	AttendanceResponse saveLeaveClock(LeaveClockRequest bean, String userCode);

	AttendanceResponse saveAttendanceClockOut(AttendanceClockRequest bean, String userCode);

	AttendanceResponse cancelLeave(Integer leaveId, String userCode);

	AttendanceResponse getLeaveAndAttend(String requestDate, String userCode);

	TodayStatusResponse getTodayStatus(String userCode);

	List<String> futureDaysLeave(String userCode);

}
