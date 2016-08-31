package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;
import fwp.utils.FwpStringUtils;

public class GenNonAlsEntriesAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(GenNonAlsEntriesAction.class);

	private String fundLst;
	private String subClassLst;
	private String jlrLst;
	private String projectGrantLst;
	private String orgLst;
	private String accountLst;
	private List<ListComp> transGroupTypeLst;
	private List<ListComp> budgetYearSel;
	private List<ListComp> providerLst;

	public GenNonAlsEntriesAction(){
	}
	
	public String input(){
		ListUtils lu = new ListUtils();
		try {
			setFundLst(FwpStringUtils.listCompListToString(lu.getFundList(null)));
			setSubClassLst(FwpStringUtils.listCompListToString(lu.getSubclassList(null)));
			setJlrLst(FwpStringUtils.listCompListToString(lu.getJLRBudgYearList(null)));
			setProjectGrantLst(lu.getProjectGrantsListTxt(null, false));
			setOrgLst(FwpStringUtils.listCompListToString(lu.getOrgList(null)));
			setAccountLst(FwpStringUtils.listCompListToString(lu.getAccountList(null)));
			setBudgetYearSel(lu.getBudgetYearList());
			transGroupTypeLst = lu.getSabhrsTransGroupTypeLst();
			providerLst = lu.getProviderList();
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	public String getFundLst() {
		return fundLst;
	}

	public void setFundLst(String fundLst) {
		this.fundLst = fundLst;
	}

	public String getSubClassLst() {
		return subClassLst;
	}

	public void setSubClassLst(String subClassLst) {
		this.subClassLst = subClassLst;
	}

	public String getJlrLst() {
		return jlrLst;
	}

	public void setJlrLst(String jlrLst) {
		this.jlrLst = jlrLst;
	}

	public String getProjectGrantLst() {
		return projectGrantLst;
	}

	public void setProjectGrantLst(String projectGrantLst) {
		this.projectGrantLst = projectGrantLst;
	}

	/**
	 * @return the orgLst
	 */
	public String getOrgLst() {
		return orgLst;
	}

	/**
	 * @param orgLst the orgLst to set
	 */
	public void setOrgLst(String orgLst) {
		this.orgLst = orgLst;
	}

	/**
	 * @return the accountLst
	 */
	public String getAccountLst() {
		return accountLst;
	}

	/**
	 * @param accountLst the accountLst to set
	 */
	public void setAccountLst(String accountLst) {
		this.accountLst = accountLst;
	}

	/**
	 * @return the transGroupTypeLst
	 */
	public List<ListComp> getTransGroupTypeLst() {
		return transGroupTypeLst;
	}

	/**
	 * @param transGroupTypeLst the transGroupTypeLst to set
	 */
	public void setTransGroupTypeLst(List<ListComp> transGroupTypeLst) {
		this.transGroupTypeLst = transGroupTypeLst;
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
	 * @return the providerLst
	 */
	public List<ListComp> getProviderLst() {
		return providerLst;
	}

	/**
	 * @param providerLst the providerLst to set
	 */
	public void setProviderLst(List<ListComp> providerLst) {
		this.providerLst = providerLst;
	}

}
