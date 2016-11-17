package fwp.alsaccount.sabhrs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.ListUtils;
import fwp.utils.FwpStringUtils;

public class ProvAdjEntPostingAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(ProvAdjEntPostingAction.class);

	private String fundLst;
	private String subClassLst;
	private String jlrLst;
	private String orgLst;
	private String accountLst;


	public ProvAdjEntPostingAction(){
	}

	public String input(){
		ListUtils lu = new ListUtils();
		try {
			setFundLst(FwpStringUtils.listCompListToString(lu.getFundList(null)));
			setSubClassLst(FwpStringUtils.listCompListToString(lu.getSubclassList(null)));
			setJlrLst(FwpStringUtils.listCompListToString(lu.getJLRBudgYearList(null)));
			setOrgLst(FwpStringUtils.listCompListToString(lu.getOrgList(null)));
			setAccountLst(FwpStringUtils.listCompListToString(lu.getAccountList(null)));
			
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
	public String getAccountLst() {
		return accountLst;
	}
	public void setAccountLst(String accountLst) {
		this.accountLst = accountLst;
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
	public String getOrgLst() {
		return orgLst;
	}
	public void setOrgLst(String orgLst) {
		this.orgLst = orgLst;
	}
	
}
