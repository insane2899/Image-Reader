package Application;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import ImageConvert.ScannedImage;
import java.awt.BorderLayout;
import javax.swing.JButton;

public class MainImage {

	private JFrame frame;
	private BufferedImage img;
	private String imageName;

	/**
	 * Launch the application.
	 */
	public void mainImage(String fileName,BufferedImage img) {
		imageName = fileName;
		this.img = img;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainImage window = new MainImage(fileName,img);
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
	public MainImage(String fileName,BufferedImage img) {
		this.img = img;
		this.imageName = fileName;
		try {
			initialize();
		}catch(Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()throws IOException {
		frame = new JFrame();
		frame.setTitle(imageName);
		frame.setBounds(100, 100, 750,600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel pane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img,0,0,getWidth(),getHeight(),this);
			}
		};
		pane.setBounds(12, 12, 719, 499);
		
		frame.getContentPane().add(pane);
		
		JButton btnNewButton = new JButton("Convert Image");
		btnNewButton.setBounds(278, 523, 159, 35);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScannedImage si = new ScannedImage();
				String s = si.convert(img);
				Textshow ts = new Textshow(s,imageName,"original");
				ts.textshow(s, imageName, "original");
			}
		});
	}
}
