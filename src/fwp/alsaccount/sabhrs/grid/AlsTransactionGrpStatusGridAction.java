package fwp.alsaccount.sabhrs.grid;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.dto.sabhrs.AlsTransactionGrpStatusDTO;
import fwp.alsaccount.utils.HibHelpers;

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
	private Integer provNo;
    private Integer transGrpType;
    private String transGrpIdentifier;

	@SuppressWarnings("unchecked")
	public String buildgrid(){  
    	String srchStr = buildQueryStr();
    	
    	AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
    	List<AlsTransactionGrpStatus> atgs = new ArrayList<AlsTransactionGrpStatus>();
    	
        try{
        	atgs = atgsAS.findAllByWhere(srchStr);
        	
        	setModel(new ArrayList<AlsTransactionGrpStatusDTO>());
        	AlsTransactionGrpStatusDTO tmp;
        	AlsTransactionGrpStatusDTO tmp2 = null;
        	String curDate = null;
        	for(AlsTransactionGrpStatus a : atgs){
        		tmp = new AlsTransactionGrpStatusDTO();
        		tmp.setGridKey(a.getIdPk().getAtgTransactionCd()+"_"+a.getIdPk().getAtgsGroupIdentifier());
        		tmp.setIdPk(a.getIdPk());
        		tmp.setDesc(atgsAS.getTransDesc(a.getIdPk().getAtgTransactionCd()));
        		tmp.setAtgsWhenCreated(a.getAtgsWhenCreated());
        		tmp.setAtgsNetDrCr(a.getAtgsNetDrCr());
        		
        		/*If transaction group of 8 exclude all but the last sequence number*/
        		
        		if(a.getIdPk().getAtgTransactionCd()!=8){
        			model.add(tmp);
        		}else{
        			if(curDate == null){
        				curDate = a.getIdPk().getAtgsGroupIdentifier().substring(8, 18);
        			}else if(!curDate.equals(a.getIdPk().getAtgsGroupIdentifier().substring(8, 18))){
        				String t = tmp2.getIdPk().getAtgsGroupIdentifier().subSequence(19, 22).toString();
        				if(!"001".equals(tmp2.getIdPk().getAtgsGroupIdentifier().subSequence(19, 22))){
        					model.add(tmp2);
        				}
        				curDate = a.getIdPk().getAtgsGroupIdentifier().substring(8, 18);
        			}
        			tmp2=tmp;
        		}
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
	
	private String buildQueryStr(){
		HibHelpers hh = new HibHelpers();
		if(budgYear == null || "".equals(budgYear)){
			budgYear =  Integer.parseInt(hh.getCurrentBudgetYear());
		}
		StringBuilder srchStr = new StringBuilder();

		srchStr.append("WHERE atgsFileCreationDt IS NULL "
    				   + " AND  NVL(atgsSummaryStatus,'X')<>'N' "
    				   + "AND ((TO_CHAR(atgsWhenCreated,'YYYY')= "+budgYear+") "
    				   + "  or (TO_CHAR(atgsWhenCreated,'YYYY')= "+(budgYear+1)+") "
    				   + "  or (TO_CHAR(atgsWhenCreated,'YYYY')= "+(budgYear-1)+")) ");
		
		if(transGrpType != null && !"".equals(transGrpType)){
			srchStr.append("AND idPk.atgTransactionCd = "+transGrpType+" ");		
		}
		if(transGrpIdentifier != null && !"".equals(transGrpIdentifier)){
			srchStr.append("AND idPk.atgsGroupIdentifier like UPPER('"+transGrpIdentifier+"%') ");		
		}
		if(provNo != null && !"".equals(provNo)){
			srchStr.append("AND TRIM(TRIM(LEADING 0 FROM substr(idPk.atgsGroupIdentifier,3,6))) = '"+provNo+"' ");
		}
		
		srchStr.append("ORDER BY idPk.atgTransactionCd,idPk.atgsGroupIdentifier");
		
		return srchStr.toString();
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
	
	public Integer getProvNo() {
		return provNo;
	}


	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}


	public Integer getTransGrpType() {
		return transGrpType;
	}


	public void setTransGrpType(Integer transGrpType) {
		this.transGrpType = transGrpType;
	}


	public String getTransGrpIdentifier() {
		return transGrpIdentifier;
	}


	public void setTransGrpIdentifier(String transGrpIdentifier) {
		this.transGrpIdentifier = transGrpIdentifier;
	}

}
