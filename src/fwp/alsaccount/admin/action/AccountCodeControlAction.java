package fwp.alsaccount.admin.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;



public class AccountCodeControlAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(AccountCodeControlAction.class);

	private List<ListComp> budgetYearSel;
	private List<ListComp> itemTypeLst;

	public String input(){
		try {
			ListUtils lu = new ListUtils();
			setBudgetYearSel(lu.getBudgetYearList());
			setItemTypeLst(lu.getItemTypeCd(null));
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	public List<ListComp> getBudgetYearSel() {
		return budgetYearSel;
	}

	public void setBudgetYearSel(List<ListComp> budgetYearSel) {
		this.budgetYearSel = budgetYearSel;
	}

	/**
	 * @return the itemTypeLst
	 */
	public List<ListComp> getItemTypeLst() {
		return itemTypeLst;
	}

	/**
	 * @param itemTypeLst the itemTypeLst to set
	 */
	public void setItemTypeLst(List<ListComp> itemTypeLst) {
		this.itemTypeLst = itemTypeLst;
	}
}