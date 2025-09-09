/**
 * 
 */
package com.hitech.dms.web.entity.activity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ACT_ACTUAL_ENQ")
@Entity
@Data
public class ActualActivityENQEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -2858401487563208142L;
	@EmbeddedId
	private ActualActivityENQPEntity actualActivityPENQ;
}
