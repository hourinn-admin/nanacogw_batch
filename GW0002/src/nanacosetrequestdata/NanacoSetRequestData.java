package nanacosetrequestdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class NanacoSetRequestData {
	
	private static Logger log = Logger.getLogger(NanacoSetRequestData.class);
	
	private static final String LOG_PGM_ID = NanacoSetRequestData.class.getSimpleName();
	
	
	public static void main(String [] args) {
		
		log.info("付与依頼ファイル受信処理を開始します。[プログラムID:" + LOG_PGM_ID + "]");
		
		boolean flg = false;
		
		BufferedReader br = null;
		
		NanacoSetRequestDataDao dao = null;
		
		try {
			dao = new NanacoSetRequestDataDao();
			
			String requestFileNm = System.getenv("CSV_FILE_PATH");
			
			log.debug("付与依頼ファイル:" + requestFileNm);
			
			String strHigherTermId = System.getenv("SUPPORT_HIGHER_TERM_ID");
			if (strHigherTermId.length() != 20)
				strHigherTermId = strHigherTermId + "           ";
			log.debug("▲上位端末ID:" + strHigherTermId);
			
			File file = new File(requestFileNm);
			br = new BufferedReader(new FileReader(file));
			
			String line;
			int lineNo = 1;
			
			List<String> errList = new ArrayList<String>();
			
			String strDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			
			while ((line = br.readLine()) != null) {
				boolean result = false;
				Map<String,List<String>> map = CommonUtils.chkCsvFileInfo(line, dao);
				
				List<String> resultMsgList = map.get("errInfo");
				
				if (resultMsgList.size() > 0) {
					for (int i = 0; i < resultMsgList.size(); i++) {
						errList.add(String.valueOf(lineNo) + "レコード目データ:" + resultMsgList.get(i));
					}
				} else {
					result = true;
				}
				
				String nanacoNo = "";
				if (map.get("nanacoNo").size() > 0)
					nanacoNo = map.get("nanacoNo").get(0);
				
				boolean custChkIsNg = false;
				if (map.get("custNo").size() > 0) {
					custChkIsNg = true;
				}
				
				Long seq = Long.parseLong(dao.getSeqNo());
				
				dao.insCsvInfo(line, result, strDate, seq, nanacoNo, strHigherTermId, custChkIsNg);
				
				lineNo++;
			}
			
			
			if (errList.size() > 0) {
				String errFilePath = System.getenv("BATCH_FORMAT_ERROR_LOG_DIR");
				log.debug("▲エラーファイル出力先:" + errFilePath);
				
				String errFileNm = System.getenv("BATCH_FORMAT_ERROR_LOG_FILE_NAME");
				log.debug("▲エラーファイル名:" + errFileNm);
				
				StringBuilder sbErrFileName = new StringBuilder();
				sbErrFileName.append(errFilePath + File.separator + errFileNm);
				log.debug("▲エラーファイルフルパス:" + sbErrFileName.toString());
				
				CreateErrFileLogic.createErrFile(errList, errFilePath);
				
			}
			
		} catch (FileNotFoundException e) {
			flg = true;
			log.error("プロパティファイルが存在しません。[プログラム:" + LOG_PGM_ID + "]",e);
		} catch (Exception e) {
			flg = true;
			log.error("エラー発生。[プログラムID:" + LOG_PGM_ID + "]",e);
		} finally {
			try {
				dao.closeConnection();
			} catch (SQLException e) {
				flg = true;
				log.error("DBを閉じる際にエラー発生。",e);
			}
			
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					flg = true;
					log.error("受信ファイルを閉じる際にエラー発生。[プログラム:" + LOG_PGM_ID + "]",e);
				}
			}
		}
		
		if (flg) 
			System.exit(255);
		else 
			System.exit(0);
	}

}
