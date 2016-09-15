package fwp.alsaccount.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;




public class BudgetYearListAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(BudgetYearListAction.class);

	private List<ListComp> budgetYearSel;

	public BudgetYearListAction(){
	}
	
	public String input(){
		
		try {
			ListUtils lu = new ListUtils();
			budgetYearSel = lu.getBudgetYearList();
			
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

}
