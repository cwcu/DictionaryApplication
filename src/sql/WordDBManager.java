package sql;
import java.sql.Connection; 
import java.sql.ResultSet; 
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import org.sqlite.SQLiteException;
/**
 * 用于创建、删除、修改单词数据库。
 * @author 汪永毅
 * @see WordDBQuerier
 */
public class WordDBManager {
	/* 加载SQLite所需类 */
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	/**
	 * 数据表的各列，与WordDBQuerier保持一致
	 * @see WordDBQuerier
	 */
	public final static String TABLE_NAME = WordDBQuerier.TABLE_NAME;
	private final static String ID = WordDBQuerier.ID, WORD = WordDBQuerier.WORD,
		PHONETIC = WordDBQuerier.PHONETIC, MEANING = WordDBQuerier.MEANING;
	/**
	 * 创建表的格式
	 * @see WordDBQuerier
	 */
	public final static String TABLE_FORMAT = "("+ID+" integer primary key, "+
		WORD+" text, "+PHONETIC+" text, "+MEANING+" text)";
	
	/**
	 * 插入数据的格式
	 * @see TABLE_FORMAT
	 * @param id 全局唯一单词编号
	 * @param word 单词
	 * @param phonetic 音标
	 * @param meaning 汉语释义
	 * @return 待插入数据的格式串，用于拼接SQL。
	 */
	private static String recordFormat(
		Integer id, String word, String phonetic, String meaning) {
		return "("+id+", \""+word+"\", \""+phonetic+"\", \""+meaning+"\")";
	}
	/**
	 * 获取指定文件位置的数据库连接
	 * @param dbFilePath 数据库文件绝对路径
	 * @return 连接对象
	 * @throws SQLException 会在public函数中得到处理
	 */
	private static Connection getConnection(String dbFilePath) 
		throws SQLException {
		String connStr = "jdbc:sqlite:" + dbFilePath;
		return DriverManager.getConnection(connStr);
	}
	/**
	 * 在指定位置创建数据库
	 * @param dbFilePath 指定绝对路径(xxx.db)
	 * @return 创建是否成功
	 */
	public static Boolean createWordDB(String dbFilePath) {
		File dbFile = new File(dbFilePath);
		if (dbFile.exists()) {
			System.err.println("Database already exists.");
			return false;
		}
		try (Connection conn = getConnection(dbFilePath)) {
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			stmt.executeUpdate("create table "+
				TABLE_NAME+" "+TABLE_FORMAT+";");
			conn.commit();
			return true;
		} catch (SQLiteException sqlte) {
			sqlte.printStackTrace();
			return false;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
	}
	/**
	 * 向数据库插入结果集合(必须包含所有字段：ID/WORD/PHONETIC/MEANING)
	 * 已设置自动提交。
	 * @param dbFilePath 数据库文件绝对路径
	 * @param rs 待插入的结果集
	 * @return 插入是否成功
	 */
	public static Boolean insertIntoDB(
		String dbFilePath, ResultSet rs) {
		File dbFile = new File(dbFilePath);
		if (! dbFile.exists()) {
			System.err.println("Database does not exist, can not insert into it.");
			return false;
		}
		try (Connection conn = getConnection(dbFilePath)) {
			Statement stmt = conn.createStatement();
			Integer id=null;
			String word=null, phonetic=null, meaning=null;
			while (rs.next()) {
				id = rs.getInt(ID);
				word = rs.getString(WORD);
				phonetic = rs.getString(PHONETIC);
				meaning = rs.getString(MEANING);
				stmt.executeUpdate("insert into "+TABLE_NAME+" values "+
					recordFormat(id, word, phonetic, meaning)+";");
			}
			return true;
			
		} catch (SQLiteException sqlte) {
			sqlte.printStackTrace();
			return false;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
	}
	/**
	 * 尝试删除整个数据库文件
	 * @param dbFilePath 数据库文件的绝对路径
	 * @return 是否成功删除
	 */
	public static Boolean dropDB(String dbFilePath) {
		File dbFile = new File(dbFilePath);
		if (! dbFile.exists()) {
			System.err.println("Database does not exist, can not drop it.");
			return false;
		}
		if (! dbFilePath.endsWith(".db")) {
			System.err.println("Not database file (ends with .db).");
			return false;
		}
		return dbFile.delete();
	}
	/**
	 * 删除ID为特定值的数据项
	 * 未设置自动提交。
	 * @param dbFilePath 数据库文件绝对路径
	 * @param id 待删除项ID的取值
	 * @return 删除是否成功
	 */
	public static Boolean deleteFromDBById(String dbFilePath, Integer id) {
		File dbFile = new File(dbFilePath);
		if (! dbFile.exists()) {
			System.err.println(
				"Database does not exist, can not delete from it.");
			return false;
		}
		try (Connection conn = getConnection(dbFilePath)) {
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			stmt.executeUpdate("delete from "+TABLE_NAME+" where "+ID+"="+id+";");
			conn.commit();
			return true;
		} catch (SQLiteException sqlte) {
			sqlte.printStackTrace();
			return false;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
	}
	/**
	 * 删除WORD字段为特定值的数据项
	 * 未设定自动提交。
	 * @param dbFilePath 数据库文件路径
	 * @param word 待删除数据项WORD字段取值
	 * @return 删除是否成功
	 */
	public static Boolean deleteFromDBByWord(String dbFilePath, String word) {
		File dbFile = new File(dbFilePath);
		if (! dbFile.exists()) {
			System.err.println(
				"Database does not exist, can not delete from it.");
			return false;
		}
		try (Connection conn = getConnection(dbFilePath)) {
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			stmt.executeUpdate("delete from "+TABLE_NAME+" where "+WORD+"="+word+";");
			conn.commit();
			return true;
		} catch (SQLiteException sqlte) {
			sqlte.printStackTrace();
			return false;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
	}
}
