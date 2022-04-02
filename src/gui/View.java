package gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.*;
import listener.*;
import logic.WordItem;
import logic.WordList;

public class View extends JFrame{
	JPanel panel = new JPanel();
	JTextArea wordtext = new JTextArea(80,100);
	public static JTextArea wordlist = new JTextArea(80,100);
	//查询框部分
	public JTextField input = new JTextField(10);
	public static JTextArea showResult[] = new JTextArea[10];
	public static JButton detailList[] = new JButton[10];
	public static JButton mp3[] = new JButton[10];
	public static int wordIndex = -1;
	
	//查询界面
	public void view()
	{
		ViewAction exwpAction=new ViewAction(this);
		setLayout(new BorderLayout());//使用BorderLayout布局
		setSize(400,400);//设置界面大小
		this.setTitle("词典");//设置界面名称
		this.setLocationRelativeTo(null);
		//三个切换按钮
		JButton eb1=new JButton("词典");
		JButton eb2=new JButton("单词本");
		JButton eb3=new JButton("背单词");
		eb1.addActionListener(exwpAction);
		eb2.addActionListener(exwpAction);
		eb3.addActionListener(exwpAction);
		//三个按钮以流式并列排在底部
		JPanel pnlBody = new JPanel(new FlowLayout());
		pnlBody.add(eb1);
		pnlBody.add(eb2);
		pnlBody.add(eb3);
		add(pnlBody, BorderLayout.SOUTH);
		
		//JLabel tip_l=new JLabel("词典：显示结果",0);//
		// queryw.setText("单词查询结果\n……");
		// add(queryw, BorderLayout.CENTER);//将tip_l这个标签添加到布局的中间
		// 这个地方添加发音和进入单词本的功能，我们默认最多显示十条信息
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for(int i = 0; i < 10; ++i){
			showResult[i] = new JTextArea("文字部分");
			showResult[i].setLineWrap(true);
			showResult[i].setVisible(false);
			showResult[i].setEditable(false);
			detailList[i] = new JButton("更多操作");
			detailList[i].addActionListener(new DetailsAction(this, ""));
			detailList[i].setVisible(false);
			mp3[i] = new JButton("发音");
			mp3[i].addActionListener(new Mp3Listener(""));
			mp3[i].setVisible(false);
			panel.add(showResult[i]);
			JPanel innerPanel = new JPanel(new FlowLayout());
			innerPanel.add(detailList[i]);
			innerPanel.add(mp3[i]);
			panel.add(innerPanel);
		}
		// 添加滚动条
		JScrollPane queryResult = new JScrollPane(panel);
		add(queryResult, BorderLayout.CENTER);

		//************************
		JButton key = new JButton("查询");//确定键，用于连接数据库
		WordAction queryAction = new WordAction(this);//查询事件响应
		key.addActionListener(queryAction);
		//*************************
		JPanel searchtext = new JPanel(new FlowLayout());
		searchtext.add(input);
		searchtext.add(key);
		add(searchtext,BorderLayout.NORTH);
		//JLabel tip_s = new JLabel("查询框");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//关闭
		setResizable(false);
		setVisible(true);//使视图可视化
	}
	
	//单词本界面
	public void view2()
	{
		ViewAction exwpAction=new ViewAction(this);
		setLayout(new BorderLayout());//使用BorderLayout布局
		setSize(400,400);//设置界面大小
		this.setTitle("单词本");//设置界面名称
		this.setLocationRelativeTo(null);
		//三个切换按钮
		JButton eb1=new JButton("词典");
		JButton eb2=new JButton("单词本");
		JButton eb3=new JButton("背单词");
		eb1.addActionListener(exwpAction);
		eb2.addActionListener(exwpAction);
		eb3.addActionListener(exwpAction);
		//三个按钮以流式并列排在底部
		JPanel pnlBody = new JPanel(new FlowLayout());
		pnlBody.add(eb1);
		pnlBody.add(eb2);
		pnlBody.add(eb3);
		add(pnlBody, BorderLayout.SOUTH);
		wordtext.setLineWrap(true);
		wordtext.setEditable(false);
		for(String w : WordList.wordSet){
			wordtext.append(w + "\n");
			wordtext.append(WordItem.getWordItem(w).getSymbol() + "\n");
			wordtext.append(WordItem.getWordItem(w).getMeaning() + "\n\n");
		}
		add(wordtext,BorderLayout.CENTER);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//关闭
		setVisible(true);
	}
	
	//背单词界面
	public void view3()
	{
		ViewAction exwpAction=new ViewAction(this);
		setLayout(new BorderLayout());//使用BorderLayout布局
		setSize(400,400);//设置界面大小
		this.setTitle("背单词");//设置界面名称
		this.setLocationRelativeTo(null);
		//三个切换按钮
		//*********************Actionlistener需要补充
		JButton eb1=new JButton("词典");
		JButton eb2=new JButton("单词本");
		JButton eb3=new JButton("背单词");
		eb1.addActionListener(exwpAction);
		eb2.addActionListener(exwpAction);
		eb3.addActionListener(exwpAction);
		//三个按钮以流式并列排在底部
		JPanel pnlBody = new JPanel(new FlowLayout());
		pnlBody.add(eb1);
		pnlBody.add(eb2);
		pnlBody.add(eb3);
		add(pnlBody, BorderLayout.SOUTH);
		JPanel showword = new JPanel(new BorderLayout());
		wordlist.setEditable(false);
		if(WordList.wordList.isEmpty()){
			WordList.refresh();
		}
		if(WordList.wordList.isEmpty()){
			wordlist.setText("单词本为空");
			wordIndex = -1;
		} else {
			if(wordIndex == -1 || wordIndex >= WordList.wordList.size()){
				wordlist.setText(WordList.wordList.get(0));
				wordIndex = 0;
			} else{
				wordlist.setText(WordList.wordList.get(wordIndex));
			}
		}
		showword.add(wordlist,BorderLayout.CENTER);
		
		JPanel panel = new JPanel(new FlowLayout());
		JButton confirmed = new JButton("已掌握，下一个");//此处响应未完成
		confirmed.addActionListener(new NextActionListener());
		JButton read = new JButton("导入单词本");//此处响应未完成
		read.addActionListener(new ReadActionListener());
		JButton write = new JButton("导出单词本");//此处响应未完成
		write.addActionListener(new WriteActionListener());
		panel.add(read);
		panel.add(confirmed);
		panel.add(write);

		showword.add(panel,BorderLayout.SOUTH);
		add(showword,BorderLayout.CENTER);
		
		JPanel choice = new JPanel(new FlowLayout());
		ReciteAction Ract = new ReciteAction();
		JButton choice1 = new JButton("单词");
		JButton choice2 = new JButton("汉语释义");
		JButton choice3 = new JButton("音标");
		JButton choice4 = new JButton("发音");
		choice1.addActionListener(Ract);
		choice2.addActionListener(Ract);
		choice3.addActionListener(Ract);
		choice4.addActionListener(Ract);
		choice.add(choice1);
		choice.add(choice2);
		choice.add(choice3);
		choice.add(choice4);
		add(choice,BorderLayout.NORTH);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//关闭
		setVisible(true);
	}

}
