package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
// Code Came from here.
// http://www.udel.edu/CIS/software/dist/batik-src/xml-batik/sources/org/apache/batik/svggen/
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Fonts.PDFFont;
import Fonts.fontToPDFfont;
import Fonts.table.CmapFormat0;
import Fonts.table.CmapFormat2;
import Fonts.table.CmapFormat4;
import Fonts.table.CmapFormat6;



public class FrmTestCode extends JFrame {
	private static final long serialVersionUID = 3607491399201743045L;
	private JPanel contentPane;
	private JComboBox<String> cboFonts;
	private JTextArea txtDisplayResults;
	private String path;
	private JScrollPane scrollPaneTable;
	final public static String JavaNewLine = System.getProperty("line.separator");
	private JTable fontTable;
	DefaultTableModel model;
	private JLabel lblTableData;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmTestCode frame = new FrmTestCode();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FrmTestCode() {
		setTitle("Test Code Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 710, 459);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 52, 670, 128);
		contentPane.add(scrollPane);
		
		txtDisplayResults = new JTextArea();
		txtDisplayResults.setLocation(35, 0);
		scrollPane.setViewportView(txtDisplayResults);
		
		cboFonts = new JComboBox<String>();
		cboFonts.setBackground(Color.WHITE);
		cboFonts.setBounds(118, 9, 363, 30);
		contentPane.add(cboFonts);
		
		JLabel lblNewLabel = new JLabel("Select Font File");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(15, 12, 111, 20);
		contentPane.add(lblNewLabel);
		
		JButton btnPDFFontDictionary = new JButton("Get PDF font dictionary");
		btnPDFFontDictionary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String fileName = path + File.separator + "Fonts"+ File.separator +(String) cboFonts.getSelectedItem();
				
				PDFFont myPDFFont = fontToPDFfont.ConvertFontFileToPDFFont(fileName);
				// Table need to be set as same font to show the correct symbols 
				Font font = new Font (myPDFFont.getFontFamilyName(), Font.TRUETYPE_FONT, 14);
				System.out.println("The Font Family Names is " + myPDFFont.getFontFamilyName());
				fontTable.setFont(font);
				
				txtDisplayResults.setText(myPDFFont.toString());
							
				model = (DefaultTableModel) fontTable.getModel();
				model.setColumnIdentifiers(new String[] {"Unicode", "Character", "Symbol", "GlyphID", "PDF Width"});

				refreshTableModel();
				//	Check what format of table are we getting
				if (myPDFFont.getCmapFormat() != null) {
					if (myPDFFont.getCmapFormat().getFormat() == 4) {
						CmapFormat4 cmapFormat4 = (CmapFormat4) myPDFFont.getCmapFormat();
						lblTableData.setText("Cmap Format 4");
						getGlyphsAndWidths(cmapFormat4.getGlyphIdArray(), myPDFFont);}
					else if(myPDFFont.getCmapFormat().getFormat() == 2){
						CmapFormat2 cmapFormat2 = (CmapFormat2) myPDFFont.getCmapFormat();
						lblTableData.setText("Cmap Format 2");
						getGlyphsAndWidths(cmapFormat2.getGlyphIndexArray(), myPDFFont);}
					else if(myPDFFont.getCmapFormat().getFormat() == 0){
						CmapFormat0 cmapFormat0 = (CmapFormat0) myPDFFont.getCmapFormat();
						lblTableData.setText("Cmap Format 0");
						getGlyphsAndWidths(cmapFormat0.getGlyphIdArray(), myPDFFont);}
					else if(myPDFFont.getCmapFormat().getFormat() == 6){
						CmapFormat6 cmapFormat6 = (CmapFormat6) myPDFFont.getCmapFormat();
						lblTableData.setText("Cmap Format 6");
						getGlyphsAndWidths(cmapFormat6.getGlyphIdArray(), myPDFFont);
					}
				}
				// Show data center in table.
				DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();  
				dtcr.setHorizontalAlignment(SwingConstants.CENTER);
				fontTable.getColumnModel().getColumn(0).setCellRenderer(dtcr);
				fontTable.getColumnModel().getColumn(1).setCellRenderer(dtcr);
				fontTable.getColumnModel().getColumn(2).setCellRenderer(dtcr);
				fontTable.getColumnModel().getColumn(3).setCellRenderer(dtcr);
				fontTable.getColumnModel().getColumn(4).setCellRenderer(dtcr);
				scrollPaneTable.setViewportView(fontTable);
				
			}
		});
		btnPDFFontDictionary.setBounds(491, 9, 195, 30);
		contentPane.add(btnPDFFontDictionary);
		
		scrollPaneTable = new JScrollPane();
		scrollPaneTable.setBounds(15, 229, 670, 180);
		contentPane.add(scrollPaneTable);
		
		lblTableData = new JLabel("");
		lblTableData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTableData.setBounds(25, 191, 484, 30);
		contentPane.add(lblTableData);
		
		fontTable = new JTable();
		
		
		// Give them a list to choose from.
		listFonts();
	}
		
	
	public void refreshTableModel(){
		for (int i = model.getRowCount(); i > 0; i--) {
			model.removeRow(0);
		}
	}
	
	public void getGlyphsAndWidths(int[] glyphIDs, PDFFont myPDFFont){
	
		int CharCode = 0;
		// Hard Code the first row
		String temp = myPDFFont.getUnicodeEscapeString(0);
		char symbol = (char) Integer.parseInt( temp.substring(2), 16 );
		String unicode = myPDFFont.getUnicodeString(0);
		CharCode = myPDFFont.getCmapFormat().mapCharCode(0);
		int pdfwidth = myPDFFont.getGlyphWidthToPDFWidth(CharCode);
		model.addRow(new Object[]{unicode,0,symbol,CharCode,pdfwidth});
		String strTest = null;
		for(int i= 1 ; i < 65535 ; i++){
			temp = myPDFFont.getUnicodeEscapeString(i);
			symbol = (char) Integer.parseInt( temp.substring(2), 16 );
			unicode = myPDFFont.getUnicodeString(i);
			CharCode = myPDFFont.getCmapFormat().mapCharCode(i);
			if(CharCode > 0 ){
				// Deal with the space
				if (i == 32){pdfwidth = myPDFFont.getSpaceWidthToPDFWidth();}
				else{pdfwidth = myPDFFont.getGlyphWidthToPDFWidth(CharCode);}
				strTest+= pdfwidth +",";
				model.addRow(new Object[]{unicode,i,symbol,CharCode,pdfwidth});
			}
		}
		System.out.println("The Table has " + model.getRowCount()+ " Rows");
		//System.out.print(strTest);
	}
	
	public void listFonts(){
		path = System.getenv("WINDIR");
		File directory =  new File(path, "Fonts");
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			if (file.isFile()){
				if(file.getName().endsWith("ttf")){cboFonts.addItem(file.getName());}
			}
		}
	}
}
