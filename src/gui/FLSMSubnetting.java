package gui;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FLSMSubnetting extends JFrame{
	private static final long serialVersionUID = 1L;
	final static int WIDTH = 700;
	final static int HEIGHT = 700;
	private JTextField x1; 
	private JTextField x2;
	private JTextField x3;
	private JTextField x4;
	private JTextField x5;
	private JTextField no;
	private JButton btn; 
	private JPanel panel;
	private JTextArea result;
	
	
	public FLSMSubnetting() {
		super("Subnet Calculator");
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
		JLabel label2 = new JLabel("Insert no. of Subnets:");
		no = new JTextField(3);
		ActionListener listener = new Subnet();
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
		panel.add(label2);
		panel.add(no);
		panel.add(btn);
		result = new JTextArea(50,50);
		panel.add(result);
		add(panel);
	}
	
	public class Subnet implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int n1 = Integer.parseInt(x1.getText());
				int n2 = Integer.parseInt(x2.getText());
				int n3 = Integer.parseInt(x3.getText());
				int n4 = Integer.parseInt(x4.getText());
				int n5 = Integer.parseInt(x5.getText());
				int n6 = Integer.parseInt(no.getText());
				if((n1<0 || n1>255) || (n2<0 || n2>255) || (n3<0 || n3>255) || (n4<0 || n4>255) || (n5<8 || n5>32)) {
						result.setText("Invalid input");
				}else {
					int convertedBits = findNoBits(n6);
					if((convertedBits+n5) > 32) {
						result.setText("Invalid input");
					}else {
						String s1 = konverto(n1,8).toString();
						String s2 = konverto(n2,8).toString();
						String s3 = konverto(n3,8).toString();
						String s4 = konverto(n4,8).toString();
						
						String binIP = s1+""+s2+""+s3+""+s4;
						String netBits = binIP.substring(0,n5);
						
						String subnets[] = new String[n6];

				        for (int i=0; i<n6;i++){
				            String str = konverto(i,convertedBits).toString();
				            subnets[i] = str;
				        }
				        
				        String[] networkID = new String[n6];
				        String[] brcID = new String[n6]; 
				        
				        for (int i = 0; i < n6; i++){
				            
				            String networkID1 = netBits+""+subnets[i];
				            networkID1 = completeIP(networkID1,"0");
				            networkID1 = network(networkID1,".");
				            networkID[i] = networkID1;

				            String broadcastID = netBits+""+subnets[i];
				            broadcastID = completeIP(broadcastID,"1");
				            broadcastID = network(broadcastID,".");
				            brcID[i] = broadcastID;

				        }
				        
				        for (int i = 0; i < n6; i++){
			                afisho(networkID[i],brcID[i],i,n5,convertedBits);
			            }
				        
					}
				}
						
			}
			catch(NumberFormatException c) {
				result.setText("Invalid input");
			}
		}

		public void afisho(String net, String brc, int i, int mask, int convertedBits) {
			String nr = "Subnet #"+(i+1);
			String netID = "Network ID: "+ net + " /" + (mask+convertedBits);
			String broadcastID = "Broadcast ID: "+ brc + " /" + (mask+convertedBits);
			String[] host = net.split("\\."); 
			String FH = host[0]+"."+host[1]+"."+host[2]+"."+(Integer.parseInt(host[3])+1)+ " /" + (mask+convertedBits);
			String[] host2 = brc.split("\\.");
			String LH  = host2[0]+"."+host2[1]+"."+host2[2]+"."+(Integer.parseInt(host2[3])-1)+ " /" + (mask+convertedBits);
			int total_hosts = (int)Math.pow(2,(32-mask-convertedBits))-2;
			result.append(nr+"\n"+netID+"\n"+broadcastID+"\n"+FH+"\n"+LH+"\nTotal Hosts: "+total_hosts+"\n\n");
		}
	}
	
	public StringBuilder konverto(int nr, int bits) {
		StringBuilder S = new StringBuilder();
		for(int i=0; i<bits; i++) {
			S.append(Integer.toString(nr%2));
			nr = nr/2;
		}
		return S.reverse();
	}
	
	public String completeIP(String ip, String s) {
		String s2 = ip;
        int nrBitSubnet = ip.length(); 
        for (int i=1; i<=32-nrBitSubnet; i++){
            s2 = s2 + s;
        }
        return s2;
	}
	
	public int findNoBits(int no_subnets) {
		boolean done = false;
        int i=-1;
        while (!done){
            i++;
            if (no_subnets<=Math.pow(2,i))
                done=true;
        }
        return i;
	}
	
	public String network (String s, String c){
        String s1 = "";
        for (int i = 0; i < s.length(); i = i + 8){
            String sbstr = s.substring(i,i+8);
            int dec = decimal(sbstr);
            s1 += dec+c;
        }
        s1 = s1.substring(0,s1.length()-1);
        return s1;
    }
	
	public int decimal(String nr){
        int fuqia = nr.length()-1;
        int sum = 0;
        for (int i = 0; i < nr.length(); i++){
            if(nr.charAt(i)=='1'){
                sum += Math.pow(2,fuqia-i);
            }
        }
        return sum;
    }
}
