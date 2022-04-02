package logic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 通过联网进行单词查询，本地数据库查询失败后的备用方法
 */
public class OnlineQuery {
    /**
     * 英文查询
     * 
     * @param word
     * @return WordItem对象，查询失败返回默认WordItem（空对象）
     */
    public static WordItem queryEnglish(String word) {
        try {
            Document doc = Jsoup.parse(new URL("https://dict.cn/search?q=" + word), 1000);
            Elements meaning = doc.getElementsByClass("dict-basic-ul");
            Elements phonetic = doc.getElementsByClass("phonetic");
            // System.out.println(meaning.text());
            // System.out.println(phonetic.text());
            if (meaning.text() == "")
                return new WordItem();
            WordItem wordItem = WordItem.getWordItem(word);
            wordItem.setMeaning(meaning.text());
            wordItem.setSymbol(phonetic.text());
            return wordItem;
        } catch (IOException e) {
            return new WordItem();
        }
    }

    /**
     * 中文查询
     * 
     * @param word
     * @return 一列WordItem对象，此处只返回匹配的单词（不包括短语），没有的话返回空ArrayList
     */
    public static ArrayList<WordItem> queryChinese(String word) {
        ArrayList<WordItem> ans = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(new URL("https://dict.cn/search?q=" + word), 1000);
            Elements meaning = doc.getElementsByClass("layout cn").select("ul").select("li");
            for (Element i : meaning) {
                if (i.text().indexOf(" ") == -1) {
                    System.out.println(i.text());
                    WordItem tmp = queryEnglish(i.text());
                    if (tmp.IsDefined())
                        ans.add(tmp);
                }
            }
        } catch (IOException e) {
        }
        return ans;
    }

}