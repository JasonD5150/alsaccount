package fwp.alsaccount.sabhrs.json;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesIdPk;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.utils.Utils;
import fwp.gen.utils.ListComp;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class SabhrsQueryBuildCsvAction extends ActionSupport {

	private static final Logger log = LoggerFactory.getLogger(SabhrsQueryBuildCsvAction.class);
	private static final long serialVersionUID = -198737835399515405L;

	private List<AlsSabhrsEntriesDTO> sabhrsEntries = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();

	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		buildcsv();
		return SUCCESS;
	}

	private void buildcsv() throws Exception {
		File tempFile = File.createTempFile("personinformation", "csv");
		fileName="ALSPerson.csv";
		StringBuilder titleLine = new StringBuilder();

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
			   "intStat".equals(listComp.getItemVal())){
				AlsSabhrsEntriesDTO.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
			}else{
				AlsSabhrsEntries.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
			}
			
			return true;
		} catch (NoSuchMethodException e) {
			return false;
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
}

