package nanacosetrequestdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DBManager {
	private static final String CONF_DIR = System.getenv("CONF_DIR");
	
	private static final String DBCONNECT_FILE_NAME = "DBConnect.txt";
	
	private static final String DBCONNECT_FILE_PATH = CONF_DIR + DBCONNECT_FILE_NAME;
	
	private static Logger log = Logger.getLogger(DBManager.class);
	
	private static String kikanHost;
	
	private static String kikanUser;
	
	private static String kikanPassword;
	
	private DBManager() {}
	
	static {
		setConstants();
	}
	
	public static Connection createConnection(boolean isReadOnly,boolean isTransactionModeOn) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection(kikanHost,kikanUser,kikanPassword);
		conn.setReadOnly(isReadOnly);
		conn.setAutoCommit(isTransactionModeOn);
		return conn;
	}
	
	public static void closeConnection(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();
	}
	
	private static void setConstants() {
		Properties props = getProperties();
		kikanHost = props.getProperty("kikan.host");
		kikanUser = props.getProperty("kikan.user");
		kikanPassword = props.getProperty("kikan.password");
	}
	
	private static Properties getProperties() {
		FileInputStream stream = null;
		Properties props = new Properties();
		try {
			stream = new FileInputStream(new File(DBCONNECT_FILE_PATH));
			props.load(stream);
		} catch (FileNotFoundException e) {
			log.error("DB接続設定ファイルが見つかりません。ファイルパス：" + DBCONNECT_FILE_PATH);
			throw new RuntimeException();
		} catch (IOException e) {
			log.error("DB接続設定ファイルの読込に失敗しました。ファイルパス：" + DBCONNECT_FILE_PATH);
			throw new RuntimeException();
		} finally {
			if (null != stream) {
				try {
					stream.close();
				} catch (IOException e) {
					log.error("DB接続設定ファイルのクローズに失敗しました。ファイルパス：" + DBCONNECT_FILE_PATH);
					throw new RuntimeException();
				}
			}
		}
		return props;
	}
}
