/**
 * 
 */
package com.hitech.dms.web.model.jobcard.save.request;

import java.util.List;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SelectedCodes {
	private List<Integer> selectedInventoryCodes;
    private List<Integer> selectedPaintScratched;
}
