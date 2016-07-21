package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsBankCodeAS;
import fwp.alsaccount.appservice.admin.AlsNonAlsTemplateAS;
import fwp.alsaccount.dao.admin.AlsBankCode;
import fwp.alsaccount.dao.admin.AlsNonAlsTemplate;
import fwp.alsaccount.dao.admin.AlsNonAlsTemplateIdPk;
import fwp.alsaccount.utils.Utils;
import fwp.security.user.UserDTO;


public class AlsBankCodeGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;

	private String id;
	private String abcBankCd;
	private String abcAccountNo;
	private String abcCompanyId;
	private String abcBankNm;
	private String azcZipCd;
	private String azcCityNm;
	private String abcWhoLog;
	private Timestamp abcWhenLog;
	private String abcCreatePersonid;
	private String abcActive;


	public String execute() throws Exception{
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";

		try{
			if(validation()){

				AlsBankCodeAS appSer = new AlsBankCodeAS();
				AlsBankCode tmp = null;

				if (oper.equalsIgnoreCase("add")) {
					tmp = new AlsBankCode();

					tmp.setAbcBankCd(abcBankCd);

				} else {

					String idString = id.toString();
					tmp = appSer.findById(idString);
					abcWhenLog = tmp.getAbcWhenLog();
					abcWhoLog = tmp.getAbcWhoLog();
				}

				if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){

					//validate();

					if (azcZipCd.length() != 5)
					{

						addActionError("Pease enter a valid zip code.");

					}

					if(oper.equalsIgnoreCase("add")){
						if(appSer.isDuplicateEntry(abcBankCd)){
							addActionError("Unable to add this record due to duplicate Bank Code.");
						}
					}
					if (this.hasActionErrors() || this.hasFieldErrors()) {
						return "error_json";
					}

					azcCityNm = "HELENA";
					abcCreatePersonid = userInfo.getDisplayName();

					tmp.setAbcAccountNo(abcAccountNo);
					tmp.setAbcActive(abcActive);
					tmp.setAbcBankCd(abcBankCd);
					tmp.setAbcBankNm(abcBankNm);
					tmp.setAbcCompanyId(abcCompanyId);
					tmp.setAbcCreatePersonid(abcCreatePersonid);
					tmp.setAbcWhenLog(abcWhenLog);
					tmp.setAbcWhoLog(abcWhoLog);
					tmp.setAzcCityNm(azcCityNm);
					tmp.setAzcZipCd(azcZipCd);

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

	private boolean validation()
	{


		if (azcZipCd.length() != 5)
		{
			addActionError("Zip code needs to be 5 characters.");
		}
		
		
		
		if(hasActionErrors()){

			return false;	
		}
		else{
			return true;
		}

	}


	public String getAbcBankCd() {
		return abcBankCd;
	}


	public void setAbcBankCd(String abcBankCd) {
		this.abcBankCd = abcBankCd;
	}


	public String getAbcAccountNo() {
		return abcAccountNo;
	}


	public void setAbcAccountNo(String abcAccountNo) {
		this.abcAccountNo = abcAccountNo;
	}


	public String getAbcCompanyId() {
		return abcCompanyId;
	}


	public void setAbcCompanyId(String abcCompanyId) {
		this.abcCompanyId = abcCompanyId;
	}


	public String getAbcBankNm() {
		return abcBankNm;
	}


	public void setAbcBankNm(String abcBankNm) {
		this.abcBankNm = abcBankNm;
	}


	public String getAzcZipCd() {
		return azcZipCd;
	}


	public void setAzcZipCd(String azcZipCd) {
		this.azcZipCd = azcZipCd;
	}


	public String getAzcCityNm() {
		return azcCityNm;
	}


	public void setAzcCityNm(String azcCityNm) {
		this.azcCityNm = azcCityNm;
	}


	public String getAbcWhoLog() {
		return abcWhoLog;
	}


	public void setAbcWhoLog(String abcWhoLog) {
		this.abcWhoLog = abcWhoLog;
	}


	public Timestamp getAbcWhenLog() {
		return abcWhenLog;
	}


	public void setAbcWhenLog(Timestamp abcWhenLog) {
		this.abcWhenLog = abcWhenLog;
	}


	public String getAbcCreatePersonid() {
		return abcCreatePersonid;
	}


	public void setAbcCreatePersonid(String abcCreatePersonid) {
		this.abcCreatePersonid = abcCreatePersonid;
	}


	public String getAbcActive() {
		return abcActive;
	}


	public void setAbcActive(String abcActive) {
		this.abcActive = abcActive;
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


}
