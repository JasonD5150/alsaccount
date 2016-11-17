package fwp.alsaccount.sabhrs.json;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.als.hibernate.inventory.dao.AlsInternalRemittanceIdPk;
import fwp.als.hibernate.item.dao.AlsItemApplFeeAcctIdPk;
import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesIdPk;
import fwp.alsaccount.dto.sabhrs.AlsInternalRemittanceDTO;
import fwp.alsaccount.dto.sabhrs.ProvAdjEntAIAFAGridDTO;
import fwp.alsaccount.utils.Utils;

/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class ProvAdjEntGridsToCsvAction extends ActionSupport {

	private static final long serialVersionUID = -198737835399515405L;

	private List<ProvAdjEntAIAFAGridDTO> iafaRecords = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();

	private String filters;
	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		buildcsv();
		return SUCCESS;
	}

	private void buildcsv() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		File tempFile = File.createTempFile("iafaRecords", "csv");
		fileName="ProviderAdjustedEntries.csv";

		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.write("Provider Adjusted Entries\n\n");
		
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
				}else if(length > 1 && !tmp.split("=")[0].contains("widget") && !tmp.split("=")[0].contains("remittanceInd")){
					column = tmp.split("=")[0];
					value = tmp.split("=")[1];
					fileWriter.write(getColumnLabel(column)+" = "+value+"\n");
				}
			}
			fileWriter.write("\n");
		}
		
		StringBuilder titleLine = new StringBuilder();
		for (ListComp listComp : this.columnNameValues) {
			if (validColumn(listComp)) {
				if (titleLine.length() > 0) {
					titleLine.append(",");
				}
				titleLine.append(StringEscapeUtils.escapeCsv(listComp.getItemLabel()));
			}
		}
		titleLine.append(",Business Unit,Journal Line Ref,Account,Fund,Org,Program,Sub-Class,Budget Year,Project Grant,SABHRS Amount,System Activity Type Code,Transaction Code,Dr / Cr Code,SNo,Line Desc");
		fileWriter.write(titleLine.toString());
		fileWriter.write("\n");
		
		for (ProvAdjEntAIAFAGridDTO air : iafaRecords) {	
			StringBuilder line = new StringBuilder();
			AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
			List<AlsSabhrsEntries> aseLst = aseAS.getProvAdjEntRecords(air.getApiProviderNo(), air.getBillingFrom(), air.getBillingTo(), air.getAiafaSeqNo());
			if(!aseLst.isEmpty()){
				
				for(AlsSabhrsEntries ase:aseLst){
					line.append(air.getApiProviderNo()+","+
								sdf.format(air.getBillingFrom())+","+
								sdf.format(air.getBillingTo())+","+
								air.getAiafaSeqNo()+","+
								air.getItemTypeCd()+","+
								air.getItemTypeDesc()+","+
								ase.getAamBusinessUnit()+","+
								Utils.nullFix(ase.getAsacReference())+","+
								Utils.nullFix(ase.getAamAccount())+","+
								Utils.nullFix(ase.getAamFund())+","+
								Utils.nullFix(ase.getAocOrg())+","+
								Utils.nullFix(ase.getAsacProgram())+","+
								Utils.nullFix(ase.getAsacSubclass())+","+
								Utils.nullFix(ase.getAsacBudgetYear())+","+
								Utils.nullFix(ase.getAsacProjectGrant())+","+
								Utils.nullFix(ase.getAseAmt())+","+
								Utils.nullFix(ase.getAsacSystemActivityTypeCd())+","+
								Utils.nullFix(ase.getAsacTxnCd())+",");
								if(ase.getIdPk().getAseDrCrCd() != null){
									if("C".equals(ase.getIdPk().getAseDrCrCd())){
										line.append("Credit");
									}else{
										line.append("Debit");
									}
								}
								line.append(",");
								line.append(Utils.nullFix(ase.getIdPk().getAseSeqNo())+","+
								Utils.nullFix(ase.getAseLineDescription())+"\n");
				}
			}else{
				line.append(air.getApiProviderNo()+","+sdf.format(air.getBillingFrom())+","+sdf.format(air.getBillingTo())+","+air.getAiafaSeqNo()+","+air.getItemTypeCd()+","+air.getItemTypeDesc()+"\n");
			}		
			fileWriter.write(line.toString());
		}
		
		fileWriter.close();
		csvFileName = tempFile.getName();
	}
	
	private Boolean validColumn(ListComp listComp) {
		try {
			AlsItemApplFeeAcctIdPk.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
			return true;
		} catch (NoSuchMethodException e) {
			try {
				ProvAdjEntAIAFAGridDTO.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
				return true;
			} catch (NoSuchMethodException e2) {
				return false;
			}
		}
	}
	
	private String getColumnLabel(String column){
		switch(column){
		case "provNo":
			return "Provider No ";
		case "bpFrom":
			return "Billing Period From ";	
		case "bpTo":
			return "Billing Period To ";
		case "tribeCd":
			return "Tribe Code ";
		case "appTypeCd":
			return "App Type Code ";
		case "amtTypeCd":
			return "Amount Type Code ";
		case "reasonCd":
			return "Reason Code ";
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

	public List<ProvAdjEntAIAFAGridDTO> getIafaRecords() {
		return iafaRecords;
	}

	public void setIafaRecords(List<ProvAdjEntAIAFAGridDTO> iafaRecords) {
		this.iafaRecords = iafaRecords;
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

