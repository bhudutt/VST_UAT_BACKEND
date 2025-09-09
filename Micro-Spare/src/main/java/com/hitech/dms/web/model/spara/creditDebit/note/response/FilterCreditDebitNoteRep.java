package com.hitech.dms.web.model.spara.creditDebit.note.response;

import java.util.List;

import lombok.Data;
@Data
public class FilterCreditDebitNoteRep {
	
	List<CreditDebitNoteReponse> searchList;
	private Integer totalCount;
	private Integer rowCount;
	private String msg;
	private Integer statusCode;
	
	

}
