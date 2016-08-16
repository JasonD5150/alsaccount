package fwp.alsaccount.admin.json;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;


public class ItemTypeListJson extends ActionSupport {

	private static final long serialVersionUID = 1L;
	public List<ListComp> itemTypes;
	public String upf;
	public String upt;


	public String getJSON(){
		itemTypes = new ArrayList<ListComp>();
		ListUtils lu = new ListUtils();
		itemTypes = lu.getItemTypeList(upf,upt);
		
		return SUCCESS;
	}

	/**
	 * @return the upf
	 */
	public String getUpf() {
		return upf;
	}


	/**
	 * @param upf the upf to set
	 */
	public void setUpf(String upf) {
		this.upf = upf;
	}


	/**
	 * @return the upt
	 */
	public String getUpt() {
		return upt;
	}


	/**
	 * @param upt the upt to set
	 */
	public void setUpt(String upt) {
		this.upt = upt;
	}

	/**
	 * @return the itemTypes
	 */
	public List<ListComp> getItemTypes() {
		return itemTypes;
	}

	/**
	 * @param itemTypes the itemTypes to set
	 */
	public void setItemTypes(List<ListComp> itemTypes) {
		this.itemTypes = itemTypes;
	}


	
	
}