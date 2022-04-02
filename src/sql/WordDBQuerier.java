package sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

/**
 * @author 汪永毅 
 * 创建本类的对象时会自动建立到单词数据库的连接
 * 连接之后可以调用本类的方法查询单词数据库
 * 推荐创建对象时使用try with方式，可以自动关闭
 * 否则应当手动调用close方法
 * 
 * 使用示例：
 *     try(WordDBConnector c = new WordDBConnector("xxx.db")){...}
 * xxx.db应当为数据库文件的绝对路径、
 */
public class WordDBQuerier implements AutoCloseable{
	/* 数据库默认表名（且只有这一张表） */
	public final static String TABLE_NAME = "word";
	/** 
	 * 这些字符串常量表示数据库中列的名字，可以用于解析返回结果集中的字段：
	 * 使用示例：
	 *     ResultSet s = getMeaningSetByWord("word");
	 *     while (s.next()) {
	 *         System.out.println(s.getString(WordDBConnector.MEANING));
	 *     } 
	 */
	public final static String ID="_id", WORD="word", 
		PHONETIC="phonetic", MEANING="meaning"; 
	
	private Connection conn;
	private String connStr;
	
	/**
	 * 关闭和数据库的连接
	 */
	@Override
	public void close() {
		try {
			conn.close();
		} catch(Exception e) {}
	}
	
	/**
	 * @param dbFilePath 单词数据库文件的绝对路径
	 */
	public WordDBQuerier(String dbFilePath) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		File dbFile = new File(dbFilePath);
		if (! dbFile.exists()) {
			System.err.println("Database does not exist, create new one.");
		}
		connStr = "jdbc:sqlite:" + dbFilePath;
		try {
			conn = DriverManager.getConnection(connStr);
			conn.setAutoCommit(false);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * 对每一行，在条件列上执行严格匹配，匹配成功则获取该行目标列上的值。
	 * @param conditionColumn 条件列
	 * @param conditionValue 条件列取值
	 * @param targetColumn 目标列
	 * @return 在条件列上匹配成功的行的目标列取值。
	 */
	private ResultSet getFieldByStrictMatching(
		String conditionColumn, String conditionValue,
		String targetColumn) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select "+targetColumn+" from "+TABLE_NAME+
				" where "+conditionColumn+"=\""+conditionValue+"\";");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return rs;
	}
	/**
	 * 根据单词唯一编号返回单词字符串
	 * @param id 单词唯一编号
	 * @return 存在则返回单词，否则为null
	 * @see getFieldByStrictMatching
	 */
	public String getWordById(Integer id) {
		ResultSet rs = getFieldByStrictMatching(ID, id.toString(), WORD);
		try {
			rs.next();
		    return rs.getString(WORD);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		}
	}
	/**
	 * 根据单词唯一编号返回音标
	 * @param id 单词唯一编号
	 * @return 存在则返回音标，否则为null
	 * @see getFieldByStrictMatching
	 */
	public String getPhoneticById(Integer id) {
		ResultSet rs = getFieldByStrictMatching(ID, id.toString(), PHONETIC);
		try {
			rs.next();
		    return rs.getString(PHONETIC);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		}
	}
	/**
	 * 根据单词唯一编号返回汉语释义
	 * @param id 单词唯一编号
	 * @return 存在则返回汉语释义，否则为null
	 * @see getFieldByStrictMatching
	 */
	public String getMeaningById(Integer id) {
		ResultSet rs = getFieldByStrictMatching(ID, id.toString(), MEANING);
		try {
			rs.next();
		    return rs.getString(MEANING);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		}
	}
	/**
	 * 对每一行，条件列上执行严格匹配，获取匹配成功的行
	 * @param conditionColumn 条件列
	 * @param conditionValue 条件列取值
	 * @return 匹配成功的行
	 */
	public ResultSet getRecordSetByStrictMatching(
		String conditionColumn, String conditionValue) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from "+TABLE_NAME+
				" where "+conditionColumn+"=\""+conditionValue+"\";");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return rs;
	}
	/**
	 * 通过单词唯一编号获取该单词所在行
	 * @param id 编号
	 * @return 单词所在行
	 * @see getRecordSetByStrictMatching
	 */
	public ResultSet getRecordById(Integer id) {
		return getRecordSetByStrictMatching(ID, id.toString());
	}
	/**
	 * 对每一行，用conditionValue分别严格匹配单词、汉语释义，返回至少一项严格匹配成功的行
	 * @param conditionValue 条件值
	 * @return 匹配成功的行
	 */
	public ResultSet getRecordSetByStrictMatching(String conditionValue) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from "+TABLE_NAME+
				" where "+WORD+"=\""+conditionValue+"\" or "+
				MEANING+"=\""+conditionValue+"\";");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return rs;
	}
	/**
	 * 严格匹配单词，查找汉语释义
	 * @param word 单词
	 * @return 汉语释义的集合
	 * @see getFieldByStrictMatching
	 */
	public ResultSet getMeaningSetByWord(String word) {
		return getFieldByStrictMatching(WORD, word, MEANING);
	}
	/**
	 * 严格匹配单词，查找读音
	 * @param word 单词
	 * @return 读音集合
	 * @see getFieldByStrictMatching
	 */
	public ResultSet getPhoneticSetByWord(String word) {
		return getFieldByStrictMatching(WORD, word, PHONETIC);
	}
	/**
	 * 在条件列上模糊匹配，查找满足条件的记录
	 * @param conditionColumn 条件列
	 * @param conditionValue 条件列取值（模糊匹配）
	 * @return 满足条件的行
	 */
	public ResultSet getRecordSetByFuzzyMatching(
		String conditionColumn, String conditionValue) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from "+TABLE_NAME+
				" where "+conditionColumn+" like \"%"+conditionValue+"%\";");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return rs;
	}
	/**
	 * 同时模糊匹配单词、释义两列，返回单词、释义中至少有一个包含conditionValue的行
	 * @param conditionValue 条件取值（模糊匹配）
	 * @return 满足条件的行
	 */
	public ResultSet getRecordSetByFuzzyMatching(String conditionValue) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from "+TABLE_NAME+
				" where "+WORD+" like \"%"+conditionValue+"%\" or "+
				MEANING+" like \"%"+conditionValue+"%\";");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return rs;
	}
}