package Application;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Textshow {

	private JFrame frame;
	private String text,fileName,filter;

	/**
	 * Launch the application.
	 */
	public void textshow(String s,String fileName,String filter) {
		this.text = s;
		this.fileName = fileName;
		this.filter = filter;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Textshow window = new Textshow(s,fileName,filter);
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
	public Textshow(String s,String fileName,String filter) {
		this.text = s;
		this.fileName = fileName;
		this.filter = filter;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle(fileName+" --> "+ filter);
		frame.setBounds(100, 100, 527, 494);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
			
		JTextArea area = new JTextArea(text);
		area.setFont(new Font("Serif",Font.PLAIN,20));
		area.setBounds(39, 31, 456, 403);
		area.setEditable(false);
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(39, 23, 453, 411);
		frame.add(scroll);
	}

}
