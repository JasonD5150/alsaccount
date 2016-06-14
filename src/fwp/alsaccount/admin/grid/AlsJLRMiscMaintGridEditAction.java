package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.admin.appservice.AlsMiscAS;
import fwp.alsaccount.hibernate.dao.AlsMisc;
import fwp.security.user.UserDTO;


public class AlsJLRMiscMaintGridEditAction extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private String oper;
	private HttpServletRequest request;	
	
	private String id;
	private String amValDesc;
	private String amDesc2;
	private String amParVal;
	
	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());

		String errMsg="";			
		
		try{
			AlsMiscAS appSer = new AlsMiscAS();
			AlsMisc tmp = null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsMisc();
				tmp.setAmKey1("JOURNAL_LINE_REFERENCE");
				tmp.setAmKey2("NON ALS "+amParVal);
				tmp.setAmDesc1("PVDesc=8 chars max. Acct 02504. Used by U9070, U7935b, U0420, U0421, U7940, D0311, R0310, R0320.  Up to 3 item types can be maintained in Key2.  Parm value corresponds to App Type Cd.");
				//TODO need to remove this logic when the triggers and correct audit columns are added to the db	
				tmp.setAmWhoLog(userInfo.getStateId().toString());
				tmp.setAmWhenLog(date);
				//********************************************************************
			} else {				
				tmp = appSer.findById(Integer.parseInt(id));
			}
			
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(oper.equalsIgnoreCase("add")){
					if(appSer.isDuplicateJLRKey2(amParVal)){
						addActionError("Parameter value (JLR Seq No) already exists in the Miscellaneous table.");
					}
					if(appSer.isDuplicateJLRNonAls(amParVal)){
						addActionError("JLR Non-ALS Parameter Value (JLR Seq No) already exists as a JLR Parameter Value.");
					}
					if (this.hasActionErrors()) {
						return "error_json";
					}
				}
				
				tmp.setAmValDesc(amValDesc);
				tmp.setAmDesc2(amDesc2);
				tmp.setAmParVal(amParVal);
				
				appSer.save(tmp);
			}else if(oper.equalsIgnoreCase("del")){
				appSer.delete(tmp);
			}
		}  catch(Exception ex) {
			 if (ex.toString().contains("ORA-02292")){
				  errMsg += "Grid has child record(s) which would need to be deleted first!";
			  } else if (ex.toString().contains("ORA-02291")){
				  errMsg += "Cannot save the record without Account being set up in Account Master first.";
			  } else if (ex.toString().contains("ORA-00001")){
				  errMsg += "Unable to add this record due to duplicate.";
			  }	else if (ex.toString().contains("ORA-12899")){
				  errMsg += "A value entered is too large for the database column.";
			  } else {
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


	public String getAmValDesc() {
		return amValDesc;
	}


	public void setAmValDesc(String amValDesc) {
		this.amValDesc = amValDesc;
	}


	public String getAmDesc2() {
		return amDesc2;
	}


	public void setAmDesc2(String amDesc2) {
		this.amDesc2 = amDesc2;
	}


	public String getAmParVal() {
		return amParVal;
	}


	public void setAmParVal(String amParVal) {
		this.amParVal = amParVal;
	}

	
}
