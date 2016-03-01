/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Gwynb1eid
 */
public class TCPClient extends JFrame implements ActionListener, KeyListener, WindowListener, Runnable {

	private final JTextField tf;
	private final JScrollPane skr;
	private final JTextArea panelg;
	private final JButton bok;
	private PrintWriter out = null;
	private BufferedReader in = null;

	public TCPClient(String title){
	 	super(title);
		setSize(500,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container wnetrze=getContentPane();
		wnetrze.setLayout(new BorderLayout());
		panelg=new JTextArea();
		panelg.setEditable(false);
		skr=new JScrollPane(panelg);
		wnetrze.add(skr, BorderLayout.CENTER);
		JPanel paneld=new JPanel();
		paneld.setLayout(new BorderLayout());
		tf=new JTextField();
		paneld.add(tf, BorderLayout.CENTER);
		bok=new JButton("OK");
		bok.addActionListener(this);
		tf.addKeyListener(this);
		paneld.add (bok, BorderLayout.EAST);
		wnetrze.add (paneld, BorderLayout.SOUTH);
		addWindowListener(this);
		Dimension dim = getToolkit().getScreenSize();
		Rectangle abounds = getBounds();
		setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
	}

        @Override
	public void keyReleased(KeyEvent e) {}
	
        @Override
	public void keyPressed(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_ENTER ){
			bok.doClick();
		}
	}

        @Override
	public void keyTyped(KeyEvent e) {}
	
        @Override
	public void windowOpened(WindowEvent e) {
		tf.requestFocus();
	}
        @Override
	public void windowClosed(WindowEvent e) {}
        @Override
	public void windowClosing(WindowEvent e) {}
        @Override
	public void windowActivated(WindowEvent e) {}
        @Override
	public void windowDeactivated(WindowEvent e) {}
        @Override
	public void windowIconified(WindowEvent e) {}
        @Override
	public void windowDeiconified(WindowEvent e) {}

        @Override
	public void actionPerformed(ActionEvent ae) {
		String s = tf.getText();
		if(s.equals("")) return;
		try {
			out.println(s);
		} catch(Exception e) { JOptionPane.showMessageDialog(null, e); System.exit(0); }
		tf.setText(null);	
	}
		
        @Override
	public void run() {
		for(;;) {
			
                    
                        
                        
                        try {
				String s=in.readLine();
				if(s == null) {
					JOptionPane.showMessageDialog(null, "Serwer zakonczyl polaczenie");
					System.exit(0);
				}
				panelg.append(s+"\n");
				skr.getVerticalScrollBar().setValue(skr.getVerticalScrollBar().getMaximum());
			} catch(Exception e) { JOptionPane.showMessageDialog(null, e); System.exit(0); }
		}
	}
////////////////////////////////////////////////////////////        
        public void shit()
        {
            FileOutputStream fis = null;
            try
            {
                Socket socket = new Socket("localhost", 8888);
                InputStream socketIn = socket.getInputStream();
                fis = new FileOutputStream("C:\\2222.txt");
                byte[] buffer = new byte[64 * 1024];
                int read;
                while((read = socketIn.read(buffer)) != -1)
                    fis.write(buffer, 0, read);
                socket.close();
            } catch(IOException ex) { ex.printStackTrace(); }
            finally
            {
                try
                {
                if(fis != null)
                    fis.close();
                } catch(IOException ex) { ex.printStackTrace(); }
            }
        }
////////////////////////////////////////////////////////////	
	public static void main (String[] args) {
		TCPClient f=new TCPClient("TCPClient");
		
		String connectTo=null;
		try{
			Properties props = new Properties();
			props.load(new FileInputStream("TCPClient.cfg"));
			InetAddress addr=InetAddress.getByName(props.getProperty("host"));
			int port=Integer.parseInt(props.getProperty("port"));
			connectTo=addr.getHostAddress()+":"+port;
			Socket sock=new Socket (addr.getHostName(), port);
			f.setTitle("Connected to "+connectTo);
			f.out=new PrintWriter(sock.getOutputStream(), true);
			f.in=new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "While connecting to "+connectTo+"\n"+e);
			System.exit(1);
		}
		
		new Thread(f).start();
		f.setVisible(true);
		}
}