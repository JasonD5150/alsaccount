package fwp.alsaccount.admin.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.ListUtils;

public class OrgControlDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(OrgControlDivAction.class);

	private String accountCdLst;
	private String providerLst;
	private String budgYear;

	public OrgControlDivAction(){
	}
	
	public String input(){
		try {
			ListUtils lu = new ListUtils();
			setAccountCdLst(lu.getAccCdListTxt(budgYear, false));
			setProviderLst(lu.getProviderListTxt(false));
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

	public String getProviderLst() {
		return providerLst;
	}

	public void setProviderLst(String providerLst) {
		this.providerLst = providerLst;
	}

	public String getAccountCdLst() {
		return accountCdLst;
	}

	public void setAccountCdLst(String accountCdLst) {
		this.accountCdLst = accountCdLst;
	}

}
