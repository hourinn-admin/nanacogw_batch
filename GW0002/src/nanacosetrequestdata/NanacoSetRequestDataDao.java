package nanacosetrequestdata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class NanacoSetRequestDataDao {
	private static Logger log = Logger.getLogger(NanacoSetRequestDataDao.class);
	
	private static final String LOG_PGM_ID = NanacoSetRequestDataDao.class.getSimpleName();
	
	private static Connection conn ;
	
	private static PreparedStatement pstmt;
	
	private static final String COMMA = ",";
	
	private static final String STR_NANACO_NO = "nanaco番号";
	
	public NanacoSetRequestDataDao() throws Exception {
		conn = DBManager.createConnection(true, false);
		if (conn != null)
			log.info("DB接続に成功しました。[プログラムID:" + LOG_PGM_ID + "]");
		else
			log.error("DB接続に失敗しました。[プログラムID:" + LOG_PGM_ID + "]");
	}
	
	public void insCsvInfo(String strLine, boolean result, String strDate,Long seq, String nanacoNo, 
			String strHigherTermed, boolean custChkIsNg) throws Exception {
		log.info("付与依頼情報登録処理開始");
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("INSERT INTO /* J_ONG0002_011001 */ ");
		sbSql.append("(SEQ_NO,");
		sbSql.append("REQUEST_BUSINESS_NO,");
		sbSql.append("ORDER_NO,");
		sbSql.append("USER_HASH,");
		sbSql.append("CUST_ID,");
		sbSql.append("CAMPAIGN_ID,");
		sbSql.append("CAMPAIGN_ID_7CN,");
		sbSql.append("NANACO_NO,");
		sbSql.append("OLD_NANACO_NO,");
		sbSql.append("ADD_POINT,");
		sbSql.append("RESULT_ADD_POINT,");
		sbSql.append("DATA_STATUS,");
		sbSql.append("APPLICATION_YMD,");
		sbSql.append("REFLECT_HOPE_YMD,");
		sbSql.append("ISSUE_YMD,");
		sbSql.append("OMNI_FLG,");
		sbSql.append("ERROR_FLG,");
		sbSql.append("DATA_DIVISION_ERROR,");
		sbSql.append("REFLECT_HOPE_DAY_ERROR,");
		sbSql.append("POINT_ERROR,");
		sbSql.append("REFLECT_HOPE_YMD_RANGE_ERROR,");
		sbSql.append("APPLICATION_YMD_CORP_ERROR,");
		sbSql.append("CUST_ID_ERROR,");
		sbSql.append("UNAVAILABLE_CUST_ERROR,");
		sbSql.append("UNAVAILABLE_CUST_NEGA_ERROR,");
		sbSql.append("POINT_CODE_ERROR,");
		sbSql.append("CAMPAIGN_ID_ERROR,");
		sbSql.append("FILLER1,");
		sbSql.append("FILLER2,");
		sbSql.append("FILLER3,");
		sbSql.append("FILLER4,");
		sbSql.append("FILLER5,");
		sbSql.append("FILLER6,");
		sbSql.append("FILLER7,");
		sbSql.append("FILLER8,");
		sbSql.append("FILLER9,");
		sbSql.append("INST_YMD,");
		sbSql.append("UPDT_YMD,");
		sbSql.append("DEL_YMD,");
		sbSql.append("DEL_FLG,");
		sbSql.append("MGR_NAME,");
		sbSql.append("UPD_PGM_ID,");
		sbSql.append("UPDT_HISTRY");
		sbSql.append(" ) VALUES ( ");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?,");
		sbSql.append("?)");
		
		pstmt = conn.prepareStatement(sbSql.toString());
		
		log.info("受信ファイル情報登録SQL:" + sbSql.toString());
		
		String[] strItem = strLine.split(COMMA);
		
		int index = 1;
		
		StringBuilder sbParam = new StringBuilder();
		sbParam.append("{");
		
		// シーケンス番号
		pstmt.setLong(index++, seq);
		sbParam.append(seq).append(COMMA);
		
		// 上位端末ID
		pstmt.setString(index++, strHigherTermed);
		sbParam.append(strHigherTermed).append(COMMA);
		
		// 案件番号
		if (strItem[0].length() > 11) {
			pstmt.setString(index++, "");
			sbParam.append("").append(COMMA);
		} else {
			pstmt.setString(index++, strItem[0]);
			sbParam.append(strItem[0]).append(COMMA);
		}
		
		// 共通会員IDハッシュ
		if (result) {
			pstmt.setString(index++, strItem[3]);
			sbParam.append(strItem[3]).append(COMMA);
		} else {
			pstmt.setString(index++, "");
			sbParam.append("").append(COMMA);
		}
		
		// 共通会員ID
		pstmt.setString(index++, null);
		sbParam.append("").append(COMMA);
		
		// キャンペーンID
		pstmt.setString(index++, strItem[1]);
		sbParam.append(strItem[1]).append(COMMA);
		
		// 内部キャンペーンID
		pstmt.setString(index++, strItem[2]);
		sbParam.append(strItem[2]).append(COMMA);
		
		// nanaco番号
		if (strItem[4].length() >  16) {
			pstmt.setString(index++, "");
			sbParam.append("").append(COMMA);
		} else {
			pstmt.setString(index++, nanacoNo);
			sbParam.append(STR_NANACO_NO).append(COMMA);
		}
		
		// 旧nanaco番号
		pstmt.setString(index++, null);
		sbParam.append("").append(COMMA);
		
		// 申込付与ポイント数
		pstmt.setString(index++, strItem[5]);
		sbParam.append(strItem[5]).append(COMMA);
		
		// 結果付与ポイント数
		pstmt.setString(index++, null);
		sbParam.append("").append(COMMA);
		
		// データステータス
		if (result) {
			pstmt.setString(index++, "0");
			sbParam.append("0").append(COMMA);
		} else {
			if (custChkIsNg) {
				pstmt.setString(index++, "20");
				sbParam.append("20").append(COMMA);
			} else {
				pstmt.setString(index++, "90");
				sbParam.append("90").append(COMMA);
			}
		}
		
		// 申請日
		pstmt.setString(index++, strDate);
		sbParam.append(strDate).append(COMMA);
		
		// センター反映希望日
		pstmt.setString(index++, null);
		sbParam.append("").append(COMMA);
		
		// センター処理日
		pstmt.setString(index++, null);
		sbParam.append("").append(COMMA);
		
		// オムニフラグ
		pstmt.setString(index++, "1");
		sbParam.append("1").append(COMMA);
		
		// エラーフラグ(結果コード)
		if (result) {
			pstmt.setString(index++, "0");
			sbParam.append("0").append(COMMA);
		} else {
			if (custChkIsNg) {
				pstmt.setString(index++, "1");
				sbParam.append("1").append(COMMA);
			} else {
				pstmt.setString(index++, "9");
				sbParam.append("9").append(COMMA);
			}
		}
		
		// 本部登録データ区分エラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// 反映希望日エラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// 申請日エラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// ポイントエラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// 反映希望日範囲エラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// 申請法人エラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// 会員番号エラー
		if (custChkIsNg) {
			pstmt.setString(index++, "1");
			sbParam.append("1").append(COMMA);
		} else {
			pstmt.setString(index++, "0");
			sbParam.append("0").append(COMMA);
		}
		
		// 会員無効エラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// 会員無効(ネガ登録)エラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// ポイント符号エラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// キャンペーンIDエラー
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// (予備)エラーフラグ1
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// (予備)エラーフラグ2
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// (予備)エラーフラグ3
		if (!strItem[3].isEmpty() && !strItem[4].isEmpty()) {
			pstmt.setString(index++, "2");
			sbParam.append("2").append(COMMA);
		} else if (!strItem[3].isEmpty()) {
			pstmt.setString(index++, "1");
			sbParam.append("1").append(COMMA);
		} else if (!strItem[4].isEmpty()) {
			pstmt.setString(index++, "4");
			sbParam.append("4").append(COMMA);
		} else {
			pstmt.setString(index++, "3");
			sbParam.append("3").append(COMMA);
		}
		
		// (予備)エラーフラグ
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// (予備)エラーフラグ
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// (予備)エラーフラグ
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// (予備)エラーフラグ
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// (予備)エラーフラグ
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// (予備)エラーフラグ
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// 登録年月日
		pstmt.setString(index++, strDate);
		sbParam.append(strDate).append(COMMA);
		
		// 更新年月日
		pstmt.setString(index++, strDate);
		sbParam.append(strDate).append(COMMA);
		
		// 削除年月日
		pstmt.setString(index++, null);
		sbParam.append("").append(COMMA);
		
		// 削除フラグ
		pstmt.setString(index++, "0");
		sbParam.append("0").append(COMMA);
		
		// オペレータ氏名
		pstmt.setString(index++, "GW0002");
		sbParam.append("GW0002").append(COMMA);
		
		// 更新プログラムID
		pstmt.setString(index++, "GW0002");
		sbParam.append("GW0002").append(COMMA);
		
		// システム更新最終履歴
		pstmt.setString(index++, "");
		sbParam.append("").append("}");
		
		pstmt.executeQuery();
		pstmt.close();
		
		log.info("付与依頼情報登録処理終了。");
	}
	
	public String getNanacoNo(String strUserHash) throws SQLException {
		log.info("nanaco番号取得処理開始");
		
		String nanacoNo = "";
		
		StringBuilder sbSql = new StringBuilder();
		
		sbSql.append("SELECT /* J_ONG0002_01S001 */");
		sbSql.append("TNL_NANACO_NO");
		sbSql.append("FROM");
		sbSql.append("TRN_NANACO_NO_LINK TNL");
		sbSql.append("WHERE");
		sbSql.append("TNL.CMN_MMBR_ID_HASH = ? ");
		
		pstmt = conn.prepareStatement(sbSql.toString());
		log.info("nanaco番号紐付テーブルからnanaco番号取得SQL:" + sbSql.toString());
		
		pstmt.setString(1, strUserHash);
		log.info("パラメータ:{" + strUserHash + "}");
		
		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) 
			nanacoNo = rs.getString("NANACO_NO");
		
		rs.close();
		pstmt.close();
		
		log.info("nanaco番号取得処理終了。");
		
		return nanacoNo;
	}
	
	public String getSeqNo() throws Exception {
		log.info("シーケンス番号取得処理開始。");
		
		String seqNo = null;
		
		pstmt = conn.prepareStatement("insert into S_NANACO_POINT_TORI_SEQ values (null)");
		pstmt.executeUpdate();
		conn.commit();
		pstmt.close();
		
		pstmt = conn.prepareStatement("select last_insert_id()");
		ResultSet rs = pstmt.executeQuery();
		while(rs.next())
			seqNo = rs.getString(1);
		
		rs.close();
		pstmt.close();
		
		log.info("シーケンス番号取得処理終了。");
		
		return seqNo;
	}
	
	public void closeConnection() throws SQLException {
		DBManager.closeConnection(conn);
		log.info("DB接続を切断しました。[プログラムID:" + LOG_PGM_ID + "]");
	}
}
