package fwp.alsaccount.admin.grid;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsNonAlsOrgControlAS;
import fwp.alsaccount.dao.admin.AlsNonAlsOrgControl;


public class AlsNonAlsOrgControlGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private String id;
	private String anaocOrg;
	private Integer apiProviderNo;
    private Integer budgYear;
    private String crDrCd;
    private String tempCd;
	
	
	public String execute() throws Exception{
		String errMsg="";			
		
		try{
			AlsNonAlsOrgControlAS appSer = new AlsNonAlsOrgControlAS();
			AlsNonAlsOrgControl tmp = null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsNonAlsOrgControl();
				tmp.setAnaocId(appSer.getNextSeqNo());
			} else {
				tmp = appSer.findById(Integer.parseInt(id));
			}
			
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				tmp.setAnaocOrg(anaocOrg);
				tmp.setApiProviderNo(apiProviderNo);
				tmp.setAnaocCrDrCd(crDrCd);
				tmp.setAnatBudgetYear(budgYear);
				tmp.setAnatCd(tempCd);
				
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
	

	public String getAnaocOrg() {
		return anaocOrg;
	}


	public void setAnaocOrg(String anaocOrg) {
		this.anaocOrg = anaocOrg;
	}


	public Integer getApiProviderNo() {
		return apiProviderNo;
	}


	public void setApiProviderNo(Integer apiProviderNo) {
		this.apiProviderNo = apiProviderNo;
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


	public String getCrDrCd() {
		return crDrCd;
	}


	public void setCrDrCd(String crDrCd) {
		this.crDrCd = crDrCd;
	}


	public String getTempCd() {
		return tempCd;
	}


	public void setTempCd(String tempCd) {
		this.tempCd = tempCd;
	}

	
}
