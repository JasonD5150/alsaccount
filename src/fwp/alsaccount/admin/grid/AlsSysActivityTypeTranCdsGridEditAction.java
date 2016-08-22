package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.admin.dao.AlsSysActivityTypeTranCds;
import fwp.als.hibernate.admin.dao.AlsSysActivityTypeTranCdsIdPk;
import fwp.alsaccount.appservice.admin.AlsSysActivityTypeTranCdsAS;
import fwp.security.user.UserDTO;


public class AlsSysActivityTypeTranCdsGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private AlsSysActivityTypeTranCdsIdPk idPk = new AlsSysActivityTypeTranCdsIdPk();
	private String id;
	private String asattcDesc;
	private String asacSystemActivityTypeCd;
	
	

	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		try{
			AlsSysActivityTypeTranCdsAS appSer = new AlsSysActivityTypeTranCdsAS();
			AlsSysActivityTypeTranCds tmp =null;
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsSysActivityTypeTranCds();
				idPk.setAsacSystemActivityTypeCd(asacSystemActivityTypeCd);
				tmp.setIdPk(idPk);
				tmp.setAsattcWhoLog(userInfo.getStateId().toString());
				tmp.setAsattcWhenLog(date); 
			} else {
				//Set current idPk
				AlsSysActivityTypeTranCdsIdPk tmpIdPk = new AlsSysActivityTypeTranCdsIdPk();
				String[] keys = this.id.split("_");
				tmpIdPk.setAsacSystemActivityTypeCd(keys[0]);
				tmpIdPk.setAsacTxnCd(keys[1]);
				tmp = appSer.findById(tmpIdPk);
				tmp.setAsattcWhoModi(userInfo.getStateId().toString());
				tmp.setAsattcWhenModi(date); 
			}
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(oper.equalsIgnoreCase("add")){
					AlsSysActivityTypeTranCds dupCheck = appSer.findById(idPk);
					if(dupCheck != null){
						errMsg += "Unable to add this record due to duplicate";
						addActionError(errMsg);
				        return "error_json";
					}
				}
				
				tmp.setAsattcDesc(asattcDesc);
				
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

	public AlsSysActivityTypeTranCdsIdPk getIdPk() {
		return idPk;
	}

	public void setIdPk(AlsSysActivityTypeTranCdsIdPk idPk) {
		this.idPk = idPk;
	}

	public String getAsattcDesc() {
		return asattcDesc;
	}

	public void setAsattcDesc(String asattcDesc) {
		this.asattcDesc = asattcDesc;
	}

	public String getAsacSystemActivityTypeCd() {
		return asacSystemActivityTypeCd;
	}

	public void setAsacSystemActivityTypeCd(String asacSystemActivityTypeCd) {
		this.asacSystemActivityTypeCd = asacSystemActivityTypeCd;
	}

}
