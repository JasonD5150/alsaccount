package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;

public class DistributionQueryDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(DistributionQueryDivAction.class);

	private List<ListComp> provLst;
	private List<ListComp> itemTypeLst;

	public DistributionQueryDivAction(){
	}

	public String input(){
		ListUtils lu = new ListUtils();
		try {
			provLst = lu.getIntOnActProviderList();
			itemTypeLst = lu.getItemTypeCd(null);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}

	public String execute(){
		return SUCCESS;
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
}
