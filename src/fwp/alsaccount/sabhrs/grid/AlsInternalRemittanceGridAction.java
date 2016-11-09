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
import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.dto.sabhrs.AlsInternalRemittanceDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;

public class AlsInternalRemittanceGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsInternalRemittanceGridAction.class);

    private List<AlsInternalRemittanceDTO>    model;
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
    private String				comByProv;
    private Date				comByProvDt;
    private String				app;
    private String				appBy;
    private Date				appDt;
    private String				appCom;
    private Boolean 			hasNonAlsDetails;
    private Boolean 			hasOverShortDetails;
    private Boolean				hasPaeAmt;
    private Boolean 			srchAll;
    
	public String buildgrid(){ 
        try{
        	setModel(new ArrayList<AlsInternalRemittanceDTO>());   
        	if(validateForm()){
        		AlsInternalRemittanceAS airAS = new AlsInternalRemittanceAS();
    			List<AlsInternalRemittanceDTO> airLst = new ArrayList<AlsInternalRemittanceDTO>();
        		
        		airLst = airAS.getRemittanceRecords(provNo, bpFrom, bpTo, hasNonAlsDetails, hasOverShortDetails, hasPaeAmt, comByProv, comByProvDt, app, appBy, appDt, appCom, srchAll);
        		if(airLst.size() > 10000){
        			setUserdata("Please narrow search. The search grid is limited to 10000 rows. There were " + airLst.size() + " entries selected.");
        		}else{
        			DecimalFormat df = new DecimalFormat("#.##");
        			//df.setRoundingMode(RoundingMode.CEILING);
        			
        			AlsInternalRemittanceDTO airDTO = null;
        			AlsProviderRemittanceAS aprAS = new AlsProviderRemittanceAS();
        			AlsProviderRemittance apr = new AlsProviderRemittance();
        			AlsProviderRemittanceIdPk aprIdPk = null;
        			AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
        			List<AlsTransactionGrpStatus> atgsLst = new ArrayList<AlsTransactionGrpStatus>();
        			
        			HibHelpers hh = new HibHelpers();
        			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        			AlsInternalRemittanceIdPk airIdPk = null;
        			for(AlsInternalRemittanceDTO tmp : airLst){ 
        				sdf.applyPattern("dd/MM/yyyy");
            			airDTO = new AlsInternalRemittanceDTO();
            			airIdPk = new AlsInternalRemittanceIdPk();
            			airIdPk.setApiProviderNo(tmp.getApiProviderNo());
            			airIdPk.setAirBillingFrom(new Timestamp(tmp.getAirBillingFrom().getTime()));
            			airIdPk.setAirBillingTo(new Timestamp(tmp.getAirBillingTo().getTime()));
            			airDTO.setGridKey(sdf.format(tmp.getAirBillingFrom())+"_"+sdf.format(tmp.getAirBillingTo())+"_"+tmp.getApiProviderNo());
            			airDTO.setIdPk(airIdPk);
            			airDTO.setAirSystemSales(tmp.getAirSystemSales());
            			airDTO.setAirOtcPhoneSales(tmp.getAirOtcPhoneSales());
            			airDTO.setAirPae(tmp.getAirPae());
            			airDTO.setAirNonAlsSales(tmp.getAirNonAlsSales());
            			airDTO.setAirCreditSales(tmp.getAirCreditSales());
            			airDTO.setAirOverSales(tmp.getAirOverSales());
            			airDTO.setAirShortSales(tmp.getAirShortSales());
            			airDTO.setAirOfflnPaymentAppBy(tmp.getAirOfflnPaymentAppBy());
            			airDTO.setAirOfflnPaymentAppCom(tmp.getAirOfflnPaymentAppCom());
            			airDTO.setAirOfflnPaymentApproved(tmp.getAirOfflnPaymentApproved());
            			airDTO.setAirOfflnPaymentReviewed(tmp.getAirOfflnPaymentReviewed());

            			
            			/*SETTING DATE INSTEAD OF TIMESTAMP, exportToCSV() DOES NOT LIKE TIMESTAMPS*/
            			airDTO.setEftddd(tmp.getAirEftddd());
            			airDTO.setCompleteProvider(tmp.getCompleteProvider());
            			airDTO.setOfflnPaymentAppDt(tmp.getOfflnPaymentAppDt());
            			
            			/*HIBHELPERs*/
            				/*GET PROVIDER NAME*/
            			airDTO.setProvNm(hh.getProviderName(tmp.getApiProviderNo()));
            				/*GET TOTAL BANK DEPOSIT*/
            			airDTO.setTotBankDep(hh.getTotalBankDeposit(tmp.getApiProviderNo(), new Date(tmp.getAirBillingFrom().getTime()), new Date(tmp.getAirBillingTo().getTime())));
            				/*INTERFACE FILE GENERATED, controls if the grids can be edited or not*/
            			airDTO.setIntFileGenerated(hh.getInterfaceFileGenerated(tmp.getApiProviderNo(), new Date(tmp.getAirBillingTo().getTime())));
            			
            			/*GET AMOUNT DUE AND RECEIVED FROM ALS_PROVIDER_REMITTANCE*/
            			aprIdPk = new AlsProviderRemittanceIdPk();
            			aprIdPk.setApiProviderNo(tmp.getApiProviderNo());
            			aprIdPk.setAprBillingFrom(new Timestamp(tmp.getAirBillingFrom().getTime()));
            			aprIdPk.setAprBillingTo(new Timestamp(tmp.getAirBillingTo().getTime()));
            			apr = aprAS.findById(aprIdPk);
            			if(apr != null){
            				airDTO.setAmtDue(apr.getAprAmtDue());
                			airDTO.setAmtRec(apr.getAprAmtReceived());
            			}
            			
            			/*GET INTERFACE FILE CREATION DATE FROM ALS_TRANSACTION_GRP_STATUS*/
            			sdf.applyPattern("yyyy/MM/dd");
            			String where = "WHERE idPk.atgsGroupIdentifier = '"+Utils.createIntProvGroupIdentifier(tmp.getApiProviderNo(), sdf.format(new Date(tmp.getAirBillingTo().getTime())),001)+"' ";
            			atgsLst = atgsAS.findAllByWhere(where);
            			if(!atgsLst.isEmpty()){
            				airDTO.setIntFileCreateDt(atgsLst.get(0).getAtgsFileCreationDt());
            			}
            			
            			/*CALCULATED FIELDS*/
            			airDTO.setAirTotSales(Utils.nullFix(tmp.getAirSystemSales())+Utils.nullFix(tmp.getAirOtcPhoneSales())+Utils.nullFix(tmp.getAirPae())+Utils.nullFix(tmp.getAirNonAlsSales()));
            			airDTO.setTotFundsRec(Utils.nullFix(airDTO.getTotBankDep())+Utils.nullFix(tmp.getAirCreditSales()));
            			airDTO.setAirDifference(Double.valueOf(df.format(Utils.nullFix(airDTO.getTotFundsRec())-Utils.nullFix(airDTO.getAirTotSales()))));
            			airDTO.setNetOverShortOfSales(Utils.nullFix(tmp.getAirOverSales())-Utils.nullFix(tmp.getAirShortSales()));
            			
            			airDTO.setBillingBallanced((Double.compare(Math.abs(airDTO.getAirDifference()), Math.abs(airDTO.getNetOverShortOfSales())) == 0 ? "Y" : "N"));
            			
            			airDTO.setBankDepEditOnly(tmp.getBankDepEditOnly());
            			model.add(airDTO);
            		}
        		}
        		
        	}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsInternalRemittance did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }
	
	public Boolean getHasNonAlsDetails() {
		return hasNonAlsDetails;
	}

	public void setHasNonAlsDetails(Boolean hasNonAlsDetails) {
		this.hasNonAlsDetails = hasNonAlsDetails;
	}

	public Boolean getHasOverShortDetails() {
		return hasOverShortDetails;
	}

	public void setHasOverShortDetails(Boolean hasOverShortDetails) {
		this.hasOverShortDetails = hasOverShortDetails;
	}

	public Boolean getHasPaeAmt() {
		return hasPaeAmt;
	}

	public void setHasPaeAmt(Boolean hasPaeAmt) {
		this.hasPaeAmt = hasPaeAmt;
	}

	public String getComByProv() {
		return comByProv;
	}

	public void setComByProv(String comByProv) {
		this.comByProv = comByProv;
	}

	private Boolean validateForm(){
		Boolean rtn = false;
		if(provNo != null && !"".equals(provNo)){
			rtn = true;
		}
		if(bpFrom != null){
			rtn = true;
		}
		if(bpTo != null){
			rtn = true;
		}
		if(comByProv != null && !"".equals(comByProv)){
			rtn = true;
		}
		if(comByProvDt != null){
			rtn = true;
		}
		if(app != null && !"".equals(app)){
			rtn = true;
		}
		if(appBy != null && !"".equals(appBy)){
			rtn = true;
		}
		if(appDt != null){
			rtn = true;
		}
		if(appCom != null && !"".equals(appCom)){
			rtn = true;
		}
		if(hasNonAlsDetails != null){
			if(hasNonAlsDetails == true){
				rtn = true;
			}
		}
		if(hasOverShortDetails != null){
			if(hasOverShortDetails == true){
				rtn = true;
			}
		}
		if(hasPaeAmt != null){
			if(hasPaeAmt == true){
				rtn = true;
			}
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


	public List<AlsInternalRemittanceDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsInternalRemittanceDTO> model) {
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

	public Date getComByProvDt() {
		return comByProvDt;
	}

	public void setComByProvDt(Date comByProvDt) {
		this.comByProvDt = comByProvDt;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getAppBy() {
		return appBy;
	}

	public void setAppBy(String appBy) {
		this.appBy = appBy;
	}

	public Date getAppDt() {
		return appDt;
	}

	public void setAppDt(Date appDt) {
		this.appDt = appDt;
	}

	public String getAppCom() {
		return appCom;
	}

	public void setAppCom(String appCom) {
		this.appCom = appCom;
	}

	public Boolean getSrchAll() {
		return srchAll;
	}

	public void setSrchAll(Boolean srchAll) {
		this.srchAll = srchAll;
	}
	
}
