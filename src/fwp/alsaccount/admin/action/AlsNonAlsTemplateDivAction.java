package fwp.alsaccount.admin.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.ListUtils;

public class AlsNonAlsTemplateDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(AlsNonAlsTemplateDivAction.class);

	private String budgYear;
	private String accountLst;
	private String fundLst;
	private String subClassLst;
	private String jlrLst;
	private String projectGrantLst;

	public AlsNonAlsTemplateDivAction(){
	}
	
	public String input(){
		try {
			ListUtils lu = new ListUtils();
			setAccountLst(lu.getAccountListTxt(budgYear, false));
			setFundLst(lu.getFundListTxt(null,false));
			setSubClassLst(lu.getSubclassListTxt(null,false));
			setJlrLst(lu.getJLRListTxt(false));
			setProjectGrantLst(lu.getProjectGrantsListTxt(null,false));
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}
	
	
	public String getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(String budgYear) {
		this.budgYear = budgYear;
	}

	public String getAccountLst() {
		return accountLst;
	}

	public void setAccountLst(String accountLst) {
		this.accountLst = accountLst;
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

}
