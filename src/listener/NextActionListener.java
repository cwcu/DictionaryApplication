package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.View;
import logic.WordList;

/**
 * 显示下一个单词，必要时更新单词本显示缓存
 */
public class NextActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        int oldIndex = View.wordIndex;
        if (View.wordIndex == WordList.wordList.size() - 1) {
            WordList.refresh();
            View.wordIndex = WordList.wordList.isEmpty() ? -1 : 0;
        } else if (View.wordIndex == -1 && !WordList.wordSet.isEmpty()) {
            WordList.refresh();
            View.wordIndex = 0;
        } else if (View.wordIndex != -1) {
            View.wordIndex++;
        }
        if (oldIndex != View.wordIndex) {
            if (View.wordIndex != -1)
                View.wordlist.setText(WordList.wordList.get(View.wordIndex));
            else
                View.wordlist.setText("单词本为空");
        }
    }

}