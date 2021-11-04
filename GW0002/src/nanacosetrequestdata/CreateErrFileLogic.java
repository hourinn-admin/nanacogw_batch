package nanacosetrequestdata;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.log4j.Logger;

public class CreateErrFileLogic {
	
	private static Logger log = Logger.getLogger(CreateErrFileLogic.class);
	
	private static final String LOG_PGM_ID = CreateErrFileLogic.class.getSimpleName();
	
	private static final String CRLF = System.getProperty("line.separator");
	
	public static boolean createErrFile(List<String> errList, String errFilePath) {
		log.info("エラーファイルを作成開始。[プログラムID:" + LOG_PGM_ID + "]");
		
		boolean errorFlg = false;
		
		
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errFilePath),"Shift_JIS"))) {
			
			log.debug("★★★ エラーファイル名:" + errFilePath + " ★★★");
			
			for(int i = 0; i < errList.size(); i++) {
				String strErrInfo = errList.get(i);
				bw.write(strErrInfo);
				if (i != errList.size() - 1) {
					bw.write(CRLF);
				}
			}
		} catch (Exception e) {
			errorFlg = true;
			log.error("CSVファイルに書き込む際にエラー発生。[プログラムID:" + LOG_PGM_ID + "]",e);
		}
		
		return errorFlg;
	}
}
