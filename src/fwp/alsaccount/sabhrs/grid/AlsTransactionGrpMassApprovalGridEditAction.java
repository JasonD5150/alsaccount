package fwp.alsaccount.sabhrs.grid;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.utils.HibHelpers;
import fwp.security.user.UserDTO;


public class AlsTransactionGrpMassApprovalGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String id;
	
	private String appLst;

	@SuppressWarnings("unchecked")
	public String execute() throws Exception{
		HibHelpers hh = new HibHelpers();
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp curDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		String curBudgYear = hh.getCurrentBudgetYear();
		
		String errMsg="";
		
		AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
		List<AlsTransactionGrpStatus> atgsLst = new ArrayList<AlsTransactionGrpStatus>();

		String where = "";
		try{
			for (String tmpApp:appLst.split(",")){
				String provider = tmpApp.split("_")[0];
				String bpe = tmpApp.split("_")[1].replace("-", "/");
				where = "WHERE idPk.atgTransactionCd = 8 "
					  + "AND Substr(idPk.atgsGroupIdentifier,2,6) = LPAD("+provider+", 6, '0') "
					  + "AND Substr(idPk.atgsGroupIdentifier,9,10) = '"+bpe+"' "
					  + "AND (atgsSummaryStatus IS NULL OR atgsInterfaceStatus IS NULL) "
					  + "AND atgsDepositId IS NOT NULL ";
				atgsLst = atgsAS.findAllByWhere(where);
				
				if(!atgsLst.isEmpty()){
					for(AlsTransactionGrpStatus tmp:atgsLst){
						if("1".equals(tmp.getAtgsSummaryStatus()) || tmp.getAtgsSummaryStatus() == null){
							tmp.setAtgsSummaryStatus("A");
							tmp.setAtgsSummaryApprovedBy(userInfo.getStateId().toString());
							tmp.setAtgsSummaryDt(curDate);
						}
						if("1".equals(tmp.getAtgsInterfaceStatus()) || tmp.getAtgsInterfaceStatus() == null){
							tmp.setAtgsInterfaceStatus("A");
							tmp.setAtgsInterfaceApprovedBy(userInfo.getStateId().toString());
							tmp.setAtgsInterfaceDt(curDate);
						}
						
						tmp.setAtgsWhoModi(userInfo.getStateId().toString());
						tmp.setAtgsWhenModi(curDate);
						atgsAS.save(tmp);
					}
				}else{
					this.addActionError("Error while updating Interface Status in ALS.ALSTRANSACTION_GRP_STATUS for Provider "+provider+", for BPE "+bpe+".");
					return "error_json";
				}
				String type = "IP";
				String by = curBudgYear.substring(2, 4);
				Integer seq = hh.getAlsDepIdSeq(curBudgYear, type);
				String depId = String.format("%02d", Integer.parseInt(by))+type+String.format("%05d", seq);
				
				where = "WHERE idPk.atgTransactionCd = 8 "
						  + "AND Substr(idPk.atgsGroupIdentifier,2,6) = LPAD("+provider+", 6, '0') "
						  + "AND Substr(idPk.atgsGroupIdentifier,9,10) = '"+bpe+"' "
						  + "AND (atgsSummaryStatus IS NULL OR atgsInterfaceStatus IS NULL) "
						  + "AND atgsDepositId IS NULL ";
				atgsLst = atgsAS.findAllByWhere(where);
				if(!atgsLst.isEmpty()){
					for(AlsTransactionGrpStatus tmp:atgsLst){
						if("1".equals(tmp.getAtgsSummaryStatus()) || tmp.getAtgsSummaryStatus() == null){
							tmp.setAtgsSummaryStatus("A");
							tmp.setAtgsSummaryApprovedBy(userInfo.getStateId().toString());
							tmp.setAtgsSummaryDt(curDate);
						}
						if("1".equals(tmp.getAtgsInterfaceStatus()) || tmp.getAtgsInterfaceStatus() == null){
							tmp.setAtgsInterfaceStatus("A");
							tmp.setAtgsInterfaceApprovedBy(userInfo.getStateId().toString());
							tmp.setAtgsInterfaceDt(curDate);
						}
						
						tmp.setAtgsWhoModi(userInfo.getStateId().toString());
						tmp.setAtgsWhenModi(curDate);
						tmp.setAtgsDepositId(depId);
						atgsAS.save(tmp);	
					}	
				}
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
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getAppLst() {
		return appLst;
	}

	public void setAppLst(String appLst) {
		this.appLst = appLst;
	}

}
