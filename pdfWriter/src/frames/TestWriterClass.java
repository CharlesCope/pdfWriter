package frames;
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

import pdfWriter.clsPdfWriter;
import javax.swing.JCheckBox;



public class TestWriterClass extends JDialog {
	private static final long serialVersionUID = -1816624295986831984L;
	private final JPanel contentPanel = new JPanel();
	private JButton btnPDF;
	private JComboBox<String> cboLanguage;
	private JLabel lblMessage;
	String strFontLoc;
	private boolean blnEmbedded = false;
	
	Font fontTimes = new Font ("Times New Roman", Font.TRUETYPE_FONT, 14);
	Font fontMalgun = new Font ("Malgun Gothic", Font.TRUETYPE_FONT, 14);
	
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
		strFontLoc = "../pdfWriter/src/resources/fonts/times.ttf";
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
		cboLanguage.addItem("Chinese");
		cboLanguage.setSelectedIndex(0);
		contentPanel.add(cboLanguage);
		
		JLabel lblMessageToPrint = new JLabel("Message To Print");
		lblMessageToPrint.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblMessageToPrint.setBounds(149, 48, 135, 26);
		contentPanel.add(lblMessageToPrint);
	
		// create a line border with the specified color and width
	    Border border = BorderFactory.createLineBorder(Color.BLUE, 2);

		lblMessage = new JLabel();
		lblMessage.setFont(fontTimes);
		lblMessage.setText("English Hello ");
		lblMessage.setBorder(border);
		lblMessage.setBounds(10, 85, 414, 85);
		contentPanel.add(lblMessage);
		
		btnPDF = new JButton("Create PDF");
		btnPDF.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnPDF.setBounds(156, 197, 122, 23);
		contentPanel.add(btnPDF);
		
		JCheckBox chckbxEmbedded = new JCheckBox("Embed Font File");
		chckbxEmbedded.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxEmbedded.isSelected()== true ){blnEmbedded = true;}
				if(chckbxEmbedded.isSelected()== false ){blnEmbedded = false;}
			}
		});
		chckbxEmbedded.setBackground(Color.WHITE);
		chckbxEmbedded.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		chckbxEmbedded.setBounds(10, 196, 135, 23);
		contentPanel.add(chckbxEmbedded);
	}
	private void setUpEvents(){
		btnPDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clsPdfWriter myPDFClass = new clsPdfWriter();

				//-- Comment the file for learning
				myPDFClass.CommentFile(true);
				myPDFClass.pdfTitle("Example Use of pdfWriter Class Java");
				myPDFClass.pdfSubject("Testing the pdfWriter Class");
				myPDFClass.pdfCreator("Java TradeWare");
				myPDFClass.pdfAuthor("Charles Cope");
				myPDFClass.pdfProducer("pdfWritter");
				myPDFClass.PageCount(1);
				myPDFClass.PaperSize(clsPdfWriter.pdfPaperSize.pdfLetter);
				
				myPDFClass.ShowingText(1, 100, 700, lblMessage.getText(), lblMessage.getFont(),strFontLoc, 16, Color.BLACK, clsPdfWriter.pdfTextAlign.pdfAlignLeft, 0);
				
				
				//-- Put the file on the user desk top
				String strFileName = "Example.pdf";
				String strPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + strFileName ;
				myPDFClass.WritePDF(strPath,blnEmbedded);

				JOptionPane.showMessageDialog(null, "The Example.pdf  File has been created ");				

			}});
		
		cboLanguage.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent event) {
		    	Font fontTimes = new Font ("TimesRoman", Font.TRUETYPE_FONT, 14);
		    	Font fontMalgun = new Font ("Malgun Gothic", Font.ITALIC, 14);
		    	
		    	String selected = (String) cboLanguage.getSelectedItem();
		    	 
		        switch (selected) {
		         case "English":
		        	 lblMessage.setFont(fontTimes);
		        	 lblMessage.setText("English Hello ");
		        	 strFontLoc = "../pdfWriter/src/resources/fonts/times.ttf";
		             break;
		         case "Japanese":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Japanese Hello こんにちは  
		        	 lblMessage.setText("Japanese Hello " + "\u3053\u3093\u306B\u3061\u306F");
		        	 strFontLoc = "../pdfWriter/src/resources/fonts/malgun.ttf";
		        	 break;
		         case "Korean":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Korean 안녕하세요
		        	 lblMessage.setText("Korean Hello = \uc548\ub155\ud558\uc138\uc694");
		        	 strFontLoc = "../pdfWriter/src/resources/fonts/malgun.ttf";
		        	 break;
		         case "Spanish":
		        	 lblMessage.setFont(fontTimes);
		        	 // Should look like this Spanish Hello Hola
		        	 lblMessage.setText("Spanish Hello " + "Hola");
		        	 strFontLoc = "../pdfWriter/src/resources/fonts/times.ttf";
		             break;
		         case "Chinese":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Simplified Chinese Hello 你好
		        	 lblMessage.setText("Chinese Hello " + "\u4F60\u597D");
		        	 strFontLoc = "../pdfWriter/src/resources/fonts/malgun.ttf";
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
