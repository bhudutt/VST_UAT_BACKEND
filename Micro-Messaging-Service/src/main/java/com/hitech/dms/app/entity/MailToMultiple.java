/**
 * 
 */
package com.hitech.dms.app.entity;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MailToMultiple implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8418874375131014912L;
	@NotBlank(message = "To Email cannot be blank ")
	private String toEmailIDs;
	private String ccEmailIDs;
	private String bccEmailIDs;
	@NotBlank(message = "Title cannot be blank ")
	private String title;

	@NotBlank(message = "Body cannot be blank ")
	private String content;

	private String msgId; // message id
}
