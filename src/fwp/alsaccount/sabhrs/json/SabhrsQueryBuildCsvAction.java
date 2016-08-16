package fwp.alsaccount.sabhrs.json;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesIdPk;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;


/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class SabhrsQueryBuildCsvAction extends ActionSupport {

	private static final long serialVersionUID = -198737835399515405L;

	private List<AlsSabhrsEntriesDTO> sabhrsEntries = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();
	private String filters;

	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		buildcsv();
		return SUCCESS;
	}

	private void buildcsv() throws Exception {
		File tempFile = File.createTempFile("personinformation", "csv");
		fileName="SABHRSDetail.csv";
		StringBuilder titleLine = new StringBuilder();

		/*String[] csvHeaders={"Budget Year",
							 "JLR",
							 "Account",
							 "Fund",
							 "Org",
							 "Program",
							 "Subclass",
							 "Business Unit",
							 "Project Grant",
							 "Amount",
							 "Sys Activity Type Code",
							 "Transaction Cd",
							 "Dr/CR Code",
							 "Summary Seq No",
							 "Line Desc",
							 "When Entry Posted",
							 "Allow Upload To Summary",
							 "When Uploaded To Summary",
							 "Seq No",
							 "Provider No",
							 "Billing From",
							 "Billing To",
							 "AIAFA Seq No",
							 "Transaction Type Cd",
							 "Group Identifier",
							 "Non Als Flag",
							 "Tribe Cd",
							 "ANAT Cd",
							 "Summary Status",
							 "Interface Status" };
		for(int i=0;i<csvHeaders.length;i++){
			titleLine.append(csvHeaders[i]+",");
		}*/
		
		for (ListComp listComp : this.columnNameValues) {
			if (validColumn(listComp)) {
				if (titleLine.length() > 0) {
					titleLine.append(",");
				}
				titleLine.append(StringEscapeUtils.escapeCsv(listComp.getItemLabel()));
			}
		}
		titleLine.append("\n");
		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.write("SABHRS Detail Report\n\n");
		
		/*Search Criteria*/
		if(!"".equals(filters)&& filters != null){
			String[] criteria = URLDecoder.decode(filters,"UTF-8").split("&");
			for(String tmp : criteria){
				if(!tmp.contains("_widget")){
					Integer length = tmp.trim().split("=").length;
					if(length > 1){
						String column = tmp.split("=")[0];
						String value = tmp.split("=")[1];
						fileWriter.write(getColumnLabel(column)+" = "+value+"\n");
						//System.out.println(column +" = "+value);
					}
				}
			}
			fileWriter.write("\n");
		}
		fileWriter.write(titleLine.toString());
		
		for (AlsSabhrsEntriesDTO se : sabhrsEntries) {
			
			AlsSabhrsEntriesIdPk seIdPk = new AlsSabhrsEntriesIdPk();
			seIdPk.setAseDrCrCd(se.getGridKey().split("_")[2]);
			seIdPk.setAseSeqNo(Integer.parseInt(se.getGridKey().split("_")[1]));
			seIdPk.setAseTxnCdSeqNo(Integer.parseInt(se.getGridKey().split("_")[3]));
			seIdPk.setAseWhenEntryPosted(Timestamp.valueOf(se.getGridKey().split("_")[0]));
			se.setIdPk(seIdPk);
			
			StringBuilder line = new StringBuilder();
			Boolean firstColumn = true;
			for (ListComp listComp : this.columnNameValues) {
				if (validColumn(listComp)) {
					if (!firstColumn) {
						line.append(",");
					} else {
						firstColumn = false;
					}
					Object gotten;
					
					gotten = se.getClass().getMethod("get" + StringUtils.capitalize(listComp.getItemVal())).invoke(se);
		
					if (gotten != null && gotten instanceof Date) {
						line.append(StringEscapeUtils.escapeCsv(DateFormatUtils.format((Date) gotten, "MM/dd/yyyy")));
					} else {
						line.append(StringEscapeUtils.escapeCsv(ObjectUtils.toString(gotten)));
					}
				}
			}
			line.append("\n");
			fileWriter.write(line.toString());
		}
		fileWriter.close();
		csvFileName = tempFile.getName();
	}
	private Boolean validColumn(ListComp listComp) {
		try {
			if("bpFromDt".equals(listComp.getItemVal())||
			   "bpToDt".equals(listComp.getItemVal())||
			   "upToSummDt".equals(listComp.getItemVal())||
			   "aseWhenEntryPosted".equals(listComp.getItemVal())||
			   "aseSeqNo".equals(listComp.getItemVal())||
			   "aseDrCrCd".equals(listComp.getItemVal())||
			   "aseTxnCdSeqNo".equals(listComp.getItemVal())||
			   "sumStat".equals(listComp.getItemVal())||
			   "intStat".equals(listComp.getItemVal())||
			   "jlr".equals(listComp.getItemVal())){
				AlsSabhrsEntriesDTO.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
			}else{
				AlsSabhrsEntries.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
			}
			
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}
	
	private String getColumnLabel(String column){
		switch(column){
		case "providerNo":
			return "Provider No ";
		case "seqNo":
			return "IAFA Seq No ";
		case "bpFromDt":
			return "Billing Period From Date ";
		case "bpToDt":
			return "Billing Period To Date ";
		case "fromDt":
			return "From Date ";
		case "toDt":
			return "To Date ";
		case "budgYear":
			return "Budget Year ";
		case "progYear":
			return "Program Year ";
		case "jlr":
			return "Journal Line Reference ";
		case "account":
			return "Account ";
		case "fund":
			return "Fund ";
		case "org":
			return "Org ";
		case "subClass":
			return "Subclass ";
		case "tribeCd":
			return "Tribe Code ";
		case "txnGrpIdentifier":
			return "Transaction Group Identifier ";
		case "sysActTypeCd":
			return "System Activity Type Code ";
		case "transGrpType":
			return "Transaction Type Code ";
		case "sumAppStat":
			return "Summary Approval Status ";
		case "intAppStat":
			return "Interface Approval Status ";			
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

	public List<AlsSabhrsEntriesDTO> getSabhrsEntries() {
		return sabhrsEntries;
	}

	public void setSabhrsEntries(List<AlsSabhrsEntriesDTO> sabhrsEntries) {
		this.sabhrsEntries = sabhrsEntries;
	}

	public List<ListComp> getColumnNameValues() {
		return columnNameValues;
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
}

