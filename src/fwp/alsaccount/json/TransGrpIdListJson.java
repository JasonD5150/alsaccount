package fwp.alsaccount.json;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;


public class TransGrpIdListJson extends ActionSupport {

	private static final long serialVersionUID = 1L;
	public List<ListComp> transGrpIdLst;
	public Integer provNo;

	public String getJSON(){
		ListUtils lu = new ListUtils();
		transGrpIdLst = new ArrayList<ListComp>();
		if(provNo != null){
			transGrpIdLst = lu.getTransGrpIdList(provNo);
		}else{
			transGrpIdLst = lu.getTransGrpIdList(null);
		}
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
	
}