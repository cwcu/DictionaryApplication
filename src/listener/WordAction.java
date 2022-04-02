package listener;

import java.awt.event.*;
import java.util.ArrayList;

import gui.*;
import logic.QueryWrapper;
import logic.WordItem;

/*
 * 该部分为查询单词的响应事件
 * 思路为在单词库中寻找单词，如何通过setText展现在屏幕上
 */
public class WordAction implements ActionListener {
	View v = new View();

	public WordAction(View v) {
		this.v = v;
	}

	public void actionPerformed(ActionEvent event) {
		for (int i = 0; i < 10; ++i) {
			View.showResult[i].setVisible(false);
			View.detailList[i].setVisible(false);
			View.mp3[i].setVisible(false);
		}
		String word = v.input.getText();
		boolean English = true;
		for (int i = 0; i < word.length(); ++i) {
			if (!(word.charAt(i) >= 'A' && word.charAt(i) <= 'Z' || word.charAt(i) >= 'a' && word.charAt(i) <= 'z')) {
				English = false;
				break;
			}
		}
		if (English) {
			WordItem ans = QueryWrapper.getEnglishWord(word);
			if (ans.IsDefined()) {
				View.showResult[0].setText(ans.getWord() + "\n" + ans.getSymbol() + "\n" + ans.getMeaning());
				View.detailList[0].removeActionListener(View.detailList[0].getActionListeners()[0]);
				View.detailList[0].addActionListener(new DetailsAction(v, word));
				View.mp3[0].removeActionListener(View.mp3[0].getActionListeners()[0]);
				View.mp3[0].addActionListener(new Mp3Listener(word));
				View.showResult[0].setVisible(true);
				View.detailList[0].setVisible(true);
				View.mp3[0].setVisible(true);
			} else {
				View.showResult[0].setText("查询结果为空");
				View.showResult[0].setVisible(true);
			}
		} else {
			ArrayList<WordItem> ans = QueryWrapper.getChineseWords(word);
			if (ans.isEmpty()) {
				View.showResult[0].setText("查询结果为空");
				View.showResult[0].setVisible(true);
			} else {
				for (int i = 0; i < 10 && i < ans.size(); ++i) {
					View.showResult[i].setText(
							ans.get(i).getWord() + "\n" + ans.get(i).getSymbol() + "\n" + ans.get(i).getMeaning());
					View.detailList[i].removeActionListener(View.detailList[i].getActionListeners()[0]);
					View.detailList[i].addActionListener(new DetailsAction(v, ans.get(i).getWord()));
					View.mp3[i].removeActionListener(View.mp3[i].getActionListeners()[0]);
					View.mp3[i].addActionListener(new Mp3Listener(ans.get(i).getWord()));
					View.showResult[i].setVisible(true);
					View.detailList[i].setVisible(true);
					View.mp3[i].setVisible(true);
				}
			}
		}
	}
}
