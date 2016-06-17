package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsNonAlsTemplateAS;
import fwp.alsaccount.dao.admin.AlsNonAlsTemplate;
import fwp.alsaccount.dao.admin.AlsNonAlsTemplateIdPk;
import fwp.alsaccount.utils.HibHelpers;
import fwp.security.user.UserDTO;


public class AlsNonAlsTemplateGridEditAction extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private String oper;
	private HttpServletRequest request;	
	
	private AlsNonAlsTemplateIdPk idPk = new AlsNonAlsTemplateIdPk();
	private String id;
	private String anatDesc;
    private String anatBusinessUnit;
    private String anatDrAccount;
    private String anatCrAccount;
    private String anatFund;
    private String anatDrOrg;
	private String anatCrOrg;
    private String anatDrSubclass;
    private String anatCrSubclass;
    private Integer anatProgramYear;
    private String anatDrProjectGrant;
    private String anatCrProjectGrant;
    private Integer anatDrJournalLineRefr;
    private Integer anatCrJournalLineRefr;
    private String anatDrLineDesc;
    private String anatCrLineDesc;
    private Integer budgYear;
    private String anatDrJournalLineRefrDesc;
    private String anatCrJournalLineRefrDesc;


	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		HibHelpers hh = new HibHelpers();
		String errMsg="";			
		
		try{
			AlsNonAlsTemplateAS appSer = new AlsNonAlsTemplateAS();
			AlsNonAlsTemplate tmp = null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsNonAlsTemplate();
				idPk.setAnatBudgetYear(budgYear);
				tmp.setIdPk(idPk);
				
				//TODO need to remove this logic when the triggers and correct audit columns are added to the db	
				tmp.setAnatWhoLog(userInfo.getStateId().toString());
				tmp.setAnatWhenLog(date);
				//********************************************************************
			} else {
				//Set current idPk
				AlsNonAlsTemplateIdPk tmpIdPk = new AlsNonAlsTemplateIdPk();
				String[] keys = this.id.split("_");
				tmpIdPk.setAnatBudgetYear(Integer.parseInt(keys[0]));
				tmpIdPk.setAnatCd(keys[1]);
				
				tmp = appSer.findById(tmpIdPk);
			}
			
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(oper.equalsIgnoreCase("add")){
					if(appSer.isDuplicateEntry(idPk)){
						addActionError("Unable to add this record due to duplicate Template Code.");
					}else if(appSer.isDuplicateDesc(budgYear, anatDesc)){
						addActionError("Unable to add this record due to duplicate Template Description.");
					}
				}
				if (this.hasActionErrors()) {
					return "error_json";
				}
				tmp.setAnatDesc(anatDesc);
				tmp.setAnatBusinessUnit("52010");
				tmp.setAnatFund(anatFund);
				tmp.setAnatProgramYear(budgYear);
				tmp.setAnatCrAccount(anatCrAccount);
				tmp.setAnatCrLineDesc(anatCrLineDesc);
				tmp.setAnatCrOrg(anatCrOrg);
				if(appSer.getJlrParVal(anatCrJournalLineRefrDesc) != 0){
					tmp.setAnatCrJournalLineRefr(appSer.getJlrParVal(anatCrJournalLineRefrDesc));
				}
				tmp.setAnatCrProjectGrant(anatCrProjectGrant);
				tmp.setAnatCrSubclass(anatCrSubclass);
				tmp.setAnatDrAccount(anatDrAccount);
				tmp.setAnatDrLineDesc(anatDrLineDesc);
				tmp.setAnatDrOrg(anatDrOrg);
				if(appSer.getJlrParVal(anatDrJournalLineRefrDesc) != 0){
					tmp.setAnatDrJournalLineRefr(appSer.getJlrParVal(anatDrJournalLineRefrDesc));
				}
				tmp.setAnatDrProjectGrant(anatDrProjectGrant);
				tmp.setAnatDrSubclass(anatDrSubclass);

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

	public String getAnatDesc() {
		return anatDesc;
	}


	public void setAnatDesc(String anatDesc) {
		this.anatDesc = anatDesc;
	}


	public String getAnatBusinessUnit() {
		return anatBusinessUnit;
	}


	public void setAnatBusinessUnit(String anatBusinessUnit) {
		this.anatBusinessUnit = anatBusinessUnit;
	}


	public String getAnatDrAccount() {
		return anatDrAccount;
	}


	public void setAnatDrAccount(String anatDrAccount) {
		this.anatDrAccount = anatDrAccount;
	}


	public String getAnatCrAccount() {
		return anatCrAccount;
	}


	public void setAnatCrAccount(String anatCrAccount) {
		this.anatCrAccount = anatCrAccount;
	}


	public String getAnatFund() {
		return anatFund;
	}


	public void setAnatFund(String anatFund) {
		this.anatFund = anatFund;
	}


	public String getAnatDrOrg() {
		return anatDrOrg;
	}


	public void setAnatDrOrg(String anatDrOrg) {
		this.anatDrOrg = anatDrOrg;
	}


	public String getAnatCrOrg() {
		return anatCrOrg;
	}


	public void setAnatCrOrg(String anatCrOrg) {
		this.anatCrOrg = anatCrOrg;
	}


	public String getAnatDrSubclass() {
		return anatDrSubclass;
	}


	public void setAnatDrSubclass(String anatDrSubclass) {
		this.anatDrSubclass = anatDrSubclass;
	}


	public String getAnatCrSubclass() {
		return anatCrSubclass;
	}


	public void setAnatCrSubclass(String anatCrSubclass) {
		this.anatCrSubclass = anatCrSubclass;
	}


	public Integer getAnatProgramYear() {
		return anatProgramYear;
	}


	public void setAnatProgramYear(Integer anatProgramYear) {
		this.anatProgramYear = anatProgramYear;
	}


	public String getAnatDrProjectGrant() {
		return anatDrProjectGrant;
	}


	public void setAnatDrProjectGrant(String anatDrProjectGrant) {
		this.anatDrProjectGrant = anatDrProjectGrant;
	}


	public String getAnatCrProjectGrant() {
		return anatCrProjectGrant;
	}


	public void setAnatCrProjectGrant(String anatCrProjectGrant) {
		this.anatCrProjectGrant = anatCrProjectGrant;
	}


	public Integer getAnatDrJournalLineRefr() {
		return anatDrJournalLineRefr;
	}


	public void setAnatDrJournalLineRefr(Integer anatDrJournalLineRefr) {
		this.anatDrJournalLineRefr = anatDrJournalLineRefr;
	}


	public Integer getAnatCrJournalLineRefr() {
		return anatCrJournalLineRefr;
	}


	public void setAnatCrJournalLineRefr(Integer anatCrJournalLineRefr) {
		this.anatCrJournalLineRefr = anatCrJournalLineRefr;
	}


	public String getAnatDrLineDesc() {
		return anatDrLineDesc;
	}


	public void setAnatDrLineDesc(String anatDrLineDesc) {
		this.anatDrLineDesc = anatDrLineDesc;
	}


	public String getAnatCrLineDesc() {
		return anatCrLineDesc;
	}


	public void setAnatCrLineDesc(String anatCrLineDesc) {
		this.anatCrLineDesc = anatCrLineDesc;
	}


	public void setIdPk(AlsNonAlsTemplateIdPk idPk) {
		this.idPk = idPk;
	}

	public Integer getBudgYear() {
		return budgYear;
	}


	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}


	public String getAnatDrJournalLineRefrDesc() {
		return anatDrJournalLineRefrDesc;
	}


	public void setAnatDrJournalLineRefrDesc(String anatDrJournalLineRefrDesc) {
		this.anatDrJournalLineRefrDesc = anatDrJournalLineRefrDesc;
	}


	public String getAnatCrJournalLineRefrDesc() {
		return anatCrJournalLineRefrDesc;
	}


	public void setAnatCrJournalLineRefrDesc(String anatCrJournalLineRefrDesc) {
		this.anatCrJournalLineRefrDesc = anatCrJournalLineRefrDesc;
	}
}
