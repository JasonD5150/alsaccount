package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.ListUtils;
import fwp.alsaccount.utils.Utils;
import fwp.security.user.UserDTO;
import fwp.utils.FwpStringUtils;

public class IntProvRemittanceAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(IntProvRemittanceAction.class);

	private List<ListComp> providerLst;
	private String fundLst;
	private String subClassLst;
	private String jlrLst;
	private String projectGrantLst;
	private String orgLst;
	private String accountLst;
	private String curBudgYear;
	Boolean hasIntProvRole = false;
	Boolean hasUserRole = false;
	private String user;

	public IntProvRemittanceAction(){
	}

	public String input(){
		setRoles();
		HibHelpers hh = new HibHelpers();
		ListUtils lu = new ListUtils();
		try {
			curBudgYear = hh.getCurrentBudgetYear();
			providerLst = lu.getIntOnActProviderList();
		
			setFundLst(FwpStringUtils.listCompListToString(lu.getFundList(null)));
			setSubClassLst(FwpStringUtils.listCompListToString(lu.getSubclassList(null)));
			setJlrLst(FwpStringUtils.listCompListToString(lu.getJLRList()));
			setProjectGrantLst(lu.getProjectGrantsListTxt(null, false));
			setOrgLst(FwpStringUtils.listCompListToString(lu.getOrgList(null)));
			setAccountLst(FwpStringUtils.listCompListToString(lu.getAccountList(null)));
			
			UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
			setUser(userInfo.getStateId().toString());
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	void setRoles() {
		Utils utils = new Utils();
		if (utils.hasAnyRole("ALSACCOUNT_USER")) {
			hasUserRole = true;
		}
		if (utils.hasAnyRole("ALSACCOUNT_INTPROV")) {
			hasIntProvRole = true;
		}
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
	public Boolean getHasIntProvRole() {
		return hasIntProvRole;
	}
	public void setHasIntProvRole(Boolean hasIntProvRole) {
		this.hasIntProvRole = hasIntProvRole;
	}
	public Boolean getHasUserRole() {
		return hasUserRole;
	}
	public void setHasUserRole(Boolean hasUserRole) {
		this.hasUserRole = hasUserRole;
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
	public String getCurBudgYear() {
		return curBudgYear;
	}
	public void setCurBudgYear(String curBudgYear) {
		this.curBudgYear = curBudgYear;
	}
}
