package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsTransactionGroupAS;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGroup;
import fwp.security.user.UserDTO;


public class AlsTransactionGroupGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private String id;
	private Integer atgTransactionCd;
	private String atgBusinessProcessType;
	private String atgTransactionDesc;
	private String atgIdentifierString;
	private String atgInterfaceFile;
	

	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		try{
			AlsTransactionGroupAS appSer = new AlsTransactionGroupAS();
			AlsTransactionGroup tmp = null;

			if("0".equals(atgBusinessProcessType)){
				addActionError("Business Process Type: Field is required");
			}else if ("0".equals(atgInterfaceFile)){
				addActionError("Interface File: Field is required");
			}
			if (this.hasActionErrors()) {
				return "error_json";
			}
			
			if (oper.equalsIgnoreCase("add")) {
				tmp = new AlsTransactionGroup();
				tmp.setAtgTransactionCd(appSer.getNextSeqNo());
			} else {				
				tmp = appSer.findById(Integer.parseInt(id));
			}
			
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				tmp.setAtgBusinessProcessType(atgBusinessProcessType);
				tmp.setAtgIdentifierString(atgIdentifierString);
				tmp.setAtgInterfaceFile(atgInterfaceFile);
				tmp.setAtgTransactionDesc(atgTransactionDesc);
				//TODO need to remove this logic when the triggers and correct audit columns are added to the db	
				tmp.setAtgWhoLog(userInfo.getStateId().toString());
				tmp.setAtgWhenLog(date);
				//********************************************************************
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
	
	public void validate(){
		
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


	/**
	 * @return the atgTransactionCd
	 */
	public Integer getAtgTransactionCd() {
		return atgTransactionCd;
	}


	/**
	 * @param atgTransactionCd the atgTransactionCd to set
	 */
	public void setAtgTransactionCd(Integer atgTransactionCd) {
		this.atgTransactionCd = atgTransactionCd;
	}


	/**
	 * @return the atgBusinessProcessType
	 */
	public String getAtgBusinessProcessType() {
		return atgBusinessProcessType;
	}


	/**
	 * @param atgBusinessProcessType the atgBusinessProcessType to set
	 */
	public void setAtgBusinessProcessType(String atgBusinessProcessType) {
		this.atgBusinessProcessType = atgBusinessProcessType;
	}


	/**
	 * @return the atgTransactionDesc
	 */
	public String getAtgTransactionDesc() {
		return atgTransactionDesc;
	}


	/**
	 * @param atgTransactionDesc the atgTransactionDesc to set
	 */
	public void setAtgTransactionDesc(String atgTransactionDesc) {
		this.atgTransactionDesc = atgTransactionDesc;
	}


	/**
	 * @return the atgIdentifierString
	 */
	public String getAtgIdentifierString() {
		return atgIdentifierString;
	}


	/**
	 * @param atgIdentifierString the atgIdentifierString to set
	 */
	public void setAtgIdentifierString(String atgIdentifierString) {
		this.atgIdentifierString = atgIdentifierString;
	}


	/**
	 * @return the atgInterfaceFile
	 */
	public String getAtgInterfaceFile() {
		return atgInterfaceFile;
	}


	/**
	 * @param atgInterfaceFile the atgInterfaceFile to set
	 */
	public void setAtgInterfaceFile(String atgInterfaceFile) {
		this.atgInterfaceFile = atgInterfaceFile;
	}


}
