package fwp.alsaccount.refund.grid;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.admin.dao.AlsRefundInfo;
import fwp.als.hibernate.admin.dao.AlsRefundInfoIdPk;
import fwp.als.hibernate.admin.dao.AlsSessionTrans;
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
	
	private Double totRefund = 0.0;
	
	private UserDTO userInfo;
	private Timestamp date;
	private String rrAppMispays;
	private String rrAppReject;
	private String rrInsPointMispay;
	private String rrWithdraw;
	private Double minRefundAmt;
	private List<String> procTypeLst = new ArrayList<String>();
	private String procTypeSys;
	

	public String execute() throws Exception{
		HibHelpers hh = new HibHelpers();

		AlsRefundInfoAS ariAS = new AlsRefundInfoAS();
		AlsRefundInfoDTO original = new AlsRefundInfoDTO();
		AlsRefundInfo ari = new AlsRefundInfo();
		AlsRefundInfoIdPk ariIdPk = new AlsRefundInfoIdPk();
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		
		userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		date = new Timestamp(System.currentTimeMillis());
		rrAppMispays = hh.getPval("REFUND REASON", "APPLICATION MISPAY", null);
		rrAppReject = hh.getPval("REFUND REASON", "APPLICATION REJECT", null);
		rrInsPointMispay = hh.getPval("REFUND REASON", "INSURANCE POINTS MISPAY", null);
		rrWithdraw = hh.getPval("REFUND REASON", "INSURANCE POINTS MISPAY", null);
		minRefundAmt = Double.parseDouble(hh.getPval("REFUND", "MINIMUM REFUND", null));
		procTypeLst.add(hh.getPval("PROCESS TYPE", "ISSUE", null));
		procTypeLst.add(hh.getPval("PROCESS TYPE", "OFFLINE ISSUE", null));
		procTypeSys = hh.getPval("PROCESS TYPE", "SYSTEM", null);
		
		String errMsg="";
		String dispCd = null;
		String lAhmType = null;
		Integer lAhmCd = null;
		Timestamp lSessionDt = null;
		try{
			Date d = sdf.parse(id.split("_")[0]);
			original = ariAS.getPersonRefundRecord(new Timestamp(d.getTime()), Integer.parseInt(id.split("_")[1]));
			ariIdPk.setAriRefundRequestDt(new Timestamp(d.getTime()));
			ariIdPk.setAriRefundSeqNo(Integer.parseInt(id.split("_")[1]));
			ari = ariAS.findById(ariIdPk);
			if(validation(original)){
				/*PRE-UPDATE*/
				if(totRefund >= minRefundAmt&&"D".equals(original.getAriMinimumOverride())){
					ari.setAriMinimumOverride("N");
				}
				if(rrWithdraw.equals(original.getAriRefundReasonCd())){
					AlsSessionTrans ast = ariAS.getWithdrawSessionTrans(original.getAppIdNo(), procTypeLst);
					if(ast != null){
						lAhmType = ast.getIdPk().getAhmType();
						lAhmCd = ast.getIdPk().getAhmCd();
						lSessionDt = ast.getIdPk().getAsSessionDt();
					}
				}else{
					lAhmType = original.getAhmType();
					lAhmCd = original.getAhmCd();
					lSessionDt = original.getAsSessionDt();
				}
				if(!"Y".equals(original.getAriPreferenceFeeApproved())&& "Y".equals(ariPreferenceFeeApproved)){
					ariAS.updatePrefFeeInfo(true,original.getAriPreferenceFee(), lAhmType, lAhmCd, lSessionDt, original.getDesignatedRefundeeDob(), original.getAriDesignatedRefundeeAlsNo());
				}else if("Y".equals(original.getAriPreferenceFeeApproved())&&
							(!"Y".equals(ariPreferenceFeeApproved)||
								("Y".equals(original.getAriRefundApproved())&& !"Y".equals(ariRefundApproved)))){
					ariAS.updatePrefFeeInfo(false,original.getAriPreferenceFee(), lAhmType, lAhmCd, lSessionDt, original.getDesignatedRefundeeDob(), original.getAriDesignatedRefundeeAlsNo());
				}
				/*PERSON_PROC*/
				if(!rrAppMispays.equals(original.getAriRefundReasonCd())){
					if(!"Y".equals(original.getAriRefundApproved())&& "Y".equals(ariRefundApproved)){
						ari.setAriApprovedDt(date);
						ari.setAriApprovedBy(userInfo.getStateId());
						ari.setAriWhenLog(date);
						ari.setAriWhoLog(userInfo.getStateId());
						if(!rrAppMispays.equals(original.getAriRefundReasonCd())&&
						   !rrAppReject.equals(original.getAriRefundReasonCd())&&
						   !rrInsPointMispay.equals(original.getAriRefundReasonCd())&&
						   original.getAppIdNo() == null){
							ariAS.processItemInfoUpdate(original.getUsagePeriodFrom(),
												  original.getUsagePeriodTo(),
												  original.getItemTypeCd(),
												  original.getDesignatedRefundeeDob(),
												  original.getAriDesignatedRefundeeAlsNo(),
												  original.getAiiItemTxnInd(),
												  original.getAiiSeqNo(),
												  original.getItemIndCd(),
												  userInfo.getStateId(),
												  date,
												  ariAS.getSessionTrans(original));
						}
						if(original.getAppIdNo() != null){
							dispCd = hh.getPval("APPLICATION DISPOSITION", "REFUND ISSUED", null);
							ariAS.updateAppInfo(original,userInfo.getStateId(), dispCd);
						}
					}
					if(!"N".equals(original.getAriRefundApproved())&& "N".equals(ariRefundApproved)){
						ari.setAriWhenLog(date);
						ari.setAriWhoLog(userInfo.getStateId());
						if(!rrAppMispays.equals(original.getAriRefundReasonCd())&&
						   !rrInsPointMispay.equals(original.getAriRefundReasonCd())&&
						   original.getAppIdNo() == null){
							ariAS.procNo(original, userInfo.getStateId());
						}
						if(original.getAppIdNo() != null){
							dispCd = hh.getPval("APPLICATION DISPOSITION", "REFUND DENIED", null);
							ariAS.updateAppInfo(original,userInfo.getStateId(), dispCd);
						}
					}
				}
				ari.setAriRefundApproved(ariRefundApproved);
				ari.setAriDrawingFeeApproved(ariDrawingFeeApproved);
				ari.setAriPreferenceFeeApproved(ariPreferenceFeeApproved);
				ari.setAriRemarks(ariRemarks);
				ariAS.save(ari);
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
		HibHelpers hh = new HibHelpers();

		Boolean rtn = true;
		String nsfStatus= null;
		
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
			}else if(!rrAppMispays.equals(original.getAriRefundReasonCd())&&
					   !rrAppReject.equals(original.getAriRefundReasonCd())&&
					   !rrInsPointMispay.equals(original.getAriRefundReasonCd())&&
					   original.getAppIdNo() == null){
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
				if(!rrAppMispays.equals(original.getAriRefundReasonCd())&&
				   !rrAppReject.equals(original.getAriRefundReasonCd())&&
				   !rrInsPointMispay.equals(original.getAriRefundReasonCd())&&
				   original.getAppIdNo() == null){
					nsfStatus = hh.getNSFStatus(1, null, procTypeSys, null, original.getAhmType(), original.getAhmCd(), original.getAsSessionDt(), original.getAstTransactionCd(), original.getAstTransactionSeqNo());
				}else if(rrAppMispays.equals(original.getAriRefundReasonCd())||rrInsPointMispay.equals(original.getAriRefundReasonCd())){
					nsfStatus = hh.getNSFStatus(2, procTypeLst, null, null, original.getAhmType(), original.getAhmCd(), original.getAsSessionDt(), original.getAstTransactionCd(), original.getAstTransactionSeqNo());
				}else if(rrAppReject.equals(original.getAriRefundReasonCd())&&original.getAppIdNo() == null){
					nsfStatus = hh.getNSFStatus(3, procTypeLst, null, null, original.getAhmType(), original.getAhmCd(), original.getAsSessionDt(), original.getAstTransactionCd(), original.getAstTransactionSeqNo());
				}else if(original.getAppIdNo() != null){
					nsfStatus = hh.getNSFStatus(4, procTypeLst, null, original.getAppIdNo(), null, null, null, null, null);
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

	
}
