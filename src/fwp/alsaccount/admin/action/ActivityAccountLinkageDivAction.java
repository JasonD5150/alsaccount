package fwp.alsaccount.admin.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.ListUtils;
import fwp.utils.StringUtils;


public class ActivityAccountLinkageDivAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ActivityAccountLinkageDivAction.class);

	private String accountLst;
	private String sysActivityTypeTransCodeLst;
	private String budgYear;

	public String input(){
		StringUtils su = new StringUtils();
		ListUtils lu = new ListUtils();
		try {
			accountLst = su.listCompListToString(lu.getAccountList(budgYear));
			sysActivityTypeTransCodeLst = su.listCompListToString(lu.getActTypeTranCdList(budgYear));
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}

	public String execute(){
		return SUCCESS;
	}

	public String getAccountLst() {
		return accountLst;
	}

	public void setAccountLst(String accountLst) {
		this.accountLst = accountLst;
	}

	public String getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(String budgYear) {
		this.budgYear = budgYear;
	}
	
	public String getSysActivityTypeTransCodeLst() {
		return sysActivityTypeTransCodeLst;
	}

	public void setSysActivityTypeTransCodeLst(String sysActivityTypeTransCodeLst) {
		this.sysActivityTypeTransCodeLst = sysActivityTypeTransCodeLst;
	}

}