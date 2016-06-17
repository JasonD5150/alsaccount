package fwp.alsaccount.admin.grid;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsSysActivityControlAS;
import fwp.alsaccount.appservice.admin.AlsSysActivityTypeCodesAS;
import fwp.alsaccount.appservice.admin.AlsSysActivityTypeTranCdsAS;
import fwp.alsaccount.dao.admin.AlsSysActivityControl;
import fwp.alsaccount.dto.admin.AlsSysActivityControlDTO;
import fwp.alsaccount.utils.Utils;

public class AlsSysActivityControlGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsSysActivityControlGridAction.class);

    private List<AlsSysActivityControlDTO>    model;
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
    	String srchStr = " where idPk.asacBudgetYear = "+budgYear;
    	String orderStr = " order by idPk.asacBudgetYear desc,idPk.asacSystemActivityTypeCd,cast(idPk.asacTxnCd as int) asc";

    	
    	if(filters != null && !"".equals(filters)){
    		//srchStr = buildStr(srchStr);
    		srchStr = Utils.buildStr(srchStr, filters);
    		if(srchStr.contains("Build String Error:")){
    			addActionError(srchStr);
    		}
    	}
    	
    	AlsSysActivityControlAS appSer = new AlsSysActivityControlAS();
    	List<AlsSysActivityControl> asac;
    	
        try{
        	asac = appSer.findAllByWhere(srchStr+orderStr);
			
        	setModel(new ArrayList<AlsSysActivityControlDTO>());
        	AlsSysActivityControlDTO tmp;
        	AlsSysActivityTypeCodesAS asatcAS = new AlsSysActivityTypeCodesAS();
        	AlsSysActivityTypeTranCdsAS asattcAS = new AlsSysActivityTypeTranCdsAS();
        	
        	for(AlsSysActivityControl as : asac){
        		tmp = new AlsSysActivityControlDTO();
				tmp.setIdPk(as.getIdPk());
				tmp.setAsacProgram(as.getAsacProgram());
				tmp.setAsacSysActivityTypeCdDesc(as.getAsacSysActivityTypeCdDesc());
				tmp.setAsacProjectGrant(as.getAsacProjectGrant());
				tmp.setGridKey(as.getIdPk().getAsacBudgetYear()+"_"+as.getIdPk().getAsacSystemActivityTypeCd()+"_"+as.getIdPk().getAsacTxnCd());
				tmp.setSysActivityTypeDesc(asatcAS.getCodeDesc(as.getIdPk().getAsacSystemActivityTypeCd()));
				tmp.setSysTranCodeDesc(asattcAS.getCodeDesc(as.getIdPk().getAsacSystemActivityTypeCd(),as.getIdPk().getAsacTxnCd()));
				model.add(tmp);
			}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsSysActivityControl did not load " + re.getMessage());
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

	public List<AlsSysActivityControlDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsSysActivityControlDTO> model) {
		this.model = model;
	}

	public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}


 
}
