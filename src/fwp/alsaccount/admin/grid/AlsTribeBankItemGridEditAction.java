package fwp.alsaccount.admin.grid;

import java.sql.Date;
import java.sql.Timestamp;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsTribeInfoAS;
import fwp.alsaccount.dao.admin.AlsTribeInfo;
import fwp.security.user.UserDTO;


public class AlsTribeBankItemGridEditAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5310775194459329314L;

	/**
	 * 
	 */
	

	private String oper;

	private String id;
	
	private Date aictUsagePeriodFrom;
    private Date aictUsagePeriodTo;
    private String aictItemTypeCd;
    private String aitTypeDesc;
    private String tribeID;
    
	

public String input() throws Exception{
	
	return this.execute();
	
}

public String getJSON() throws Exception{
	
	return this.execute();
}
	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		
		String errMsg="";

		try{
			
			
			
			
			if(validation()){

				AlsTribeInfoAS appSer = new AlsTribeInfoAS();
				AlsTribeInfo tmp = null;
				
				

				if (oper.equalsIgnoreCase("add")) {
					tmp = new AlsTribeInfo();

					/*tmp.setAtiTribeCd(atiTribeCd);*/

				} else {

					String idString = id.toString();
					tmp = appSer.findById(idString);
					
				}

				if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
					
					

					if(oper.equalsIgnoreCase("add")){
//						if(appSer.isDuplicateEntry(atiTribeCd)){
//							addActionError("Unable to add this record due to duplicate Tribe Bank Code.");
//						}
					}
					if (this.hasActionErrors() || this.hasFieldErrors()) {
						return "error_json";
					}

					
					
					
					//abcCreatePersonid = userInfo.getDisplayName();
					
					tmp.setAtiWhoLog(userInfo.getStateId().toString());
					tmp.setAtiWhenLog(date);

				/*	tmp.setAbcBankCd(abcBankCd);
					tmp.setAtiDirectorNm(atiDirectorNm);
					tmp.setAtiTribeAcctBankNm(atiTribeAcctBankNm);
					tmp.setAtiTribeAcctNo(atiTribeAcctNo);
					tmp.setAtiTribeAcctRoutingNo(atiTribeAcctRoutingNo);
					tmp.setAtiTribeCd(atiTribeCd);
					tmp.setAtiTribeNm(atiTribeNm);
					*/

					appSer.save(tmp);
				}else if(oper.equalsIgnoreCase("del")){
					appSer.delete(tmp);
				}
			}else{
				return "error_json";
			}

		}

		catch(Exception ex) {
			if (ex.toString().contains("ORA-02292")){
				errMsg += "Bank Code has child record(s) which would need to be deleted first.";
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

	private boolean validation()
	{
		//doesn't need to validate if deleting
		/*if(!oper.equalsIgnoreCase("del"))
		{
        
        GenZipCodesAS zipCodeAS = new GenZipCodesAS();
		
		
       
        
		if (azcZipCd.length() != 5)
		{
			addActionError("Zip code needs to be 5 characters.");
		}
		
		else if ( zipCodeAS.findByZipCode(azcZipCd).isEmpty() ){
			addActionError("Please enter a valid zip code.");
	        }
		
		
		
	
		if(hasActionErrors()){

			return false;	
		}
		else{
			return true;
		}
		}*/
		
		return true;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public Date getAictUsagePeriodFrom() {
		return aictUsagePeriodFrom;
	}

	public void setAictUsagePeriodFrom(Date aictUsagePeriodFrom) {
		this.aictUsagePeriodFrom = aictUsagePeriodFrom;
	}

	public Date getAictUsagePeriodTo() {
		return aictUsagePeriodTo;
	}

	public void setAictUsagePeriodTo(Date aictUsagePeriodTo) {
		this.aictUsagePeriodTo = aictUsagePeriodTo;
	}

	public String getAictItemTypeCd() {
		return aictItemTypeCd;
	}

	public void setAictItemTypeCd(String aictItemTypeCd) {
		this.aictItemTypeCd = aictItemTypeCd;
	}

	public String getAitTypeDesc() {
		return aitTypeDesc;
	}

	public void setAitTypeDesc(String aitTypeDesc) {
		this.aitTypeDesc = aitTypeDesc;
	}

	public String getTribeID() {
		return tribeID;
	}

	public void setTribeID(String tribeID) {
		this.tribeID = tribeID;
	}

	




}
