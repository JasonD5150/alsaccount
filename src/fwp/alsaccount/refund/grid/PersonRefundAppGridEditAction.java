package fwp.alsaccount.refund.grid;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.admin.dao.AlsRefundInfo;
import fwp.alsaccount.appservice.refund.AlsRefundInfoAS;
import fwp.alsaccount.dto.refund.AlsRefundInfoDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.security.user.UserDTO;


public class PersonRefundAppGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	private String id;

	private String ariRefundApproved;
	private String ariDrawingFeeApproved;
	private String ariPreferenceFeeApproved;
	private String ariRemarks;
	
	private String 		originalAppIdNo;
	private Date   		originalUpFrom;
	private Date   		originalUpTo;
	private Integer 	originalItemIndCd;
	
	Double totRefund = 0.0;
	
	UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
	Timestamp date = new Timestamp(System.currentTimeMillis());
	HibHelpers hh = new HibHelpers();
	String appMispays = hh.getPval("REFUND REASON", "APPLICATION MISPAY", null);
	String appReject = hh.getPval("REFUND REASON", "APPLICATION REJECT", null);
	String insPointMispay = hh.getPval("REFUND REASON", "INSURANCE POINTS MISPAY", null);
	

	public String execute() throws Exception{
		String errMsg="";
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		
		AlsRefundInfoAS ariAS = new AlsRefundInfoAS();
		AlsRefundInfoDTO original = new AlsRefundInfoDTO();
		AlsRefundInfo ari = new AlsRefundInfo();
		
		String dispCd = null;
		
		try{
			Date d = sdf.parse(id.split("_")[0]);
			original = ariAS.getPersonRefundRecord(new Timestamp(d.getTime()), Integer.parseInt(id.split("_")[1]));
			
			if(validation(original)){
				ari = original;
				/*PERSO_PROC*/
				if(!appMispays.equals(original.getAriRefundReasonCd())){
					if(!"Y".equals(original.getAriRefundApproved())&& "Y".equals(ariRefundApproved)){
						ari.setAriApprovedDt(date);
						ari.setAriApprovedBy(userInfo.getStateId());
						ari.setAriWhenLog(date);
						ari.setAriWhoLog(userInfo.getStateId());
						if(!appMispays.equals(original.getAriRefundReasonCd())&&
						   !appReject.equals(original.getAriRefundReasonCd())&&
						   !insPointMispay.equals(original.getAriRefundReasonCd())&&
						   original.getAppIdNo() == null){
							ariAS.updateItemInfo(ariAS.getSessionTrans(original),userInfo.getStateId());
						}
						if(original.getAppIdNo() != null){
							dispCd = hh.getPval("APPLICATION DISPOSITION", "REFUND ISSUED", null);
							ariAS.updateAppInfo(original,userInfo.getStateId(), dispCd);
						}
					}
					if(!"N".equals(original.getAriRefundApproved())&& "N".equals(ariRefundApproved)){
						ari.setAriWhenLog(date);
						ari.setAriWhoLog(userInfo.getStateId());
						if(!appMispays.equals(original.getAriRefundReasonCd())&&
						   !insPointMispay.equals(original.getAriRefundReasonCd())&&
						   original.getAppIdNo() == null){
							ariAS.procNo(original, userInfo.getStateId());
						}
						if(original.getAppIdNo() != null){
							dispCd = hh.getPval("APPLICATION DISPOSITION", "REFUND DENIED", null);
							ariAS.updateAppInfo(original,userInfo.getStateId(), dispCd);
						}
					}
				}
				//ariAS.save(ari);
			}
			if (this.hasActionErrors()) {
				return "error_json";
			}
		}  catch(Exception ex) {
			 if (ex.toString().contains("ORA-02292")){
				  errMsg += "Grid has child record(s) which would need to be deleted first.";
			  } else if (ex.toString().contains("ORA-02291")){
				  errMsg += "Parent record not found.";
			  } else if (ex.toString().contains("ORA-00001")){
				  errMsg += "Unable to add this record due to duplicate.";
			  }	else {
				  errMsg += " " + ex.toString();
			  }
		    addActionError(errMsg);
	        return "error_json";
		}	
		return SUCCESS;
	}

	private Boolean validation(AlsRefundInfoDTO original){
		Boolean rtn = true;
		String nsfStatus= null;
		List<String> procTypeLst = new ArrayList<String>();
		procTypeLst.add(hh.getPval("PROCESS TYPE", "ISSUE", null));
		procTypeLst.add(hh.getPval("PROCESS TYPE", "OFFLINE ISSUE", null));
		String procTypeSys = hh.getPval("PROCESS TYPE", "SYSTEM", null);
		
		if(original == null){
			addActionError("Original refund record not found.");
			rtn = false;
		}else{
			if("Y".equals(original.getAriRefundApproved())&&"N".equals(ariRefundApproved)&&(ariRemarks == null || "".equals(ariRemarks))){
				addActionError("Remark is required when Item Fee Refund is changes from Yes to No.");
				rtn = false;
			}else if("Y".equals(original.getAriDrawingFeeApproved())&&"N".equals(ariDrawingFeeApproved)&&(ariRemarks == null || "".equals(ariRemarks))){
				addActionError("Remark is required when Application Fee Refund is changes from Yes to No.");
				rtn = false;
			}else if("Y".equals(original.getAriPreferenceFeeApproved())&&"N".equals(ariPreferenceFeeApproved)&&(ariRemarks == null || "".equals(ariRemarks))){
				addActionError("Remark is required when Preference Fee Refund is changes from Yes to No.");
				rtn = false;
			}else if(!appMispays.equals(original.getAriRefundReasonCd())&&
					   !appReject.equals(original.getAriRefundReasonCd())&&
					   !insPointMispay.equals(original.getAriRefundReasonCd())&&
					   originalAppIdNo == null){
				if(original.getUsagePeriodFrom() == null&&
				  (ariRefundApproved != null || ariDrawingFeeApproved != null || ariPreferenceFeeApproved != null)){
					addActionError("Usage Period not available for the record so cannot be Approved or Disapproved.");
					rtn = false;
				}
			}else if("N".equals(ariRefundApproved)&&"Y".equals(ariDrawingFeeApproved)){
				addActionError("Drawing Fee cannot be approved, if Item Fee is disapproved.");
				rtn = false;
			}else if("N".equals(ariRefundApproved)&&"Y".equals(ariPreferenceFeeApproved)){
				addActionError("Preference Fee cannot be approved, if Item Fee is disapproved.");
				rtn = false;
			}else if(("N".equals(ariRefundApproved)||"Y".equals(ariRefundApproved))&&original.getAriRefundAmt() == 0.0){
				addActionError("Item Fee Refund cannot be approved or disapproved, if Item Fee is null.");
				rtn = false;
			}else if(("N".equals(ariDrawingFeeApproved)||"Y".equals(ariDrawingFeeApproved))&&original.getAriDrawingFee() == 0.0){
				addActionError("Drawing Fee Refund cannot be approved or disapproved, if Drawing Fee is null.");
				rtn = false;
			}else if(("N".equals(ariPreferenceFeeApproved)||"Y".equals(ariPreferenceFeeApproved))&&original.getAriPreferenceFee() == 0.0){
				addActionError("Preference Fee Refund cannot be approved or disapproved, if Preference Fee is null.");
				rtn = false;
			}else if("Y".equals(ariRefundApproved)||"Y".equals(ariDrawingFeeApproved)||"Y".equals(ariPreferenceFeeApproved)){
				if(!appMispays.equals(original.getAriRefundReasonCd())&&
						   !appReject.equals(original.getAriRefundReasonCd())&&
						   !insPointMispay.equals(original.getAriRefundReasonCd())&&
						   originalAppIdNo == null){
					nsfStatus = hh.getNSFStatus(1, null, procTypeSys, null, original.getAhmType(), original.getAhmCd(), original.getAsSessionDt(), original.getAstTransactionCd(), original.getAstTransactionSeqNo());
				}else if(appMispays.equals(original.getAriRefundReasonCd())||insPointMispay.equals(original.getAriRefundReasonCd())){
					nsfStatus = hh.getNSFStatus(2, procTypeLst, null, null, original.getAhmType(), original.getAhmCd(), original.getAsSessionDt(), original.getAstTransactionCd(), original.getAstTransactionSeqNo());
				}else if(appReject.equals(original.getAriRefundReasonCd())&&originalAppIdNo == null){
					nsfStatus = hh.getNSFStatus(3, procTypeLst, null, null, original.getAhmType(), original.getAhmCd(), original.getAsSessionDt(), original.getAstTransactionCd(), original.getAstTransactionSeqNo());
				}else if(originalAppIdNo != null){
					nsfStatus = hh.getNSFStatus(4, procTypeLst, null, originalAppIdNo, null, null, null, null, null);
				}
				if(!"R".equals(nsfStatus)&&("Y".equals(ariRefundApproved)||
											"Y".equals(ariDrawingFeeApproved)||
											"Y".equals(ariPreferenceFeeApproved))){
					addActionError("Record cannot be approved as Cheque Status is NSF and NSF Status is not resolved.");
					rtn = false;
				}
			}
			if(!"Y".equals(original.getAriRefundApproved())&& "Y".equals(ariRefundApproved)){
				totRefund += original.getAriRefundAmt();
			}
			if(!"Y".equals(original.getAriDrawingFeeApproved())&& "Y".equals(ariDrawingFeeApproved)){
				totRefund += original.getAriDrawingFee();
			}
			if("Y".equals(original.getAriDrawingFeeApproved())&& "N".equals(ariDrawingFeeApproved)){
				totRefund -= original.getAriDrawingFee();
			}
			if(!"Y".equals(original.getAriPreferenceFeeApproved())&& "Y".equals(ariPreferenceFeeApproved)){
				totRefund += original.getAriPreferenceFee();
			}
			if("Y".equals(original.getAriPreferenceFeeApproved())&& "N".equals(ariPreferenceFeeApproved)){
				totRefund -= original.getAriPreferenceFee();
			}
		}
		return rtn;
	}
		
	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAriRefundApproved() {
		return ariRefundApproved;
	}

	public void setAriRefundApproved(String ariRefundApproved) {
		this.ariRefundApproved = ariRefundApproved;
	}

	public String getAriDrawingFeeApproved() {
		return ariDrawingFeeApproved;
	}

	public void setAriDrawingFeeApproved(String ariDrawingFeeApproved) {
		this.ariDrawingFeeApproved = ariDrawingFeeApproved;
	}

	public String getAriPreferenceFeeApproved() {
		return ariPreferenceFeeApproved;
	}

	public void setAriPreferenceFeeApproved(String ariPreferenceFeeApproved) {
		this.ariPreferenceFeeApproved = ariPreferenceFeeApproved;
	}

	public String getAriRemarks() {
		return ariRemarks;
	}

	public void setAriRemarks(String ariRemarks) {
		this.ariRemarks = ariRemarks;
	}

	public String getOriginalAppIdNo() {
		return originalAppIdNo;
	}

	public void setOriginalAppIdNo(String originalAppIdNo) {
		this.originalAppIdNo = originalAppIdNo;
	}

	public Date getOriginalUpFrom() {
		return originalUpFrom;
	}

	public void setOriginalUpFrom(Date originalUpFrom) {
		this.originalUpFrom = originalUpFrom;
	}

	public Date getOriginalUpTo() {
		return originalUpTo;
	}

	public void setOriginalUpTo(Date originalUpTo) {
		this.originalUpTo = originalUpTo;
	}

	public Integer getOriginalItemIndCd() {
		return originalItemIndCd;
	}

	public void setOriginalItemIndCd(Integer originalItemIndCd) {
		this.originalItemIndCd = originalItemIndCd;
	}
	
}
