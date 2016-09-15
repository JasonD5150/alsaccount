package fwp.alsaccount.sabhrs.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;

public class ManualProviderAdjEntriesSABHRSGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(ManualProviderAdjEntriesSABHRSGridAction.class);

    private List<AlsSabhrsEntriesDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    
    private Integer 			selProvNo;
    private Integer 			selIafaSeqNo;
    private Date				selBpToDt;
    private Date				selBpFromDt;
    private Integer 			transCd;
    private String 				groupId;

	@SuppressWarnings("unchecked")
	public String buildgrid(){ 		
    	AlsSabhrsEntriesAS aaccAS = new AlsSabhrsEntriesAS();
    	List<AlsSabhrsEntries> aacc;
    	
        try{
        	aacc = aaccAS.getManualProviderAdjEntriesRecords(transCd, groupId.substring(0, groupId.length()-3)+'%', selProvNo, selIafaSeqNo, selBpFromDt, selBpToDt);
        	
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

	public List<AlsSabhrsEntriesDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsSabhrsEntriesDTO> model) {
		this.model = model;
	}

	

	public Integer getSelProvNo() {
		return selProvNo;
	}


	public void setSelProvNo(Integer selProvNo) {
		this.selProvNo = selProvNo;
	}
	
	public Integer getSelIafaSeqNo() {
		return selIafaSeqNo;
	}


	public void setSelIafaSeqNo(Integer selIafaSeqNo) {
		this.selIafaSeqNo = selIafaSeqNo;
	}


	public Date getSelBpToDt() {
		return selBpToDt;
	}


	public void setSelBpToDt(Date selBpToDt) {
		this.selBpToDt = selBpToDt;
	}


	public Date getSelBpFromDt() {
		return selBpFromDt;
	}


	public void setSelBpFromDt(Date selBpFromDt) {
		this.selBpFromDt = selBpFromDt;
	}


	public Integer getTransCd() {
		return transCd;
	}


	public void setTransCd(Integer transCd) {
		this.transCd = transCd;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
    
	
}
