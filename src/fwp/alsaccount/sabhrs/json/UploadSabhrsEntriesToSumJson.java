package fwp.alsaccount.sabhrs.json;

import java.sql.Date;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.hibernate.utils.ProcRtrn;
import fwp.alsaccount.utils.HibHelpers;

import javax.servlet.http.HttpServletRequest;

public class UploadSabhrsEntriesToSumJson extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;

	private ProcRtrn rtrn;


	public UploadSabhrsEntriesToSumJson() {

	}

	public String input() {
		HibHelpers hh = new HibHelpers();
		Integer returnCode = -1;
		rtrn = new ProcRtrn();
		try {
			returnCode = hh.uploadSabhrsToSum();
			if (returnCode == -1) {
				rtrn.setProcStatus("ERROR");
				rtrn.setProcMsg("There were some errors while uploading SABHRS Entries, Please check the Error Log.");
			} else {
				rtrn.setProcStatus("SUCCESS");
				rtrn.setProcMsg("SABHRS Entries uploaded successfully.");
				rtrn.setFieldName("HTML");
			}		
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtrn.setProcStatus("ERROR");
			rtrn.setProcMsg(e.toString());
		}

		return SUCCESS;
	}


	public String execute() {
		return SUCCESS;
	}

	public String getJSON() {
		return input();
	}

	public void setServletRequest(HttpServletRequest arg0) {
		request = arg0;
	}

	/**
	 * @return the rtrn
	 */
	public ProcRtrn getRtrn() {
		return rtrn;
	}

	/**
	 * @param rtrn
	 *            the rtrn to set
	 */
	public void setRtrn(ProcRtrn rtrn) {
		this.rtrn = rtrn;
	}
}
