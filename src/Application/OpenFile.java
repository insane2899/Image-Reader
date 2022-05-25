package Application;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class OpenFile {

	private JFrame frame;
	private JTextField textField;
	private JFileChooser fc;
	

	/**
	 * Launch the application.
	 */
	public void openFile() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OpenFile window = new OpenFile();
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
	public OpenFile() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("New File");
		frame.setBounds(100 ,100, 472, 256);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblEnterThePath = new JLabel("Enter the Path to the Image:");
		lblEnterThePath.setFont(new Font("Dialog", Font.PLAIN, 24));
		lblEnterThePath.setBounds(64, 25, 336, 28);
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
		btnEnter.setBounds(161, 157, 117, 25);
		frame.getContentPane().add(btnEnter);
		btnEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = textField.getText();
				if(s.equals("")) {
					JDialog dialog = new JDialog(frame,"Error!");
					JLabel label = new JLabel("No File Selected");
					dialog.add(label);
					dialog.setSize(500,100);
					dialog.setVisible(true);
				}
				else {
					int l=0;
					for(int i=0;i<s.length();i++) {
						if(s.charAt(i)=='/') {
							l=i;
						}
					}
					String fileName = s.substring(l+1,s.length());
					try {
						BufferedImage img = ImageIO.read(new File(s));
						WorkPlace wp = new WorkPlace(fileName,img);
						wp.workPlace(fileName,img);
						Thread.sleep(200);
						MainImage mi = new MainImage(fileName,img);
						mi.mainImage(fileName,img);
						frame.dispose();						
					}catch(Exception z) {
						System.out.println(z);
					}
				}
			}
		});
		
	}
}
