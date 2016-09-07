package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.dto.sabhrs.IafaDetailsDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;

public class IafaQueryGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(IafaQueryGridAction.class);

    private List<IafaDetailsDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    
    private Integer 			issProvNo;
    private Date				fromDt;
    private Date 				toDt;
    
    private Boolean				alxInd;
    private Integer				entProvNo;
    private Date 				bpFromDt;
    private Date 				bpToDt;
    private Integer 			iafaSeqNo;
    
    private String 				sessStat;
    private Date				upFromDt;
    private Date 				upToDt;
    private String 				tribeCd;
    private String 				appType;
    
    private String 				itemTypeCd;
    private String				noCharge;
    private Double				amount;
    
    private String 				itemInd;
    private String 				itemStat;
    
    private String 				itemCatCd;
    private String 				appDis;
    
    private String 				itemTransInd;
    private Integer 			seqNoInItemTrans;
    private Date 				dob;
    private Integer				alsNo;
    private String				bonusPoints;
    
    private String 				amountTypeCd;
    private String 				costPrereqCd;
    
    private String 				procCatCd;
    private String 				sessOrigin;
    private String				resIndicator;

	private String				procTypeCd;
    private Date				batchRecDt;
    
    private String 				reasonCd;
    private String 				transGrpIdentifier;
    private Boolean 				nullTGI;
    
    private String 				ahmType;
    private String 				ahmCd;
	private Date				sessDt;
    private Date				sessVoidDt;
    private Date				recordVoidDt;
    
    private String 				sumAppStat;
    private String				intAppStat;
    private String 				otherTransGrp;
    
    private String				modeOfPayment;
    private Integer				chckNo;
    private String 				chckWriter;
    private String 				remarks;
    
    private Boolean 			search = false;
    private String 				userdata;
    
	public String buildgrid(){ 
    	HibHelpers hh = new HibHelpers();
    	
    	List<IafaDetailsDTO> iafaLst = new ArrayList<IafaDetailsDTO>();
    	IafaDetailsDTO iafa = null;
        try{
    		iafaLst = hh.getIafaQueryRecords(fromDt, Utils.convertJavaDateToSqlDate(Utils.addDays(toDt, 1)), issProvNo, entProvNo, 
											bpFromDt, bpToDt, upFromDt, upToDt,
											modeOfPayment, chckNo, chckWriter, remarks,
											iafaSeqNo, dob, alsNo, transGrpIdentifier,
											sumAppStat, intAppStat, ahmType, ahmCd,
											recordVoidDt, tribeCd, appType, amountTypeCd,
											amount, sessDt, sessVoidDt, sessStat, reasonCd,
											itemTypeCd, itemCatCd, bonusPoints, itemInd,
											itemStat, costPrereqCd, resIndicator, appDis,
											procCatCd, procTypeCd, batchRecDt, noCharge, 
											itemTransInd, seqNoInItemTrans, alxInd, nullTGI);
    		setModel(new ArrayList<IafaDetailsDTO>());
    		/*if(iafaLst.size() > 10000){
    			setUserdata("Please narrow search. The search grid is limited to 10000 rows. There were " + iafaLst.size() + " entries selected.");
        	}else{
    			
    		}*/
    		for(IafaDetailsDTO tmp : iafaLst){
    			iafa = tmp;
    			iafa.setOtherTxnGrp(hh.getOtherTxnGrp(iafa.getApiProviderNo(), iafa.getAprBillingFrom(), iafa.getAprBillingTo(), iafa.getAiafaSeqNo(), iafa.getAtgsGroupIdentifier()));
    			if(iafa.getAcdCostGroupSeqNo() != null){
    				if(iafa.getAcdCostGroupSeqNo() == 0){
        				iafa.setCostGrpDesc("INDIVIDUAL");
        			}else{
        				iafa.setCostGrpDesc(hh.getCostGrpDesc(iafa.getAictUsagePeriodFrom(), iafa.getAictUsagePeriodTo(), iafa.getAictItemTypeCd(), iafa.getResidency(), iafa.getAcdCostGroupSeqNo()));
        			}
    			}
    			iafa.setSessionTotal(hh.getSessionTotal(iafa.getAhmType(), iafa.getAhmCd(), iafa.getAsSessionDt(), fromDt, toDt));
    			iafa.setSessionDt(iafa.getAsSessionDt());
    			iafa.setSeqNoforPrintedItems(hh.getSeqNoForPrintedItems(iafa.getDob(), iafa.getAlsNo(), iafa.getAictUsagePeriodFrom(), iafa.getAictUsagePeriodTo(), iafa.getAictItemTypeCd(), iafa.getAiiItemTxnInd(), iafa.getAiiSeqNo()));
    			model.add(iafa);
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

	public List<IafaDetailsDTO> getModel() {
		return model;
	}

	public void setModel(ArrayList<IafaDetailsDTO> arrayList) {
		this.model = arrayList;
	}

	public Integer getIssProvNo() {
		return issProvNo;
	}

	public void setIssProvNo(Integer issProvNo) {
		this.issProvNo = issProvNo;
	}

	public Date getFromDt() {
		return fromDt;
	}

	public void setFromDt(Date fromDt) {
		this.fromDt = fromDt;
	}

	public Date getToDt() {
		return toDt;
	}

	public void setToDt(Date toDt) {
		this.toDt = toDt;
	}

	public Boolean getAlxInd() {
		return alxInd;
	}

	public void setAlxInd(Boolean alxInd) {
		this.alxInd = alxInd;
	}

	public Integer getEntProvNo() {
		return entProvNo;
	}

	public void setEntProvNo(Integer entProvNo) {
		this.entProvNo = entProvNo;
	}

	public Date getBpFromDt() {
		return bpFromDt;
	}

	public void setBpFromDt(Date bpFromDt) {
		this.bpFromDt = bpFromDt;
	}

	public Date getBpToDt() {
		return bpToDt;
	}

	public void setBpToDt(Date bpToDt) {
		this.bpToDt = bpToDt;
	}

	public Integer getIafaSeqNo() {
		return iafaSeqNo;
	}

	public void setIafaSeqNo(Integer iafaSeqNo) {
		this.iafaSeqNo = iafaSeqNo;
	}

	public String getSessStat() {
		return sessStat;
	}

	public void setSessStat(String sessStat) {
		this.sessStat = sessStat;
	}

	public Date getUpFromDt() {
		return upFromDt;
	}

	public void setUpFromDt(Date upFromDt) {
		this.upFromDt = upFromDt;
	}

	public Date getUpToDt() {
		return upToDt;
	}

	public void setUpToDt(Date upToDt) {
		this.upToDt = upToDt;
	}

	public String getTribeCd() {
		return tribeCd;
	}

	public void setTribeCd(String tribeCd) {
		this.tribeCd = tribeCd;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getItemTypeCd() {
		return itemTypeCd;
	}

	public void setItemTypeCd(String itemTypeCd) {
		this.itemTypeCd = itemTypeCd;
	}

	public String getNoCharge() {
		return noCharge;
	}

	public void setNoCharge(String noCharge) {
		this.noCharge = noCharge;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getItemInd() {
		return itemInd;
	}

	public void setItemInd(String itemInd) {
		this.itemInd = itemInd;
	}

	public String getItemStat() {
		return itemStat;
	}

	public void setItemStat(String itemStat) {
		this.itemStat = itemStat;
	}

	public String getItemCatCd() {
		return itemCatCd;
	}

	public void setItemCatCd(String itemCatCd) {
		this.itemCatCd = itemCatCd;
	}

	public String getAppDis() {
		return appDis;
	}

	public void setAppDis(String appDis) {
		this.appDis = appDis;
	}

	public String getItemTransInd() {
		return itemTransInd;
	}

	public void setItemTransInd(String itemTransInd) {
		this.itemTransInd = itemTransInd;
	}

	public Integer getSeqNoInItemTrans() {
		return seqNoInItemTrans;
	}

	public void setSeqNoInItemTrans(Integer seqNoInItemTrans) {
		this.seqNoInItemTrans = seqNoInItemTrans;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Integer getAlsNo() {
		return alsNo;
	}

	public void setAlsNo(Integer alsNo) {
		this.alsNo = alsNo;
	}

	public String getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(String bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public String getAmountTypeCd() {
		return amountTypeCd;
	}

	public void setAmountTypeCd(String amountTypeCd) {
		this.amountTypeCd = amountTypeCd;
	}

	public String getCostPrereqCd() {
		return costPrereqCd;
	}

	public void setCostPrereqCd(String costPrereqCd) {
		this.costPrereqCd = costPrereqCd;
	}

	public String getProcCatCd() {
		return procCatCd;
	}

	public void setProcCatCd(String procCatCd) {
		this.procCatCd = procCatCd;
	}

	public String getSessOrigin() {
		return sessOrigin;
	}

	public void setSessOrigin(String sessOrigin) {
		this.sessOrigin = sessOrigin;
	}

	public String getProcTypeCd() {
		return procTypeCd;
	}

	public void setProcTypeCd(String procTypeCd) {
		this.procTypeCd = procTypeCd;
	}

	public Date getBatchRecDt() {
		return batchRecDt;
	}

	public void setBatchRecDt(Date batchRecDt) {
		this.batchRecDt = batchRecDt;
	}

	public String getReasonCd() {
		return reasonCd;
	}

	public void setReasonCd(String reasonCd) {
		this.reasonCd = reasonCd;
	}

	public String getTransGrpIdentifier() {
		return transGrpIdentifier;
	}

	public void setTransGrpIdentifier(String transGrpIdentifier) {
		this.transGrpIdentifier = transGrpIdentifier;
	}

	public Boolean getNullTGI() {
		return nullTGI;
	}

	public void setNullTGI(Boolean nullTGI) {
		this.nullTGI = nullTGI;
	}

	public String getAhmType() {
		return ahmType;
	}

	public void setAhmType(String ahmType) {
		this.ahmType = ahmType;
	}

	public String getAhmCd() {
		return ahmCd;
	}

	public void setAhmCd(String ahmCd) {
		this.ahmCd = ahmCd;
	}

	public Date getSessDt() {
		return sessDt;
	}

	public void setSessDt(Date sessDt) {
		this.sessDt = sessDt;
	}

	public Date getSessVoidDt() {
		return sessVoidDt;
	}

	public void setSessVoidDt(Date sessVoidDt) {
		this.sessVoidDt = sessVoidDt;
	}

	public Date getRecordVoidDt() {
		return recordVoidDt;
	}

	public void setRecordVoidDt(Date recordVoidDt) {
		this.recordVoidDt = recordVoidDt;
	}

	public String getSumAppStat() {
		return sumAppStat;
	}

	public void setSumAppStat(String sumAppStat) {
		this.sumAppStat = sumAppStat;
	}

	public String getIntAppStat() {
		return intAppStat;
	}

	public void setIntAppStat(String intAppStat) {
		this.intAppStat = intAppStat;
	}

	public String getOtherTransGrp() {
		return otherTransGrp;
	}

	public void setOtherTransGrp(String otherTransGrp) {
		this.otherTransGrp = otherTransGrp;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public Integer getChckNo() {
		return chckNo;
	}

	public void setChckNo(Integer chckNo) {
		this.chckNo = chckNo;
	}

	public String getChckWriter() {
		return chckWriter;
	}

	public void setChckWriter(String chckWriter) {
		this.chckWriter = chckWriter;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getSearch() {
		return search;
	}

	public void setSearch(Boolean search) {
		this.search = search;
	}
	
	public String getResIndicator() {
		return resIndicator;
	}

	public void setResIndicator(String resIndicator) {
		this.resIndicator = resIndicator;
	}

	/**
	 * @return the userdata
	 */
	public String getUserdata() {
		return userdata;
	}

	/**
	 * @param userdata the userdata to set
	 */
	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}
    
	
}
