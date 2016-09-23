package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.ListUtils;

public class ManualProviderAdjEntriesAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(ManualProviderAdjEntriesAction.class);

	private List<ListComp> providerLst;
	private String budgYear;

	public ManualProviderAdjEntriesAction(){
	}
	
	public String input(){
		HibHelpers hh = new HibHelpers();
		ListUtils lu = new ListUtils();
		try {
			providerLst = lu.getProviderList();
			budgYear = hh.getCurrentBudgetYear();
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(String budgYear) {
		this.budgYear = budgYear;
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
}
