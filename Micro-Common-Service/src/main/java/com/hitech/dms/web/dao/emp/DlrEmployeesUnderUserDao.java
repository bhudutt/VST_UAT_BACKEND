package com.hitech.dms.web.dao.emp;

import java.util.List;

import com.hitech.dms.web.model.emp.response.DlrEmployeesUnderUserResponseModel;

public interface DlrEmployeesUnderUserDao {
	public List<DlrEmployeesUnderUserResponseModel> fetchDlrEmpUnderUserList(String userCode, String includeInactive);
}
