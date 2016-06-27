package fwp.alsaccount.sabhrs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.ListUtils;

public class GenNonAlsEntriesAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(GenNonAlsEntriesAction.class);

	private String fundLst;
	private String subClassLst;
	private String jlrLst;
	private String projectGrantLst;
	private String orgLst;
	private String accountLst;

	public GenNonAlsEntriesAction(){
	}
	
	public String input(){
		try {
			ListUtils lu = new ListUtils();
			setFundLst(lu.getFundListTxt(null,false));
			setSubClassLst(lu.getSubclassListTxt(null,false));
			setJlrLst(lu.getJLRListTxt(false));
			setProjectGrantLst(lu.getProjectGrantsListTxt(null,false));
			setOrgLst(lu.getOrgListTxt(null,false));
			setAccountLst(lu.getAccountListTxt(null,false));
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

}
