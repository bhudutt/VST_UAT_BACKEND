package com.hitech.dms.app.entity;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Mail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9122265473417131510L;

	@Pattern(regexp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+( -[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message = "Incorrect mailbox format")
	private String to;

	@NotBlank(message = "Title cannot be blank ")
	private String title;

	@NotBlank(message = "Body cannot be blank ")
	private String content;

	private String msgId; // message id

}
