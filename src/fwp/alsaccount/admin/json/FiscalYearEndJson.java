package fwp.alsaccount.admin.json;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.admin.dao.AlsMisc;
import fwp.alsaccount.appservice.admin.AlsMiscAS;
import fwp.alsaccount.hibernate.utils.ProcRtrn;
import fwp.alsaccount.utils.HibHelpers;

public class FiscalYearEndJson extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String yearEndStep;
	private ProcRtrn rtrn;
	private Date upf;
	private Date upt;
	private String itc;
	private String budgYear;
	private String budgYearChangeDt;

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
			} else if ("updateAccCd".equals(yearEndStep)) {//STEP TWO Update Item Accounting Codes
				if("0".equals(itc)) {
					itc = null;
				}
				String rtn = hh.updateAccountingCodes(upf, upt, itc, budgYear);
				if (rtn != null) {
					rtrn.setProcStatus("SUCCESS");
					rtrn.setProcMsg(rtn);
					rtrn.setFieldName("REPORT");
				}
			} else if ("updateAccCdAll".equals(yearEndStep)) {//STEP TWO Update Item Accounting Codes ALL
				String rtn = hh.updateAccountingCodesAll();
				if (rtn != null) {
					rtrn.setProcStatus("SUCCESS");
					rtrn.setProcMsg(rtn);
					rtrn.setFieldName("REPORT");
				}
			} else if ("createTransGroup".equals(yearEndStep)) {//STEP THREE Generate FY Adjusted Entries
				if("".equals(budgYear) || budgYear == null){
					rtrn.setProcStatus("ERROR");
					rtrn.setProcMsg("Budget Year is required.");
				}else{
					hh.createTransactionGroup(budgYear);
					rtrn.setProcStatus("SUCCESS");
					rtrn.setProcMsg("Transaction group created successfully.");
					rtrn.setFieldName("HTML");
				}
			} else if ("updateBudgYrChngDt".equals(yearEndStep)) {//STEP FOUR Update Budget Year Change Date in als_misc
				if("".equals(budgYearChangeDt) || budgYearChangeDt == null){
					rtrn.setProcStatus("ERROR");
					rtrn.setProcMsg("Budget Year Change Date is required.");
				}else{
					String where = " WHERE amKey1 = 'BUDGET YEAR CHANGE DATE'";
					AlsMiscAS amAS = new AlsMiscAS();
					List<AlsMisc> amList  = new ArrayList<AlsMisc>();
					AlsMisc tmp  = new AlsMisc();
					amList = amAS.findAllByWhere(where);
					tmp = amList.get(0);
					tmp.setAmParVal(budgYearChangeDt);
					amAS.save(tmp);
					rtrn.setProcStatus("SUCCESS");
					rtrn.setProcMsg("Budget Year Change Date successfully updated.");
					rtrn.setFieldName("HTML");
				}
			} else if ("copyAlsNonAlsTemplates".equals(yearEndStep)) {//STEP FIVE Copy Templates
				returnCode = hh.alsNonAlsTemplateCopy();
				if (returnCode == -1) {
					rtrn.setProcStatus("ERROR");
					rtrn.setProcMsg("Copy Als Non Als Templates did not finish successfully.");
				} else {
					rtrn.setProcStatus("SUCCESS");
					rtrn.setProcMsg("Copy Als Non Als Templates finished successfully.");
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

	/**
	 * @return the budgYearChangeDt
	 */
	public String getBudgYearChangeDt() {
		return budgYearChangeDt;
	}

	/**
	 * @param budgYearChangeDt the budgYearChangeDt to set
	 */
	public void setBudgYearChangeDt(String budgYearChangeDt) {
		this.budgYearChangeDt = budgYearChangeDt;
	}
}
