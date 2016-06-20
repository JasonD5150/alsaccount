package fwp.alsaccount.admin.grid;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsNonAlsTemplateAS;
import fwp.alsaccount.dao.admin.AlsNonAlsTemplate;
import fwp.alsaccount.dto.admin.AlsNonAlsTemplateDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;

public class AlsNonAlsTemplateGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsNonAlsTemplateGridAction.class);

    private List<AlsNonAlsTemplateDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    private Integer budgYear;

	@SuppressWarnings("unchecked")
	public String buildgrid(){  
		if(budgYear == null){
			HibHelpers hh = new HibHelpers();
			budgYear = Integer.parseInt(hh.getCurrentBudgetYear());
		}
    	String srchStr = " where idPk.anatBudgetYear = "+budgYear;
    	String orderStr = " order by idPk.anatCd";
    	
    	if(filters != null && !"".equals(filters)){
    		//srchStr = buildStr(srchStr);
    		srchStr = Utils.buildStr(srchStr, filters);
    		if(srchStr.contains("Build String Error:")){
    			addActionError(srchStr);
    		}
    	}
    	
    	AlsNonAlsTemplateAS anatAS = new AlsNonAlsTemplateAS();
    	List<AlsNonAlsTemplate> anat;
    	
        try{
        	anat = anatAS.findAllByWhere(srchStr+orderStr);
        	
			setModel(new ArrayList<AlsNonAlsTemplateDTO>());
			AlsNonAlsTemplateDTO tmp;

        	for(AlsNonAlsTemplate an : anat){
				tmp = new AlsNonAlsTemplateDTO();
				tmp.setGridKey(an.getIdPk().getAnatBudgetYear()+"_"+an.getIdPk().getAnatCd());
				tmp.setIdPk(an.getIdPk());
				tmp.setAnatBusinessUnit(an.getAnatBusinessUnit());
				tmp.setAnatCrAccount(an.getAnatCrAccount());
				if(an.getAnatCrJournalLineRefr() != null){
					tmp.setAnatCrJournalLineRefrDesc(anatAS.getJlrValDesc(an.getAnatCrJournalLineRefr()));
				}
				tmp.setAnatCrJournalLineRefr(an.getAnatCrJournalLineRefr());
				tmp.setAnatCrLineDesc(an.getAnatCrLineDesc());
				tmp.setAnatCrOrg(an.getAnatCrOrg());
				tmp.setAnatCrProjectGrant(an.getAnatCrProjectGrant());
				tmp.setAnatCrSubclass(an.getAnatCrSubclass());
				tmp.setAnatDesc(an.getAnatDesc());
				tmp.setAnatDrAccount(an.getAnatDrAccount());
				if(an.getAnatDrJournalLineRefr() != null){
					tmp.setAnatDrJournalLineRefrDesc(anatAS.getJlrValDesc(an.getAnatDrJournalLineRefr()));
				}
				tmp.setAnatDrJournalLineRefr(an.getAnatDrJournalLineRefr());
				tmp.setAnatDrLineDesc(an.getAnatDrLineDesc());
				tmp.setAnatDrOrg(an.getAnatDrOrg());
				tmp.setAnatDrProjectGrant(an.getAnatDrProjectGrant());
				tmp.setAnatDrSubclass(an.getAnatDrSubclass());
				tmp.setAnatFund(an.getAnatFund());
				tmp.setAnatProgramYear(an.getAnatProgramYear());
				
				model.add(tmp);
			}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsNonAlsTemplate did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }


	public String getJSON()
	{
		return buildgrid();
	}


    /**
     * @return the rows
     */
    public Integer getRows() {
        return rows;
    }
    /**
     * @param rows the rows to set
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }
    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }
    /**
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }
    /**
     * @param total the total to set
     */
    public void setTotal(Integer total) {
        this.total = total;
    }
    /**
     * @return the records
     */
    public Integer getRecords() {
        return records;
    }
    /**
     * @param records the records to set
     */
    public void setRecords(Integer records) {
        this.records = records;
    }
    /**
     * @return the sord
     */
    public String getSord() {
        return sord;
    }
    /**
     * @param sord the sord to set
     */
    public void setSord(String sord) {
        this.sord = sord;
    }
    /**
     * @return the sidx
     */
    public String getSidx() {
        return sidx;
    }
    /**
     * @param sidx the sidx to set
     */
    public void setSidx(String sidx) {
        this.sidx = sidx;
    }
    /**
     * @return the filters
     */
    public String getFilters() {
        return filters;
    }
    /**
     * @param filters the filters to set
     */
    public void setFilters(String filters) {
        this.filters = filters;
    }
    /**
     * @return the loadonce
     */
    public boolean isLoadonce() {
        return loadonce;
    }
    /**
     * @param loadonce the loadonce to set
     */
    public void setLoadonce(boolean loadonce) {
        this.loadonce = loadonce;
    }

    public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}

	public List<AlsNonAlsTemplateDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsNonAlsTemplateDTO> model) {
		this.model = model;
	}
	
	

}
