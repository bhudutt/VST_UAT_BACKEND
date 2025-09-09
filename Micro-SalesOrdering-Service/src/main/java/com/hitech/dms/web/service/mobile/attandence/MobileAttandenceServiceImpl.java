package com.hitech.dms.web.service.mobile.attandence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.mobile.attandence.MobileAttandenceDao;
import com.hitech.dms.web.model.mobile.attendance.request.AttendanceClockList;
import com.hitech.dms.web.model.mobile.attendance.request.AttendanceClockRequest;
import com.hitech.dms.web.model.mobile.attendance.request.LeaveClockRequest;
import com.hitech.dms.web.model.mobile.attendance.response.AttendanceClockRes;
import com.hitech.dms.web.model.mobile.attendance.response.AttendanceResponse;
import com.hitech.dms.web.model.mobile.attendance.response.ClockTypeResponse;
import com.hitech.dms.web.model.mobile.attendance.response.TodayStatusResponse;

@Service
public class MobileAttandenceServiceImpl implements MobileAttandenceService {

	
	@Autowired
	private MobileAttandenceDao dao;
	
	
	@Override
	public List<ClockTypeResponse> getClockTypeList() {
		return dao.getClockTypeList();
	}


	@Override
	public AttendanceResponse saveAttendanceClock(AttendanceClockRequest bean, String userCode) {
	
		return dao.saveAttendanceClock(bean, userCode);
	}


	@Override
	public AttendanceClockRes getAttendanceClockList(AttendanceClockList bean, String userCode) {
		
		return dao.getAttendanceClockList(bean, userCode);
	}


	@Override
	public AttendanceResponse saveLeaveClock(LeaveClockRequest bean, String userCode) {
		
		return dao.saveLeaveClock( bean,  userCode);
	}


	@Override
	public AttendanceResponse saveAttendanceClockOut(AttendanceClockRequest bean, String userCode) {
		
		return dao.saveAttendanceClockOut(bean, userCode);
	}


	@Override
	public AttendanceResponse cancelLeave(Integer leaveId, String userCode) {
		
		return dao.cancelLeave(leaveId, userCode);
	}


	@Override
	public AttendanceResponse getLeaveAndAttend(String requestDate, String userCode) {
		return  dao.getLeaveAndAttend(requestDate, userCode);
	}


	@Override
	public TodayStatusResponse getTodayStatus(String userCode) {
		return  dao.getTodayStatus(userCode);
	}


	@Override
	public List<String> futureDaysLeave(String userCode) {
		return  dao.futureDaysLeave(userCode);
	}



}
