package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.appservice.inventory.AlsProviderRemittanceAS;
import fwp.als.hibernate.inventory.dao.AlsInternalRemittanceIdPk;
import fwp.als.hibernate.inventory.dao.AlsProviderRemittance;
import fwp.als.hibernate.inventory.dao.AlsProviderRemittanceIdPk;
import fwp.alsaccount.appservice.sabhrs.AlsInternalRemittanceAS;
import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesIdPk;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.dto.sabhrs.AlsInternalRemittanceDTO;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;

public class ProvAdjEntSABHRSGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(ProvAdjEntSABHRSGridAction.class);

    private List<AlsSabhrsEntriesDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    private String 				userdata;
    
    private Integer				provNo;
    private Date 				bpFrom;
    private Date 				bpTo;
    private Integer				iafaSeqNo;
    private Boolean 			remittanceInd;

	public String buildgrid(){ 
        try{
        	setModel(new ArrayList<AlsSabhrsEntriesDTO>());   
        	if(validateForm()){
        		AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
    			List<AlsSabhrsEntries> aseLst = new ArrayList<AlsSabhrsEntries>();
    			
    			if(remittanceInd){
    				aseLst = aseAS.getManualProviderAdjEntriesRecords(provNo, iafaSeqNo, bpFrom, bpTo);
            	}else{
            		aseLst = aseAS.getProvAdjEntRecords(provNo, bpFrom, bpTo, iafaSeqNo);
            	}
        		
        		if(aseLst.size() > 10000){
        			setUserdata("Please narrow search. The search grid is limited to 10000 rows. There were " + aseLst.size() + " entries selected.");
        		}else{
        			AlsSabhrsEntriesDTO ase = null;
        			AlsSabhrsEntriesIdPk aseIdPk = null;
        			for(AlsSabhrsEntries tmp : aseLst){ 
        				ase = new AlsSabhrsEntriesDTO();
        				aseIdPk = new AlsSabhrsEntriesIdPk();
        				aseIdPk.setAseDrCrCd(tmp.getIdPk().getAseDrCrCd());
        				aseIdPk.setAseSeqNo(tmp.getIdPk().getAseSeqNo());
        				aseIdPk.setAseTxnCdSeqNo(tmp.getIdPk().getAseTxnCdSeqNo());
        				aseIdPk.setAseWhenEntryPosted(tmp.getIdPk().getAseWhenEntryPosted());
        				ase.setIdPk(aseIdPk);
        				ase.setGridKey(aseIdPk.getAseDrCrCd()+"_"+aseIdPk.getAseSeqNo()+"_"+aseIdPk.getAseTxnCdSeqNo()+"_"+aseIdPk.getAseWhenEntryPosted());
        				ase.setAamBusinessUnit(tmp.getAamBusinessUnit());
        				ase.setAsacReference(tmp.getAsacReference());
        				ase.setAamAccount(tmp.getAamAccount());
        				ase.setAamFund(tmp.getAamFund());
        				ase.setAocOrg(tmp.getAocOrg());
        				ase.setAsacProgram(tmp.getAsacProgram());
        				ase.setAsacSubclass(tmp.getAsacSubclass());
        				ase.setAsacBudgetYear(tmp.getAsacBudgetYear());
        				ase.setAsacSystemActivityTypeCd(tmp.getAsacSystemActivityTypeCd());
        				ase.setAsacTxnCd(tmp.getAsacTxnCd());
        				ase.setAseLineDescription(tmp.getAseLineDescription());
        				ase.setAseAmt(tmp.getAseAmt());
        				
            			model.add(ase);
            		}
        		}
        		
        	}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("ProvAdjEnt did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }

	private Boolean validateForm(){
		Boolean rtn = false;
		if(!Utils.isNil(provNo)){
			rtn = true;
		}
		return rtn;
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

	public Date getBpFrom() {
		return bpFrom;
	}

	public void setBpFrom(Date bpFrom) {
		this.bpFrom = bpFrom;
	}

	public Date getBpTo() {
		return bpTo;
	}

	public void setBpTo(Date bpTo) {
		this.bpTo = bpTo;
	}

	public Integer getProvNo() {
		return provNo;
	}

	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}

	public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}

	public Integer getIafaSeqNo() {
		return iafaSeqNo;
	}

	public void setIafaSeqNo(Integer iafaSeqNo) {
		this.iafaSeqNo = iafaSeqNo;
	}

	public Boolean getRemittanceInd() {
		return remittanceInd;
	}

	public void setRemittanceInd(Boolean remittanceInd) {
		this.remittanceInd = remittanceInd;
	}

	
	
}
