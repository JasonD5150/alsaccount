package fwp.alsaccount.utils;


import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.internal.OracleTypes;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import fwp.alsaccount.appservice.admin.AlsMiscAS;
import fwp.alsaccount.dao.admin.AlsMisc;
import fwp.alsaccount.dao.admin.AlsProviderInfo;
import fwp.alsaccount.hibernate.HibernateSessionFactory;

public class HibHelpers {
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}
	
	/*
	 * FISCAL YEAR END PROCESS
	 */
	public Boolean accountingTableCopyCompleted() {
		Boolean rtn = false;
		Integer tmp = 0;
		
		String queryString = "SELECT COUNT(1) count FROM ALS_ORG_CONTROL "
						   + "WHERE asac_budget_year = (SELECT TO_NUMBER(am.am_par_val+1) FROM als.als_misc am "
						   							   + "WHERE am.am_key1 = 'BUDGET YEAR')";

		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("count", IntegerType.INSTANCE);
			
			tmp = (Integer) query.uniqueResult();
			if(tmp > 0){
				rtn = true;
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	//Fiscal Year End Step One
	public Integer accountingTableCopy() {
		Integer rtnCd = 0;

		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call ALS.ALSU1010(?,?)}");

			cs.setDouble(1, rtnCd);
			cs.registerOutParameter(1, OracleTypes.INTEGER);
			cs.registerOutParameter(2, OracleTypes.DATE);
			cs.execute();

			rtnCd = cs.getInt(1);

			conn.close();
			getSession().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return rtnCd;
	}
	
	//Fiscal Year End Step Two
	public String updateAccountingCodes(Date upf, Date upt, String itc, String budgYear) {
		String outString = "";
		Clob tmpClob = null;
		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call als.als_accounting.als_update_account_codes (?,?,?,?,?)}");

			cs.setDate(1, upf);
			cs.setDate(2, upt);
			cs.setString(3, itc);
			cs.setString(4, budgYear);
			cs.setClob(5, tmpClob);

			cs.registerOutParameter(5, OracleTypes.CLOB);
			cs.execute();
			
			tmpClob = cs.getClob(5);
			
			if (tmpClob != null && tmpClob.length() > 10)
				outString = tmpClob.getSubString(1, (int) tmpClob.length());
			else {
				outString = null;
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return outString;
	}
	
	public String updateAccountingCodesAll() {
		String outString = "";
		Clob tmpClob = null;
		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call als.als_accounting.als_update_account_codes_all(?)}");

			cs.setClob(1, tmpClob);

			cs.registerOutParameter(1, OracleTypes.CLOB);
			cs.execute();
			
			tmpClob = cs.getClob(1);
			
			if (tmpClob != null && tmpClob.length() > 10)
				outString = tmpClob.getSubString(1, (int) tmpClob.length());
			else {
				outString = null;
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return outString;
	}
	
	public Boolean alsNonAlsTemplateCopyCompleted() {
		Boolean rtn = false;
		Integer tmp = 0;
		
		String queryString = "SELECT COUNT(1) count FROM als.als_non_als_template "
						   + "WHERE anat_budget_year = (SELECT TO_NUMBER(am_par_val+1) FROM als.als_misc "
													 + "WHERE am_key1 = 'BUDGET YEAR')";

		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("count", IntegerType.INSTANCE);
			
			tmp = (Integer) query.uniqueResult();
			if(tmp > 0){
				rtn = true;
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	//Fiscal Year End Step Three
	public Integer alsNonAlsTemplateCopy() {
		Integer rtnCd = 0;

		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call als.als_accounting.als_copy_non_als_template(?)}");

			cs.setInt(1, rtnCd);
			cs.registerOutParameter(1, OracleTypes.INTEGER);
			cs.execute();

			rtnCd = cs.getInt(1);

			conn.close();
			getSession().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return rtnCd;
	}
	
	//Fiscal Year End Step Four
	public void createTransactionGroup(String budgYear) {
		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call als.als_fy_end.create_sabhrs_entries(?)}");

			cs.setString(1, budgYear);
			cs.execute();

			conn.close();
			getSession().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public AlsProviderInfo getProviderInfo(Integer providerNo) {
		List<AlsProviderInfo> rtn = new ArrayList<AlsProviderInfo>();

		String queryString = "SELECT api_business_nm apiBusinessNm, "+
							 "       ar_business_region arBusinessRegion "+
							 "FROM ALS.ALS_PROVIDER_INFO  "+
							 "WHERE api_provider_no = "+providerNo;

		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("apiBusinessNm", StringType.INSTANCE)
					.addScalar("arBusinessRegion", StringType.INSTANCE)
	
					.setResultTransformer(
							Transformers.aliasToBean(AlsProviderInfo.class));

			rtn = query.list();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn.get(0);
	}
	
	public Date getBillPeriodEndDate(Integer year) {
		Date rtn = null;

		String queryString = "SELECT MAX(APR_BILLING_TO) maxBillingTo "
						   + "FROM ALS.ALS_PROVIDER_REMITTANCE "
						   + "WHERE APR_BILLING_TO  <= To_Date('30-JUN-"+year+"')";

		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("maxBillingTo", DateType.INSTANCE);

			rtn = (Date) query.uniqueResult();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	
	/*
	 * MISCELLANEOUS
	 */
	public String getCurrentBudgetYear() {
		String curBudgYear = null;
		
		AlsMiscAS appSer = new AlsMiscAS();
		List<AlsMisc> tmpLst = new ArrayList<AlsMisc>();
		AlsMisc tmp = new AlsMisc();
		
		try {
			String queryString =  " WHERE amKey1 = 'BUDGET YEAR'";
			tmpLst = appSer.findAllByWhere(queryString);
			if(!tmpLst.isEmpty()){
				tmp = tmpLst.get(0);
				curBudgYear = tmp.getAmParVal();			
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return curBudgYear;
	}
	
	public String getCurrentBudgetYearChangeDt() {
		String curBudgYearChangeDt = null;
		
		AlsMiscAS appSer = new AlsMiscAS();
		List<AlsMisc> tmpLst = new ArrayList<AlsMisc>();
		AlsMisc tmp = new AlsMisc();
		
		try {
			String queryString =  " WHERE amKey1 = 'BUDGET YEAR CHANGE DATE'";
			tmpLst = appSer.findAllByWhere(queryString);
			if(!tmpLst.isEmpty()){
				tmp = tmpLst.get(0);
				curBudgYearChangeDt = tmp.getAmParVal();			
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return curBudgYearChangeDt;
	}
	
	//Upload SABHRS entries into the Summary
	public Integer uploadSabhrsToSum() {
		Integer rtnCd = 0;

		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call ALS.ALSU0290(?,?)}");

			cs.setDouble(1, rtnCd);
			cs.registerOutParameter(1, OracleTypes.INTEGER);
			cs.registerOutParameter(2, OracleTypes.DATE);
			cs.execute();

			rtnCd = cs.getInt(1);

			conn.close();
			getSession().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return rtnCd;
	}
	
}