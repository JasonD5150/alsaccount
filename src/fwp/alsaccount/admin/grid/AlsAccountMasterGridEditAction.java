package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.admin.appservice.AlsAccountMasterAS;
import fwp.alsaccount.hibernate.dao.AlsAccountMaster;
import fwp.alsaccount.hibernate.dao.AlsAccountMasterIdPk;
import fwp.alsaccount.utils.HibHelpers;
import fwp.security.user.UserDTO;


public class AlsAccountMasterGridEditAction extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private String oper;
	private HttpServletRequest request;	
	
	private AlsAccountMasterIdPk idPk = new AlsAccountMasterIdPk();
	private String aamAccountDesc;
	private String id;
	private Integer budgYear;
	

	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		try{
			AlsAccountMasterAS appSer = new AlsAccountMasterAS();
			AlsAccountMaster tmp = null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsAccountMaster();
				idPk.setAsacBudgetYear(budgYear);
				tmp.setIdPk(idPk);
			} else {
				//Set current idPk
				AlsAccountMasterIdPk tmpIdPk = new AlsAccountMasterIdPk();
				String[] keys = this.id.split("_");
				tmpIdPk.setAsacBudgetYear(Integer.parseInt(keys[0]));
				tmpIdPk.setAamAccount(keys[1]);
				
				tmp = appSer.findById(tmpIdPk);
			}
			
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(oper.equalsIgnoreCase("add")){
					AlsAccountMaster dupCheck = appSer.findById(idPk);
					if(dupCheck != null){
						errMsg += "Unable to add this record due to duplicate";
						addActionError(errMsg);
				        return "error_json";
					}
				}
				tmp.setAamAccountDesc(aamAccountDesc);
				tmp.setAamWhoLog(userInfo.getStateId().toString());
				tmp.setAamWhenLog(date);
				
				appSer.save(tmp);
			}else if(oper.equalsIgnoreCase("del")){
				if(appSer.isBudgYearAccUsed(tmp.getIdPk().getAsacBudgetYear(), tmp.getIdPk().getAamAccount())){
					errMsg += "This Account and Budget Year exists in ALS_SABHRS_ENTRIES, ALS_REFUND_SUMMARY, or ALS_SABHRS_ENTRIES_SUMMARY table, can not delete this record.";
					addActionError(errMsg);
			        return "error_json";
				}
				appSer.delete(tmp);
			}
		}  catch(Exception ex) {
			 if (ex.toString().contains("ORA-02292")){
				  errMsg += "Grid has child record(s) which would need to be deleted first!";
			  } else if (ex.toString().contains("ORA-02291")){
				  errMsg += "Cannot save the record without Program being defined first!";
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

	public AlsAccountMasterIdPk getIdPk() {
		return idPk;
	}

	public void setIdPk(AlsAccountMasterIdPk idPk) {
		this.idPk = idPk;
	}

	public String getAamAccountDesc() {
		return aamAccountDesc;
	}

	public void setAamAccountDesc(String aamAccountDesc) {
		this.aamAccountDesc = aamAccountDesc;
	}


	public Integer getBudgYear() {
		return budgYear;
	}


	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}

}
