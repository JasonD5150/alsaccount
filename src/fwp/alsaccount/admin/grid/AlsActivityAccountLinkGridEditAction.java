package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.admin.appservice.AlsActivityAccountLinkageAS;
import fwp.alsaccount.hibernate.dao.AlsActivityAccountLinkage;
import fwp.alsaccount.hibernate.dao.AlsActivityAccountLinkageIdPk;
import fwp.security.user.UserDTO;


public class AlsActivityAccountLinkGridEditAction extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private String oper;
	private HttpServletRequest request;	
	
	private AlsActivityAccountLinkageIdPk idPk = new AlsActivityAccountLinkageIdPk();
	private String id;
	private String aamAccount;
	private Integer aaalReference;
	private String aamFund;
	private String aocOrg;
	private String asacSubclass;
	private String aaalAccountingCdFlag;
	private Integer budgYear;
	private String sysActTypeTransCd;
	

	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		try{
			AlsActivityAccountLinkageAS appSer = new AlsActivityAccountLinkageAS();
			AlsActivityAccountLinkage tmp =null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsActivityAccountLinkage();
				String[] keys = this.sysActTypeTransCd.split("\\s+");
				idPk.setAsacBudgetYear(budgYear);
				idPk.setAsacSystemActivityTypeCd(keys[0]);
				idPk.setAsacTxnCd(keys[1]);
				tmp.setIdPk(idPk);
			} else {
				//Set current idPk
				AlsActivityAccountLinkageIdPk tmpIdPk = new AlsActivityAccountLinkageIdPk();
				String[] keys = this.id.split("_");
				tmpIdPk.setAsacBudgetYear(Integer.parseInt(keys[0]));
				tmpIdPk.setAsacSystemActivityTypeCd(keys[1]);
				tmpIdPk.setAsacTxnCd(keys[2]);
				tmpIdPk.setAaalDrCrCd(keys[3]);
				
				tmp = appSer.findById(tmpIdPk);
			}
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(oper.equalsIgnoreCase("add")){
					AlsActivityAccountLinkage dupCheck = appSer.findById(idPk);
					if(dupCheck != null){
						errMsg += "Unable to add this record due to duplicate";
						addActionError(errMsg);
				        return "error_json";
					}
				}
				tmp.setAaalAccountingCdFlag(aaalAccountingCdFlag);
				tmp.setAaalReference(aaalReference);
				tmp.setAamAccount(aamAccount.trim());
				tmp.setAamFund(aamFund);
				tmp.setAocOrg(aocOrg);
				tmp.setAsacSubclass(asacSubclass);
				tmp.setAaalWhoLog(userInfo.getStateId().toString());
				tmp.setAaalWhenLog(date);
				
				appSer.save(tmp);
				
			}else if(oper.equalsIgnoreCase("del")){
				appSer.delete(tmp);
			}
		}  catch(Exception ex) {
			 if (ex.toString().contains("ORA-02292")){
				  errMsg += "Grid has child record(s) which would need to be deleted first!";
			  } else if (ex.toString().contains("ORA-02291")){
				  errMsg += "Cannot save the record without System Activity Code being set up first.";
			  } else if (ex.toString().contains("ORA-00001")){
				  errMsg += "Unable to add this record due to duplicate";
			  }	else {
				  errMsg += " " + ex.toString();
			  }
			  
		    addActionError(errMsg);
	        return "error_json";
		}
			
	    return SUCCESS;
	}
	
	
	public AlsActivityAccountLinkageIdPk getIdPk() {
		return idPk;
	}

	public void setIdPk(AlsActivityAccountLinkageIdPk idPk) {
		this.idPk = idPk;
	}

	public String getAamAccount() {
		return aamAccount;
	}

	public void setAamAccount(String aamAccount) {
		this.aamAccount = aamAccount;
	}

	public Integer getAaalReference() {
		return aaalReference;
	}

	public void setAaalReference(Integer aaalReference) {
		this.aaalReference = aaalReference;
	}

	public String getAamFund() {
		return aamFund;
	}

	public void setAamFund(String aamFund) {
		this.aamFund = aamFund;
	}

	public String getAocOrg() {
		return aocOrg;
	}

	public void setAocOrg(String aocOrg) {
		this.aocOrg = aocOrg;
	}

	public String getAsacSubclass() {
		return asacSubclass;
	}

	public void setAsacSubclass(String asacSubclass) {
		this.asacSubclass = asacSubclass;
	}

	public String getAaalAccountingCdFlag() {
		return aaalAccountingCdFlag;
	}

	public void setAaalAccountingCdFlag(String aaalAccountingCdFlag) {
		this.aaalAccountingCdFlag = aaalAccountingCdFlag;
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


	public Integer getBudgYear() {
		return budgYear;
	}


	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}


	public String getSysActTypeTransCd() {
		return sysActTypeTransCd;
	}


	public void setSysActTypeTransCd(String sysActTypeTransCd) {
		this.sysActTypeTransCd = sysActTypeTransCd;
	}

}
