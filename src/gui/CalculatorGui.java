package gui;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGui extends JFrame{
	private static final long serialVersionUID = 1L;
	final static int WIDTH = 358;
	final static int HEIGHT = 400;
	private JLabel binOutput;
	private JLabel netID;
	private JLabel firstHost;
	private JLabel lastHost;
	private JLabel totalHost;
	private JLabel broadcast;
	private JTextField x1; 
	private JTextField x2;
	private JTextField x3;
	private JTextField x4;
	private JTextField x5;
	private JButton btn; 
	private JPanel panel;
	
	public CalculatorGui() {
		super("Simple IP Calculator");
		setSize(WIDTH,HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label = new JLabel("Insert IP address:");
		JLabel dot1 = new JLabel(".");
		JLabel dot2 = new JLabel(".");
		JLabel dot3 = new JLabel(".");
		JLabel slash = new JLabel("/");
		x1 = new JTextField(3);
		x2 = new JTextField(3);
		x3 = new JTextField(3);
		x4 = new JTextField(3);
		x5 = new JTextField(3);
		ActionListener listener = new Calc();
		btn = new JButton("Calculate");
		btn.addActionListener(listener);
		panel = new JPanel();
		panel.add(label);
		panel.add(x1);
		panel.add(dot1);
		panel.add(x2);
		panel.add(dot2);
		panel.add(x3);
		panel.add(dot3);
		panel.add(x4);
		panel.add(slash);
		panel.add(x5);
		panel.add(btn);
		binOutput = new JLabel();
		netID = new JLabel();
		firstHost = new JLabel("",JLabel.CENTER);
		lastHost = new JLabel("",JLabel.CENTER);
		totalHost = new JLabel("",JLabel.CENTER);
		broadcast = new JLabel();
		panel.add(binOutput);
		panel.add(netID);
		panel.add(firstHost);
		panel.add(lastHost);
		panel.add(totalHost);
		panel.add(broadcast);
		add(panel);
	}
	
	public class Calc implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int n1 = Integer.parseInt(x1.getText());
				int n2 = Integer.parseInt(x2.getText());
				int n3 = Integer.parseInt(x3.getText());
				int n4 = Integer.parseInt(x4.getText());
				int n5 = Integer.parseInt(x5.getText());
				if(n1<0 || n1>255 || n2<0 || n2>255 || n3<0 || n3>255 || n4<0 || n4>255 || n5<8 || n5>24) {
					binOutput.setText("Invalid input");
				}else {
					String s1 = konverto(n1).toString();
					String s2 = konverto(n2).toString();
					String s3 = konverto(n3).toString();
					String s4 = konverto(n4).toString();
					
					int binIP = Integer.parseInt(s1+s2+s3,2);
					binIP = (binIP << 8)+Integer.parseInt(s4,2);
					int binMask = ~0 << (32-n5);
	
					String binary = s1+"."+s2+"."+s3+"."+s4;
					binOutput.setText("Address in binary: "+binary);
					
					String net = networkIP(s1, s2, s3, n5);
					netID.setText("Network Address: "+net);
					
					String host1 = net.substring(0,net.length()-1);
					host1 += 1;
					firstHost.setPreferredSize(new Dimension(300, 15));
					firstHost.setText("First Host Address: "+host1);
					
					int total = totalHost(n5);
					totalHost.setPreferredSize(new Dimension(300, 15));
					totalHost.setText("Total Hosts: "+total);
					
					broadcast(binIP, binMask);
				}
			}
			catch(NumberFormatException c) {
				binOutput.setText("Invalid input");
			}
		}
	}
	
	public StringBuilder konverto(int nr) {
		StringBuilder S = new StringBuilder();
		for(int i=0; i<8; i++) {
			S.append(Integer.toString(nr%2));
			nr = nr/2;
		}
		return S.reverse();
	}
	
	
	public String networkIP(String s1, String s2, String s3, int bit) {
		String net = "00000000";
		if(bit==8) {
			return Integer.parseInt(s1,2)+".0.0.0";
		}
		else if(bit<=16){
			int net1 = Integer.parseInt(s1,2);
			String net2 = s2.substring(0,bit-8);
			net2 += net.substring(0,net.length()-net2.length());
			return net1+"."+Integer.parseInt(net2,2)+".0.0";
		}
		else {
			int net1 = Integer.parseInt(s1,2);
			int net2 = Integer.parseInt(s2,2);
			String net3 = s3.substring(0,bit-16);
			net3 += net.substring(0,net.length()-net3.length());
			return net1+"."+net2+"."+Integer.parseInt(net3,2)+".0";
		}
	}
	
	public int totalHost(int mask) {
		int pow = 32 - mask; 
		return (int) Math.pow(2, pow)-2;
	}
	
	public void broadcast(int binIP, int sm) {
		int brc = binIP | ~sm;
		String binaryBroadcast = Integer.toBinaryString(brc);
		while(binaryBroadcast.length() != 32) {
			binaryBroadcast = "0"+binaryBroadcast;
		}

		String[] brcFormat = new String[4];
		brcFormat[0] = binaryBroadcast.substring(0,8);
		brcFormat[1] = binaryBroadcast.substring(8,16);
		brcFormat[2] = binaryBroadcast.substring(16,24);
		brcFormat[3] = binaryBroadcast.substring(24);
		lastHost.setText("Last Host: "
						+String.valueOf(Integer.parseInt(brcFormat[0],2)) +"."
						+String.valueOf(Integer.parseInt(brcFormat[1],2)) +"."
						+String.valueOf(Integer.parseInt(brcFormat[2],2)) +"."
						+(Integer.parseInt(brcFormat[3],2)-1)
						);
		broadcast.setText("Broadcast Address: "
						+String.valueOf(Integer.parseInt(brcFormat[0],2)) +"."
						+String.valueOf(Integer.parseInt(brcFormat[1],2)) +"."
						+String.valueOf(Integer.parseInt(brcFormat[2],2)) +"."
						+String.valueOf(Integer.parseInt(brcFormat[3],2))
		);
	}

}
