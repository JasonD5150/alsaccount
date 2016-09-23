package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.ListUtils;
import fwp.utils.FwpStringUtils;

public class GenNonAlsEntriesAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(GenNonAlsEntriesAction.class);

	private String hidBudgYear;
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
		HibHelpers hh = new HibHelpers();
		ListUtils lu = new ListUtils();
		try {
			hidBudgYear = hh.getCurrentBudgetYear();
			setFundLst(FwpStringUtils.listCompListToString(lu.getFundList(null)));
			setSubClassLst(FwpStringUtils.listCompListToString(lu.getSubclassList(null)));
			setJlrLst(FwpStringUtils.listCompListToString(lu.getJLRBudgYearList(null)));
			setProjectGrantLst(lu.getProjectGrantsListTxt(null, false));
			setOrgLst(FwpStringUtils.listCompListToString(lu.getOrgList(null)));
			setAccountLst(FwpStringUtils.listCompListToString(lu.getAccountList(null)));
			setBudgetYearSel(lu.getBudgetYearList());
			transGroupTypeLst = lu.getSabhrsTransGroupTypeLst();
			providerLst = lu.getIntOnActProviderList();
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String getHidBudgYear() {
		return hidBudgYear;
	}

	public void setHidBudgYear(String hidBudgYear) {
		this.hidBudgYear = hidBudgYear;
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

	public String getOrgLst() {
		return orgLst;
	}

	public void setOrgLst(String orgLst) {
		this.orgLst = orgLst;
	}

	public String getAccountLst() {
		return accountLst;
	}

	public void setAccountLst(String accountLst) {
		this.accountLst = accountLst;
	}

	public List<ListComp> getTransGroupTypeLst() {
		return transGroupTypeLst;
	}

	public void setTransGroupTypeLst(List<ListComp> transGroupTypeLst) {
		this.transGroupTypeLst = transGroupTypeLst;
	}

	public List<ListComp> getBudgetYearSel() {
		return budgetYearSel;
	}

	public void setBudgetYearSel(List<ListComp> budgetYearSel) {
		this.budgetYearSel = budgetYearSel;
	}

	public List<ListComp> getProviderLst() {
		return providerLst;
	}

	public void setProviderLst(List<ListComp> providerLst) {
		this.providerLst = providerLst;
	}
}
