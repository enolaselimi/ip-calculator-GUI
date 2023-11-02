package gui;

import javax.swing.JFrame;

public class Driver {

	public static void main(String[] args) {
		JFrame frame = new FLSMSubnetting();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		JFrame frame2 = new CalculatorGui();
		frame2.setVisible(true);
	}

}
