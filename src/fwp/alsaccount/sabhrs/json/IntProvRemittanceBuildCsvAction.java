package fwp.alsaccount.sabhrs.json;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.als.appservice.inventory.AlsNonAlsDetailsAS;
import fwp.als.hibernate.inventory.dao.AlsNonAlsDetails;
import fwp.alsaccount.appservice.sabhrs.AlsOverUnderSalesDetsAS;
import fwp.alsaccount.dao.sabhrs.AlsOverUnderSalesDets;
import fwp.alsaccount.dto.sabhrs.AlsInternalRemittanceDTO;

/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class IntProvRemittanceBuildCsvAction extends ActionSupport {

	private static final long serialVersionUID = -198737835399515405L;

	private List<AlsInternalRemittanceDTO> remittanceRecords = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();
	private String filters;

	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		buildcsv();
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	private void buildcsv() throws Exception {
		File tempFile = File.createTempFile("remittancerecords", "csv");
		fileName="InternalProviderRemittance.csv";

		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.write("Internal Provider Remittance Report\n\n");
		
		if(!"".equals(filters)&& filters != null){
			String[] criteria = URLDecoder.decode(filters,"UTF-8").split("&");
			for(String tmp : criteria){
				Integer length = tmp.trim().split("=").length;
				String column;
				String value;
				if(tmp.split("=")[0].contains("__checkbox_")){
					column = tmp.split("=")[0].replace("__checkbox_", "");
					value = tmp.split("=")[1];
					if(value == "true"){
						fileWriter.write(getColumnLabel(column)+" = "+value+"\n");
					}
				}else if(length > 1){
					column = tmp.split("=")[0];
					value = tmp.split("=")[1];
					fileWriter.write(getColumnLabel(column)+" = "+value+"\n");
				}
			}
			fileWriter.write("\n");
		}
		
		AlsNonAlsDetailsAS anadAS = new AlsNonAlsDetailsAS();
		List<AlsNonAlsDetails> anadLst = null;
		AlsOverUnderSalesDetsAS aousdAS = new AlsOverUnderSalesDetsAS();
		List<AlsOverUnderSalesDets> aousdLst = null;
		for (AlsInternalRemittanceDTO rr : remittanceRecords) {	
			String where = "WHERE idPk.apiProviderNo = "+rr.getGridKey().split("_")[2]+" "
					+ "AND idPk.airBillingFrom = TO_DATE('"+rr.getGridKey().split("_")[0]+"','yyyy/mm/dd') "
					+ "AND idPk.airBillingTo = TO_DATE('"+rr.getGridKey().split("_")[1]+"','yyyy/mm/dd') ";
			
			StringBuilder line = new StringBuilder();
			line.append("Provider No:, "+rr.getGridKey().split("_")[2]+",Provider Name:,"+rr.getProvNm()+"\n");
			line.append("Billing Period From:, "+rr.getGridKey().split("_")[0]+",Billing Period To:,"+rr.getGridKey().split("_")[1]+"\n");
			line.append(",Amount, Total\n");
			line.append("System Sales:,$"+rr.getAirSystemSales()+"\n");
			line.append("OTC Sales:,$"+rr.getAirOtcPhoneSales()+"\n");
			line.append("PAEs:,$"+rr.getAirPae()+"\n");
			line.append("Total ALS Sales:,,$"+rr.getAmtDue()+"\n");
			
			/*NON ALS SALES DETAILS*/
			anadLst = new ArrayList<AlsNonAlsDetails>();
			anadLst = anadAS.findAllByWhere(where);
			if(!anadLst.isEmpty()){
				line.append("Non ALS Sales Details\n");
				line.append("Description\n");
				for(AlsNonAlsDetails anadTmp : anadLst){
					line.append(StringEscapeUtils.escapeCsv(anadTmp.getAnadDesc())+",$"+anadTmp.getAnadAmount()+"\n");
				}
			}
			
			line.append("Total Non ALS Sales:,,$"+rr.getAirNonAlsSales()+"\n");
			line.append("Total Sales:,,$"+rr.getAirTotSales()+"\n");
			line.append("Total Bank Deposits:,$"+rr.getTotBankDep()+"\n");
			line.append("Credit Card Sales:,$"+rr.getAirCreditSales()+"\n");
			line.append("Total Funds Received:,,$"+rr.getTotFundsRec()+"\n");
			line.append("Difference:,,$"+rr.getAirDifference()+"\n");
			
			/*TOTAL FUNDS RECEIVED SHORT OF SALES*/
			aousdLst = new ArrayList<AlsOverUnderSalesDets>();
			aousdLst = aousdAS.findAllByWhere(where);
			if(!aousdLst.isEmpty()){
				line.append("Total Funds Received Short of Sales -\n");
				line.append("Details\n");
				for(AlsOverUnderSalesDets aousdTmp : aousdLst){
					line.append(StringEscapeUtils.escapeCsv(aousdTmp.getAousdDesc())+",$"+aousdTmp.getAousdAmount()+"\n");
				}
				line.append("Net Over/Short of Sales:,,$"+rr.getNetOverShortOfSales());
			}
			line.append("\n\n");
			fileWriter.write(line.toString());
		}
		
		fileWriter.close();
		csvFileName = tempFile.getName();
	}
	
	private String getColumnLabel(String column){
		switch(column){
		case "provNo":
			return "Provider No ";
		case "bpFrom":
			return "Billing Period From ";	
		case "bpTo":
			return "Billing Period To ";	
		default:
			return "N/A";
		}	
	}

	public String getCsvFileName() {
		return csvFileName;
	}

	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}

	public List<ListComp> getColumnNameValues() {
		return columnNameValues;
	}

	public List<AlsInternalRemittanceDTO> getRemittanceRecords() {
		return remittanceRecords;
	}

	public void setRemittanceRecords(
			List<AlsInternalRemittanceDTO> remittanceRecords) {
		this.remittanceRecords = remittanceRecords;
	}

	public void setColumnNameValues(List<ListComp> columnNameValues) {
		this.columnNameValues = columnNameValues;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}
}

