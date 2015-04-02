package view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("GenMeowth");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("Meowth.png");
		frame.setIconImage(img);
		frame.add(new GUI(frame));
		frame.pack();
		Dimension screenSize = toolkit.getScreenSize();
		frame.setLocation((screenSize.width - frame.getSize().width) / 2,
				(screenSize.height - frame.getSize().height) / 2);
		frame.setVisible(true);

	}

}
