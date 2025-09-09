/**
 * 
 */
package com.hitech.dms.web.model.jobcard.search.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class TyreDetailsResponse {
    private String frontRHPSI;
    private String frontLHPSI;
    private String rearRHPSI;
    private String rearLHPSI;
    private String frontTireMakeRHNumber;
    private String frontTireMakeLHNumber;
    private String rearTireMakeRHNumber;
    private String rearTireMakeLHNumber;

}
