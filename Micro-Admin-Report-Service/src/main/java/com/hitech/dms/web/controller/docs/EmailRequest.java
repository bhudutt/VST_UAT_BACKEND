/**
 * 
 */
package com.hitech.dms.web.controller.docs;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class EmailRequest {

	    private String smtpServer;
	    private String smtpPort;
	    private String fromEmail;
	    private String password;
	    private String toEmail;
	    private String subject;
	    private String body;

	
}
