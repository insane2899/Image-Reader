package Application;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.StringTokenizer;

import Thresholding.*;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ImageConvert.ScannedImage;

public class BinarizedImages {

	private JFrame frame;
	private String method;
	private BufferedImage img,img2;
	private String imageName;
	private int R;
	private double k;
	private int size;

	/**
	 * Launch the application.
	 */
	public void binarizedImages(String method,BufferedImage img,String imageName) {
		this.imageName = imageName;
		this.method = method;
		this.img = img;
		R = -1;
		k = -1.0;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BinarizedImages window = new BinarizedImages(method,img,imageName);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void binarizedImages(String method,BufferedImage img,String imageName,int R,double k) {
		this.imageName = imageName;
		this.method = method;
		this.R = R;
		this.k = k;
		this.img = img;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BinarizedImages window = new BinarizedImages(method,img,imageName,R,k);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void binarizedImages(String method,BufferedImage img,String imageName,double k) {
		this.imageName = imageName;
		this.method = method;
		this.k = k;
		this.img = img;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BinarizedImages window = new BinarizedImages(method,img,imageName,k);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void binarizedImages(String method,BufferedImage img,String imageName,int size) {
		this.imageName = imageName;
		this.method = method;
		this.size=size;
		this.img = img;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BinarizedImages window = new BinarizedImages(method,img,imageName,size);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BinarizedImages(String method,BufferedImage img,String imageName) {
		this.imageName = imageName;
		this.method = method;
		this.img = img;
		img2 = null;
		R = -1;
		k = -1.0;
		size=350;
		initialize();
	}
	
	public BinarizedImages(String method,BufferedImage img,String imageName,int R,double k) {
		this.imageName = imageName;
		this.method = method;
		this.img = img;
		img2 = null;
		this.R = R;
		this.k = k;
		initialize();
	}
	
	public BinarizedImages(String method,BufferedImage img,String imageName,double k) {
		this.imageName = imageName;
		this.method = method;
		this.img = img;
		img2 = null;
		this.R = -1;
		this.k = k;
		initialize();
	}
	
	public BinarizedImages(String method,BufferedImage img,String imageName,int size) {
		this.imageName = imageName;
		this.method = method;
		this.img = img;
		img2 = null;
		this.size = size;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle(imageName+" "+method);
		frame.setBounds(100, 100, 750, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Thresholding th = new Thresholding(20,20,img);
		
		if(method.charAt(0)=='S') {
			if(R == -1 && k == -1.0) {
				img2 = th.Sauvola();
			}
			else {
				img2 = th.Sauvola(R,k);
			}
		}
		else if(method.charAt(0)=='N') {
			if(k==-1.0) {
				img2 = th.Niblack();
			}
			else {
				img2 = th.Niblack(k);
			}
		}
		else if(method.charAt(0)=='B') {
			img2 = th.Brensen();
		}
		else if(method.charAt(0)=='O') {
			if(method.charAt(20)=='L') {
				img2 = th.otsuLocalConvert(size);
			}
			else if(method.charAt(20)=='G') {
				img2 = th.OtsuGlobal(Thresholding.otsuTreshold(img));
			}
		}
		
		JPanel pane = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img2,0,0,getWidth(),getHeight(),this);
			}
		};
		pane.setBounds(12, 12, 719, 499);
		
		frame.getContentPane().add(pane);
		
		JButton btnNewButton = new JButton("Convert Image");
		btnNewButton.setBounds(195, 523, 165, 35);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScannedImage si = new ScannedImage();
				String s = si.convert(img2);
				Textshow ts = new Textshow(s,imageName,method);
				ts.textshow(s, imageName, method);
			}
		});
		
		JButton btnNewButton_1 = new JButton("Save Image");
		btnNewButton_1.setBounds(398, 523, 152, 35);
		frame.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String s2 ="";
					for(int i=0;i<imageName.length();i++) {
						if(imageName.charAt(i)=='.') {
							break;
						}
						s2+=imageName.charAt(i);
					}
					File file = new File("Results/"+s2);
					if(!file.exists()) {
						file.mkdir();
					}
					File image = new File("Results/"+s2+"/"+method+".jpg");
					ImageIO.write(img2,"jpg",image);
					
				}catch(Exception f) {
					System.out.println(f);
				}
			}
		});
		
		JButton btnNewButton_2 = new JButton("Compare");
		btnNewButton_2.setBounds(580, 523, 132, 35);
		frame.getContentPane().add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				CompareImages ci = new CompareImages(img2);
				ci.compareImages(img2);
			}
		});
		
		JButton btnNewButton_3 = new JButton("Custom");
		btnNewButton_3.setBounds(22, 523, 146, 35);
		frame.getContentPane().add(btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(method.charAt(0)=='S') {
					String message = JOptionPane.showInputDialog(null,"Enter the value of R(integer) and k(double)(with space in between):");
					StringTokenizer st = new StringTokenizer(message);
					int R = Integer.parseInt(st.nextToken());
					double k = Double.parseDouble(st.nextToken());
					String imgn = method+": Custom("+R+","+k+")";
					BinarizedImages bi = new BinarizedImages(imgn,img,imageName,R,k);
					bi.binarizedImages(imgn,img,imageName,R,k);
				}
				else if(method.charAt(0)=='N') {
					String message = JOptionPane.showInputDialog(null,"Enter the value of k(double):");
					double k = Double.parseDouble(message);
					String imgn = method+": Custom("+k+")";
					BinarizedImages bi = new BinarizedImages(imgn,img,imageName,k);
					bi.binarizedImages(imgn,img,imageName,k);
					
				}
				else if(method.charAt(0)=='O'&&method.charAt(20)=='L') {
					String message = JOptionPane.showInputDialog(null,"Enter the value of window size(int):");
					int size = Integer.parseInt(message);
					String imgn = method+": Custom("+size+")";
					BinarizedImages bi = new BinarizedImages(imgn,img,imageName,size);
					bi.binarizedImages(imgn,img,imageName,size);
					
				}
			}
		});
	}
}
