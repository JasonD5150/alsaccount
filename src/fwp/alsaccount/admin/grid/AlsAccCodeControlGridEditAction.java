package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport; 

import fwp.alsaccount.appservice.admin.AlsAccCdControlAS;
import fwp.alsaccount.dao.admin.AlsAccCdControl;
import fwp.alsaccount.dao.admin.AlsAccCdControlIdPk;
import fwp.security.user.UserDTO;


public class AlsAccCodeControlGridEditAction extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private String oper;
	private HttpServletRequest request;	
	
	private AlsAccCdControlIdPk idPk = new AlsAccCdControlIdPk();
	private String id;
	private Integer budgYear;
	private String aamAccount;
	private String aocOrg;
	private String aaccOrgFlag;
	private String aaccFund;
	private Double aaccAllocatedAmt;
	private String aaccBalancingAmtFlag;
	private String asacSubclass;
	private String aaccRemarks;
	private String aaccJlrRequired;

	
	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		try{
			AlsAccCdControlAS appSer = new AlsAccCdControlAS();
			AlsAccCdControl tmp = null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsAccCdControl();
				idPk.setAsacBudgetYear(budgYear);
				idPk.setAaccSeqNo(appSer.getNextSeqNo(budgYear, idPk.getAaccAccCd()));
				tmp.setIdPk(idPk);
				
				//TODO need to remove this logic when the triggers and correct audit columns are added to the db	
				tmp.setAaccWhoLog(userInfo.getStateId().toString());
				tmp.setAaccWhenLog(date);
				//********************************************************************
			} else {
				AlsAccCdControlIdPk tmpIdPk = new AlsAccCdControlIdPk();
				String[] keys = this.id.split("_");
				tmpIdPk.setAsacBudgetYear(Integer.parseInt(keys[0]));
				tmpIdPk.setAaccAccCd(keys[1]);
				tmpIdPk.setAaccSeqNo(Integer.parseInt(keys[2]));
				
				tmp = appSer.findById(tmpIdPk);
			}
			
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(oper.equalsIgnoreCase("add")){
					if(appSer.isDuplicateEntry(idPk)){
						addActionError("Unable to add this record due to duplicate");
					}
					if(appSer.isDuplicateBudgYearAccCdAccFund(idPk.getAsacBudgetYear(), idPk.getAaccAccCd(), aamAccount, aaccFund)){
						addActionError("The combination of Budget Year, Account Code, Account, and Fund already exists in the database.");
					}
				}
				
				Boolean balFlagSet = appSer.isBalanceAmtFlagSet(budgYear, idPk.getAaccAccCd());
				if("Y".equals(aaccBalancingAmtFlag) && balFlagSet){
					addActionError("Balancing Amount Flag cannot be Yes twice for the same combination of Budget Year and Accounting Code.");
				}else if ("N".equals(aaccBalancingAmtFlag) && balFlagSet == false){
					addActionError("Balancing Amount Flag should be selected as Yes, if only one combination of Budget Year and Accounting Code exists.");
				}
				if (this.hasActionErrors()) {
					return "error_json";
				}
				
				tmp.setAaccAllocatedAmt(aaccAllocatedAmt);
				tmp.setAaccBalancingAmtFlag(aaccBalancingAmtFlag);
				tmp.setAaccFund(aaccFund);
				tmp.setAaccJlrRequired(aaccJlrRequired);
				tmp.setAaccOrgFlag(aaccOrgFlag);
				tmp.setAaccRemarks(aaccRemarks);
				tmp.setAamAccount(aamAccount);
				tmp.setAocOrg(aocOrg);
				tmp.setAsacSubclass(asacSubclass);
				
				appSer.save(tmp);
			}else if(oper.equalsIgnoreCase("del")){
				if(appSer.isBudgYearAccCdUsed(tmp.getIdPk().getAsacBudgetYear(), tmp.getIdPk().getAaccAccCd())){
					errMsg += "This Budget Year and Account Code exists in ALS_ORG_CONTROL table, can not delete this record.";
					addActionError(errMsg);
			        return "error_json";
				}
				appSer.delete(tmp);
			}
		}  catch(Exception ex) {
			 if (ex.toString().contains("ORA-02292")){
				  errMsg += "Grid has child record(s) which would need to be deleted first.";
			  } else if (ex.toString().contains("ORA-02291")){
				  errMsg += "Cannot save the record without Account being set up in Account Master first.";
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
	

	public void setServletRequest(HttpServletRequest arg0) {
		request = arg0;		
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

	public AlsAccCdControlIdPk getIdPk() {
		return idPk;
	}

	public void setIdPk(AlsAccCdControlIdPk idPk) {
		this.idPk = idPk;
	}

	public Integer getBudgYear() {
		return budgYear;
	}


	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}


	public String getAamAccount() {
		return aamAccount;
	}


	public void setAamAccount(String aamAccount) {
		this.aamAccount = aamAccount;
	}


	public String getAocOrg() {
		return aocOrg;
	}


	public void setAocOrg(String aocOrg) {
		this.aocOrg = aocOrg;
	}


	public String getAaccOrgFlag() {
		return aaccOrgFlag;
	}


	public void setAaccOrgFlag(String aaccOrgFlag) {
		this.aaccOrgFlag = aaccOrgFlag;
	}


	public String getAaccFund() {
		return aaccFund;
	}


	public void setAaccFund(String aaccFund) {
		this.aaccFund = aaccFund;
	}


	public Double getAaccAllocatedAmt() {
		return aaccAllocatedAmt;
	}


	public void setAaccAllocatedAmt(Double aaccAllocatedAmt) {
		this.aaccAllocatedAmt = aaccAllocatedAmt;
	}


	public String getAaccBalancingAmtFlag() {
		return aaccBalancingAmtFlag;
	}


	public void setAaccBalancingAmtFlag(String aaccBalancingAmtFlag) {
		this.aaccBalancingAmtFlag = aaccBalancingAmtFlag;
	}


	public String getAsacSubclass() {
		return asacSubclass;
	}


	public void setAsacSubclass(String asacSubclass) {
		this.asacSubclass = asacSubclass;
	}


	public String getAaccRemarks() {
		return aaccRemarks;
	}


	public void setAaccRemarks(String aaccRemarks) {
		this.aaccRemarks = aaccRemarks;
	}


	public String getAaccJlrRequired() {
		return aaccJlrRequired;
	}


	public void setAaccJlrRequired(String aaccJlrRequired) {
		this.aaccJlrRequired = aaccJlrRequired;
	}

	
}
