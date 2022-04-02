package listener;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.*;

import gui.View;
import javazoom.jl.player.Player;
import logic.WordItem;
import logic.WordList;

/*
 * 背单词界面，将各个部分选择性显示
 */
public class ReciteAction implements ActionListener {
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
			case "单词":
				if (View.wordIndex == -1) {
					View.wordlist.setText("单词本为空");
				} else {
					View.wordlist.setText(WordList.wordList.get(View.wordIndex));
				}
				break;
			case "汉语释义":
				if (View.wordIndex == -1) {
					View.wordlist.setText("单词本为空");
				} else {
					View.wordlist.setText(WordItem.getWordItem(WordList.wordList.get(View.wordIndex)).getMeaning());
				}
				break;
			case "音标":
				if (View.wordIndex == -1) {
					View.wordlist.setText("单词本为空");
				} else {
					View.wordlist.setText(WordItem.getWordItem(WordList.wordList.get(View.wordIndex)).getSymbol());
				}
				break;
			case "发音":
				if (View.wordIndex != -1) {
					File mp3 = WordItem.getWordItem(WordList.wordList.get(View.wordIndex)).getMp3();
					if (mp3 == null)
						return;
					try {
						BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(mp3));
						Player player = new Player(buffer);
						player.play();
					} catch (Exception ie) {
						ie.printStackTrace();
						return;
					}
				}
		}
	}
}
