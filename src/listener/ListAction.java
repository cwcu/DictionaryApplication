package listener;

import java.awt.event.*;

import logic.WordList;

/*
 * 将单词加入或者移出单词本
 */
public class ListAction implements ActionListener {
	String word;

	public ListAction(String word) {
		this.word = word;
	}

	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
			case "加入单词本":
				WordList.wordSet.add(word);
				break;
			case "移出单词本":
				WordList.wordSet.remove(word);

		}
	}
}
