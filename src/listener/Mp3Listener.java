package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JOptionPane;

import javazoom.jl.player.*;
import logic.WordItem;

/**
 * MP3播放，如果没有对应音频会弹出相应提示
 */
public class Mp3Listener implements ActionListener {
    String word;

    public Mp3Listener(String word) {
        this.word = word;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File mp3 = WordItem.getWordItem(word).getMp3();
        if (mp3 == null) {
            JOptionPane.showMessageDialog(null, "未能获取音频", "音频播放", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(mp3));
            Player player = new Player(buffer);
            player.play();
        } catch (Exception ie) {
            JOptionPane.showMessageDialog(null, "未能获取音频", "音频播放", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

}