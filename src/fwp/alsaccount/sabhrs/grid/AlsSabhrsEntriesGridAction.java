package fwp.alsaccount.sabhrs.grid;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;

public class AlsSabhrsEntriesGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsSabhrsEntriesGridAction.class);

    private List<AlsSabhrsEntriesDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    private Integer budgYear;
    private Integer transGrp;
    private String transIdentifier;

	@SuppressWarnings("unchecked")
	public String buildgrid(){ 
		HibHelpers hh = new HibHelpers();
		Integer curBudgYear = Integer.parseInt(hh.getCurrentBudgetYear());
		
    	String srchStr = "WHERE asacBudgetYear ="+curBudgYear+" "+
    					 "AND atgsGroupIdentifier ='"+transIdentifier+"'"+
    					 "AND atgTransactionCd ="+transGrp;
    	String orderStr = "";
    	
    	if(filters != null && !"".equals(filters)){
    		//srchStr = buildStr(srchStr);
    		srchStr = Utils.buildStr(srchStr, filters);
    		if(srchStr.contains("Build String Error:")){
    			addActionError(srchStr);
    		}
    	}
    	
    	AlsSabhrsEntriesAS aaccAS = new AlsSabhrsEntriesAS();
    	List<AlsSabhrsEntries> aacc;
    	
        try{
        	aacc = aaccAS.findAllByWhere(srchStr+orderStr);
        	
			setModel(new ArrayList<AlsSabhrsEntriesDTO>());
			AlsSabhrsEntriesDTO tmp;

			
        	for(AlsSabhrsEntries aa : aacc){
				tmp = new AlsSabhrsEntriesDTO();

				tmp.setGridKey(aa.getIdPk().getAseWhenEntryPosted()+"_"+aa.getIdPk().getAseSeqNo()+"_"+aa.getIdPk().getAseDrCrCd()+"_"+aa.getIdPk().getAseTxnCdSeqNo());
				tmp.setIdPk(aa.getIdPk());
				tmp.setAsacBudgetYear(aa.getAsacBudgetYear());
				tmp.setAsacReference(aa.getAsacReference());
				tmp.setAamAccount(aa.getAamAccount());
				tmp.setAamFund(aa.getAamFund());
				tmp.setAocOrg(aa.getAocOrg());
				tmp.setAsacProgram(aa.getAsacProgram());
				tmp.setAsacSubclass(aa.getAsacSubclass());
				tmp.setAamBusinessUnit(aa.getAamBusinessUnit());
				tmp.setAsacProjectGrant(aa.getAsacProjectGrant());
				tmp.setAseAmt(aa.getAseAmt());
				tmp.setAsacSystemActivityTypeCd(aa.getAsacSystemActivityTypeCd());
				tmp.setAsacTxnCd(aa.getAsacTxnCd());
				tmp.setAseLineDescription(aa.getAseLineDescription());
				
				model.add(tmp);
			}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsSabhrsEntries did not load " + re.getMessage());
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

	public List<AlsSabhrsEntriesDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsSabhrsEntriesDTO> model) {
		this.model = model;
	}
    

    public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}

	/**
	 * @return the transGrp
	 */
	public Integer getTransGrp() {
		return transGrp;
	}

	/**
	 * @param transGrp the transGrp to set
	 */
	public void setTransGrp(Integer transGrp) {
		this.transGrp = transGrp;
	}
	
	/**
	 * @return the transIndetifier
	 */
	public String getTransIdentifier() {
		return transIdentifier;
	}

	/**
	 * @param transIndetifier the transIndetifier to set
	 */
	public void setTransIdentifier(String transIndetifier) {
		this.transIdentifier = transIndetifier;
	}

}
