package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.inventory.dao.AlsInternalRemittance;
import fwp.als.hibernate.inventory.dao.AlsInternalRemittanceIdPk;
import fwp.alsaccount.appservice.sabhrs.AlsInternalRemittanceAS;
import fwp.alsaccount.appservice.sabhrs.AlsOverUnderSalesDetsAS;
import fwp.alsaccount.dao.sabhrs.AlsOverUnderSalesDets;
import fwp.alsaccount.dao.sabhrs.AlsOverUnderSalesDetsIdPk;
import fwp.security.user.UserDTO;
import fwp.utils.FwpDateUtils;


public class AlsOverUnderSalesDetsGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private String id;
	private Integer provNo;
	private Date apbdBillingFrom;
	private Date apbdBillingTo;
	private String aousdFlag;
	private String aousdDesc;
	private Double aousdAmount;

	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	public String execute() throws Exception{		
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		AlsOverUnderSalesDetsAS appSer = new AlsOverUnderSalesDetsAS();
		AlsOverUnderSalesDets tmp = null;
		
		AlsInternalRemittanceAS airAS = new AlsInternalRemittanceAS();
		AlsInternalRemittanceIdPk airIdPk = null;
		AlsInternalRemittance air = null;
		
		try{
			if(oper.equalsIgnoreCase("edit") || oper.equalsIgnoreCase("del")){
				String[] keys = id.split("_");
				AlsOverUnderSalesDetsIdPk tmpIdPk = new AlsOverUnderSalesDetsIdPk();
				tmpIdPk.setApiProviderNo(Integer.parseInt(keys[0]));
				tmpIdPk.setAirBillingFrom(FwpDateUtils.getStrToTimestamp(keys[1]));
				tmpIdPk.setAirBillingTo(FwpDateUtils.getStrToTimestamp(keys[2]));
				tmpIdPk.setAousdSeqNo(Integer.parseInt(keys[3]));
				
				tmp = appSer.findById(tmpIdPk);
				
				airIdPk = new AlsInternalRemittanceIdPk(tmp.getIdPk().getApiProviderNo(), tmp.getIdPk().getAirBillingFrom(), tmp.getIdPk().getAirBillingTo());
				air = airAS.findById(airIdPk);
			}else{
				airIdPk = new AlsInternalRemittanceIdPk(provNo, new Timestamp(apbdBillingFrom.getTime()), new Timestamp(apbdBillingTo.getTime()));
				air = airAS.findById(airIdPk);
			}
	
			if (oper.equalsIgnoreCase("add")) {
				AlsOverUnderSalesDetsIdPk tmpIdPk = new AlsOverUnderSalesDetsIdPk();
				tmpIdPk.setApiProviderNo(provNo);
				tmpIdPk.setAirBillingFrom(new Timestamp(apbdBillingFrom.getTime()));
				tmpIdPk.setAirBillingTo(new Timestamp(apbdBillingTo.getTime()));
				tmpIdPk.setAousdSeqNo(appSer.getNextSeqNo(tmpIdPk));
				tmp = new AlsOverUnderSalesDets();
				tmp.setIdPk(tmpIdPk);
				tmp.setAousdFlag(aousdFlag);
				tmp.setAousdDesc(aousdDesc);
				tmp.setAousdAmount(aousdAmount);
				
				tmp.setAousdWhoLog(userInfo.getStateId());
				tmp.setAousdWhenLog(date);

				tmp.setAousdCreatePersonid(userInfo.getStateId());
				tmp.setAousdCreateDate(date);
				
				appSer.save(tmp);
				
				//Update Als_Internal_Remittance
				if("O".equals(aousdFlag)){
					if(air.getAirOverSales() != null){
						air.setAirOverSales(air.getAirOverSales()+aousdAmount);
					}else{
						air.setAirOverSales(aousdAmount);
					}
				}else if("U".equals(aousdFlag)){
					if(air.getAirShortSales() != null){
						air.setAirShortSales(air.getAirShortSales()+aousdAmount);
					}else{
						air.setAirOverSales(aousdAmount);
					}
				}
				airAS.save(air);
			} else if((oper.equalsIgnoreCase("edit"))){	
				//Update Als_Internal_Remittance
				if("O".equals(tmp.getAousdFlag())){
					air.setAirOverSales(air.getAirOverSales()-tmp.getAousdAmount()+aousdAmount);
				}else if("U".equals(tmp.getAousdFlag())){
					air.setAirShortSales(air.getAirShortSales()-tmp.getAousdAmount()+aousdAmount);
				}
				airAS.save(air);

				tmp.setAousdFlag(aousdFlag);
				tmp.setAousdDesc(aousdDesc);
				tmp.setAousdAmount(aousdAmount);
				tmp.setAousdLastModPersonid(userInfo.getStateId());
				tmp.setAousdLastModDate(date);
				appSer.save(tmp);
			}else if (oper.equalsIgnoreCase("del")){
				//Update Als_Internal_Remittance
				if("O".equals(tmp.getAousdFlag())){
					air.setAirOverSales(air.getAirOverSales()-tmp.getAousdAmount());
				}else if("U".equals(tmp.getAousdFlag())){
					air.setAirShortSales(air.getAirShortSales()-tmp.getAousdAmount());
				}
				airAS.save(air);
				
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

	public String getAousdFlag() {
		return aousdFlag;
	}

	public void setAousdFlag(String aousdFlag) {
		this.aousdFlag = aousdFlag;
	}

	public String getAousdDesc() {
		return aousdDesc;
	}

	public void setAousdDesc(String aousdDesc) {
		this.aousdDesc = aousdDesc;
	}

	public Double getAousdAmount() {
		return aousdAmount;
	}

	public void setAousdAmount(Double aousdAmount) {
		this.aousdAmount = aousdAmount;
	}
	
	
}
