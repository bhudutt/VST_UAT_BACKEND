/**
 * 
 */
package com.hitech.dms.web.entity.enquiry;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ENQ_ATTACH_IMAGES")
@Entity
@Data
public class EnquiryAttachImagesEntity implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5762671684503123928L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private BigInteger enquiryAttachImgsId;

	@JoinColumn(name = "enquiry_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EnquiryHdrEntity enquiryHdr;

	@Column(name = "file_name")
	private String file_name;

	private transient CommonsMultipartFile file;

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
