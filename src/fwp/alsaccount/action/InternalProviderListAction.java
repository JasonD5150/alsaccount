package fwp.alsaccount.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;

public class InternalProviderListAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(InternalProviderListAction.class);

	private List<ListComp> providerLst;

	public InternalProviderListAction(){
	}
	
	public String input(){
		ListUtils lu = new ListUtils();
		try {
			providerLst = lu.getIntOnActProviderList();
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
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
