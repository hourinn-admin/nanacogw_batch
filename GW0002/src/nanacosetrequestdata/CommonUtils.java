package nanacosetrequestdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class CommonUtils {
	private static final Logger LOG = Logger.getLogger(CommonUtils.class.getName());
	
	private static final String KANMA = ",";
	
	private static final String HANKAKU_SUJI = "0123456789";
	
	public static boolean isEmpty(String value) {
		if (value == null || "".equals(value))
			return true;
		else
			return false;
	}
	
	public static Map<String,List<String>> chkCsvFileInfo(String strLineInfo,NanacoSetRequestDataDao dao) throws Exception {
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		
		List<String> errList = new ArrayList<String>();
		
		List<String> nanacoNoList = new ArrayList<String>();
		List<String> custNoErrList = new ArrayList<String>();
		
		String [] strItem = strLineInfo.split(KANMA,-1);
		
		int ret = strLineInfo.getBytes("SJIS").length;
		
		if (ret >= 1114) {
			LOG.info("レコードの長さが1114バイト以上です。[案件番号:" + strItem[0] + "]");
			errList.add("レコードの長さが1114バイト以上です。[案件番号:" + strItem[0] + "]");
		}
		
		if (strItem.length != 29) {
			LOG.info("レコードの項目数が不正です。[案件番号:" + strItem[0] + "]");
			errList.add("レコードの項目数が不正です。[案件番号:" + strItem[0] + "]");
		}
		
		if (!isEmpty(strItem[4])) {
			LOG.debug("★nanaco番号を設定:" +strItem[4]);
			nanacoNoList.add(strItem[4]);
			char[] ch = strItem[4].toCharArray();
			for (int i = 0; i < ch.length; i++) {
				if (!HANKAKU_SUJI.contains(String.valueOf(ch[i]))) {
					LOG.info("nanaco番号が不正です(属性)。[案件番号:" + strItem[0] + "]");
					errList.add("nanaco番号が不正です(属性)。[案件番号:" + strItem[0] + "]");
					break;
				}
			}
			
			if (strItem[4].length() != 16) {
				LOG.info("nanaco番号が不正です(桁数)。[案件番号:" + strItem[0] + "]");
				errList.add("nanaco番号が不正です(桁数)。[案件番号:" + strItem[0] + "]");
			}
		}
		
		if (!isEmpty(strItem[3]) && isEmpty(strItem[4])) {
			String strUserHash = strItem[3];
			String strNanacoNo = dao.getNanacoNo(strUserHash);
			if (isEmpty(strNanacoNo)) {
				LOG.info("共通会員IDハッシュでnanaco番号の取得に失敗しました。[案件番号:" + strItem[0] + "]");
				errList.add("共通会員IDハッシュでnanaco番号の取得に失敗しました。[案件番号:" + strItem[0] + "]");
				custNoErrList.add("共通会員IDハッシュでnanaco番号の取得に失敗しました。[案件番号:" + strItem[0] + "]");
			} else {
				LOG.debug("★nanaco番号を設定:" + strNanacoNo);
				nanacoNoList.add(strNanacoNo);
			}
		}
		
		if (!isEmpty(strItem[3]) && strItem[3].length() != 128) {
			LOG.info("共通会員IDハッシュが不正です(属性)。[案件番号:" + strItem[0] + "]");
			errList.add("共通会員IDハッシュが不正です(属性)。[案件番号:" + strItem[0] + "]");
		}
		
		if (isEmpty(strItem[5])) {
			LOG.info("付与ポイント数が存在しません。[案件番号:" + strItem[0] + "]");
			errList.add("付与ポイント数が存在しません。[案件番号:" + strItem[0] + "]");
		} else {
			if (strItem[5].contains("-")) {
				if (!strItem[5].startsWith("-")) {
					LOG.info("付与ポイント数が不正です。[案件番号:" + strItem[0] + "[付与ポイント数:" + strItem[5] + "]");
					errList.add("付与ポイント数が不正です。[案件番号:" + strItem[0] + "[付与ポイント数:" + strItem[5] + "]");
				}
			} else {
				char[] chPoint = strItem[5].toCharArray();
				for (int i = 0; i < chPoint.length ; i++) {
					if (!HANKAKU_SUJI.contains(String.valueOf(chPoint[i]))) {
						LOG.info("付与ポイント数が不正です。[案件番号:" + strItem[0] + "[付与ポイント数:" + strItem[5] + "]");
						errList.add("付与ポイント数が不正です。[案件番号:" + strItem[0] + "[付与ポイント数:" + strItem[5] + "]");
						break;
					}
				}
			}
		}
		
		if (strItem[5].length() > 6) {
			LOG.info("付与ポイント数が不正です。[案件番号:" + strItem[0] + "[付与ポイント数:" + strItem[5] + "]");
			errList.add("付与ポイント数が不正です。[案件番号:" + strItem[0] + "[付与ポイント数:" + strItem[5] + "]");
		}
		
		if (isEmpty(strItem[0])) {
			LOG.info("案件番号が設定されていません。[案件番号:" + strItem[0] + "]");
			errList.add("案件番号が設定されていません。[案件番号:" + strItem[0] + "]");
		}
		
		if (strItem[0].length() > 11) {
			LOG.info("案件番号が不正です。[案件番号:" + strItem[0] + "]");
			errList.add("案件番号が不正です。[案件番号:" + strItem[0] + "]");
		}
		
		map.put("errInfo", errList);
		map.put("nanacoNo",nanacoNoList);
		
		map.put("custNo", custNoErrList);
		
		return map;
	}
}
