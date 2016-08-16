package fwp.alsaccount.sabhrs.action;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.ListUtils;
import fwp.security.user.UserDTO;
import fwp.utils.StringUtils;

public class TransactionGroupApprovalAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(TransactionGroupApprovalAction.class);

	private String bankCodeLst;
	private String providerLst;
	private String groupIdentifierLst;
	private String user;


	public TransactionGroupApprovalAction(){
	}
	
	public String input(){
		StringUtils su = new StringUtils();
		ListUtils lu = new ListUtils();
		try {
			setBankCodeLst(su.listCompListToString(lu.getTransGrpBankCodeList()));
			setProviderLst(su.listCompListToString(lu.getProviderList()));
			
			UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
			setUser(userInfo.getStateId().toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	/**
	 * @return the bankCodeLst
	 */
	public String getBankCodeLst() {
		return bankCodeLst;
	}

	/**
	 * @param bankCodeLst the bankCodeLst to set
	 */
	public void setBankCodeLst(String bankCodeLst) {
		this.bankCodeLst = bankCodeLst;
	}

	/**
	 * @return the providerLst
	 */
	public String getProviderLst() {
		return providerLst;
	}

	/**
	 * @param providerLst the providerLst to set
	 */
	public void setProviderLst(String providerLst) {
		this.providerLst = providerLst;
	}

	/**
	 * @return the groupIdentifierLst
	 */
	public String getGroupIdentifierLst() {
		return groupIdentifierLst;
	}

	/**
	 * @param groupIdentifierLst the groupIdentifierLst to set
	 */
	public void setGroupIdentifierLst(String groupIdentifierLst) {
		this.groupIdentifierLst = groupIdentifierLst;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

}
