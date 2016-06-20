package fwp.alsaccount.sabhrs.grid;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.dto.sabhrs.AlsTransactionGrpStatusDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;

public class AlsTransactionGrpStatusGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsTransactionGrpStatusGridAction.class);

    private List<AlsTransactionGrpStatusDTO>    model;
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
		HibHelpers hh = new HibHelpers();
		Integer curBudgYear = Integer.parseInt(hh.getCurrentBudgetYear());
    	String srchStr = " WHERE ATGS_FILE_CREATION_DT IS NULL"
    				   + " AND  NVL(ATGS_SUMMARY_STATUS,'X')<>'N'"
    				   + "AND ((TO_CHAR(ATGS_WHEN_CREATED,'YYYY')= "+curBudgYear+") "
    				   + "  or (TO_CHAR(ATGS_WHEN_CREATED,'YYYY')= "+(curBudgYear+1)+")"
    				   + "  or (TO_CHAR(ATGS_WHEN_CREATED,'YYYY')= "+(curBudgYear-1)+"))";
    	String orderStr = " ORDER BY ATG_TRANSACTION_CD,ATGS_GROUP_IDENTIFIER";
    	
    	if(filters != null && !"".equals(filters)){
    		//srchStr = buildStr(srchStr);
    		srchStr = Utils.buildStr(srchStr, filters);
    		if(srchStr.contains("Build String Error:")){
    			addActionError(srchStr);
    		}
    	}
    	
    	AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
    	List<AlsTransactionGrpStatus> atgs = new ArrayList<AlsTransactionGrpStatus>();
    	
        try{
        	atgs = atgsAS.findAllByWhere(srchStr+orderStr);
        	
        	setModel(new ArrayList<AlsTransactionGrpStatusDTO>());
        	AlsTransactionGrpStatusDTO tmp;
        	
        	for(AlsTransactionGrpStatus a : atgs){
        		tmp = new AlsTransactionGrpStatusDTO();
        		tmp.setGridKey(a.getIdPk().getAtgTransactionCd()+"_"+a.getIdPk().getAtgsGroupIdentifier());
        		tmp.setIdPk(a.getIdPk());
        		tmp.setDesc(atgsAS.getTransDesc(a.getIdPk().getAtgTransactionCd()));
        		tmp.setCreateYear(Utils.YearFromTimestamp(a.getAtgsWhenCreated()));
        		tmp.setAtgsNetDrCr(a.getAtgsNetDrCr());
        		model.add(tmp);
        	}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsTransactionGroup did not load " + re.getMessage());
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


	/**
	 * @return the model
	 */
	public List<AlsTransactionGrpStatusDTO> getModel() {
		return model;
	}


	/**
	 * @param model the model to set
	 */
	public void setModel(List<AlsTransactionGrpStatusDTO> model) {
		this.model = model;
	}
	
	

}
