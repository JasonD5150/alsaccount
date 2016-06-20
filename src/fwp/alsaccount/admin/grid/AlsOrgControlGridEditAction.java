package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsOrgControlAS;
import fwp.alsaccount.dao.admin.AlsOrgControl;
import fwp.alsaccount.dao.admin.AlsOrgControlIdPk;
import fwp.security.user.UserDTO;


public class AlsOrgControlGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private AlsOrgControlIdPk idPk = new AlsOrgControlIdPk();
	private String id;
	private Integer budgYear;
	private String aocOrg;


	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		try{
			AlsOrgControlAS appSer = new AlsOrgControlAS();
			AlsOrgControl tmp = null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsOrgControl();
				idPk.setAsacBudgetYear(budgYear);
				tmp.setIdPk(idPk);
				//TODO need to remove this logic when the triggers and correct audit columns are added to the db	
				tmp.setAocWhoLog(userInfo.getStateId().toString());
				tmp.setAocWhenLog(date);
				//********************************************************************
			} else {
				//Set current idPk
				AlsOrgControlIdPk tmpIdPk = new AlsOrgControlIdPk();
				String[] keys = this.id.split("_");
				tmpIdPk.setAsacBudgetYear(Integer.parseInt(keys[0]));
				tmpIdPk.setAaccAccCd(keys[1]);
				tmpIdPk.setApiProviderNo(Integer.parseInt(keys[2]));
				
				tmp = appSer.findById(tmpIdPk);
			}
			
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(oper.equalsIgnoreCase("add")){
					if(appSer.isDuplicateEntry(tmp.getIdPk())){
						addActionError("Unable to add this record due to duplicate");
					}
				}else{
					idPk.setAsacBudgetYear(budgYear);
					if(appSer.isDuplicateAccCdProviderOrg(idPk, Integer.parseInt(aocOrg))){
						addActionError("Unable to add this record due to duplicate Test");
					}
				}
				if(!appSer.isValidProvider(tmp.getIdPk().getApiProviderNo())){
					addActionError("Issuing Provider Number does not exist in Provider Info table.");
				}
				if(!appSer.isValidBudgYearAccCd(tmp.getIdPk().getAsacBudgetYear(), tmp.getIdPk().getAaccAccCd())){
					addActionError("Budget Year and Accounting Code does not exist in accounting code control table.");
				}
				if (this.hasActionErrors()) {
					return "error_json";
				}
				
				tmp.setAocOrg(aocOrg);

				appSer.save(tmp);
			}else if(oper.equalsIgnoreCase("del")){
				appSer.delete(tmp);
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

	public AlsOrgControlIdPk getIdPk() {
		return idPk;
	}

	public void setIdPk(AlsOrgControlIdPk idPk) {
		this.idPk = idPk;
	}

	public Integer getBudgYear() {
		return budgYear;
	}


	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}


	public String getAocOrg() {
		return aocOrg;
	}


	public void setAocOrg(String aocOrg) {
		this.aocOrg = aocOrg;
	}

	
}
