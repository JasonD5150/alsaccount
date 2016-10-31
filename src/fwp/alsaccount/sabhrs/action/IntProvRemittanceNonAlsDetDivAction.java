package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsNonAlsTemplateAS;
import fwp.alsaccount.dao.admin.AlsNonAlsTemplate;
import fwp.alsaccount.utils.HibHelpers;

public class IntProvRemittanceNonAlsDetDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(IntProvRemittanceNonAlsDetDivAction.class);

	private Integer budgYear;
	private String tmpCdLst;
	
	public IntProvRemittanceNonAlsDetDivAction(){
	}
	
	public String input(){
		AlsNonAlsTemplateAS anatAS = new AlsNonAlsTemplateAS();
    	List<AlsNonAlsTemplate> anatLst;
		if(budgYear == null){
			HibHelpers hh = new HibHelpers();
			budgYear = Integer.parseInt(hh.getCurrentBudgetYear());
		}
		String srchStr = " where idPk.anatBudgetYear = "+budgYear;
    	String orderStr = " order by idPk.anatCd";
		try{
			anatLst = anatAS.findAllByWhere(srchStr+orderStr);
        	StringBuilder sb = new StringBuilder(":-- Select One --;");
        	for(AlsNonAlsTemplate anat : anatLst){
				sb.append(anat.getIdPk().getAnatCd()+":"+anat.getIdPk().getAnatCd()+"  -  "+anat.getAnatDesc()+(anatLst.indexOf(anat) != anatLst.size()-1?";":""));
			}
        	setTmpCdLst(sb.toString());
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsNonAlsTemplateCdList did not load " + re.getMessage());
        }
		
		return SUCCESS;
	}

	public String execute(){
		return SUCCESS;
	}
	
	public String getTmpCdLst() {
		return tmpCdLst;
	}
	public void setTmpCdLst(String tmpCdLst) {
		this.tmpCdLst = tmpCdLst;
	}
	public Integer getBudgYear() {
		return budgYear;
	}
	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}
	
}
