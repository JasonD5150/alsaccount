package fwp.alsaccount.json;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;


public class ProviderListJson extends ActionSupport {

	private static final long serialVersionUID = 1L;
	public List<ListComp> providerLst;
	public Integer txGrpType;



	public String getJSON(){
		ListUtils lu = new ListUtils();
		providerLst = new ArrayList<ListComp>();
		if(txGrpType != null && txGrpType == 8){
			providerLst = lu.getTransGrpAppProviderList();
		}else{
			providerLst = lu.getProviderList();
		}
		return SUCCESS;
	}



	public List<ListComp> getProviderLst() {
		return providerLst;
	}



	public void setProviderLst(List<ListComp> providerLst) {
		this.providerLst = providerLst;
	}



	public Integer getTxGrpType() {
		return txGrpType;
	}



	public void setTxGrpType(Integer txGrpType) {
		this.txGrpType = txGrpType;
	}
	
	
}