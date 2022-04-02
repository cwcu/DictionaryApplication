package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import logic.WordList;

/**
 * 导出单词本
 */
public class WriteActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String path = this.getClass().getResource("/data/WordList.txt").getPath();
        File file = new File(path);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            for (String i : WordList.wordSet) {
                writer.write(i);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }

    }

}
