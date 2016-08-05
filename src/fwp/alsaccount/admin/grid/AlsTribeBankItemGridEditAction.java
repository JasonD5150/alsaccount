package fwp.alsaccount.admin.grid;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsTribeItemInfoAS;
import fwp.alsaccount.dao.admin.AlsTribeItemInfo;
import fwp.alsaccount.dao.admin.AlsTribeItemInfoIdPk;
import fwp.alsaccount.utils.Utils;


public class AlsTribeBankItemGridEditAction extends ActionSupport{
	
	
	private static final long serialVersionUID = 5310775194459329314L;

	
	

	private String oper;

	private String id;

 


public String execute() throws Exception{

		
		String errMsg="";
		AlsTribeItemInfoAS appSer = new AlsTribeItemInfoAS();
		AlsTribeItemInfo tmp = null;
		AlsTribeItemInfoIdPk idPk= new AlsTribeItemInfoIdPk();
		
		Date aictUsagePeriodFrom;
	    Date aictUsagePeriodTo;
	    String aictItemTypeCd;
	    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
		

		try{
	
			if(validation()){
				List<String> parts = Utils.getParts(id, 9);
				
				aictUsagePeriodFrom = Utils.convertJavaDateToSqlDate(formatter.parse(parts.get(0)));
				aictUsagePeriodTo = Utils.convertJavaDateToSqlDate(formatter.parse(parts.get(1)));
				aictItemTypeCd = parts.get(2);
				
				 if(oper.equalsIgnoreCase("del")){
					 idPk.setAictItemTypeCd(aictItemTypeCd);
						idPk.setAictUsagePeriodFrom(aictUsagePeriodFrom);
						idPk.setAictUsagePeriodTo(aictUsagePeriodTo);
					tmp = appSer.findById(idPk);
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

	
	




}
