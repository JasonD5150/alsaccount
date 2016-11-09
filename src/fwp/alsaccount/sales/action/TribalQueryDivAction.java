package fwp.alsaccount.sales.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;

public class TribalQueryDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(TribalQueryDivAction.class);

	private List<ListComp> tribeCdLst;
	private List<ListComp> provLst;
	private List<ListComp> itemTypeLst;
	private List<ListComp> usagePeriodLst;

	public TribalQueryDivAction(){
	}

	public String input(){
		ListUtils lu = new ListUtils();
		try {
			tribeCdLst = lu.getTribeCdList();
			provLst = lu.getActiveProviderList();
			itemTypeLst = lu.getTribalItemTypeList();
			usagePeriodLst = lu.getTribalUsagePeriodList();
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}

	public String execute(){
		return SUCCESS;
	}

	
	public List<ListComp> getTribeCdLst() {
		return tribeCdLst;
	}

	public void setTribeCdLst(List<ListComp> tribeCdLst) {
		this.tribeCdLst = tribeCdLst;
	}

	public List<ListComp> getItemTypeLst() {
		return itemTypeLst;
	}

	public void setItemTypeLst(List<ListComp> itemTypeLst) {
		this.itemTypeLst = itemTypeLst;
	}

	public List<ListComp> getProvLst() {
		return provLst;
	}

	public void setProvLst(List<ListComp> provLst) {
		this.provLst = provLst;
	}

	public List<ListComp> getUsagePeriodLst() {
		return usagePeriodLst;
	}

	public void setUsagePeriodLst(List<ListComp> usagePeriodLst) {
		this.usagePeriodLst = usagePeriodLst;
	}
	
}
