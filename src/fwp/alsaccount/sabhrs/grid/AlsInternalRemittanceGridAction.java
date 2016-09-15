package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.inventory.dao.AlsInternalRemittance;
import fwp.alsaccount.appservice.sabhrs.AlsInternalRemittanceAS;
import fwp.alsaccount.appservice.sabhrs.AlsProviderRemittanceAS;
import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.sabhrs.AlsProviderRemittance;
import fwp.alsaccount.dao.sabhrs.AlsProviderRemittanceIdPk;
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
    
    private String				provNo;
    private Date 				bpFrom;
    private Date 				bpTo;
    private Double				sysSales;
    private Date				eftDdd;
    private Double				otcSales;
    private Double				pae;
	private Double				nonAlsSales;
    private Double				crCardSales;
    private Double				totShortOfSales;
    private Double				totOverSales;
    private Date				comByProvDt;
    private Boolean				app;
    private String				appBy;
    private Date				appDt;
    private String				appCom;
    private Boolean				search = false;
    
	public String buildgrid(){ 
		String srchStr = buildQueryStr();

        try{
        	setModel(new ArrayList<AlsInternalRemittanceDTO>());   
        	if(search){
        		AlsInternalRemittanceAS airAS = new AlsInternalRemittanceAS();
    			List<AlsInternalRemittance> airLst = new ArrayList<AlsInternalRemittance>();
        		
        		airLst = airAS.findAllByWhere(srchStr); 
        		if(airLst.size() > 10000){
        			setUserdata("Please narrow search. The search grid is limited to 10000 rows. There were " + airLst.size() + " entries selected.");
        		}else{
        			AlsInternalRemittanceDTO airDTO = null;
        			AlsProviderRemittanceAS aprAS = new AlsProviderRemittanceAS();
        			AlsProviderRemittance apr = new AlsProviderRemittance();
        			AlsProviderRemittanceIdPk aprIdPk = null;
        			AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
        			List<AlsTransactionGrpStatus> atgsLst = new ArrayList<AlsTransactionGrpStatus>();
        			
        			HibHelpers hh = new HibHelpers();
        			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        			for(AlsInternalRemittance tmp : airLst){ 
        				sdf.applyPattern("yyyy-MM-dd");
            			airDTO = new AlsInternalRemittanceDTO();
            			airDTO.setGridKey(sdf.format(tmp.getIdPk().getAirBillingFrom())+"_"+sdf.format(tmp.getIdPk().getAirBillingTo())+"_"+tmp.getIdPk().getApiProviderNo());
            			airDTO.setIdPk(tmp.getIdPk());
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
            			
            			/*SETTING DATE INSTEAD OF TIMESTAMP, exportToCSV() DOES NOT LIKE TIMESTAMPS*/
            			airDTO.setEftddd(tmp.getAirEftddd());
            			airDTO.setCompleteProvider(tmp.getAirCompleteProvider());
            			airDTO.setOfflnPaymentAppDt(tmp.getAirOfflnPaymentAppDt());
            			
            			/*HIBHELPERs*/
            				/*GET PROVIDER NAME*/
            			airDTO.setProvNm(hh.getProviderName(tmp.getIdPk().getApiProviderNo()));
            				/*GET TOTAL BANK DEPOSIT*/
            			airDTO.setTotBankDep(hh.getTotalBankDeposit(tmp.getIdPk().getApiProviderNo(), new Date(tmp.getIdPk().getAirBillingFrom().getTime()), new Date(tmp.getIdPk().getAirBillingTo().getTime())));
            				/*INTERFACE FILE GENERATED, controls if the grids can be edited or not*/
            			airDTO.setIntFileGenerated(hh.getInterfaceFileGenerated(tmp.getIdPk().getApiProviderNo(), new Date(tmp.getIdPk().getAirBillingTo().getTime())));
            			
            			/*GET AMOUNT DUE AND RECEIVED FROM ALS_PROVIDER_REMITTANCE*/
            			aprIdPk = new AlsProviderRemittanceIdPk();
            			aprIdPk.setApiProviderNo(tmp.getIdPk().getApiProviderNo());
            			aprIdPk.setAprBillingFrom(tmp.getIdPk().getAirBillingFrom());
            			aprIdPk.setAprBillingTo(tmp.getIdPk().getAirBillingTo());
            			apr = aprAS.findById(aprIdPk);
            			if(apr != null){
            				airDTO.setAmtDue(apr.getAprAmtDue());
                			airDTO.setAmtRec(apr.getAprAmtReceived());
            			}
            			
            			/*GET INTERFACE FILE CREATION DATE FROM ALS_TRANSACTION_GRP_STATUS*/
            			sdf.applyPattern("yyyy/MM/dd");
            			String where = "WHERE idPk.atgsGroupIdentifier = '"+Utils.createIntProvGroupIdentifier(tmp.getIdPk().getApiProviderNo(), sdf.format(new Date(tmp.getIdPk().getAirBillingTo().getTime())),"001")+"' ";
            			atgsLst = atgsAS.findAllByWhere(where);
            			if(!atgsLst.isEmpty()){
            				airDTO.setIntFileCreateDt(atgsLst.get(0).getAtgsFileCreationDt());
            			}
            			
            			/*CALCULATED FIELDS*/
            			airDTO.setAirTotSales(Utils.nullFix(tmp.getAirSystemSales())+Utils.nullFix(tmp.getAirOtcPhoneSales())+Utils.nullFix(tmp.getAirPae())+Utils.nullFix(tmp.getAirNonAlsSales()));
            			airDTO.setTotFundsRec(Utils.nullFix(airDTO.getTotBankDep())+Utils.nullFix(tmp.getAirCreditSales()));
            			airDTO.setAirDifference(Utils.nullFix(airDTO.getTotFundsRec())-Utils.nullFix(airDTO.getAirTotSales()));
            			airDTO.setNetOverShortOfSales(Utils.nullFix(tmp.getAirOverSales())-Utils.nullFix(tmp.getAirShortSales()));
            			airDTO.setBillingBallanced((Double.compare(airDTO.getAirDifference(), airDTO.getNetOverShortOfSales()) == 0 ? "Y" : "N"));
            			
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
	
	private String buildQueryStr(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		StringBuilder srchStr = new StringBuilder();
		srchStr.append("WHERE 1=1 ");
		if(provNo != null && !"".equals(provNo)){
			srchStr.append("AND idPk.apiProviderNo ="+provNo+" ");
			search = true;
		}
		if(bpFrom != null){
			srchStr.append("AND idPk.airBillingFrom = TO_DATE('"+sdf.format(bpFrom)+"','mm/dd/yyyy') ");
			search = true;
		}
		if(bpTo != null){
			srchStr.append("AND idPk.airBillingTo = TO_DATE('"+sdf.format(bpTo)+"','mm/dd/yyyy') ");
			search = true;
		}
		if(sysSales != null){
			srchStr.append("AND airSystemSales = "+sysSales+" ");
			search = true;
		}
		if(eftDdd != null){
			srchStr.append("AND airSystemSales = TO_DATE('"+eftDdd+"','mm/dd/yyyy') ");
			search = true;
		}
		if(otcSales != null){
			srchStr.append("AND airOtcPhoneSales = "+otcSales+" ");
			search = true;
		}
		if(pae != null){
			srchStr.append("AND airPae = "+pae+" ");
			search = true;
		}
		if(nonAlsSales != null){
			srchStr.append("AND airNonAlsSales = "+nonAlsSales+" ");
			search = true;
		}
		if(crCardSales != null){
			srchStr.append("AND airCreditSales = "+crCardSales+" ");
			search = true;
		}
		if(totShortOfSales != null){
			srchStr.append("AND airShortSales = "+totShortOfSales+" ");
			search = true;
		}
		if(totOverSales != null){
			srchStr.append("AND airOverSales = "+totOverSales+" ");
			search = true;
		}
		if(comByProvDt != null){
			srchStr.append("AND airCompleteProvider = TO_DATE('"+comByProvDt+"','mm/dd/yyyy') ");
			search = true;
		}
		if(app != null && app != false){
			srchStr.append("AND airOfflnPaymentApproved = 'Y' ");
			search = true;
		}
		if(appBy != null && !"".equals(appBy)){
			srchStr.append("AND airOfflnPaymentAppBy = "+appBy+" ");
			search = true;
		}
		if(appDt != null){
			srchStr.append("AND airOfflnPaymentAppDt = TO_DATE('"+appDt+"','mm/dd/yyyy')  ");
			search = true;
		}
		if(appCom != null && !"".equals(appCom)){
			srchStr.append("AND airOfflnPaymentAppCom = '"+appCom+"' ");
			search = true;
		}
		srchStr.append("ORDER BY idPk.apiProviderNo, idPk.airBillingFrom DESC");
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

	public String getProvNo() {
		return provNo;
	}

	public void setProvNo(String provNo) {
		this.provNo = provNo;
	}

	public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}
	public Double getSysSales() {
		return sysSales;
	}

	public void setSysSales(Double sysSales) {
		this.sysSales = sysSales;
	}

	public Date getEftDdd() {
		return eftDdd;
	}

	public void setEftDdd(Date eftDdd) {
		this.eftDdd = eftDdd;
	}

	public Double getOtcSales() {
		return otcSales;
	}

	public void setOtcSales(Double otcSales) {
		this.otcSales = otcSales;
	}

	public Double getPae() {
		return pae;
	}

	public void setPae(Double pae) {
		this.pae = pae;
	}

	public Double getNonAlsSales() {
		return nonAlsSales;
	}

	public void setNonAlsSales(Double nonAlsSales) {
		this.nonAlsSales = nonAlsSales;
	}

	public Double getCrCardSales() {
		return crCardSales;
	}

	public void setCrCardSales(Double crCardSales) {
		this.crCardSales = crCardSales;
	}

	public Double getTotShortOfSales() {
		return totShortOfSales;
	}

	public void setTotShortOfSales(Double totShortOfSales) {
		this.totShortOfSales = totShortOfSales;
	}

	public Double getTotOverSales() {
		return totOverSales;
	}

	public void setTotOverSales(Double totOverSales) {
		this.totOverSales = totOverSales;
	}

	public Date getComByProvDt() {
		return comByProvDt;
	}

	public void setComByProvDt(Date comByProvDt) {
		this.comByProvDt = comByProvDt;
	}

	public Boolean getApp() {
		return app;
	}

	public void setApp(Boolean app) {
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
}
