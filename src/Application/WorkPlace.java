package Application;


import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;
import javax.imageio.ImageIO;
import javax.swing.JButton;

public class WorkPlace {

	private JFrame frame;
	private String fileName;
	private BufferedImage img;
	private JComboBox c1,c2;
	private String s,ss,textField;
	/**
	 * Launch the application.
	 */
	public void workPlace(String fileName,BufferedImage img) {
		this.fileName = fileName;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WorkPlace window = new WorkPlace(fileName,img);
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
	public WorkPlace(String fileName,BufferedImage img) {
		this.fileName = fileName;
		this.img = img;
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(fileName);
		frame.setBounds(100, 100, 570, 333);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//frame.getContentPane().setLayout(null);
		
		String s1[] = {"Otsu's Binarization(Local)","Otsu's Binarization(Global)","Sauvola Binarization","Niblack Binarization","Brensen Binarization"};
		
		c1 = new JComboBox(s1);
		c1.setBounds(22, 64, 187, 27);
		c1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getSource() == c1) {
					s = (String) e.getItem();
				}
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(c1);
		
		String s2[] = {".jpg/.jpeg",".png",".gif",".tiff",".bmp",".wbmp"};
		
		c2 = new JComboBox(s2);
		c2.setBounds(299, 64, 187, 27);
		c2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getSource() == c2) {
					ss = (String) e.getItem();
				}
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(c2);
		
		JLabel lblNewLabel = new JLabel("Select Binarization Method:");
		lblNewLabel.setBounds(22, 12, 208, 26);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.setBounds(60, 130, 117, 25);
		frame.getContentPane().add(btnSelect);
		btnSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(s==null) {
					System.out.println("No image");
				}
				else {
					BinarizedImages bi = new BinarizedImages(s,img,fileName);
					bi.binarizedImages(s,img,fileName);
				}
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("Select Format of Converted Image:");
		lblNewLabel_1.setBounds(276, 12, 255, 24);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnSelect_1 = new JButton("Select");
		btnSelect_1.setBounds(322, 130, 117, 25);
		frame.getContentPane().add(btnSelect_1);
		btnSelect_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser jc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);			
					int r = jc.showOpenDialog(null);
					if(r==JFileChooser.APPROVE_OPTION) {
						textField = jc.getSelectedFile().getAbsolutePath();
					}
					else {
						textField="";
					}
					String fileN="";
					for(int i=0;i<fileName.length();i++) {
						if(fileName.charAt(i)=='.') {
							break;
						}
						fileN+=fileName.charAt(i);
					}
					if(ss.charAt(1)=='j') {
						ss = ss.substring(0,4);
					}
					System.out.println(ss);
					File f = new File(textField+"/"+fileN+ss);
					String format = ss.substring(1,ss.length());
					ImageIO.write(img,format,f);
				}catch(Exception w) {
					System.out.println(w);
				}
			}
		});
	}
}
