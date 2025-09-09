package com.hitech.dms.web.entity.enquiry;

import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "SA_ENQ_RETAIL_FOLLOWUP_HISTORY")
public class RetailFollowUpHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Retail_Followup_HIS_ID")
    private BigInteger id;

    @Column(name = "Retail_Followup_HDR_ID", nullable = false)
    private BigInteger retailFollowupHdrId;

    @Column(name = "Current_Followup_Date", nullable = false)
    private Date currentFollowupDate;

    @Column(name = "Followup_By_UserID", nullable = false)
    private BigInteger followupByUserId;

    @Column(name = "Retail_Stage_ID", nullable = false)
    private Integer retailStageId;

    @Column(name = "Remarks")
    private String remarks;

    @Column(name = "Reason_for_rejection")
    private String reasonForRejection;

    @Column(name = "CreatedBy", nullable = false)
    private BigInteger createdBy;

    @Column(name = "CreatedDate", nullable = false)
    private Date createdDate;
}
