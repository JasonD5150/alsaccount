package fwp.alsaccount.admin.json;

import java.sql.Date;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.hibernate.utils.ProcRtrn;
import fwp.alsaccount.utils.HibHelpers;
import javax.servlet.http.HttpServletRequest;

public class FiscalYearEndJson extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;

	private String yearEndStep;
	private ProcRtrn rtrn;
	private Date upf;
	private Date upt;
	private String itc;
	private String budgYear;

	public FiscalYearEndJson() {

	}

	public String input() {
		HibHelpers hh = new HibHelpers();
		Integer returnCode = -1;
		rtrn = new ProcRtrn();
		try {
			
			if ("copyAccTables".equals(yearEndStep)) {//STEP ONE Copy Accounting Tables
				returnCode = hh.accountingTableCopy();
				if (returnCode == -1) {
					rtrn.setProcStatus("ERROR");
					rtrn.setProcMsg("Process of copying accounting tables had Errors, Please check Errorlog and Restart the program after fixing the errors.");
				} else {
					rtrn.setProcStatus("SUCCESS");
					rtrn.setProcMsg("Copy Accounting Tables finished successfully.");
					rtrn.setFieldName("HTML");
				}					
			} else if ("updateAccCd".equals(yearEndStep)) {//STEP TWO Update Usage Period
				if("0".equals(itc)) {
					itc = null;
				}
				String rtn = hh.updateAccountingCodes(upf, upt, itc, budgYear);
				if (rtn != null) {
					rtrn.setProcStatus("SUCCESS");
					rtrn.setProcMsg(rtn);
					rtrn.setFieldName("REPORT");
				}
			} else if ("copyAlsNonAlsTemplates".equals(yearEndStep)) {//STEP THREE Copy Templates
				returnCode = hh.alsNonAlsTemplateCopy();
				if (returnCode == -1) {
					rtrn.setProcStatus("ERROR");
					rtrn.setProcMsg("Copy Als Non Als Templates did not finish successfully.");
				} else {
					rtrn.setProcStatus("SUCCESS");
					rtrn.setProcMsg("Copy Als Non Als Templates finished successfully.");
					rtrn.setFieldName("HTML");
				}
			} else if ("createTransGroup".equals(yearEndStep)) {//STEP FOUR Adjustment Parameters
				if("".equals(budgYear) || budgYear == null){
					rtrn.setProcStatus("ERROR");
					rtrn.setProcMsg("Budget Year is required.");
				}else{
					hh.createTransactionGroup(budgYear);
					rtrn.setProcStatus("SUCCESS");
					rtrn.setProcMsg("Transaction group created successfully.");
					rtrn.setFieldName("HTML");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtrn.setProcStatus("ERROR");
			rtrn.setProcMsg(e.toString());
		}

		return SUCCESS;
	}

	/**
	 * @return the budgYear
	 */
	public String getBudgYear() {
		return budgYear;
	}

	/**
	 * @param budgYear
	 *            the budgYear to set
	 */
	public void setBudgYear(String budgYear) {
		this.budgYear = budgYear;
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
	 * @return the yearEndAction
	 */
	public String getYearEndStep() {
		return yearEndStep;
	}

	/**
	 * @param yearEndAction
	 *            the yearEndAction to set
	 */
	public void setYearEndStep(String yearEndStep) {
		this.yearEndStep = yearEndStep;
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

	/**
	 * @return the upf
	 */
	public Date getUpf() {
		return upf;
	}

	/**
	 * @param upf
	 *            the upf to set
	 */
	public void setUpf(Date upf) {
		this.upf = upf;
	}

	/**
	 * @return the upt
	 */
	public Date getUpt() {
		return upt;
	}

	/**
	 * @param upt
	 *            the upt to set
	 */
	public void setUpt(Date upt) {
		this.upt = upt;
	}

	/**
	 * @return the itc
	 */
	public String getItc() {
		return itc;
	}

	/**
	 * @param ict
	 *            the itc to set
	 */
	public void setItc(String itc) {
		this.itc = itc;
	}
}
