package fwp.alsaccount.json;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;

public class TransGrpIdListJson extends ActionSupport {

	private static final long serialVersionUID = 1L;
	public List<ListComp> transGrpIdLst;
	public Integer provNo;
	public Integer txGrpType;

	public String getJSON(){
		AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
		transGrpIdLst = new ArrayList<ListComp>();
		transGrpIdLst = atgsAS.getTransGrpIdList(provNo,txGrpType);
		return SUCCESS;
	}

	public List<ListComp> getTransGrpIdLst() {
		return transGrpIdLst;
	}
	public void setTransGrpIdLst(List<ListComp> transGrpIdLst) {
		this.transGrpIdLst = transGrpIdLst;
	}
	public Integer getProvNo() {
		return provNo;
	}
	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}
	public Integer getTxGrpType() {
		return txGrpType;
	}
	public void setTxGrpType(Integer txGrpType) {
		this.txGrpType = txGrpType;
	}
}