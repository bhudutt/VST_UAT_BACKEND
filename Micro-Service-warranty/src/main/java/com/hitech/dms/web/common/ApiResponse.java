package com.hitech.dms.web.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	 private int status;
	 private String message;
	 private T result;
	 private Integer count;
	 private Long id;
	 private String token;
	 
}
