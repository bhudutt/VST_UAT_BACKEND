/**
 * 
 */
package com.hitech.dms.web.model.spare.create.response;

import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SubProductCategoryResponse {
	private Integer id;
    private Integer poCategoryId;
    private String paSubcategoryCode;
    private String paSubcategoryDesc;
//    private boolean isActive;
//    private Date createdDate;
//    private String createdBy;
//    private String modifiedBy;
//    private Date modifiesDate;
}
