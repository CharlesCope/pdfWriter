import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
		
		
		String[] strLanguages = { "English","Japanese","Korean", "Spanish", "Simplified Chinese", "Traditional Chinese"};
		
		cboLanguage = new JComboBox<String>(strLanguages);
		cboLanguage.setBackground(Color.WHITE);
		cboLanguage.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		cboLanguage.setBounds(159, 11, 245, 26);
		cboLanguage.setSelectedIndex(-1);
		contentPanel.add(cboLanguage);
		
		JLabel lblMessageToPrint = new JLabel("Message To Print");
		lblMessageToPrint.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblMessageToPrint.setBounds(149, 48, 135, 26);
		contentPanel.add(lblMessageToPrint);
	
		// create a line border with the specified color and width
	    Border border = BorderFactory.createLineBorder(Color.BLUE, 2);

		lblMessage = new JLabel("Message");
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
				myPDFClass.ShowingText(1, 100, 720, "Hello World using True Type Font", clsPdfWriter.pdfTrueTypeFonts.TT_Arial_BoldItalic, 16, Color.BLACK, clsPdfWriter.pdfTextAlign.pdfAlignLeft, 0);

				//-- Put the file on the user desk top
				String strFileName = "TestingTrueTypeFonts.pdf";
				String strPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + strFileName ;
				myPDFClass.WritePDF(strPath);

				JOptionPane.showMessageDialog(null, "The TestingTrueTypeFonts.pdf  File has been created ");				

			}});
		
		cboLanguage.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent event) {
		        String selected = (String) cboLanguage.getSelectedItem();
		        //"English","Japanese","Korean", "Spanish", "Simplified Chinese", " Traditional Chinese"};
		        switch (selected) {
		         case "English":
		        	 lblMessage.setText("English");
		             break;
		         case "Japanese":
		        	 lblMessage.setText("Japanese こんにちは");
		        	 break;
		         case "Korean":
		        	 lblMessage.setText("Korean");
		        	 break;
		         case "Spanish":
		        	 lblMessage.setText("Spanish");
		             break;
		         case "Simplified Chinese":
		        	 lblMessage.setText("Simplified Chinese");
		             break;
		         case "Traditional Chinese":
		        	 lblMessage.setText("Traditional Chinese");
		        	 break;
		        }
		        
		    }});
	}
}
