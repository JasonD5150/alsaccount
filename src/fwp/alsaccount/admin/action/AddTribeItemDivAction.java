package fwp.alsaccount.admin.action;


import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsTribeItemInfoAS;
import fwp.alsaccount.dao.admin.AlsTribeItemInfo;
import fwp.alsaccount.dao.admin.AlsTribeItemInfoIdPk;
import fwp.alsaccount.utils.Utils;
import fwp.security.user.UserDTO;


public class AddTribeItemDivAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String itemKey;
private String tribeId;
	
	

	/**
	 * 
	 */
	public AddTribeItemDivAction(){
		
	}
	
	public String input(){
		
		return SUCCESS;
	}
	
	public String execute(){
		String errMsg="";
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
		AlsTribeItemInfoAS itemAS = new AlsTribeItemInfoAS();
		List<AlsTribeItemInfo> toSave = new ArrayList<AlsTribeItemInfo>();
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		
		
		try{
		
		if (itemKey != null && !itemKey.equals("")) {
			for (String iqId : itemKey.split(",")) {
				
				AlsTribeItemInfo item = new AlsTribeItemInfo();
				AlsTribeItemInfoIdPk idPk = new AlsTribeItemInfoIdPk();
				
				
				List<String> parts = Utils.getParts(iqId, 9);		
			    Date aictUsagePeriodFrom = Utils.convertJavaDateToSqlDate(formatter.parse(parts.get(0)));
			    Date aictUsagePeriodTo = Utils.convertJavaDateToSqlDate(formatter.parse(parts.get(1)));
			    String aictItemTypeCd = parts.get(2);
			    
			    idPk.setAictItemTypeCd(aictItemTypeCd);
			    idPk.setAictUsagePeriodFrom(aictUsagePeriodFrom);
			    idPk.setAictUsagePeriodTo(aictUsagePeriodTo);
			    item.setAtiTribeCd(tribeId);
			    item.setIdPk(idPk);
			    
			    item.setAtiiWhoLog(userInfo.getStateId().toString());
			    item.setAtiiWhenLog(date);
			    
			    toSave.add(item);
	 
								
			}
			
			itemAS.save(toSave);
			
		} else {
			throw new Exception("Please select an item to to add.");
		}
		}catch(Exception ex) {
			if (ex.toString().contains("ORA-02292")) {
				errMsg += "Record has child record(s) which would need to be deleted first.";
			} else if (ex.toString().contains("ORA-02291")) {
				errMsg += "Cannot save the record with violation of constraint.";
			} else if (ex.toString().contains("ORA-00001")) {
				errMsg += "Unable to add this record due to duplicate record.";
			} else {
				errMsg += " " + ex.getMessage();
			}
			  
		    addActionError(errMsg);
	        return ERROR;
	  }	
		
		return "tribeBankItem_success";
	}
	
	
	
	
	
	 
	 
	 
	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	

	public String getTribeId() {
		return tribeId;
	}

	public void setTribeId(String tribeId) {
		this.tribeId = tribeId;
	}

	


}
