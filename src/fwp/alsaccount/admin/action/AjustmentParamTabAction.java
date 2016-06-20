package fwp.alsaccount.admin.action;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsSabhrsFyeAdjstAS;
import fwp.alsaccount.dao.admin.AlsSabhrsFyeAdjst;
import fwp.alsaccount.utils.HibHelpers;



public class AjustmentParamTabAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(AjustmentParamTabAction.class);
	private Integer budgYear;
	private Date billPeriodEnd;
	private Date endFY;
	private Date newFY;
	private String tranGrpNmFyeAdjust;
	private String tranGrpNmNewFY;
	
	
	public AjustmentParamTabAction(){
		
	}
	
	
	public String input(){
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		HibHelpers hh = new HibHelpers();
		Calendar cal = Calendar.getInstance();
		
		try {
		AlsSabhrsFyeAdjstAS asfaAS = new AlsSabhrsFyeAdjstAS();
		AlsSabhrsFyeAdjst asfa = new AlsSabhrsFyeAdjst();
		
		if(!"".equals(budgYear) && budgYear != null){
			cal.set(budgYear, Calendar.JUNE, 30); //Year, month and day of month
			endFY = cal.getTime();
			cal.set(budgYear, Calendar.JULY, 1); //Year, month and day of month
			newFY = cal.getTime();
			billPeriodEnd = hh.getBillPeriodEndDate(budgYear);
			
			asfa = asfaAS.findById(budgYear);
			if(asfa != null){
				tranGrpNmFyeAdjust = asfa.getAsfaFyeGroupIdentifier();
				tranGrpNmNewFY = asfa.getAsfaNfyGroupIdentifier();
			}else{
				asfa = new AlsSabhrsFyeAdjst();
				asfa.setAsfaBudgetYear(budgYear);
				asfa.setAsfaBillPrdEndDt(new Timestamp(billPeriodEnd.getTime()));
				asfa.setAsfaFyBegDt(new Timestamp(formatter.parse(formatter.format(newFY)).getTime()));
				asfa.setAsfaFyEndDt(new Timestamp(formatter.parse(formatter.format(endFY)).getTime()));
				asfaAS.save(asfa);
			}	
		}
		} catch (ParseException e) {
			log.debug("AdjustmentParamTab had a error: ",e.getMessage());
		}
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	
	/**
	 * @return the budgYear
	 */
	public Integer getBudgYear() {
		return budgYear;
	}

	/**
	 * @param budgYear the budgYear to set
	 */
	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}
	
	/**
	 * @return the billPeriodEnd
	 */
	public Date getBillPeriodEnd() {
		return billPeriodEnd;
	}

	/**
	 * @param billPeriodEnd the billPeriodEnd to set
	 */
	public void setBillPeriodEnd(Date billPeriodEnd) {
		this.billPeriodEnd = billPeriodEnd;
	}

	/**
	 * @return the endFY
	 */
	public Date getEndFY() {
		return endFY;
	}

	/**
	 * @param endFY the endFY to set
	 */
	public void setEndFY(Date endFY) {
		this.endFY = endFY;
	}

	/**
	 * @return the newFY
	 */
	public Date getNewFY() {
		return newFY;
	}

	/**
	 * @param newFY the newFY to set
	 */
	public void setNewFY(Date newFY) {
		this.newFY = newFY;
	}

	/**
	 * @return the tranGrpNmFyeAdjust
	 */
	public String getTranGrpNmFyeAdjust() {
		return tranGrpNmFyeAdjust;
	}


	/**
	 * @param tranGrpNmFyeAdjust the tranGrpNmFyeAdjust to set
	 */
	public void setTranGrpNmFyeAdjust(String tranGrpNmFyeAdjust) {
		this.tranGrpNmFyeAdjust = tranGrpNmFyeAdjust;
	}


	/**
	 * @return the tranGrpNmNewFY
	 */
	public String getTranGrpNmNewFY() {
		return tranGrpNmNewFY;
	}


	/**
	 * @param tranGrpNmNewFY the tranGrpNmNewFY to set
	 */
	public void setTranGrpNmNewFY(String tranGrpNmNewFY) {
		this.tranGrpNmNewFY = tranGrpNmNewFY;
	}


}
