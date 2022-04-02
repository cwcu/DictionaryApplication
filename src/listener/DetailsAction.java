package listener;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gui.View;
import gui.*;
import logic.WordList;

/*
 * 此部分为点击单词弹出具体信息的事件响应,通过弹出一个新窗口，来显示单词的汉语释义等信息
 */
public class DetailsAction implements ActionListener {
	View v = new View();
	String word;

	public DetailsAction(View v, String word) {
		this.v = v;
		this.word = word;
	}

	public void actionPerformed(ActionEvent event) {
		JButton eb1 = new JButton("加入单词本");
		JButton eb2 = new JButton("移出单词本");
		ListAction act = new ListAction(word);
		eb1.addActionListener(act);
		eb2.addActionListener(act);
		boolean flag = WordList.wordSet.contains(word);// 用于判断单词是否在单词本中
		String word = event.getActionCommand();
		JDialog frame = new JDialog();
		frame.setBounds(// 让新窗口与原窗口示例错开50像素。
				new Rectangle((int) v.getBounds().getX() + 50, (int) v.getBounds().getY() + 50,
						(int) v.getBounds().getWidth(), (int) v.getBounds().getHeight()));
		JTextArea worddetail = new JTextArea();
		JPanel pnlbody = new JPanel(new BorderLayout());
		pnlbody.add(worddetail, BorderLayout.CENTER);
		worddetail.setText(word);
		if (!flag) {
			pnlbody.add(eb1, BorderLayout.SOUTH);
		} else {
			pnlbody.add(eb2, BorderLayout.SOUTH);
		}

		frame.getContentPane().add(pnlbody);
		frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);// 设置阻塞模式
		frame.setVisible(true);
	}
}
