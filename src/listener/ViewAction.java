package listener;

import java.awt.event.*;
import gui.*;

public class ViewAction implements ActionListener {
	View v = new View();// 先定义一个View对象在切换界面的时候会使用到

	public ViewAction(View v)// 将外界的View对象传入这个类里
	{
		this.v = v;
	}

	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
			// 如果按下视图1
			case "词典":
				// 先创建新视图再关闭旧视图
				new View().view();// new一个View类并调用里面的view函数
				v.setVisible(false);// 关闭穿进来的那个类的视图
				break;
			// 如果按下视图2
			case "单词本":
				new View().view2();
				v.setVisible(false);
				break;
			case "背单词":
				new View().view3();
				v.setVisible(false);
		}
	}
}
