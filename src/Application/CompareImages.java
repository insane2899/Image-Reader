package Application;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import ImageConvert.ImageCompare;

public class CompareImages {

	private JFrame frame;
	private BufferedImage test,gt;
	private JTextField textField;
	private JFileChooser fc;

	/**
	 * Launch the application.
	 */
	public void compareImages(BufferedImage test) {
		this.test = test;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompareImages window = new CompareImages(test);
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
	public CompareImages(BufferedImage test) {
		this.test=test;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JLabel lblEnterThePath = new JLabel("Enter the Path to the Ground Truth Image:");
		lblEnterThePath.setFont(new Font("Dialog", Font.PLAIN, 19));
		lblEnterThePath.setBounds(12, 33, 425, 36);
		frame.getContentPane().add(lblEnterThePath);
		
		textField = new JTextField();
		textField.setBounds(64, 96, 312, 36);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("...");
		btnNewButton.setBounds(398, 96, 39, 36);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int r = fc.showOpenDialog(null);
				if(r==JFileChooser.APPROVE_OPTION) {
					textField.setText(fc.getSelectedFile().getAbsolutePath());
				}
				else {
					textField.setText("");
				}
			}
		});
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.setBounds(154, 149, 161, 45);
		frame.getContentPane().add(btnEnter);
		btnEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = textField.getText();
				if(s.equals("")) {
					JDialog dialog = new JDialog(frame,"Error!");
					JLabel label = new JLabel("No File Selected");
					dialog.getContentPane().add(label);
					dialog.setSize(500,100);
					dialog.setVisible(true);
				}
				else {
					try {
						gt = ImageIO.read(new File(s));
						ImageCompare ic = new ImageCompare(test,gt);
						double d = ic.compare();
						String error = String.format("%.2f",d);
						JOptionPane.showMessageDialog(frame,"The binarized image has "+error+"% error.","RESULT",JOptionPane.INFORMATION_MESSAGE);
						frame.dispose();						
					}catch(Exception z) {
						System.out.println(z);
					}
				}
			}
		});
	}

}
