/**
 * 
 */
package com.hitech.dms.app.config.publisher;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class PublishModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2068476416129133826L;
	private BigInteger id;
	private String topic;
}
