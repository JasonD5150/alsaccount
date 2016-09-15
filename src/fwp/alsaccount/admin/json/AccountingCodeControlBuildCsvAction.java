package fwp.alsaccount.admin.json;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.dto.admin.AccCdDistByItemTypeDTO;
import fwp.alsaccount.dto.admin.AlsAccCdControlDTO;

/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class AccountingCodeControlBuildCsvAction extends ActionSupport {

	private static final long serialVersionUID = -198737835399515405L;

	private List<AlsAccCdControlDTO> accCodeControlEntries = new ArrayList<>();
	private List<AccCdDistByItemTypeDTO> accCdDistByItemTypeEntries = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();
	private String filters;

	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		buildcsv();
		return SUCCESS;
	}

	private void buildcsv() throws Exception {
		File tempFile = File.createTempFile("iafaentries", "csv");
		fileName="Accounting Code Control.csv";

		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.write("Accounting Code Control\n\n");
		
		if(!"".equals(filters)&& filters != null){
			String[] criteria = URLDecoder.decode(filters,"UTF-8").split("&");
			for(String tmp : criteria){
				Integer length = tmp.trim().split("=").length;
				String column;
				String value;
				if(!tmp.contains("_widget")){
					if(length > 1){
						column = tmp.split("=")[0];
						value = tmp.split("=")[1];
						fileWriter.write(getColumnLabel(column)+" = "+value+"\n");
					}
				}
			}
			fileWriter.write("\n");
		}
		
		fileWriter.write("Account Code,Seq No,Account,Fund,Allocated Amount,Open Item Key,Org,Subclass,Mutiple Orgs,Balancing,Remarks");
		fileWriter.write("\n");
		for (AlsAccCdControlDTO ie : accCodeControlEntries) {			
			StringBuilder line = new StringBuilder();
			line.append(ie.getGridKey().split("_")[1]+","+ie.getGridKey().split("_")[2]+","+
						ie.getAamAccount()+","+ie.getAaccFund()+","+nullFix(ie.getAaccAllocatedAmt())+","+
						ie.getAaccJlrRequired()+","+ie.getAocOrg()+","+nullFix(ie.getAsacSubclass())+","+
						ie.getAaccOrgFlag()+","+ie.getAaccBalancingAmtFlag()+","+nullFix(ie.getAaccRemarks()));
			
			line.append("\n");
			fileWriter.write(line.toString());
		}
		fileWriter.write("\n\n");
		
		fileWriter.write("Accounting Codes Distribution by Item Type");
		fileWriter.write("\n");
		fileWriter.write("Item Type,Item Description,Usage Period From,Usage Period To,Item Cost,Cost Prerequisite Code,Cost Prerequisite Description,Residency,Budget Year,Partial Cost,Drawing Fee,Accounting Code,Account,Fund,Balancing,Distribution,Subclass,Org,Org Multiple");
		fileWriter.write("\n");
		for (AccCdDistByItemTypeDTO ie : accCdDistByItemTypeEntries) {			
			StringBuilder line = new StringBuilder();
			line.append(ie.getItemTypeCd()+","+StringEscapeUtils.escapeCsv(ie.getItemTypeDesc())+","+ie.getUpFromDt()+","+ie.getUpToDt()+",$"+
						ie.getItemCost()+","+ie.getCostPrereq()+","+StringEscapeUtils.escapeCsv(ie.getCostPrereqDesc())+","+
						ie.getResidency()+","+ie.getBudgYear()+","+nullFix(ie.getPartialCost())+","+
						nullFix(ie.getDrawFee())+","+ie.getAccCd()+","+ie.getAccount()+","+ie.getFund()+","+
						ie.getBalancing()+(ie.getDist() != null?",$"+ie.getDist():",")+","+nullFix(ie.getSubclass())+","+ie.getOrg()+","+nullFix(ie.getOrgMult()));
			
			line.append("\n");
			fileWriter.write(line.toString());
		}
		
		fileWriter.close();
		csvFileName = tempFile.getName();
	}

	private String nullFix(String in){
		if(in == null || "".equals(in)){
			return "";
		}else{
			return in;
		}
	}
	private String nullFix(Integer in){
		if(in == null || "".equals(in)){
			return "";
		}else{
			return in.toString();
		}
	}
	private String nullFix(Double in){
		if(in == null || "".equals(in)){
			return "";
		}else{
			return in.toString();
		}
	}
	private String getColumnLabel(String column){
		switch(column){
		case "budgYear":
			return "Budget Year ";
		case "upFromDt":
			return "Usage Period From ";	
		case "upToDt":
			return "Usage Period To ";	
		case "itemTypeCd":
			return "Item type Code ";	
		case "accCd":
			return "Accounting Code ";	
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

	public void setColumnNameValues(List<ListComp> columnNameValues) {
		this.columnNameValues = columnNameValues;
	}

	public List<AlsAccCdControlDTO> getAccCodeControlEntries() {
		return accCodeControlEntries;
	}

	public void setAccCodeControlEntries(List<AlsAccCdControlDTO> accCodeControlEntries) {
		this.accCodeControlEntries = accCodeControlEntries;
	}

	public List<AccCdDistByItemTypeDTO> getAccCdDistByItemTypeEntries() {
		return accCdDistByItemTypeEntries;
	}

	public void setAccCdDistByItemTypeEntries(
			List<AccCdDistByItemTypeDTO> accCdDistByItemTypeEntries) {
		this.accCdDistByItemTypeEntries = accCdDistByItemTypeEntries;
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