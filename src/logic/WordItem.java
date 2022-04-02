package logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * 所有单词对象，我们使用内部缓存避免重复查询
 */
public class WordItem {

    // 音频文件缓存在/temp文件内，如果/temp不存在则需要创建此文件夹
    static final String MP3PATH = "./temp"; //WordItem.class.getResource("/temp").getPath();
    // 缓存
    static HashMap<String, WordItem> cache = new HashMap<>();

    String word;
    // 音标
    String symbol = "";
    // 释义
    String meaning = "";
    // MP3
    File mp3 = null;

    {
        File file = new File(MP3PATH);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 我们使用静态方法获取对象避免重复创建对象的内存开销
     * 
     * @param word
     * @return 返回对象，如果对象尚不存在，则手动创建
     */
    static public WordItem getWordItem(String word) {
        if (cache.containsKey(word))
            return cache.get(word);
        if (word == "")
            return new WordItem();
        WordItem inner = new WordItem(word);
        cache.put(word, inner);
        return inner;
    }

    /**
     * 构造函数设为private防止外界手动调用，创建时下载对应音频文件
     * 
     * @param word
     */
    private WordItem(String word) {
        this.word = word;
        if (word == "")
            return;
        File mp3temp = new File(MP3PATH + "/" + word + ".mp3");
        if (mp3temp.exists()) {
            mp3 = mp3temp;
            return;
        }
        int byteread = 0;
        URL url;
        try {
            mp3temp.createNewFile();
            url = new URL("https://ssl.gstatic.com/dictionary/static/sounds/oxford/" + word + "--_gb_1.mp3");
        } catch (Exception e1) {
            if (mp3temp.exists())
                mp3temp.delete();
            return;
        }

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(mp3temp, false);
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fs.close();
        } catch (IOException e) {
            mp3temp.delete();
            return;
        }

        mp3 = mp3temp;
    }

    /** 默认构造函数，创建空对象 */
    public WordItem() {
        this.word = "";
    }

    /** 是否为空对象 */
    public boolean IsDefined() {
        return word != "";
    }

    /**
     * 单词是否已经被查询， 新创建的对象可能还没有通过查询获取释义和音标，此时haveQuery为假。
     */
    public boolean haveQuery() {
        return meaning != "";
    }

    public String getWord() {
        return new String(word);
    }

    public String getSymbol() {
        return symbol;
    }

    public String getMeaning() {
        return meaning;
    }

    public File getMp3() {
        return mp3;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

}