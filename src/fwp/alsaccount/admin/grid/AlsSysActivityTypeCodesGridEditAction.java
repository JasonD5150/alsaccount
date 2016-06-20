package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsSysActivityTypeCodesAS;
import fwp.alsaccount.dao.admin.AlsSysActivityTypeCodes;
import fwp.security.user.UserDTO;


public class AlsSysActivityTypeCodesGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private String id;
	private String asacSystemActivityTypeCd;
	private String asatcDesc;
	
	
	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		try{
			AlsSysActivityTypeCodesAS appSer = new AlsSysActivityTypeCodesAS();
			AlsSysActivityTypeCodes tmp = null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsSysActivityTypeCodes();
				tmp.setAsacSystemActivityTypeCd(asacSystemActivityTypeCd);
				tmp.setAsatcWhoLog(userInfo.getStateId().toString());
				tmp.setAsatcWhenLog(date);
			} else {				
				tmp = appSer.findById(id);
				tmp.setAsatcWhoModi(userInfo.getStateId().toString());
				tmp.setAsatcWhenModi(date);
			}
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(oper.equalsIgnoreCase("add")){
					AlsSysActivityTypeCodes dupCheck = appSer.findById(id);
					if(dupCheck != null){
						errMsg += "Unable to add this record due to duplicate";
						addActionError(errMsg);
				        return "error_json";
					}
				}
				
				tmp.setAsatcDesc(asatcDesc);
				
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

	public String getAsacSystemActivityTypeCd() {
		return asacSystemActivityTypeCd;
	}

	public void setAsacSystemActivityTypeCd(String asacSystemActivityTypeCd) {
		this.asacSystemActivityTypeCd = asacSystemActivityTypeCd;
	}

	public String getAsatcDesc() {
		return asatcDesc;
	}

	public void setAsatcDesc(String asatcDesc) {
		this.asatcDesc = asatcDesc;
	}

}
