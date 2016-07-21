package fwp.alsaccount.admin.grid;

import java.util.Calendar;
import java.util.Date;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsSabhrsFyeAdjstDtlAS;
import fwp.alsaccount.dao.admin.AlsSabhrsFyeAdjstDtl;
import fwp.alsaccount.dao.admin.AlsSabhrsFyeAdjstDtlIdPk;
import fwp.alsaccount.utils.Utils;


public class AlsSabhrsFyeAdjstDtlGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	private AlsSabhrsFyeAdjstDtlIdPk idPk;
	private String id;
	private Integer budgYear;
	private String asfadAdjstDt;
	private String asfaBudgetYear;


	public String execute() throws Exception{
		Calendar cal = Calendar.getInstance();
		String errMsg="";
		Date begDt = null;
		Date endDt = null;
		if(budgYear != null){
			cal.set(budgYear, Calendar.JUNE, 1); //Year, month and day of month
			begDt = cal.getTime();
			cal.set(budgYear, Calendar.JUNE, 30); //Year, month and day of month
			endDt = cal.getTime();
		}
		
		
		try{
			AlsSabhrsFyeAdjstDtlAS appSer = new AlsSabhrsFyeAdjstDtlAS();
			AlsSabhrsFyeAdjstDtlIdPk tmpIdPk = new AlsSabhrsFyeAdjstDtlIdPk();
			AlsSabhrsFyeAdjstDtl tmp = new AlsSabhrsFyeAdjstDtl();
			
			if (oper.equalsIgnoreCase("add")) {
				idPk.setAsfaBudgetYear(budgYear);
				tmp.setIdPk(idPk);
			} else {
				tmpIdPk.setAsfaBudgetYear(Integer.parseInt(id.split("/")[2]));
				tmpIdPk.setAsfadAdjstDt(Utils.StrToTimestamp(id,"short"));

				tmp = appSer.findById(tmpIdPk);
			}
			
			if(oper.equalsIgnoreCase("add") || oper.equalsIgnoreCase("edit")){
				if(tmp.getIdPk().getAsfadAdjstDt().compareTo(begDt)<0 || tmp.getIdPk().getAsfadAdjstDt().compareTo(endDt)>-1){
					addActionError("Adjustment Date must be in June of current Budget Year.");
				}
				if(appSer.isDuplicate(budgYear, idPk.getAsfadAdjstDt())){
					addActionError("Cannot add due to duplicate.");
				}
				if (this.hasActionErrors()) {
					return "error_json";
				}
				appSer.save(tmp);
				//System.out.println("Add/Edit");
			}else if(oper.equalsIgnoreCase("del")){
				appSer.delete(tmp);
				//System.out.println("Delete");
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
	
	/**
	 * @return the idPk
	 */
	public AlsSabhrsFyeAdjstDtlIdPk getIdPk() {
		return idPk;
	}


	/**
	 * @param idPk the idPk to set
	 */
	public void setIdPk(AlsSabhrsFyeAdjstDtlIdPk idPk) {
		this.idPk = idPk;
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
	 * @return the asfadAdjstDt
	 */
	public String getAsfadAdjstDt() {
		return asfadAdjstDt;
	}


	/**
	 * @param asfadAdjstDt the asfadAdjstDt to set
	 */
	public void setAsfadAdjstDt(String asfadAdjstDt) {
		this.asfadAdjstDt = asfadAdjstDt;
	}


	/**
	 * @return the asfaBudgetYear
	 */
	public String getAsfaBudgetYear() {
		return asfaBudgetYear;
	}


	/**
	 * @param asfaBudgetYear the asfaBudgetYear to set
	 */
	public void setAsfaBudgetYear(String asfaBudgetYear) {
		this.asfaBudgetYear = asfaBudgetYear;
	}

}
