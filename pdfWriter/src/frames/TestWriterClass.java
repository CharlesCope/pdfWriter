package frames;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import pdfWriter.clsPdfWriter;



public class TestWriterClass extends JDialog {
	private static final long serialVersionUID = -1816624295986831984L;
	private final JPanel contentPanel = new JPanel();
	private JButton btnPDF;
	private JComboBox<String> cboLanguage;
	private JLabel lblMessage;
	String strFontLoc;
	private boolean blnEmbedded = false;
	private File fileFontMalgunPlain = null;
	private File fileFontMalgunBold = null;
	private File fileFontTimesPlain = null;
	private File fileFontTimesBold = null;
	private File fileFontTimesItalic = null;
	private File fileFontTimesBoldItalic = null;
		
	
	Font fontTimesPlain = new Font ("Times New Roman", Font.PLAIN, 14);
	Font fontTimesBold = new Font ("Times New Roman", Font.BOLD, 14);
	Font fontTimesItalic = new Font ("Times New Roman", Font.ITALIC, 14);
	Font fontTimesBoldItalic = new Font ("Times New Roman", Font.BOLD + Font.ITALIC, 14);
	
	Font fontMalgunPlain = new Font ("Malgun Gothic", Font.PLAIN, 14);
	Font fontMalgunBold = new Font ("Malgun Gothic", Font.BOLD, 14);
	
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
		strFontLoc = "";
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
		lblMessage.setFont(fontTimesPlain);
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
		
		JButton btnNewButton = new JButton("Two Fonts");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				
				String textTimes ="This is Time Roman Normal .. ";
				String textTimesBold = "Your Heating And Air Professionals";
				
				Font TimesPlain = new Font("Times New Roman", Font.PLAIN, 12);
				Font TimesBold = new Font("Times New Roman", Font.BOLD, 12);
				
				myPDFClass.ShowingText(1, 100, 700, textTimes,TimesPlain,fileFontTimesPlain.getPath(), 12, Color.BLACK, clsPdfWriter.pdfTextAlign.pdfAlignLeft, 0);
				myPDFClass.ShowingText(1, 100, 600, textTimesBold, TimesBold,fileFontTimesBold.getPath(), 12, Color.BLACK, clsPdfWriter.pdfTextAlign.pdfAlignLeft, 0);
				
				//-- Put the file on the user desk top
				String strFileName = "Writer two fonts.pdf";
				String strPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + strFileName ;
				myPDFClass.WritePDF(strPath,true);
				
				JOptionPane.showMessageDialog(null, "Done " );
				
				
			}
		});
		btnNewButton.setBounds(307, 198, 117, 23);
		contentPanel.add(btnNewButton);
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
		        	 strFontLoc = fileFontTimesPlain.getPath();
		             break;
		         case "Japanese":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Japanese Hello こんにちは  
		        	 lblMessage.setText("Japanese Hello " + "\u3053\u3093\u306B\u3061\u306F");
		        	 strFontLoc = fileFontMalgunPlain.getPath();
		        	 System.out.println("The path is" + strFontLoc);
		        	 break;
		         case "Korean":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Korean 안녕하세요
		        	 lblMessage.setText("Korean Hello = \uc548\ub155\ud558\uc138\uc694");
		        	 strFontLoc = fileFontMalgunBold.getPath();
		        	 break;
		         case "Spanish":
		        	 lblMessage.setFont(fontTimes);
		        	 // Should look like this Spanish Hello Hola
		        	 lblMessage.setText("Spanish Hello " + "Hola");
		        	 strFontLoc = fileFontTimesBold.getPath();
		             break;
		         case "Chinese":
		        	 lblMessage.setFont(fontMalgun);
		        	 // Should look like this Simplified Chinese Hello 你好
		        	 lblMessage.setText("Chinese Hello " + "\u4F60\u597D");
		        	 strFontLoc = fileFontMalgunPlain.getPath();;
		             break;
		        }
		        
		    }});
	}
	private void loadFonts(){
		// Make sure fonts are available to use on local machine.
	

		try {
			GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			URL baseURL = TestWriterClass.class.getResource("/resources/fonts/");
			
			InputStream inputFontMalgunPlain = new URL(baseURL, "malgun.ttf").openStream();
			InputStream inputFontMalgunBold = new URL(baseURL, "malgunbd.ttf").openStream();
			
			InputStream inputFontTimesPlain = new URL(baseURL, "times.ttf").openStream();
			InputStream inputFontTimesBold = new URL(baseURL, "timesbd.ttf").openStream();;
			InputStream inputFontTimesItalic = new URL(baseURL, "timesbi.ttf").openStream();
			InputStream inputFontTimesBoldItalic = new URL(baseURL, "timesbi.ttf").openStream();
			
		    fileFontMalgunPlain = getResourceAsFile(inputFontMalgunPlain);
			fileFontMalgunBold = getResourceAsFile(inputFontMalgunBold);
			
			fileFontTimesPlain = getResourceAsFile(inputFontTimesPlain);
			fileFontTimesBold = getResourceAsFile(inputFontTimesBold);
			fileFontTimesItalic = getResourceAsFile(inputFontTimesItalic);
			fileFontTimesBoldItalic = getResourceAsFile(inputFontTimesBoldItalic);

			gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, fileFontMalgunPlain));
		    gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, fileFontMalgunBold));
		  
		    gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, fileFontTimesPlain));
		    gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, fileFontTimesBold));
			gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, fileFontTimesItalic));
		    gEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, fileFontTimesBoldItalic));
		  		    
		} catch (Exception e1) {
			System.err.println("Font Files not found in jar file");
		}
		
		
	}
	public static File getResourceAsFile(InputStream StreamingData) {
		File tempFile = null;
		if(StreamingData != null){

			try {
				tempFile = File.createTempFile(String.valueOf(StreamingData.hashCode()), ".tmp");
				tempFile.deleteOnExit();
			} catch (IOException e1) {System.err.println("Sorry not able to create temp  file");}


			try (FileOutputStream out = new FileOutputStream(tempFile)) {
				//copy stream
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = StreamingData.read(buffer)) != -1) {
					try {
						out.write(buffer, 0, bytesRead);
					} catch (IOException e) {System.err.println("Sorry cant write buffer data");	}
				}
			} catch (FileNotFoundException e1) {System.err.println("Cant find the file");	}
			catch (IOException e1) {e1.printStackTrace();}
			return tempFile;
		}
		return tempFile;
	}
}
