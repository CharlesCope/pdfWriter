package pdfWriter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class TestWriterClass extends JDialog {
	private static final long serialVersionUID = -1816624295986831984L;
	private final JPanel contentPanel = new JPanel();
	private JButton btnPDF;
	private JComboBox<String> cboLanguage;
	private JLabel lblMessage;

	/** Launch the application.	 */
	public static void main(String[] args) {
		try {
			TestWriterClass dialog = new TestWriterClass();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** The Class Constructor  */
	public TestWriterClass() {
		loadFonts();
		setIconImage(Toolkit.getDefaultToolkit().getImage(TestWriterClass.class.getResource("/resources/images/pencil.png")));
		setUpFrame();
		setUpEvents();
		
	}
	
	private void setUpFrame(){
		setTitle("Test Writer Class");
		setBounds(100, 100, 450, 268);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblSelectLanguage = new JLabel("Select Language:");
		lblSelectLanguage.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblSelectLanguage.setBounds(10, 11, 135, 26);
		contentPanel.add(lblSelectLanguage);
				
				
		cboLanguage = new JComboBox<String>();
		cboLanguage.setBackground(Color.WHITE);
		cboLanguage.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		cboLanguage.setBounds(159, 11, 245, 26);
		cboLanguage.addItem("English");
		cboLanguage.addItem("Japanese");
		cboLanguage.addItem("Korean");
		cboLanguage.addItem("Spanish");
		cboLanguage.addItem("Simplified Chinese");
		cboLanguage.addItem("Traditional Chinese");
		cboLanguage.setSelectedIndex(-1);
		contentPanel.add(cboLanguage);
		
		JLabel lblMessageToPrint = new JLabel("Message To Print");
		lblMessageToPrint.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblMessageToPrint.setBounds(149, 48, 135, 26);
		contentPanel.add(lblMessageToPrint);
	
		// create a line border with the specified color and width
	    Border border = BorderFactory.createLineBorder(Color.BLUE, 2);

		lblMessage = new JLabel("Message");
		//lblMessage.setIcon(new ImageIcon(TestWriterClass.class.getResource("/resources/images/pencil.png")));
		lblMessage.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblMessage.setBorder(border);
		lblMessage.setBounds(10, 85, 414, 85);
		contentPanel.add(lblMessage);
		
		btnPDF = new JButton("Create PDF");
		btnPDF.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnPDF.setBounds(156, 197, 122, 23);
		contentPanel.add(btnPDF);
	}
	private void setUpEvents(){
		btnPDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clsPdfWriter myPDFClass = new clsPdfWriter();

				//-- Comment the file for learning
				myPDFClass.CommentFile(true);
				myPDFClass.pdfTitle("Print Message using  True Type Font");
				myPDFClass.pdfSubject("Testing the pdfWriter Class");
				myPDFClass.pdfCreator("Java");
				myPDFClass.pdfAuthor("Charles Cope");
				myPDFClass.pdfProducer("pdfWritter");
				myPDFClass.PageCount(1);
				myPDFClass.PaperSize(clsPdfWriter.pdfPaperSize.pdfLetter);
				
				// TODO: Will need to change Showing Text Method
				myPDFClass.ShowingText(1, 100, 720, lblMessage.getText(), clsPdfWriter.pdfTrueTypeFonts.TT_Arial_BoldItalic, 16, Color.BLACK, clsPdfWriter.pdfTextAlign.pdfAlignLeft, 0);

				//-- Put the file on the user desk top
				String strFileName = "TestingTrueTypeFonts.pdf";
				String strPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + strFileName ;
				myPDFClass.WritePDF(strPath);

				JOptionPane.showMessageDialog(null, "The TestingTrueTypeFonts.pdf  File has been created ");				

			}});
		
		cboLanguage.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent event) {
		    	Font fontTimes = new Font ("TimesRoman", Font.TRUETYPE_FONT, 14);
		    	
		    	Font fontMalgun = new Font ("Malgun Gothic", Font.TRUETYPE_FONT, 14);
		    	String selected = (String) cboLanguage.getSelectedItem();
		        
		        switch (selected) {
		         case "English":
		        	 lblMessage.setFont(fontTimes);
		        	 lblMessage.setText("English Hello ");
		             break;
		         case "Japanese":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Japanese Hello こんにちは  
		        	 lblMessage.setText("Japanese Hello " + "\u3053\u3093\u306B\u3061\u306F");
		        	 break;
		         case "Korean":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Korean 여보세요
		        	 lblMessage.setText("Korean Hello " + "\uC5E5\uBCF4\uC138\uC694");
		        	 break;
		         case "Spanish":
		        	 lblMessage.setFont(fontTimes);
		        	 // Should look like this Spanish Hello Hola
		        	 lblMessage.setText("Spanish Hello " + "Hola");
		             break;
		         case "Simplified Chinese":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Simplified Chinese Hello 你好
		        	 lblMessage.setText("Simplified Chinese Hello " + "\u4F60\u597D");
		             break;
		         case "Traditional Chinese":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Traditional Chinese Hello 你好
		        	 lblMessage.setText("Traditional Chinese Hello " + "\u4F60\u597D");
		        	 break;
		        }
		        
		    }});
	}
	private void loadFonts(){
		// Make sure fonts are available to use on local machine.
		GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();


		try {
			gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, 
					new File(TestWriterClass.class.getResource("/resources/fonts/malgun.ttf").toURI())));


			gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, 
					new File(TestWriterClass.class.getResource("/resources/fonts/malgunbd.ttf").toURI())));
			gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, 
					new File(TestWriterClass.class.getResource("/resources/fonts/malgunsl.ttf").toURI())));

			gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, 
					new File(TestWriterClass.class.getResource("/resources/fonts/times.ttf").toURI())));
			gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, 
					new File(TestWriterClass.class.getResource("/resources/fonts/timesbd.ttf").toURI())));
			gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, 
					new File(TestWriterClass.class.getResource("/resources/fonts/timesbi.ttf").toURI())));
			gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, 
					new File(TestWriterClass.class.getResource("/resources/fonts/timesi.ttf").toURI())));
		
		} catch (FontFormatException | IOException | URISyntaxException e) {e.printStackTrace();}

	}
}
