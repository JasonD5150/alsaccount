package fwp.alsaccount.admin.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.HibHelpers;


public class FiscalYearEndAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(FiscalYearEndAction.class);
	private String curBudgetYear;
	private String curBudgetYearChangeDt;
	private Boolean copyAccTablesCompleted;
	private Boolean copyAlsNonAlsTemplatesCompleted;
	
	public FiscalYearEndAction(){
		
	}
	
	public String input(){
		HibHelpers hh = new HibHelpers();

		try {
			setCopyAccTablesCompleted(hh.accountingTableCopyCompleted());
			setCopyAlsNonAlsTemplatesCompleted(hh.alsNonAlsTemplateCopyCompleted());
			setCurBudgetYear(hh.getCurrentBudgetYear());
			setCurBudgetYearChangeDt(hh.getCurrentBudgetYearChangeDt());
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}		
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	/**
	 * @return the curBudgetYear
	 */
	public String getCurBudgetYear() {
		return curBudgetYear;
	}

	/**
	 * @param curBudgetYear the curBudgetYear to set
	 */
	public void setCurBudgetYear(String curBudgetYear) {
		this.curBudgetYear = curBudgetYear;
	}
	
	/**
	 * @return the copyAccTablesCompleted
	 */
	public Boolean getCopyAccTablesCompleted() {
		return copyAccTablesCompleted;
	}

	/**
	 * @param copyAccTablesCompleted the copyAccTablesCompleted to set
	 */
	public void setCopyAccTablesCompleted(Boolean copyAccTablesCompleted) {
		this.copyAccTablesCompleted = copyAccTablesCompleted;
	}

	/**
	 * @return the copyAlsNonAlsTemplatesCompleted
	 */
	public Boolean getCopyAlsNonAlsTemplatesCompleted() {
		return copyAlsNonAlsTemplatesCompleted;
	}

	/**
	 * @param copyAlsNonAlsTemplatesCompleted the copyAlsNonAlsTemplatesCompleted to set
	 */
	public void setCopyAlsNonAlsTemplatesCompleted(
			Boolean copyAlsNonAlsTemplatesCompleted) {
		this.copyAlsNonAlsTemplatesCompleted = copyAlsNonAlsTemplatesCompleted;
	}

	/**
	 * @return the curBudgetYearChangeDt
	 */
	public String getCurBudgetYearChangeDt() {
		return curBudgetYearChangeDt;
	}

	/**
	 * @param curBudgetYearChangeDt the curBudgetYearChangeDt to set
	 */
	public void setCurBudgetYearChangeDt(String curBudgetYearChangeDt) {
		this.curBudgetYearChangeDt = curBudgetYearChangeDt;
	}



}
