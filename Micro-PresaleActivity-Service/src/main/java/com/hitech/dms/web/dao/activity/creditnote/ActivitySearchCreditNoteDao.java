package com.hitech.dms.web.dao.activity.creditnote;

import com.hitech.dms.web.model.activitycreditnote.search.request.SearchActivityCreditNoteRequestModel;
import com.hitech.dms.web.model.activitycreditnote.search.response.SearchActivityCreditNoteResultResponseModel;


public interface ActivitySearchCreditNoteDao {

	
	public SearchActivityCreditNoteResultResponseModel searchlistActivityCreditNoteList(String userCode,
			SearchActivityCreditNoteRequestModel searchactivityCreditNoteModel);
}
