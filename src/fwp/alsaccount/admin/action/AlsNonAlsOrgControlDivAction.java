package fwp.alsaccount.admin.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.ListUtils;
import fwp.utils.FwpStringUtils;

public class AlsNonAlsOrgControlDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(AlsNonAlsOrgControlDivAction.class);

	private String orgLst;
	private String providerLst;
	private String budgYear;

	public AlsNonAlsOrgControlDivAction(){
	}
	
	public String input(){
		try {
			ListUtils lu = new ListUtils();
			setOrgLst(FwpStringUtils.listCompListToString(lu.getOrgList(null)));
			setProviderLst(FwpStringUtils.listCompListToString(lu.getProviderList()));
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

	public String getOrgLst() {
		return orgLst;
	}

	public void setOrgLst(String orgLst) {
		this.orgLst = orgLst;
	}

	

}
