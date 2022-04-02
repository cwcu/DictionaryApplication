package logic;

import java.sql.ResultSet;
import java.util.ArrayList;

import sql.WordDBQuerier;

/**
 * 查询方法的包装类
 */
public class QueryWrapper {
    static final String SOURCEPATH = QueryWrapper.class.getResource("/data/dict.db").getPath();;

    /**
     * 查询英文单词，首先查找数据库，若失败则联网查找
     * 
     * @param word
     * @return WordItem对象
     */
    public static WordItem getEnglishWord(String word) {
        WordItem wordItem = WordItem.getWordItem(word);
        if (wordItem.haveQuery())
            return wordItem;
        try (WordDBQuerier a = new WordDBQuerier(SOURCEPATH)) {
            ResultSet result = a.getRecordSetByStrictMatching(WordDBQuerier.WORD, word);
            if (result.next()) {
                wordItem.setMeaning(result.getString(WordDBQuerier.MEANING));
                wordItem.setSymbol(result.getString(WordDBQuerier.PHONETIC));
                return wordItem;
            } else {
                return OnlineQuery.queryEnglish(word);
            }
        } catch (Exception e) {
            System.out.println("get error during connecting to db.");
            e.printStackTrace();
            return OnlineQuery.queryEnglish(word);
        }
    }

    /**
     * 查询中文单词，首先查找数据库，若失败则联网查找
     * 
     * @param word
     * @return 返回所有匹配的wordItem
     */
    public static ArrayList<WordItem> getChineseWords(String word) {
        ArrayList<WordItem> ans = new ArrayList<>();
        try (WordDBQuerier a = new WordDBQuerier(SOURCEPATH)) {
            ResultSet result = a.getRecordSetByFuzzyMatching(WordDBQuerier.MEANING, word);
            while (result.next()) {
                WordItem wordItem = WordItem.getWordItem(result.getString(WordDBQuerier.WORD));
                wordItem.setMeaning(result.getString(WordDBQuerier.MEANING));
                wordItem.setSymbol(result.getString(WordDBQuerier.PHONETIC));
                ans.add(wordItem);
            }
            if (ans.isEmpty())
                return OnlineQuery.queryChinese(word);
            else
                return ans;
        } catch (Exception e) {
            System.out.println("get error during connecting to db.");
            e.printStackTrace();
            return OnlineQuery.queryChinese(word);
        }
    }

}