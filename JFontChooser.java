/**
 * Name: 		Schenck, Eric
 * Project: 	# 2
 * Due:		 	March 5, 2018
 * Course:		cs24501-w18
 * 
 * Description:	
 * 			Must use own JFontChooser that is created in a separate class file for JNotepad project. Lets user select font
 * and color using JColorChoosing. 
 *
 */


import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;



@SuppressWarnings("serial")
public class JFontChooser extends JComponent implements ActionListener{
	
	JDialog fontDialog;						// used by showDialog()
	boolean dialogResult;					// used by showDialog()

	private Font currentFont;				// used to store font
	private Color currentColor;				// used to store current color
	private String availFonts[];			// used to store generated list of fonts avail in java
	
	private String availStyles[] = {"Regular","Italic", "Bold", "Bold Italic"};
	
	private String availSizes[] = {"8", "9", "10", "11", "12", "14" , "16" , "18", "20", "24", "28", "36", "72"};
	
	@SuppressWarnings("rawtypes")
	private JList sizeList;
	@SuppressWarnings("rawtypes")
	private JList styleList;
	@SuppressWarnings("rawtypes")
	private JList fontList;					// used to hold list of fonts
	private JScrollPane fontScrPane;			// used to hold JList fonts
	private JScrollPane styleScrPane;		// used to style list
	private JScrollPane sizeScrPane;		// used for size list
	private JButton chgColor;
	private JButton okButton;
	private JButton cancel;
	private JTextField fontField;
	private JTextField styleField;
	private JTextField sizeField;
	private JLabel font;
	private JLabel style;
	private JLabel size;
	private JLabel sampleFont;
	private JPanel sample;
	
	
	private JComponent myWindow;
	
	
	public JFontChooser(){

		myWindow = buildComponents();		// building Font components and format and adding into window
		

	}
		
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public JComponent buildComponents(){
		
		JPanel mainPanel = new JPanel(new BorderLayout());						// all panels used for formating only 
		JPanel mainCenter = new JPanel(new GridLayout(2,1));		
		JPanel topCenter = new JPanel(new BorderLayout());
		JPanel firstTopLeft = new JPanel(new GridLayout(1,2));
		JPanel secondTopRight = new JPanel(new GridLayout(1,2));
		JPanel fontPanel = new JPanel(new BorderLayout());
		JPanel fontAndText = new JPanel(new GridLayout(2,1));
		JPanel fontAndTextFormat = new JPanel(new BorderLayout());
		JPanel fontScrFormat = new JPanel(new BorderLayout());
		JPanel stylePanel = new JPanel(new BorderLayout());
		JPanel styleAndText = new JPanel(new GridLayout(2,1));
		JPanel styleAndTextFormat = new JPanel(new BorderLayout());
		JPanel styleScrFormat = new JPanel(new BorderLayout());
		JPanel sizePanel = new JPanel(new BorderLayout());
		JPanel sizeAndText = new JPanel(new GridLayout(2,1));
		JPanel sizeAndTextFormat = new JPanel(new BorderLayout());
		JPanel sizeScrFormat = new JPanel(new BorderLayout());
		JPanel sampleFormat = new JPanel(new BorderLayout());
		
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
		JPanel colorButtonFormat = new JPanel(new BorderLayout());
		JPanel okButtonFormat = new JPanel(new BorderLayout());
		JPanel cancelButtonFormat = new JPanel(new BorderLayout());
			

		sample = new JPanel();
		font = new JLabel(" Font:");																// creating necessary objects
		style = new JLabel(" Style:");
		size = new JLabel(" Size:");
		sampleFont = new JLabel("AaBbYyZz");
		chgColor = new JButton("Color");
		okButton = new JButton("OK");
		cancel = new JButton("Cancel");
		fontField = new JTextField(10);
		styleField = new JTextField(10);
		sizeField = new JTextField(10);
		
		sample.add(sampleFont);
		sample.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sample")); // creating borders label
		
		chgColor.setMnemonic(KeyEvent.VK_C);													// alt c presses color
			
		
		// getting list of available fonts
		availFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		DefaultListModel listModel = new DefaultListModel(); // create instance of DefaultListModel
		for(int i = 0; i < availFonts.length ; ++i){		// adding all fonts to listModel
			listModel.addElement(availFonts[i]);
		}
		fontList = new JList(listModel);					// putting fonts into list
		fontScrPane = new JScrollPane(fontList);				// placing scroll pane around list
		
		
		// getting list of available styles
		DefaultListModel styleModel = new DefaultListModel();
		for(int i = 0; i< availStyles.length; ++i){			// adding elements to listmodel
			styleModel.addElement(availStyles[i]);
		}
		styleList = new JList(styleModel);					// adding to JList
		styleScrPane = new JScrollPane(styleList);			// adding to scrollPane
		
		// getting list of available sizes
		DefaultListModel sizeModel = new DefaultListModel();
		for(int i = 0; i< availSizes.length; ++i){			// adding elements to listmodel
			sizeModel.addElement(availSizes[i]);
		}
		sizeList = new JList(sizeModel);
		sizeScrPane = new JScrollPane(sizeList);			// adding list to scroll pane
		
		
		// font panel setup
		
		
		fontAndText.add(font);														// adding font label
		fontAndText.add(fontField);													// adding text
		fontAndTextFormat.add(fontAndText, BorderLayout.CENTER);					// used for formating
		fontAndTextFormat.add(new JLabel("    "), BorderLayout.LINE_START);
		fontAndTextFormat.add(new JLabel("    "), BorderLayout.LINE_END);		
		fontAndTextFormat.add(new JLabel("    "), BorderLayout.PAGE_START);
		
		fontScrFormat.add(fontScrPane, BorderLayout.CENTER);	// used for formating
		fontScrFormat.add(new JLabel("    "), BorderLayout.LINE_START);
		fontScrFormat.add(new JLabel("    "), BorderLayout.LINE_END);
		
		
		fontPanel.add(fontAndTextFormat, BorderLayout.PAGE_START);	// adding font and text
		fontPanel.add(fontScrFormat, BorderLayout.CENTER);
		fontPanel.add(new JLabel("    "), BorderLayout.PAGE_END);
				
		// style panel setup
		
		styleAndText.add(style);									// adding style JLabel to panel
		styleAndText.add(styleField);								// addinng text field to panel
		styleAndTextFormat.add(styleAndText, BorderLayout.CENTER);
		styleAndTextFormat.add(new JLabel("    "), BorderLayout.PAGE_START);

		styleScrFormat.add(styleScrPane, BorderLayout.CENTER);		// adding scrollpane to format panel
		
		stylePanel.add(styleAndTextFormat, BorderLayout.PAGE_START); // adding style and text
		stylePanel.add(styleScrFormat, BorderLayout.CENTER);
		stylePanel.add(new JLabel("    "), BorderLayout.PAGE_END);
		
		
		// size panel setup
		
		sizeAndText.add(size);										// adding size JLabel to panel
		sizeAndText.add(sizeField);	
		sizeAndTextFormat.add(sizeAndText, BorderLayout.CENTER);
		sizeAndTextFormat.add(new JLabel("    "), BorderLayout.PAGE_START);
		sizeAndTextFormat.add(new JLabel("    "), BorderLayout.LINE_START);
		
		sizeScrFormat.add(sizeScrPane, BorderLayout.CENTER);
		sizeScrFormat.add(new JLabel("    "), BorderLayout.LINE_START);
		
		sizePanel.add(sizeAndTextFormat, BorderLayout.PAGE_START);	//adding size and text to panel
		sizePanel.add(sizeScrFormat, BorderLayout.CENTER);
		sizePanel.add(new JLabel("    "), BorderLayout.PAGE_END);
		
		
		secondTopRight.add(stylePanel);
		secondTopRight.add(sizePanel);								// adding style and size panels 
		
		firstTopLeft.add(fontPanel);
		firstTopLeft.add(secondTopRight);
		
		topCenter.add(firstTopLeft, BorderLayout.CENTER);			// adding font and style and size panels onto one panel
		topCenter.add(new JLabel("    "), BorderLayout.LINE_END);		// used for formatting
		
		sampleFormat.add(sample , BorderLayout.CENTER);		// used for formating 
		sampleFormat.add(new JLabel("    "), BorderLayout.PAGE_END);
		sampleFormat.add(new JLabel("    "), BorderLayout.LINE_START);
		sampleFormat.add(new JLabel("    "), BorderLayout.LINE_END);
		
	
		mainCenter.add(topCenter);
		mainCenter.add(sampleFormat);							// adding sample to bottom of font style and size choices
		
		
		// button formatting
		colorButtonFormat.add(chgColor, BorderLayout.CENTER);	// adding button to panel for formating
		colorButtonFormat.add(new JLabel("    "), BorderLayout.LINE_START);
		colorButtonFormat.add(new JLabel("  "), BorderLayout.LINE_END);
		colorButtonFormat.add(new JLabel("  "), BorderLayout.PAGE_END);
		
		
		okButtonFormat.add(okButton, BorderLayout.CENTER);
		okButtonFormat.add(new JLabel("  "), BorderLayout.LINE_START);
		okButtonFormat.add(new JLabel("  "), BorderLayout.LINE_END);
		okButtonFormat.add(new JLabel("  "), BorderLayout.PAGE_END);
		
		cancelButtonFormat.add(cancel, BorderLayout.CENTER);
		cancelButtonFormat.add(new JLabel("  "), BorderLayout.LINE_START);
		cancelButtonFormat.add(new JLabel("    "), BorderLayout.LINE_END);
		cancelButtonFormat.add(new JLabel("  "), BorderLayout.PAGE_END);
		
		
		buttonPanel.add(colorButtonFormat);
		buttonPanel.add(okButtonFormat);
		buttonPanel.add(cancelButtonFormat);
	
		mainPanel.add(mainCenter , BorderLayout.CENTER);			// adding top components to main panel
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);			// adding buttons to main panel
		
		currentFont = new Font("Courier" , Font.PLAIN, 14);			// setting default current font and size. 
		currentColor = Color.black;									// setting default current color
		
		sampleFont.setFont(currentFont);
		
		
		fontList.addListSelectionListener((ListSelectionEvent le) -> {		// listening to Font list
			
			int i = 0;
			
			Object fontSelected[] = fontList.getSelectedValues();
			
		    currentFont = new Font(fontSelected[i++].toString(), currentFont.getStyle(),
					currentFont.getSize());									// setting currentFont
		
			sampleFont.setFont(currentFont);								// updating sampleFont
			fontField.setText(currentFont.getName());						// setting textField to font name
			
		});
		
		styleList.addListSelectionListener((ListSelectionEvent le) -> {		// listening to Style list
			
			int i = 0;
			
			Object styleSelected[] = styleList.getSelectedValues();
			
			switch(styleSelected[i++].toString()){							// setting font styles and text to font style name
			
			case "Regular":													
				currentFont = new Font(currentFont.getFontName(), Font.PLAIN , currentFont.getSize());
				styleField.setText("Regular");
				
				break;
			case "Italic":
				currentFont = new Font(currentFont.getFontName(), Font.ITALIC , currentFont.getSize());
				styleField.setText("Italic");
				
				break;
			case "Bold":
				currentFont = new Font(currentFont.getFontName(), Font.BOLD , currentFont.getSize());
				styleField.setText("Bold");
				
				break;
			case "Bold Italic":
				currentFont = new Font(currentFont.getFontName(), Font.BOLD | Font.ITALIC, currentFont.getSize());
				styleField.setText("Bold Italic");
				
				break;
			}
			
			sampleFont.setFont(currentFont);							// updating the sampleFont
				
		});
		
		sizeList.addListSelectionListener((ListSelectionEvent le) -> {		// listening to size list
			
			int i = 0;
			
			Object sizeSelected[] = sizeList.getSelectedValues();
			
		    currentFont = new Font(currentFont.getFontName(), currentFont.getStyle(),
				Integer.parseInt(sizeSelected[i++].toString()));			// setting currentFont
		 
			sampleFont.setFont(currentFont);								// updating sampleFont
			
			String tempSize = "";
			tempSize += currentFont.getSize();
			
			sizeField.setText(tempSize);						// setting textField to size
			
			
		});
																			// adding to JComponent
		return mainPanel;
	}
	
	
	public void setDefault(Font font){
		currentFont = font;													// setting font
		sampleFont.setFont(currentFont); 	
	}
	
	public void setDefault(Color color){
		currentColor = color;												// setting color
		sampleFont.setForeground(currentColor);
	}
	
	public Font getFont(){
		return currentFont;													// returning current font
	}
	
	public Color getColor(){
		return currentColor;												// returning current color
	}
	
	public boolean showDialog(Component parent){
		
		dialogResult = false;
		
		fontDialog = new JDialog(JOptionPane.getFrameForComponent(parent), ("JFontChooser"), true);	// JDialog being created, modal 
		
		fontDialog.setSize(325,350);
		
		fontDialog.add(myWindow);
		
		fontDialog.getRootPane().setDefaultButton(okButton);    	// presses ok when user hits enter
		
		chgColor.addActionListener(ae -> {
			
			currentColor = JColorChooser.showDialog(this, "Color Chooser", currentColor); 
			
			if(currentColor != null){								// user selected color and hit ok
				sampleFont.setForeground(currentColor); 			// setting sampleFont Color
			}
			
		});											// adding action listeners to buttons
		okButton.addActionListener(ae -> {
			dialogResult = true;
			fontDialog.setVisible(false);
			
		});
		cancel.addActionListener(ae -> {			// user selected cancel 
			dialogResult = false;
			fontDialog.setVisible(false);			// hide dialog
		});
		
		
		fontDialog.addWindowListener(new WindowAdapter(){					// closes on press of x button, returns false
			public void windowClosing(WindowEvent e){
				dialogResult = false;
			}
		}); 
		
		fontDialog.setLocationRelativeTo(null);
		fontDialog.setVisible(true);
		fontDialog.dispose();
		fontDialog = null;
		
		return dialogResult;
		
	}




	@Override
	public void actionPerformed(ActionEvent ae) {
		
	}
	
	
	
	
	
	
}
