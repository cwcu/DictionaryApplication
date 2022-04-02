package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import gui.View;
import logic.QueryWrapper;
import logic.WordItem;
import logic.WordList;

/**
 * 导入单词本
 */
public class ReadActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String path = this.getClass().getResource("/data/WordList.txt").getPath();
        File file = new File(path);
        try {
            if (!file.exists())
                file.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String i = "";
            while ((i = reader.readLine()) != null) {
                WordList.wordSet.add(i);
                // 如果新导入的单词并未被查询，手动在WordItem中创建缓存
                if (!WordItem.getWordItem(i).haveQuery()) {
                    QueryWrapper.getEnglishWord(i);
                }
            }
            reader.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        int oldIndex = View.wordIndex;
        if (View.wordIndex == WordList.wordList.size() - 1) {
            WordList.refresh();
            View.wordIndex = WordList.wordList.isEmpty() ? -1 : 0;
        } else if (View.wordIndex == -1 && !WordList.wordSet.isEmpty()) {
            WordList.refresh();
            View.wordIndex = 0;
        }
        if (oldIndex != View.wordIndex) {
            if (View.wordIndex != -1)
                View.wordlist.setText(WordList.wordList.get(View.wordIndex));
            else
                View.wordlist.setText("单词本为空");
        }

    }

}
