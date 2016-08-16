package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.ListUtils;

public class SabhrsQueryDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(SabhrsQueryDivAction.class);

	private List<ListComp> jlrLst;
	private List<ListComp> transGroupTypeLst;
	private List<ListComp> budgetYearSel;
	
	private List<ListComp> tribeCdLst;
	private List<ListComp> programLst;
	private List<ListComp> providerLst;
	private List<ListComp> accountLst;
	private List<ListComp> fundLst;
	private List<ListComp> orgLst;
	private List<ListComp> subclassLst;
	private List<ListComp> sysActTypeCdLst;
	
	private String budgYear;

	public SabhrsQueryDivAction(){
	}
	
	public String input(){
		ListUtils lu = new ListUtils();
		HibHelpers hh = new HibHelpers();
		if(budgYear == null || "".equals(budgYear)){
			budgYear = hh.getCurrentBudgetYear();
		}
		try {
			providerLst = lu.getProviderList();
			jlrLst = lu.getJLRList();
			transGroupTypeLst = lu.getSabhrsTransGroupTypeLst();
			budgetYearSel = lu.getBudgetYearList();
	
			tribeCdLst = lu.getTribeCdList();
			accountLst = lu.getAccountList(budgYear);
			fundLst = lu.getFundList(budgYear);
			orgLst = lu.getOrgList(budgYear);
			subclassLst = lu.getSubclassList(budgYear);
			sysActTypeCdLst = lu.getActTypeTranCdList(budgYear);
		
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	public List<ListComp> getProviderLst() {
		return providerLst;
	}

	public void setProviderLst(List<ListComp> providerLst) {
		this.providerLst = providerLst;
	}

	public List<ListComp> getJlrLst() {
		return jlrLst;
	}

	public void setJlrLst(List<ListComp> jlrLst) {
		this.jlrLst = jlrLst;
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
	
	public List<ListComp> getTribeCdLst() {
		return tribeCdLst;
	}

	public void setTribeCdLst(List<ListComp> tribeCdLst) {
		this.tribeCdLst = tribeCdLst;
	}

	public List<ListComp> getProgramLst() {
		return programLst;
	}

	public void setProgramLst(List<ListComp> programLst) {
		this.programLst = programLst;
	}

	public List<ListComp> getAccountLst() {
		return accountLst;
	}

	public void setAccountLst(List<ListComp> accountLst) {
		this.accountLst = accountLst;
	}

	public List<ListComp> getFundLst() {
		return fundLst;
	}

	public void setFundLst(List<ListComp> fundLst) {
		this.fundLst = fundLst;
	}

	public List<ListComp> getOrgLst() {
		return orgLst;
	}

	public void setOrgLst(List<ListComp> orgLst) {
		this.orgLst = orgLst;
	}

	public List<ListComp> getSubclassLst() {
		return subclassLst;
	}

	public void setSubclassLst(List<ListComp> subclassLst) {
		this.subclassLst = subclassLst;
	}

	public List<ListComp> getSysActTypeCdLst() {
		return sysActTypeCdLst;
	}

	public void setSysActTypeCdLst(List<ListComp> sysActTypeCdLst) {
		this.sysActTypeCdLst = sysActTypeCdLst;
	}

	public String getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(String budgYear) {
		this.budgYear = budgYear;
	}

}
