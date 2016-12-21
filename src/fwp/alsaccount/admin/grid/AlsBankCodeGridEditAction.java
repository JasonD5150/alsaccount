package fwp.alsaccount.admin.grid;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsBankCodeAS;
import fwp.alsaccount.dao.admin.AlsBankCode;
import fwp.gen.appservice.GenZipCodesAS;
import fwp.gen.hibernate.dao.GenZipCodes;
import fwp.security.utils.FwpUserUtils;

public class AlsBankCodeGridEditAction extends ActionSupport {
	
	/**
	 * cf0006
	 */
	private static final long serialVersionUID = -6297889711565843419L;

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
	private String abcAvblToAllProv;

	public String execute() throws Exception {
		String userInfoString = String.valueOf(FwpUserUtils.getPersonId());
		String errMsg = "";

		try {

			if (validation()) {

				AlsBankCodeAS appSer = new AlsBankCodeAS();
				AlsBankCode bankCodeToSave = new AlsBankCode();
				GenZipCodesAS zipCodeAS = new GenZipCodesAS();
				List<GenZipCodes> zipCode = new ArrayList<GenZipCodes>();

				if (oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")) {

					if (oper.equalsIgnoreCase("add")) {	
						bankCodeToSave.setAbcBankCd(abcBankCd);
						
						if (appSer.isDuplicateEntry(abcBankCd)) {
							addActionError("Unable to add this record due to duplicate Bank Code.");
						}
					} else {
						String idString = id.toString();
						bankCodeToSave = appSer.findById(idString);
						if(bankCodeToSave.getAbcCreatePersonid() == null){
							bankCodeToSave.setAbcCreatePersonid("Unknown");
						}
					}
					bankCodeToSave.setAbcCreatePersonid(userInfoString);
					zipCode = zipCodeAS.findByZipCode(azcZipCd);
					bankCodeToSave.setAbcAccountNo(abcAccountNo);
					bankCodeToSave.setAbcActive(abcActive);
					bankCodeToSave.setAbcAvblToAllProv(abcAvblToAllProv);
					bankCodeToSave.setAbcBankCd(abcBankCd);
					bankCodeToSave.setAbcBankNm(abcBankNm);
					bankCodeToSave.setAbcCompanyId(abcCompanyId);
					bankCodeToSave.setAzcCityNm(zipCode.get(0).getCity());
					bankCodeToSave.setAzcZipCd(azcZipCd);
					
					if (this.hasActionErrors() || this.hasFieldErrors()) {
						return "error_json";
					}

					appSer.save(bankCodeToSave);
					
				} else if (oper.equalsIgnoreCase("del")) {
					appSer.delete(bankCodeToSave);
				}
			} else {
				return "error_json";
			}

		}

		catch (Exception ex) {
			if (ex.toString().contains("ORA-02292")) {
				errMsg += "Bank Code has child record(s) which would need to be deleted first.";
			} else if (ex.toString().contains("ORA-02291")) {
				errMsg += "Parent record not found.";
			} else if (ex.toString().contains("ORA-00001")) {
				errMsg += "Unable to add this record due to duplicate.";
			} else {
				errMsg += " " + ex.toString();
			}

			addActionError(errMsg);
			return "error_json";
		}
		return SUCCESS;
	}

	private boolean validation() {
		// doesn't need to validate if deleting
		if (!oper.equalsIgnoreCase("del")) {

			GenZipCodesAS zipCodeAS = new GenZipCodesAS();

			if (azcZipCd.length() != 5) {
				addActionError("Zip code needs to be 5 characters.");
			}

			else if (zipCodeAS.findByZipCode(azcZipCd).isEmpty()) {
				addActionError("Please enter a valid zip code.");
			}

			if (hasActionErrors()) {

				return false;
			} else {
				return true;
			}
		}

		return true;

	}

	/**
	 * @return
	 */
	public String getAbcBankCd() {
		return abcBankCd;
	}

	/**
	 * @param abcBankCd
	 */
	public void setAbcBankCd(String abcBankCd) {
		this.abcBankCd = abcBankCd;
	}

	/**
	 * @return
	 */
	public String getAbcAccountNo() {
		return abcAccountNo;
	}

	/**
	 * @param abcAccountNo
	 */
	public void setAbcAccountNo(String abcAccountNo) {
		this.abcAccountNo = abcAccountNo;
	}

	/**
	 * @return
	 */
	public String getAbcCompanyId() {
		return abcCompanyId;
	}

	/**
	 * @param abcCompanyId
	 */
	public void setAbcCompanyId(String abcCompanyId) {
		this.abcCompanyId = abcCompanyId;
	}

	/**
	 * @return
	 */
	public String getAbcBankNm() {
		return abcBankNm;
	}

	/**
	 * @param abcBankNm
	 */
	public void setAbcBankNm(String abcBankNm) {
		this.abcBankNm = abcBankNm;
	}

	/**
	 * @return
	 */
	public String getAzcZipCd() {
		return azcZipCd;
	}

	/**
	 * @param azcZipCd
	 */
	public void setAzcZipCd(String azcZipCd) {
		this.azcZipCd = azcZipCd;
	}

	/**
	 * @return
	 */
	public String getAzcCityNm() {
		return azcCityNm;
	}

	/**
	 * @param azcCityNm
	 */
	public void setAzcCityNm(String azcCityNm) {
		this.azcCityNm = azcCityNm;
	}

	/**
	 * @return
	 */
	public String getAbcWhoLog() {
		return abcWhoLog;
	}

	/**
	 * @param abcWhoLog
	 */
	public void setAbcWhoLog(String abcWhoLog) {
		this.abcWhoLog = abcWhoLog;
	}

	/**
	 * @return
	 */
	public Timestamp getAbcWhenLog() {
		return abcWhenLog;
	}

	/**
	 * @param abcWhenLog
	 */
	public void setAbcWhenLog(Timestamp abcWhenLog) {
		this.abcWhenLog = abcWhenLog;
	}

	/**
	 * @return
	 */
	public String getAbcCreatePersonid() {
		return abcCreatePersonid;
	}

	/**
	 * @param abcCreatePersonid
	 */
	public void setAbcCreatePersonid(String abcCreatePersonid) {
		this.abcCreatePersonid = abcCreatePersonid;
	}

	/**
	 * @return
	 */
	public String getAbcActive() {
		return abcActive;
	}

	/**
	 * @param abcActive
	 */
	public void setAbcActive(String abcActive) {
		this.abcActive = abcActive;
	}

	/**
	 * @return
	 */
	public String getOper() {
		return oper;
	}

	/**
	 * @param oper
	 */
	public void setOper(String oper) {
		this.oper = oper;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getAbcAvblToAllProv() {
		return abcAvblToAllProv;
	}

	/**
	 * @param abcAvblToAllProv
	 */
	public void setAbcAvblToAllProv(String abcAvblToAllProv) {
		this.abcAvblToAllProv = abcAvblToAllProv;
	}

}
