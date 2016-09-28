package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.appservice.inventory.AlsNonAlsDetailsAS;
import fwp.als.hibernate.inventory.dao.AlsNonAlsDetails;
import fwp.als.hibernate.inventory.dao.AlsNonAlsDetailsIdPk;
import fwp.security.user.UserDTO;
import fwp.utils.FwpDateUtils;


public class AlsNonAlsDetialsGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	private String data;

	private String id;
	private Integer provNo;
	private String anatCd;
	private String anadDesc;
	private Double anadAmount;
	private Date apbdBillingFrom;
	private Date apbdBillingTo;
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	public String execute() throws Exception{		
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		AlsNonAlsDetailsAS appSer = new AlsNonAlsDetailsAS();
		AlsNonAlsDetails tmp = null;
		try{
			if(oper.equalsIgnoreCase("edit") || oper.equalsIgnoreCase("del")){
				String[] keys = id.split("_");
				AlsNonAlsDetailsIdPk tmpIdPk = new AlsNonAlsDetailsIdPk();
				tmpIdPk.setApiProviderNo(Integer.parseInt(keys[0]));
				tmpIdPk.setAirBillingFrom(FwpDateUtils.getStrToTimestamp(keys[1]));
				tmpIdPk.setAirBillingTo(FwpDateUtils.getStrToTimestamp(keys[2]));
				tmpIdPk.setAnadSeqNo(Integer.parseInt(keys[3]));
				
				tmp = appSer.findById(tmpIdPk);
			}
			if (oper.equalsIgnoreCase("add")) {
				AlsNonAlsDetailsIdPk tmpIdPk = new AlsNonAlsDetailsIdPk();
				tmpIdPk.setApiProviderNo(provNo);
				tmpIdPk.setAirBillingFrom(new Timestamp(apbdBillingFrom.getTime()));
				tmpIdPk.setAirBillingTo(new Timestamp(apbdBillingTo.getTime()));
				tmpIdPk.setAnadSeqNo(appSer.getNextSeqNo(tmpIdPk));
				tmp = new AlsNonAlsDetails();
				tmp.setIdPk(tmpIdPk);
				tmp.setAnadAmount(anadAmount);
				tmp.setAnadDesc(anadDesc);
				tmp.setAnatCd(anatCd.split("_")[0]);
				
				tmp.setAnadWhoLog(userInfo.getStateId());
				tmp.setAnadWhenLog(date);
				
				tmp.setCreatePersonid(userInfo.getUserId());
				tmp.setCreateDate(date);
				
				appSer.save(tmp);
			} else if((oper.equalsIgnoreCase("edit"))){						
				tmp.setAnadAmount(anadAmount);
				tmp.setAnadDesc(anadDesc);
				tmp.setAnatCd(anatCd.split("_")[0]);
				tmp.setModPersonid(userInfo.getUserId());
				tmp.setModDate(date);
				appSer.save(tmp);
			}else if (oper.equalsIgnoreCase("del")){
				appSer.delete(tmp);
			}else{
				return "error_json";
			}
		}  catch(Exception ex) {
			 if (ex.toString().contains("ORA-02292")){
				  errMsg += "Grid has child record(s) which would need to be deleted first.";
			  } else if (ex.toString().contains("ORA-02291")){
				  errMsg += "Parent record not found.";
			  } else if (ex.toString().contains("ORA-00001")){
				  errMsg += "Unable to add this record due to duplicate.";
			  }	else {
				  errMsg += " " + ex.toString();
			  }
			  
		    addActionError(errMsg);
	        return "error_json";
		}	
		return SUCCESS;
	}

	
	
	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getAnadAmount() {
		return anadAmount;
	}

	public void setAnadAmount(Double anadAmount) {
		this.anadAmount = anadAmount;
	}

	public Date getApbdBillingFrom() {
		return apbdBillingFrom;
	}

	public void setApbdBillingFrom(Date apbdBillingFrom) {
		this.apbdBillingFrom = apbdBillingFrom;
	}

	public Date getApbdBillingTo() {
		return apbdBillingTo;
	}

	public void setApbdBillingTo(Date apbdBillingTo) {
		this.apbdBillingTo = apbdBillingTo;
	}

	public Integer getProvNo() {
		return provNo;
	}

	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}

	public String getAnatCd() {
		return anatCd;
	}

	public void setAnatCd(String anatCd) {
		this.anatCd = anatCd;
	}

	public String getAnadDesc() {
		return anadDesc;
	}

	public void setAnadDesc(String anadDesc) {
		this.anadDesc = anadDesc;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
}
