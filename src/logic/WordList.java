package logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 存储单词本内容，我们仅仅存储单词本身，释义等存储在WordItem的缓存中
 */
public class WordList {

    // 存储的主体，单词表的更新在这里完成
    public static Set<String> wordSet = new HashSet<>();
    // 背单词时，我们需要以固定顺序显示单词，所以以ArrayList进行存储
    public static ArrayList<String> wordList = new ArrayList<>();

    /** 当wordList和wordSet不再对应时，我们在合适的时间手动调用刷新函数 */
    public static void refresh() {
        wordList = new ArrayList<>(wordSet);
    }
}