package fwp.alsaccount.admin.grid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.admin.appservice.AlsSabhrsFyeAdjstDtlAS;
import fwp.alsaccount.admin.dto.AlsSabhrsFyeAdjstDtlDTO;
import fwp.alsaccount.hibernate.dao.AlsSabhrsFyeAdjstDtl;
import fwp.alsaccount.utils.Utils;

public class AlsSabhrsFyeAdjstDtlGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsSabhrsFyeAdjstDtlGridAction.class);

    private List<AlsSabhrsFyeAdjstDtlDTO>    model;
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	String srchStr = " where idPk.asfaBudgetYear = "+budgYear;
    	String orderStr = " order by idPk.asfadAdjstDt";
    	
    	if(filters != null && !"".equals(filters)){
    		//srchStr = buildStr(srchStr);
    		srchStr = Utils.buildStr(srchStr, filters);
    		if(srchStr.contains("Build String Error:")){
    			addActionError(srchStr);
    		}
    	}
    	
    	AlsSabhrsFyeAdjstDtlAS aaccAS = new AlsSabhrsFyeAdjstDtlAS();
    	List<AlsSabhrsFyeAdjstDtl> aacc;
    	
        try{
        	aacc = aaccAS.findAllByWhere(srchStr+orderStr);
        	
			setModel(new ArrayList<AlsSabhrsFyeAdjstDtlDTO>());
			AlsSabhrsFyeAdjstDtlDTO tmp;
			
        	for(AlsSabhrsFyeAdjstDtl aa : aacc){
				tmp = new AlsSabhrsFyeAdjstDtlDTO();
				tmp.setGridKey(sdf.format(aa.getIdPk().getAsfadAdjstDt()));
				tmp.setIdPk(aa.getIdPk());
				model.add(tmp);
			}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsSabhrsFyeAdjstDtl did not load " + re.getMessage());
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
	public List<AlsSabhrsFyeAdjstDtlDTO> getModel() {
		return model;
	}


	/**
	 * @param model the model to set
	 */
	public void setModel(List<AlsSabhrsFyeAdjstDtlDTO> model) {
		this.model = model;
	}
	

}
