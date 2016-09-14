package fwp.alsaccount.admin.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;
import fwp.utils.FwpStringUtils;

public class AccountCodeControlQueryDivAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(AccountCodeControlQueryDivAction.class);

	private String accountLst;
	private String budgYear;
	
	private List<ListComp> budgYearLst;
	private List<ListComp> itemTypeLst;
	private List<ListComp> accCdLst;

	public String input(){
		ListUtils lu = new ListUtils();
		try {
			setAccountLst(FwpStringUtils.listCompListToString(lu.getAccountList(budgYear)));
			setBudgYearLst(lu.getBudgetYearList());
			setItemTypeLst(lu.getItemTypeCd(null));
			setAccCdLst(lu.getAccCdListTxt(budgYear, false));
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}

	public List<ListComp> getItemTypeLst() {
		return itemTypeLst;
	}

	public void setItemTypeLst(List<ListComp> itemTypeLst) {
		this.itemTypeLst = itemTypeLst;
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

	public List<ListComp> getAccCdLst() {
		return accCdLst;
	}

	public void setAccCdLst(List<ListComp> accCdLst) {
		this.accCdLst = accCdLst;
	}

	/**
	 * @return the budgYearLst
	 */
	public List<ListComp> getBudgYearLst() {
		return budgYearLst;
	}

	/**
	 * @param budgYearLst the budgYearLst to set
	 */
	public void setBudgYearLst(List<ListComp> budgYearLst) {
		this.budgYearLst = budgYearLst;
	}

}