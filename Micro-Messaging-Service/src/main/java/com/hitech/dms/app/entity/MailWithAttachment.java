/**
 * 
 */
package com.hitech.dms.app.entity;

import java.io.File;

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
public class MailWithAttachment {
	@NotBlank(message = "To Email cannot be blank ")
	private String toEmailIDs;
	private String ccEmailIDs;
	private String bccEmailIDs;
	@NotBlank(message = "Title cannot be blank ")
	private String title;

	@NotBlank(message = "Body cannot be blank ")
	private String content;
	
	private File attachment;

	private String msgId; // message id
}
