package fwp.alsaccount.sabhrs.grid;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesSummaryAS;
import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesIdPk;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesSummary;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesSummaryIdPk;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatusIdPk;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;
import fwp.security.user.UserDTO;


public class AlsTransactionGrpApprovalGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private String id;
	private Integer transGroupType;
	private String desc;
	private String transGroupIdentifier;
	private String transGroupCreateDt;
	private Integer programYear;
	private String accountDt;
	private String budgYear;
	private String sumAppStat;
	private String sumAppBy;
	private String sumAppDt;
	private String intAppStat;
	private String intAppBy;
	private String intAppDt;
	private String upToSumDt;
	private Integer bankCd;
	private String bankRefNo;
	private String intFile;
	private String intFileCreateDt;
	private String intFileName;
	private String nonAlsEnt;
	private Double netCashDrCr;
	private String depId;
	private String remarks;
	private String upToSabhrsDt;
	private Integer changeProgramYear;
	private Date changeAccountDt;
	private Boolean sumAll;
	private Boolean intAll;
	
	AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
	AlsTransactionGrpStatusIdPk atgsIdPk = new AlsTransactionGrpStatusIdPk();
	AlsTransactionGrpStatus atgsOriginal = new AlsTransactionGrpStatus();
	AlsTransactionGrpStatus atgs = new AlsTransactionGrpStatus();
	
	AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
	AlsSabhrsEntriesIdPk aseIdPk = null;
	List<AlsSabhrsEntries> aseLst = new ArrayList<AlsSabhrsEntries>();
	AlsSabhrsEntries ase = new AlsSabhrsEntries();
	
	AlsSabhrsEntriesSummaryAS asesAS = new AlsSabhrsEntriesSummaryAS();
	AlsSabhrsEntriesSummaryIdPk asesIdPk = null;
	List<AlsSabhrsEntriesSummary> asesLst = new ArrayList<AlsSabhrsEntriesSummary>();
	AlsSabhrsEntriesSummary ases = new AlsSabhrsEntriesSummary();
	
	Timestamp date = new Timestamp(System.currentTimeMillis());

	public String execute() throws Exception{
		HibHelpers hh = new HibHelpers();
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		
		String errMsg="";
		
		String curBudgYear = hh.getCurrentBudgetYear();
		String by;
		Integer seq;
		String type = "";
		
		try{
			if(oper.equalsIgnoreCase("edit")){
				validateEdit();
				if (this.hasActionErrors()) {
					return "error_json";
				}
				
				if(transGroupType == 1 || transGroupType == 3 || transGroupType == 8){
					if("A".equals(intAppStat)){
						if(transGroupType == 1 && depId == null){
							type = "SW";
							by = curBudgYear.substring(2, 3);
							seq = hh.getAlsDepIdSeq(curBudgYear, type);
							depId = String.format("%02d", Integer.parseInt(by))+String.format("%05d", seq);
						}else if(transGroupType == 3 && depId == null){
							type = "FS";
							by = curBudgYear.substring(2, 3);
							seq = hh.getAlsDepIdSeq(curBudgYear, type);
							depId = String.format("%02d", Integer.parseInt(by))+String.format("%05d", seq);
						}else if(transGroupType == 8 && depId == null){
							if(intAll == false){
								Integer cnt = hh.getTransGroupCnt(transGroupIdentifier);
								if(cnt > 0){
									type = "IP";
									by = curBudgYear.substring(2, 3);
									seq = hh.getAlsDepIdSeq(curBudgYear, type);
									depId = String.format("%02d", Integer.parseInt(by))+String.format("%05d", seq);
								}
							}
						}
					}
				}
				
				atgsIdPk.setAtgsGroupIdentifier(transGroupIdentifier);
				atgsIdPk.setAtgTransactionCd(transGroupType);
				atgs = atgsAS.findById(atgsIdPk);
				atgsOriginal = atgsAS.findById(atgsIdPk);
				
				/*if override program or override accounting date changed and summary approved, then disapprove summary*/
				if((atgsOriginal.getAtgsFyeOverrideProgram() != changeProgramYear && "A".equals(atgsOriginal.getAtgsSummaryStatus()))||(atgsOriginal.getAtgsFyeOverrideAcctDt() != changeAccountDt && "A".equals(atgsOriginal.getAtgsSummaryStatus()))){
					atgs.setAtgsSummaryStatus("D");
					atgs.setAtgsSummaryApprovedBy(null);
					atgs.setAtgsSummaryDt(null);
				}
				
				/*if override program changed and is not null, update sabhrs*/
				if(changeProgramYear != null && atgsOriginal.getAtgsFyeOverrideProgram() != changeProgramYear){
					atgs.setAtgsFyePriorProgram(programYear);
				}
				/* If Summary Approval Status is disapproved or Null then Delete from Sabhrs Summary and
			      update SABHRS entries if already uploaded to Summary. */
				if("A".equals(atgsOriginal.getAtgsSummaryStatus()) && "D".equals(sumAppStat)){
					if(upToSumDt != null && !"".equals(upToSumDt)){
						updateUploadToSummarySabhrs();
						if (this.hasActionErrors()) {
							return "error_json";
						}
					}
					upToSumDt = null;
				}
				
				/* If Summary Approval Status is disapproved or Null then Delete from Sabhrs Summary and
			      update SABHRS entries if already uploaded to Summary. */
				if(!sumAppStat.equals(atgsOriginal.getAtgsSummaryStatus())){
					if(upToSumDt != null && !"".equals(upToSumDt)){
						updateUploadToSummarySabhrs();
						if (this.hasActionErrors()) {
							return "error_json";
						}
					}
					atgs.setAtgsWhenUploadToSummary(null);
					if("-1".equals(sumAppStat)){
						sumAppStat = null;
					}
					atgs.setAtgsSummaryStatus(sumAppStat);
					if("A".equals(sumAppStat)){
						atgs.setAtgsSummaryApprovedBy(userInfo.getStateId().toString());
						atgs.setAtgsSummaryDt(Utils.StrToTimestamp(sumAppDt, "long"));
					}else{
						atgs.setAtgsSummaryApprovedBy(null);
						atgs.setAtgsSummaryDt(null);
					}
					if("-1".equals(intAppStat)){
						intAppStat = null;
					}
					atgs.setAtgsInterfaceStatus(intAppStat);
					if("A".equals(intAppStat)){
						atgs.setAtgsInterfaceApprovedBy(userInfo.getStateId().toString());
						atgs.setAtgsInterfaceDt(Utils.StrToTimestamp(sumAppDt, "long"));
					}else{
						atgs.setAtgsInterfaceApprovedBy(null);
						atgs.setAtgsInterfaceDt(null);
					}
					atgs.setAtgsWhenModi(date);
					atgs.setAtgsWhoModi(userInfo.getStateId().toString());
				}else if(!intAppStat.equals(atgsOriginal.getAtgsInterfaceStatus()) && "D".equals(intAppStat)){
					atgs.setAtgsInterfaceStatus(intAppStat);
					atgs.setAtgsInterfaceApprovedBy(null);
					atgs.setAtgsInterfaceDt(null);
					atgs.setAtgsWhoModi(userInfo.getStateId().toString());
					atgs.setAtgsWhenModi(date);
				}
				atgsAS.save(atgs);
			}else if(oper.equalsIgnoreCase("del")){
				atgsAS.delete(atgs);
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
	
	public String validateEdit(){
		HibHelpers hh = new HibHelpers();
		String offlinePayment = "";
		Integer providerNo = 0;
		String billingTo = "";
		if((transGroupType == 1 || transGroupType == 3) && "A".equals(intAppStat)){
			if(bankRefNo == null || "".equals(bankRefNo)){
				this.addActionError("Bank Reference Number must be entered.");
			}
		}
		if(!"N".equals(atgsOriginal.getAtgsSummaryStatus()) && "N".equals(sumAppStat)){
			this.addActionError("Summary Approval Status cannot be set to Not Applicable.");
		}
		if(transGroupType == 8){
			offlinePayment = hh.getAirOfflnPaymentApproved(transGroupIdentifier);
			providerNo = Integer.parseInt(transGroupIdentifier.substring(3, 7));
			billingTo = transGroupIdentifier.substring(8, 18);
			if(!"A".equals(atgsOriginal.getAtgsSummaryStatus()) && "A".equals(sumAppStat)){
				if(!"Y".equals(offlinePayment)){
					this.addActionError("Summary Approval Status cannot be set to Approved as Internal Remittance is not yet approved for Provider "+providerNo+" and Billing Period End "+billingTo+".");
				}
			}
		}
		if(!"N".equals(atgsOriginal.getAtgsSummaryStatus()) && "N".equals(intAppStat)){
			this.addActionError("Interface Approval Status cannot be set to Not Applicable.");
		}
		if(!"A".equals(atgsOriginal.getAtgsSummaryStatus()) && "A".equals(intAppStat) && transGroupType == 8){
			if(!"Y".equals(offlinePayment)){
				this.addActionError("Interface Approval Status cannot be set to Approved as Internal Remittance is not yet approved for Provider "+providerNo+" and Billing Period End "+billingTo+".");
			}
		}
		if(upToSabhrsDt != null && !"".equals(upToSabhrsDt)){
			if(date.after(Utils.StrToTimestamp(upToSabhrsDt,"short"))){
				this.addActionError("Date Uploaded to SABHRS cannot be future date.");
			}
			if(Utils.StrToTimestamp(intFileCreateDt,"short").after(Utils.StrToTimestamp(upToSabhrsDt,"short"))){
				this.addActionError("Date Uploaded to SABHRS should be greater than or equal to Date File Created.");
			}
		}
		return SUCCESS;
	}
	
	public Timestamp getUploadedToSumDt() throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date tmpDate = dateFormat.parse("01/01/1900 00:00:00");
		long time = tmpDate.getTime();
		Timestamp rtn = new Timestamp(time);
		return rtn;
	}
	
	@SuppressWarnings("unchecked")
	public void updateUploadToSummarySabhrs() throws ParseException{
		String where = "";
		where = "WHERE atgTransactionCd="+transGroupType+" "
			  + "AND SUBSTR(atgsGroupIdentifier,1,18) = SUBSTR("+transGroupIdentifier+",1,18) "
			  + "AND aseAllowUploadToSummary = 'Y'";
		aseLst = aseAS.findAllByWhere(where);
		
		if(!aseLst.isEmpty()){
			ase = aseLst.get(0);
			ase.setAseWhenUploadedToSummary(null);
			ase.setAseWhenUploadedToSumm(getUploadedToSumDt());
			ase.setAsesSeqNo(null);
			//System.out.println("Update AlsSabhrsEntries");
			aseAS.save(ase);
		}else{
			addActionError("No corresponding record found in Als_Sabhrs_Entries.");
		}
		
		where = "WHERE atgTransactionCd="+transGroupType+" "
			  + "AND SUBSTR(atgsGroupIdentifier,1,18) = 'SUBSTR("+transGroupIdentifier+",1,18)' ";
		asesLst = asesAS.findAllByWhere(where);
		
		if(!asesLst.isEmpty()){
			ases = asesLst.get(0);
			//System.out.println("Delete from AlsSabhrsEntriesSummary");
			asesAS.delete(ases);
		}else{
			addActionError("No corresponding record found in Als_Sabhrs_Entries_Summary.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateProgramSabhrs() throws ParseException{
		String where = "";
		where = "WHERE atgTransactionCd="+transGroupType+" "
			  + "AND atgsGroupIdentifier = '"+transGroupIdentifier+"' "
			  + "AND aseAllowUploadToSummary = 'Y'";
		aseLst = aseAS.findAllByWhere(where);
		
		if(!aseLst.isEmpty()){
			ase = aseLst.get(0);
			ase.setAsacProgram(changeProgramYear);
			aseAS.save(ase);
		}else{
			addActionError("No corresponding record found in Als_Sabhrs_Entries.");
		}
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

	public Integer getTransGroupType() {
		return transGroupType;
	}

	public void setTransGroupType(Integer transGroupType) {
		this.transGroupType = transGroupType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTransGroupIdentifier() {
		return transGroupIdentifier;
	}

	public void setTransGroupIdentifier(String transGroupIdentifier) {
		this.transGroupIdentifier = transGroupIdentifier;
	}

	public String getTransGroupCreateDt() {
		return transGroupCreateDt;
	}

	public void setTransGroupCreateDt(String transGroupCreateDt) {
		this.transGroupCreateDt = transGroupCreateDt;
	}

	public Integer getProgramYear() {
		return programYear;
	}

	public void setProgramYear(Integer programYear) {
		this.programYear = programYear;
	}

	public String getAccountDt() {
		return accountDt;
	}

	public void setAccountDt(String accountDt) {
		this.accountDt = accountDt;
	}

	public String getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(String budgYear) {
		this.budgYear = budgYear;
	}

	public String getSumAppStat() {
		return sumAppStat;
	}

	public void setSumAppStat(String sumAppStat) {
		this.sumAppStat = sumAppStat;
	}

	public String getSumAppBy() {
		return sumAppBy;
	}

	public void setSumAppBy(String sumAppBy) {
		this.sumAppBy = sumAppBy;
	}

	public String getSumAppDt() {
		return sumAppDt;
	}

	public void setSumAppDt(String sumAppDt) {
		this.sumAppDt = sumAppDt;
	}

	public String getIntAppStat() {
		return intAppStat;
	}

	public void setIntAppStat(String intAppStat) {
		this.intAppStat = intAppStat;
	}

	public String getIntAppBy() {
		return intAppBy;
	}

	public void setIntAppBy(String intAppBy) {
		this.intAppBy = intAppBy;
	}

	public String getIntAppDt() {
		return intAppDt;
	}

	public void setIntAppDt(String intAppDt) {
		this.intAppDt = intAppDt;
	}

	public String getUpToSumDt() {
		return upToSumDt;
	}

	public void setUpToSumDt(String upToSumDt) {
		this.upToSumDt = upToSumDt;
	}

	public Integer getBankCd() {
		return bankCd;
	}

	public void setBankCd(Integer bankCd) {
		this.bankCd = bankCd;
	}

	public String getBankRefNo() {
		return bankRefNo;
	}

	public void setBankRefNo(String bankRefNo) {
		this.bankRefNo = bankRefNo;
	}

	public String getIntFile() {
		return intFile;
	}

	public void setIntFile(String intFile) {
		this.intFile = intFile;
	}

	public String getIntFileCreateDt() {
		return intFileCreateDt;
	}

	public void setIntFileCreateDt(String intFileCreateDt) {
		this.intFileCreateDt = intFileCreateDt;
	}

	public String getIntFileName() {
		return intFileName;
	}

	public void setIntFileName(String intFileName) {
		this.intFileName = intFileName;
	}

	public String getNonAlsEnt() {
		return nonAlsEnt;
	}

	public void setNonAlsEnt(String nonAlsEnt) {
		this.nonAlsEnt = nonAlsEnt;
	}

	public Double getNetCashDrCr() {
		return netCashDrCr;
	}

	public void setNetCashDrCr(Double netCashDrCr) {
		this.netCashDrCr = netCashDrCr;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUpToSabhrsDt() {
		return upToSabhrsDt;
	}

	public void setUpToSabhrsDt(String upToSabhrsDt) {
		this.upToSabhrsDt = upToSabhrsDt;
	}
	
	public Integer getChangeProgramYear() {
		return changeProgramYear;
	}

	public void setChangeProgramYear(Integer changeProgramYear) {
		this.changeProgramYear = changeProgramYear;
	}

	public Date getChangeAccountDt() {
		return changeAccountDt;
	}

	public void setChangeAccountDt(Date changeAccountDt) {
		this.changeAccountDt = changeAccountDt;
	}

	public Boolean getSumAll() {
		return sumAll;
	}

	public void setSumAll(Boolean sumAll) {
		this.sumAll = sumAll;
	}

	public Boolean getIntAll() {
		return intAll;
	}

	public void setIntAll(Boolean intAll) {
		this.intAll = intAll;
	}
}
