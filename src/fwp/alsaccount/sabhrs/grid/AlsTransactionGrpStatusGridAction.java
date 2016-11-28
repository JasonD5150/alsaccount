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
    private String transGrpId;

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
		if(transGrpId != null && !"".equals(transGrpId)){
			srchStr.append("AND idPk.atgsGroupIdentifier LIKE UPPER('"+transGrpId+"%') ");		
		}
		if(provNo != null && !"".equals(provNo)){
			srchStr.append("AND idPk.atgsGroupIdentifier LIKE '%"+provNo+"%' ");
			//srchStr.append("AND TRIM(TRIM(LEADING 0 FROM substr(idPk.atgsGroupIdentifier,3,6))) = '"+provNo+"' ");
		}
		
		srchStr.append("ORDER BY idPk.atgTransactionCd,idPk.atgsGroupIdentifier DESC");
		
		return srchStr.toString();
	}
	
	public String getJSON()
	{
		return buildgrid();
	}

    public Integer getRows() {
        return rows;
    }
    
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public boolean isLoadonce() {
        return loadonce;
    }

    public void setLoadonce(boolean loadonce) {
        this.loadonce = loadonce;
    }

    public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}

	public List<AlsTransactionGrpStatusDTO> getModel() {
		return model;
	}

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

	public String getTransGrpId() {
		return transGrpId;
	}

	public void setTransGrpId(String transGrpId) {
		this.transGrpId = transGrpId;
	}

}
