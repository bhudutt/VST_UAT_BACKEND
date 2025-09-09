package com.hitech.dms.web.dao.mobile.attandence;

import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.entity.mobile.attendance.AttendanceClock;
import com.hitech.dms.web.entity.mobile.attendance.LeaveClock;
import com.hitech.dms.web.model.mobile.attendance.request.AttendanceClockList;
import com.hitech.dms.web.model.mobile.attendance.request.AttendanceClockRequest;
import com.hitech.dms.web.model.mobile.attendance.request.LeaveClockRequest;
import com.hitech.dms.web.model.mobile.attendance.response.AttendanceClockDtl;
import com.hitech.dms.web.model.mobile.attendance.response.AttendanceClockRes;
import com.hitech.dms.web.model.mobile.attendance.response.AttendanceResponse;
import com.hitech.dms.web.model.mobile.attendance.response.ClockTypeResponse;
import com.hitech.dms.web.model.mobile.attendance.response.TodayStatusResponse;



@Repository
public class MobileAttandenceDaoImpl implements MobileAttandenceDao {

	
private static final Logger logger = LoggerFactory.getLogger(MobileAttandenceDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<ClockTypeResponse> getClockTypeList() {
		
		Query query = null;
		Session session =null;
		List<ClockTypeResponse> list = new ArrayList<>();
		String sqlQuery = "Select id, login_type from attendance_login_type (nolock) alt where alt.isActive ='Y'";
		session = sessionFactory.openSession();
		query = session.createNativeQuery(sqlQuery);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<?> data = query.list();
		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				ClockTypeResponse res = new ClockTypeResponse();
				res.setId((Integer) row.get("id"));
				res.setLoginType((String) row.get("login_type"));
				list.add(res);
			}
		}
		return list;
     }

	@Override
	public AttendanceResponse saveAttendanceClock(AttendanceClockRequest bean, String userCode) {
	
		Integer id =null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> mapData = null;
		BigInteger userId = null;
		AttendanceResponse response = new AttendanceResponse();
		 if (bean.getClockInAttendenceType() != null) {
			 mapData = fetchUserDTLByUserCode(sessionFactory.openSession(), userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {

					userId = (BigInteger) mapData.get("userId");
				}
				
		        String query = "select id from attendance_clock where createdBy = :createdBy and clockOutDateTime is null and clockInDateTime <= :clockIn and clockIsActive='Y'";
		        id = (Integer) sessionFactory.getCurrentSession()
		                .createSQLQuery(query)
		                .setParameter("createdBy", userId)
		                .setParameter("clockIn", LocalDateTime.parse(bean.getClockInDateTime(),formatter))
		                .uniqueResult();
		    }
		
		 if(id==null) {
			
			AttendanceClock entity = new AttendanceClock();
			BeanUtils.copyProperties(bean, entity);
			if(bean.getClockInDateTime()!=null) {
			entity.setClockInDateTime(LocalDateTime.parse(bean.getClockInDateTime(),formatter));
			entity.setClockIsActive('Y');
			entity.setCreatedBy(userId.intValue());
			entity.setCreatedDate(new Date());
			}
	
			 id = (Integer) sessionFactory.getCurrentSession().save(entity);
			 response.setMsg("Clock-In Succssfully for Date :- "+LocalDateTime.parse(bean.getClockInDateTime(),formatter));
	         response.setStatusCode(200);
	        	
		}else {
			 response.setMsg("User is already ClockIn for Date :- "+LocalDateTime.parse(bean.getClockInDateTime(),formatter));
	         response.setStatusCode(406);
		}
	    return response;
	}

	@Override
	public AttendanceResponse saveLeaveClock(LeaveClockRequest bean, String userCode) {
		
		BigInteger userId = null;
		Integer id = null;
		AttendanceResponse response = new AttendanceResponse();
//		Session session = null;
//		Transaction transaction = null;
		Map<String, Object> mapData = null;
		LeaveClock entity = new LeaveClock();
		String msg=null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 mapData = fetchUserDTLByUserCode(sessionFactory.openSession(), userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}
			BeanUtils.copyProperties(bean, entity);
			LocalDateTime  startDate = LocalDateTime.parse(bean.getLeaveStartDate(), formatter);
			LocalDateTime  endDate = LocalDateTime.parse(bean.getLeaveEndDate(), formatter);
	        String query = "exec [SP_GET_LEAVE_DETAIL] :empId, :leaveType, :fromDate, :toDate";
	        List data =  sessionFactory.getCurrentSession().createSQLQuery(query)
	                .setParameter("empId", userId.intValue())
	                .setParameter("leaveType", bean.getLeavetype())
	                .setParameter("fromDate",   startDate.format(formatter))
	                .setParameter("toDate", endDate.format(formatter))
	                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("Message");
				}
			
	        if(msg.equals("success")) {
	        	entity.setLeaveStatus('Y');	 
	        	entity.setLeaveStartDate(startDate);
				entity.setLeaveEndDate(endDate);
				entity.setCreatedBy(userId.intValue());
				entity.setCreatedDate(new Date());
				entity.setLeavetype(String.valueOf(bean.getLeavetype()));
	        	id = (Integer) sessionFactory.getCurrentSession().save(entity);
	        	response.setId(id);
	        	response.setMsg("Leave applied Succssfully for Date :- "+new Date());
	        	response.setStatusCode(200);
	        }else if(msg.equals("already")) {
	        	response.setId(id);
	        	response.setMsg("Leave already applied for the given date range");
	        	response.setStatusCode(200);
	        }
			}
	    return response;
	}
	
	

	@Override
	public AttendanceClockRes getAttendanceClockList(AttendanceClockList bean, String userCode) {
		
		 Map<String, Object> mapData = null;
		 BigInteger userId = null;
		 AttendanceClockDtl atteClockDtl=null;
		 List<AttendanceClockDtl> atteClockList=new ArrayList<AttendanceClockDtl>();
		 AttendanceClockRes response = new AttendanceClockRes();
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		 LocalDate  startDate = LocalDate.parse(bean.getStartDate(), formatter);
		 LocalDate  endDate = LocalDate.parse(bean.getEndDate(), formatter);
		 try {
		 mapData = fetchUserDTLByUserCode(sessionFactory.openSession(), userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {

				userId = (BigInteger) mapData.get("userId");
			}
			
				
			
		 String query = "exec [SP_GET_EMP_LEAVE_ATTENTDANCE_DETAIL] :EmpId, :StartDate, :EndDate, :FLAG";
	        List data =  sessionFactory.getCurrentSession().createSQLQuery(query)
	                .setParameter("EmpId", userId.intValue())
	                .setParameter("StartDate",   startDate.format(formatter))
	                .setParameter("EndDate", endDate.format(formatter))
	                .setParameter("FLAG", bean.getFlag())
	                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			if (data != null && !data.isEmpty()) {
				
				for (Object object : data) {
					Map row = (Map) object;
					atteClockDtl = new AttendanceClockDtl();
					
					atteClockDtl.setLeaveId((Integer) row.get("id"));
					atteClockDtl.setLoginDate( (Date) row.get("LoginDate"));
					atteClockDtl.setLoginTime((Time)row.get("LoginTime"));
					atteClockDtl.setLogoutDate((Date) row.get("LogoutDate"));
					atteClockDtl.setLogoutTime((Time) row.get("LogoutTime"));
					atteClockDtl.setWorkingHour((String) row.get("working_hour"));
					atteClockDtl.setEmployeeRemark((String)row.get("Remarks"));
					atteClockDtl.setLoginType((String)row.get("login_type"));
					atteClockDtl.setStatus((String)row.get("status"));
					atteClockDtl.setTodayStatus((String) row.get("TODAY_STATUS"));
					atteClockList.add(atteClockDtl);
					
				}
				if(atteClockList!=null) {
				response.setAttendanceList(atteClockList);
				response.setStatusCode("EC200");
				}
			  }else {
				  response.setMsg("No data found");
				  response.setStatusCode("EC200");
			  }
			}catch (SQLGrammarException ex) {
				
				response.setDescription(ex.getMessage());
				response.setMsg("Unable to fetch data and server side error");
				response.setStatusCode("EC500");
				
			} 
			
			return response;
	}

	@Override
	public AttendanceResponse saveAttendanceClockOut(AttendanceClockRequest bean, String userCode) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		AttendanceResponse response = new AttendanceResponse();
		Integer id=null;
		BigInteger userId = null;
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		try {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		 if (bean.getClockOutAttendenceType() != null) {
			 mapData = fetchUserDTLByUserCode(sessionFactory.openSession(), userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {

					userId = (BigInteger) mapData.get("userId");
				}
				
		        String query = "select id from attendance_clock where createdBy = :createdBy and clockIsActive = :IsActive";
		        id = (Integer) sessionFactory.getCurrentSession()
		                .createSQLQuery(query)
		                .setParameter("createdBy", userId.intValue())
		                .setParameter("IsActive", 'Y')
		                .uniqueResult();
		    }
		    if (id != null) {
		    	AttendanceClock  res = session.get(AttendanceClock.class, id);
		        if(res!=null) {
		        	
		        	
				        if (bean.getClockOutDateTime() != null) {
				            res.setClockOutDateTime(LocalDateTime.parse(bean.getClockOutDateTime(), formatter));
				        }
				        if(res.getClockInDateTime().isAfter(res.getClockOutDateTime())) {
				        	response.setMsg("Clock out time cann't be less than clock in");
				        	response.setStatusCode(406);
				        	return response;
				        }
				        res.setClockIsActive('N');
				        res.setClockOutAttendenceType(bean.getClockOutAttendenceType());
				        res.setCreatedBy(userId.intValue());
				        session.update(res);
				        response.setMsg("Clock-Out Succssfully for Date :- "+LocalDateTime.parse(bean.getClockOutDateTime(), formatter));
				        response.setStatusCode(200);
		        } 
		    }else {
			        	response.setMsg("User already Clock-Out for Date :- "+new Date());
			        	response.setStatusCode(200);
			        	return response;
			        
		    }
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				transaction.commit();;
				session.close();
				response.setId(id);
			}
	
		}
		
	
		return response;
	}	
	
	@SuppressWarnings("deprecation")
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@Override
	public AttendanceResponse cancelLeave(Integer leaveId, String userCode) {
	    Map<String, Object> mapData = null;
	    BigInteger userId = null;

	    // Fetch user details
	    try (Session session = sessionFactory.openSession()) {
	        mapData = fetchUserDTLByUserCode(session, userCode);
	        if (mapData != null && mapData.get("SUCCESS") != null) {
	            userId = (BigInteger) mapData.get("userId");
	        }
	    }

	    // Check if userId is valid
	    if (userId == null) {
	        AttendanceResponse response = new AttendanceResponse();
	        response.setStatusCode(407);
	        response.setMsg("User not found.");
	        return response;
	    }

	    // Prepare and execute update query
	    try (Session session = sessionFactory.openSession()) {
	        Transaction transaction = session.beginTransaction();

	        String sqlQuery = "UPDATE LeaveClock SET leaveStatus = :leaveStatus, ModifiedBy = :modifiedBy, ModifiedDate = :modifiedDate WHERE CreatedBy = :createdBy and id=:leaveId";
	        

	        Query query = session.createQuery(sqlQuery);
	        query.setParameter("leaveId", leaveId);
	        query.setParameter("leaveStatus", 'N');
	        query.setParameter("modifiedBy", userId);
	        query.setParameter("modifiedDate", new Date());
	        query.setParameter("createdBy", userId);

	        int updatedRows = query.executeUpdate();
	        transaction.commit();

	        AttendanceResponse response = new AttendanceResponse();
	        if (updatedRows > 0) {
	            response.setStatusCode(200);
	            response.setMsg("Leave canceled successfully.");
	        } else {
	            response.setStatusCode(407);
	            response.setMsg("No leave records found for the user.");
	        }
	        return response;

	    } catch (Exception e) {
	        e.printStackTrace();
	        AttendanceResponse response = new AttendanceResponse();
	        response.setStatusCode(500);
	        response.setMsg("Error occurred while canceling leave: " + e.getMessage());
	        return response;
	    }
	}

	@Override
	public AttendanceResponse getLeaveAndAttend(String requestDate, String userCode) {
	
		  Map<String, Object> mapData = null;
		  BigInteger userId = null;
		  AttendanceResponse response = null;
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		  // Fetch user details
	    try (Session session = sessionFactory.openSession()) {
	        mapData = fetchUserDTLByUserCode(session, userCode);
	        if (mapData != null && mapData.get("SUCCESS") != null) {
	            userId = (BigInteger) mapData.get("userId");
	        }
	    }

	    // Check if userId is valid
	    if (userId == null) {
	    	response = new AttendanceResponse();
	        response.setStatusCode(407);
	        response.setMsg("User not found.");
	        return response;
	    }

	    try{

	        String query = "exec [SP_GET_ATTEND_AND_LEAVE] :EmpId, :currentDate ";
	        List data =  sessionFactory.getCurrentSession().createSQLQuery(query)
	                .setParameter("EmpId", userId.intValue())
	                .setParameter("currentDate",   LocalDate.parse(requestDate, formatter).format(formatter))
	                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
	        
	        
	       

	        if (data != null && !data.isEmpty()) {
	        	response = new AttendanceResponse();
				for (Object object : data) {
					Map row = (Map) object;
				
					if((Integer)row.get("status")==1) {
					response.setMsg((String)row.get("Msg"));
					}else if((Integer)row.get("status")==2) {
						response.setMsg((String)row.get("Msg"));
					}else {
						response.setMsg("Not found data");
					}
					
				}
				response.setStatusCode(200);
			  }

	    } catch (Exception e) {
	        e.printStackTrace();
	        response = new AttendanceResponse();
	        response.setStatusCode(500);
	        response.setMsg("Error occurred while canceling leave: " + e.getMessage());
	        return response;
	    }
		return response;
	}

	@Override
	public TodayStatusResponse getTodayStatus(String userCode) {
		  Map<String, Object> mapData = null;
		  BigInteger userId = null;
		  AttendanceResponse response = new AttendanceResponse();
		  TodayStatusResponse bean = null;
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		  // Fetch user details
	    try (Session session = sessionFactory.openSession()) {
	        mapData = fetchUserDTLByUserCode(session, userCode);
	        if (mapData != null && mapData.get("SUCCESS") != null) {
	            userId = (BigInteger) mapData.get("userId");
	        }
	    }

	    // Check if userId is valid
	    if (userId == null) {
	    	bean = new TodayStatusResponse();
	        response.setStatusCode(407);
	        response.setMsg("User not found.");
	        bean.setResponse(response);
	        return bean;
	    }

	    try{

	        String query = "exec [SP_GET_TODAY_STATUS] :empId, :currentDate ";
	        List data =  sessionFactory.getCurrentSession().createSQLQuery(query)
	                .setParameter("empId", userId.intValue())
	                .setParameter("currentDate",   String.valueOf(LocalDate.now().format(formatter)))
	                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
	        
	        
	       

	        if (data != null && !data.isEmpty()) {
	        	bean = new TodayStatusResponse();
				for (Object object : data) {
					Map row = (Map) object;
					bean.setStatus((String)row.get("Status"));
					bean.setMessage((String)row.get("Message"));
				}
				response.setStatusCode(200);
				bean.setResponse(response);
			  }

	    } catch (Exception e) {
	        e.printStackTrace();
	        bean = new TodayStatusResponse();
	        response.setStatusCode(500);
	        response.setMsg("Error occurred while canceling leave: " + e.getMessage());
	        bean.setResponse(response);
	        return bean;
	    }
		return bean;
	}

	@Override
	public List<String> futureDaysLeave(String userCode) {
		  Map<String, Object> mapData = null;
		  BigInteger userId = null;
		  List<String> list = new ArrayList<String>();
		 try (Session session = sessionFactory.openSession()) {
		        mapData = fetchUserDTLByUserCode(session, userCode);
		        if (mapData != null && mapData.get("SUCCESS") != null) {
		            userId = (BigInteger) mapData.get("userId");
		        }
		    }
		    // Check if userId is valid
		    if (userId == null) {
		        return null;
		    }
		    try{

		        String query = "exec [SP_GetLeaveData_ByEmpId] :empId";
		        List data =  sessionFactory.getCurrentSession().createSQLQuery(query)
		                .setParameter("empId", userId.intValue())
		                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		        
		        if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;
						list.add((String)row.get("LeaveDate"));	
					}
				  }

		    } catch (Exception e) {
		        e.printStackTrace();
		        return null;
		    }
			return list;
	}
	
}



