package fwp.alsaccount.admin.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.hibernate.utils.ListComp;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.ListUtils;


public class FiscalYearEndAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(FiscalYearEndAction.class);
	private List<ListComp> budgetYearSel;
	private String curBudgetYear;
	private Boolean copyAccTablesCompleted;
	private Boolean copyAlsNonAlsTemplatesCompleted;
	
	public FiscalYearEndAction(){
		
	}
	
	public String input(){
		HibHelpers hh = new HibHelpers();
		ListUtils lu = new ListUtils();
		try {
			setCopyAccTablesCompleted(hh.accountingTableCopyCompleted());
			setCopyAlsNonAlsTemplatesCompleted(hh.alsNonAlsTemplateCopyCompleted());
			setCurBudgetYear(hh.getCurrentBudgetYear());
			budgetYearSel = lu.getBudgetYearList();
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
	 * @return the budgetYearSel
	 */
	public List<ListComp> getBudgetYearSel() {
		return budgetYearSel;
	}

	/**
	 * @param budgetYearSel the budgetYearSel to set
	 */
	public void setBudgetYearSel(List<ListComp> budgetYearSel) {
		this.budgetYearSel = budgetYearSel;
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



}
