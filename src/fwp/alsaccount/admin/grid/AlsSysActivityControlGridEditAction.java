package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsSysActivityControlAS;
import fwp.alsaccount.dao.admin.AlsSysActivityControl;
import fwp.alsaccount.dao.admin.AlsSysActivityControlIdPk;
import fwp.security.user.UserDTO;


public class AlsSysActivityControlGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private AlsSysActivityControlIdPk idPk = new AlsSysActivityControlIdPk();
	private String asacSysActivityTypeCdDesc;
	private Integer asacProgram;
	private String id;
	private Integer budgYear;
	
	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		try{
			AlsSysActivityControlAS appSer = new AlsSysActivityControlAS();
			AlsSysActivityControl tmp =null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsSysActivityControl();
				idPk.setAsacBudgetYear(budgYear);
				idPk.setAsacSystemActivityTypeCd(idPk.getAsacSystemActivityTypeCd().toUpperCase());
				tmp.setIdPk(idPk);
			} else {
				//Set current idPk
				AlsSysActivityControlIdPk tmpIdPk = new AlsSysActivityControlIdPk();
				String[] keys = this.id.split("_");
				tmpIdPk.setAsacBudgetYear(Integer.parseInt(keys[0]));
				tmpIdPk.setAsacSystemActivityTypeCd(keys[1]);
				tmpIdPk.setAsacTxnCd(keys[2]);
				
				tmp = appSer.findById(tmpIdPk);
			}
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(oper.equalsIgnoreCase("add")){
					AlsSysActivityControl dupCheck = appSer.findById(idPk);
					if(dupCheck != null){
						errMsg += "Unable to add this record due to duplicate";
						addActionError(errMsg);
				        return "error_json";
					}
				}
				tmp.setAsacProgram(budgYear);
				tmp.setAsacSysActivityTypeCdDesc(asacSysActivityTypeCdDesc);
				tmp.setAsacWhoLog(userInfo.getStateId().toString());
				tmp.setAsacWhenLog(date);
				
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

	public AlsSysActivityControlIdPk getIdPk() {
		return idPk;
	}

	public void setIdPk(AlsSysActivityControlIdPk idPk) {
		this.idPk = idPk;
	}

	public Integer getAsacProgram() {
		return asacProgram;
	}

	public void setAsacProgram(Integer asacProgram) {
		this.asacProgram = asacProgram;
	}

	public String getAsacSysActivityTypeCdDesc() {
		return asacSysActivityTypeCdDesc;
	}

	public void setAsacSysActivityTypeCdDesc(String asacSysActivityTypeCdDesc) {
		this.asacSysActivityTypeCdDesc = asacSysActivityTypeCdDesc;
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

}
