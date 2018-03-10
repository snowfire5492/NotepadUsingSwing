/**
 * Name: 		Schenck, Eric
 * Project: 	# 3
 * Due:		 	March 5, 2018
 * Course:		cs24501-w18
 * 
 * Description:	
 * 			A notepad program that is very similar to a standard windows notepad. only make certain , specified features
 * work. can make more features work for extra credit. Must use own JFontChooser that is created in a separate class file.
 *
 */


import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.io.*;
import java.util.Scanner;
import javax.swing.text.Document;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Date;
import java.text.SimpleDateFormat;



@SuppressWarnings("serial")
public class JNotepad extends JFrame implements ActionListener {
	
	
	JTextArea textArea;															// using JTextArea for the text input
	
	
	
	
	JScrollPane sp;															// scroll pane for text area
	JMenuBar menuBar;														// menuBar for menus
	JMenu file, edit, format, view, help;									// JMenu's to work with
	JMenuItem optionNew, open, save, saveAs, pageSetup, print, exit;
	JMenuItem undo, cut, copy, paste, delete, find, findNext, replace, goTo, selectAll, timeDate;
	JMenuItem font, statusBar, viewHelp, aboutJNotepad;
	JMenuItem popupCut , popupCopy , popupPaste;
	JCheckBoxMenuItem wordWrap;												// wordWrap is a checkBoxItem
	JCheckBox findMatch;													// used for find dialog window
	JFileChooser fileChooserOpen;											// file chooser for opening files. needed to show .txt and .java
	JFontChooser fontChooser;
	JFileChooser fileChooser;												// file chooser for saving files. shows all files types
	JDialog closeMsg;														// message prompt on close
	JDialog aboutDialog;													// aboutJDialog window
	JDialog findDialog;														// find Dialog window
	JDialog cannotFindDialog;												// used for cannot find window
	JButton msgSave, msgDontSave, msgCancel;								// buttons on closeMsg
	JButton okay;															// button on aboutJDialog window
	JButton findNextButton , findCancel;									// buttons for find dialog window
	JButton cannotFindOk;													// button on cannot find dialog: Ok button
	JPanel menuPane;
	JPopupMenu popupMenu;
	JRadioButton upButton, downButton;										// JRadioButtons for find dialog window
	JLabel aboutLabel , aboutIcon;											// JLabels for aboutJNotepad window
	JLabel closeMsgLabel;													// for closeMsg dialog
	JLabel cannotFindLabel;													// used for cannot find string message
	JTextField findText;													// for find dialog window
	GridLayout msgGrid;														
	SimpleDateFormat dateFormat;
	ButtonGroup butGroup;													// used to contain JRadioButtons in find dialog window
	Document document;
	
	boolean caretOnLeft;													// used for find UP/DOWN button functionality
	boolean firstTimeDown;													// used for find UP/Down button switches
	boolean matchIsSelected;												// lets find know when match is selected
	boolean upButtonOn;														// upButton has been pressed if true/ else downButton
	boolean textIsFound;													// use for Find function
	boolean savedOnFile;													// using boolean to track if textArea is saved
	boolean lastSaved;														// current copy is copy saved with no changes
	boolean okToClear;														// used for New key
	boolean cancelClose;													// used to cancelClose Operation if user wants
	boolean readyToClose;													// used to check and dispose of components prior to close
	String fileName;														// used to store user given fileName
	String filePath;														// used to store fileDirectory info
	String lastFindStr;														// used to store last string searched for in FIND 
	String docuMatch;
	String textToMatch;														// both used to compare strings
	int caretLocation;														// used to store location of caret for find match
	int pos;																// used to store pos for find match method
	int strLength;
	
	public JNotepad(){
		
		readyToClose = false;
		cancelClose = false;												// initializing to false
		okToClear = false;
		lastSaved = true;													// used to track whether exiting should be prompted to save
		savedOnFile = false;												// initiallizng boolean to false. no save in directory
		fileName = "Untitled";												// default new document name
		
		
		
		setSize(900,500);												    // setting frame size
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); 		        // grabb event and dont close yet
		
		addWindowListener(new WindowAdapter(){
			
			@Override
			public void windowClosing(WindowEvent evt){
				readyToClose = true;										// setting true to track how dialog displays.
																			// Note: closeMsg can be called from new which doesnt close windows 
				if(!lastSaved){												// not the saved version of document
					closeMsg.setVisible(true);				
					// bring up prompt to save before exiting which calls save method
				}
				if(!cancelClose){											// user chose save or dont save
					closeMsg.dispose();                       				// getting rid of window as program closes
					aboutDialog.dispose();
					System.exit(0); 										// closes window and program 
				}
				cancelClose = false;
			}
		});
		
		
		setLayout(new BorderLayout());										// setting frame to borderlayout
		setTitle(fileName + " - Notepad");									// setting Title to default settings
		setIconImage(new ImageIcon("C://Users//Eric//Desktop//Programs//CS245_JavaSwingGUI//JNotepad.png").getImage());
		
		buildMenu();														// building menu with all menuItems
		
		textArea = new JTextArea(1000,1000);								// setting size of text area as a object is created
		textArea.setWrapStyleWord(true); 									
		textArea.setFont(new Font("Courier" , Font.PLAIN, 12));				// initial font courier normal 12 points
		
		
		
		
		
		DocumentListener docuListen = new DocumentListener() {				// listens for any keyinput into the textArea
			@Override
			public void changedUpdate(DocumentEvent de){
				// unused but required for DocumentListener
			}	
			@Override
			public void insertUpdate(DocumentEvent de){						// if user inserts any character into textArea
				find.setEnabled(true);
				findNext.setEnabled(true);        	 						// can now find values within textArea. otherwise nothing to look for
				lastSaved = false;											// a change has been made since saving
			}
			@Override
			public void removeUpdate(DocumentEvent de){
				if(textArea.getText().length() == 0){
					find.setEnabled(false);									// disabling find and findNext since no strings exist in textArea
					findNext.setEnabled(false);
				}
				lastSaved = false;											// a change has been made since saving
			}
		};
		textArea.getDocument().addDocumentListener(docuListen);				// adding documentListener to textArea
		
		
		sp = new JScrollPane(textArea);										// making JScrollPane and adding textArea to it
		
		
		
		add(sp, BorderLayout.CENTER);
		
		
		
		
		

		buildLastSaveMenu();												// building lastchance save menu
		buildAboutDialog();													// buiding Jdialog for About JNotepad
		buildFindDialog();													// building dialog for Find option
		buildCannotFindDialog();											// building msg for when find cannot find string
		
		
		
		fontChooser = new JFontChooser();								// creating JFontChooser class
		
		
		fileChooserOpen = new JFileChooser();								// creating file chooser for open menu
																			// creating file chooser for save menu
		fileChooser = new JFileChooser() {
			
			@Override 
			public void approveSelection() {								// code used in case of save overwrite
				File temp = getSelectedFile();								// getting file that is selected
				
				if(temp.exists() && getDialogType() == SAVE_DIALOG) {
					int result = JOptionPane.showConfirmDialog(this, getSelectedFile().getName() + " already exists.\n"
							+ "Do you want to replace it?", "Confirm Save As",
							JOptionPane.YES_NO_OPTION);	// dialog to prompt user about overwrite issue
																// and give two options yes or no
	
					switch(result){
				 
					case JOptionPane.YES_OPTION:					// user selected yes
					
						super.approveSelection();              		// continue with save
						return;
				
					case JOptionPane.NO_OPTION:
					
						cancelSelection();
						return;
					}
				}else if(getDialogType() == SAVE_DIALOG){
	
					super.approveSelection();                       // save new file
				}
			}
		};	
		
		
						// file chooser open menu will have .txt and .java in search, save menu has all files still
		fileChooserOpen.addChoosableFileFilter(new FileNameExtensionFilter("Text Documents (*.txt)", "txt"));
		fileChooserOpen.addChoosableFileFilter(new FileNameExtensionFilter("Java Documents (*.java)", "java"));
		fileChooserOpen.setAcceptAllFileFilterUsed(true);	
		
		
		
		textArea.addCaretListener(new CaretListener() {						// used to update copy,cut,delete enable methods
			@Override
			public void caretUpdate(CaretEvent ce){
				
				int selectionLength = textArea.getSelectionEnd() - textArea.getSelectionStart();	// get selection length
				
				if(selectionLength == 0){									// nothing is highlighted in textArea
					copy.setEnabled(false);									// setEnabled to False
					cut.setEnabled(false);
					delete.setEnabled(false);
					popupCut.setEnabled(false);
					popupCopy.setEnabled(false);
				}else{														// text is highlighted in textArea
					copy.setEnabled(true);									// setEnabled to true
					cut.setEnabled(true);
					delete.setEnabled(true);
					popupCut.setEnabled(true);
					popupCopy.setEnabled(true);
				}
			}
		});
		
		
		
		findText.addCaretListener(new CaretListener() {						// used to update Find Next button in Find dialog window
			@Override
			public void caretUpdate(CaretEvent ce){
			
				if(findText.getText().length() == 0){ 						// theres no text inside of find text field
					findNextButton.setEnabled(false);						// so disable Find Next button
				}else{
					findNextButton.setEnabled(true); 						// text detected so enable find next button
				}
			}
		});
		
		
		
		dateFormat = new SimpleDateFormat("hh:mm a MM/dd/yyyy");			// setting dateFormat to specified format
																			
		
		popupMenu = new JPopupMenu();										// creating popupMenu
		
		popupCut = new JMenuItem("Cut");
		popupCopy = new JMenuItem("Copy");									// popup menuItems
		popupPaste = new JMenuItem("Paste");
		
		popupCut.addActionListener(this);
		popupCopy.addActionListener(this);									// adding actionListeners.
		popupPaste.addActionListener(this);									// should set off other cut, copy, paste listeners
		
		popupCut.setEnabled(false);											// disabled until area is highlighted
		popupCopy.setEnabled(false);
		
		
		popupMenu.add(popupCut);											// adding to menu
		popupMenu.add(popupCopy);
		popupMenu.add(popupPaste);
		
		popupCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X , ActionEvent.CTRL_MASK));
		popupCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C , ActionEvent.CTRL_MASK));
		popupPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		
		
		
		addMouseListener(new MouseAdapter() {								// mouse listener to make menu popup
			public void mouseReleased(MouseEvent me){
				if(me.isPopupTrigger()){
					popupMenu.show(me.getComponent(), me.getX(), me.getY());
				}
			}
		});
		
		textArea.setComponentPopupMenu(popupMenu);							// adding to textArea 
		
		setVisible(true);													// setting frame visibility to true
	}
	
	

	
	public void actionPerformed(ActionEvent ae){
		
			
		switch(ae.getActionCommand()){
		
		case "New":
			if(!lastSaved)
				closeMsg.setVisible(true);					// showing last chance to save message 

			if(okToClear){							
			
				lastSaved = false;						// setting to false for easy exit from blank document
				savedOnFile = false;					// setting to false for new file
				fileName = "Untitled";					// resetting fileName
				updateLastSaveMenu();					// updating last save menu
				setTitle(fileName + " - Notepad");		// resetting title to default 
				textArea.setText(""); 					// clearing out text in textArea
			}
			okToClear = false;
			break;
			
		case "Open...":
			savedOnFile = true;						// open file will be saved already 
			String temp = "";						// temporary string to hold incoming file data					
			int result = fileChooserOpen.showOpenDialog(null);
			if(result == JFileChooser.APPROVE_OPTION){		// user selected an option
				try{
					
					fileName = fileChooserOpen.getSelectedFile().getName(); // get fileName
					filePath = fileChooserOpen.getSelectedFile().getAbsolutePath();
					updateLastSaveMenu();					// updating JDialog close window fileName
					File file = new File(filePath);		// getting filepath chosen
					Scanner fileInput = new Scanner(file);
					
					while(fileInput.hasNextLine()){			// while the file has input
						temp += fileInput.nextLine() + "\n";// save it to string
					}
					//Font fileFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(fileName))).deriveFont(Font.PLAIN,12);
					textArea.setText(temp);					// dislay the file text in textArea
					lastSaved = true;						// current copy is saved copy so no safety prompt required to exit
				
					setTitle(fileName + " - Notepad");		// setting to name of opened file
					//textArea.setFont(fileFont);
					fileInput.close();						// close Scanner
	
				}catch(IOException e){
					System.out.println("Something went wrong...");
				}
				
				
			}
			
			break;
			
		case "Save":
			try {
				save();								// calling save() method
			}catch(IOException e){	}
				
			closeMsg.setVisible(false);						// used to hide JDialog if it is open after hitting save
			break;
			
		case "Save As...":						
			try {
				saveAs();						// calling saveAs() function
			} catch (IOException e) {	}									
			break;
			
		case "Exit":
			
			if(lastSaved == false){
				
				closeMsg.setVisible(true);				
				// bring up prompt to save before exiting which calls save method
			}
			if(!cancelClose){					// if user didnt select cancel when/if prompted
				closeMsg.dispose();             // getting rid of window as program closes
				aboutDialog.dispose();
				System.exit(0);					// exiting program
			}
			cancelClose = true;
			break;
			
		case "Cut":
			textArea.cut();    					// cut selected text
			break;
			
		case "Copy":
			textArea.copy();					// copys selected text
			break;
			
		case "Paste":
			textArea.paste(); 	             	// pastes copied text from clipboard
			break;
			
		case "Delete":
			textArea.setText(textArea.getText().replace(textArea.getSelectedText(), "")); //deleting selected text
			break;
			
		case "Find...":
			
			findDialog.setVisible(true);							// displaying Find dialog window
			
			break;
			
		case "Find Next":											// Find Next button from menu NOT dialog window
			
			if(lastFindStr.length() == 0){							// no string is saved so goto find dialog 
				findDialog.setVisible(true);
			}else{													// string is saved so shortut to Find Next method
				findNext();
			}
			
			break;
			
		case "Select All":
			textArea.selectAll();									// selecting all text in JTextArea
			break;
			
		case "Time/Date":
			textArea.append(dateFormat.format(new Date()));			// appending formated time/date to textArea on request
			break;
			
		case "Word Wrap":
			if(wordWrap.isSelected()){								// result of menu press is that check box is selected
				textArea.setLineWrap(true);							// therefore word wrap must be implemented
			}else
				textArea.setLineWrap(false);						// else word wrap must be undone
			break;
			
		case "Font...":
			
			//fontChooser = new JFontChooser(this);								// creating JFontChooser class
			fontChooser.setDefault(new Font(textArea.getFont().getFontName() , textArea.getFont().getStyle() , 
					textArea.getFont().getSize()));
			fontChooser.setDefault(textArea.getForeground());
			if(fontChooser.showDialog(null)){
				textArea.setFont(fontChooser.getFont());
				textArea.setForeground(fontChooser.getColor());
			}
			
			break;
			
		case "About JNotepad":
			
			aboutDialog.setVisible(true);							// showing about JNotepad dialog
			
			break;
			
		case "Don't Save":
			
			if(readyToClose){						// dont save selected on a exit dialog
				aboutDialog.dispose();
				closeMsg.dispose();					// dispose of dialog 
				break;								// program closes on break
			}
			
			
			
			okToClear = true;						// new can clear the text now
			closeMsg.setVisible(false);				// used to hide JDialog
			break;
			
		case "Cancel":
			
			readyToClose = false;					// no longer is window closing since cancel was chosen
			cancelClose = true;						// used to make sure frame stays if user cancels exiting 
			okToClear = false;						// not okay to clear text. cancel JDialog 
			closeMsg.setVisible(false);						// used to hide JDialog
			break;
		
		
		
		case "Ok":									// about dialog ok button
		
			aboutDialog.setVisible(false);						// hiding dialog 
			break;
			
			
		case "FindNext":										// Find Next button from Find Dialog window
			lastFindStr = findText.getText();					// getting and saving text 
			findNext();											// call findNext() method
			break;
			
		case "findCancel":										// Cancel button from find dialog window
			findDialog.setVisible(false); 						// hiding find dialog window
			break;
			
		case "Match Case":
			
			
			if(findMatch.isSelected()){						// case sensitive
				matchIsSelected = true;						// match selected so set to true and stay true until unchecked
			}else{
				matchIsSelected = false;					// match not selected or unchecked
			}
	
			break;
			
		case "Up":
			upButtonOn = true;									// upButton pressed 
			break;
			
		case "Down":
			upButtonOn = false;									// downButton pressed
			break;
			
		case "OK":												// find string not found msg button
			cannotFindDialog.setVisible(false); 				// hide dialog
			break;
			
			
			
		
		}
		
	}
	
	// method to findNext string value, if existing, in textArea. 
	// in not existing then will show a "Cannot find "string" " message with an ok button
	private void findNext(){	
		caretLocation = textArea.getCaretPosition();		// getting location of caret
		
		
		

		textArea.requestFocusInWindow(); 					// need for highlighting to show up
		
		if( lastFindStr != null && lastFindStr.length() > 0){	// lastFindStr exists
			
			document = textArea.getDocument();
			
			strLength = lastFindStr.length();				// getting string length
			
			try{
				textIsFound = false;							// setting boolean false
				
				
				if(upButtonOn){									// up button is selected
				
					
					while(caretLocation - strLength >= 0){		// caret position - str length > 0. search towards left (0)
						
						
						pos = caretLocation - strLength;				// position to check the string like normal left to right reading
						
						if(matchIsSelected){				// requires exact ( CASE SENSITIVE MATCH )
							docuMatch = document.getText(pos, strLength);
							textToMatch = lastFindStr;
							
						}else{
							docuMatch = document.getText(pos, strLength).toLowerCase(); // ignore case
							textToMatch = lastFindStr.toLowerCase();	
						}
						
						
						
						if(docuMatch.equals(textToMatch)){      // check if matches
							textIsFound = true;					// text found so set true
							break;
						}
						--caretLocation;						// search upward or too the left
					}
					
					if(textIsFound){							// a match was found in document
						
						Rectangle viewRect = textArea.modelToView(caretLocation); // get rectangle of where text would be
						
						textArea.scrollRectToVisible(viewRect);  			// scroll to make rectangle visible
						
						textArea.setCaretPosition(caretLocation); 
						
						textArea.moveCaretPosition(caretLocation - strLength); //Highlight text
						
						caretOnLeft = true;									// used for transition to down find
						
					}else{
						// text is not found so bring up cannot find msg dialog
						cannotFindLabel.setText("Cannot find \"" + lastFindStr + "\" ");
						cannotFindDialog.setVisible(true); 					// showing Cannot find dialog message
					}
					
					
				}else if(!upButtonOn){										// down button selected
					
					if(caretOnLeft){
						caretLocation += strLength;							// setting caret to right to look onward towards right
					}
					
					while(caretLocation + strLength <= document.getLength()){ // search towards right (towards last index)
					
						if(matchIsSelected){				// requires exact ( CASE SENSITIVE MATCH )
							docuMatch = document.getText(caretLocation, strLength);
							textToMatch = lastFindStr;
							
						}else{
							docuMatch = document.getText(caretLocation, strLength).toLowerCase(); // ignore case
							textToMatch = lastFindStr.toLowerCase();
						}
						
						if(docuMatch.equals(textToMatch)){      // check if matches
							textIsFound = true;					// text found so set true
							break;
						}
						++caretLocation;						// search downward or to the right
							
					}
					

					if(textIsFound){							// a match was found in document
						
						Rectangle viewRect = textArea.modelToView(caretLocation); // get rectangle of where text would be
						
						textArea.scrollRectToVisible(viewRect);  			// scroll to make rectangle visible
						
						
						textArea.setCaretPosition(caretLocation + strLength); 
						
						textArea.moveCaretPosition(caretLocation); //Highlight text
						
						caretOnLeft = true;
						
						
					}else{
						cannotFindLabel.setText("Cannot find \"" + lastFindStr + "\" ");
						cannotFindDialog.setVisible(true); 					// showing Cannot find dialog message
					}
					
					
				}
				
				
				
				
				
			}catch(Exception e){
				// error occurred
			}
			
			
		}
		
		
		
	}
	
	
	
	
	private void save() throws IOException{
		
		if(savedOnFile){						// file has name and is only being re-saved or went through SaveAs
			try {
				File file = new File(filePath);								// creating new file
				FileWriter outputFile = new FileWriter(file);
																		
				outputFile.write(textArea.getText());						// writing text into file
				
				lastSaved = true;											// this is current saved copy
				
				okToClear = true;											// used for new command
				
				outputFile.close();											// closing file1
			}catch(IOException e){ 	}
			
		}else{
			try {
				saveAs();					// call saveAs Method to open JFileChooser and save with name
				
			} catch (IOException e) {	}							
			}

		}
	
	private void saveAs() throws IOException{
		int result = fileChooser.showSaveDialog(null);							// openning fileChooser to savefile
		
		if(result == JFileChooser.APPROVE_OPTION){								// user has hit save
			
			filePath = fileChooser.getSelectedFile().getPath();			// getting user selected path
			savedOnFile = true;											// setting true 
			
			save();														// saving 
			
			fileName = fileChooser.getSelectedFile().getName();			// getting user selected name
			updateLastSaveMenu();										// updating fileName in JDialog menu
			
			setTitle(fileName + " - Notepad");							// changing frame title to document name
		
			
		}
		
		
		
	}
	
	
	private void buildCannotFindDialog(){
		
		cannotFindDialog = new JDialog(this, "JNotepad", true);			// creating modal dialog window
		cannotFindDialog.setSize(200, 175);
		cannotFindDialog.setLocationRelativeTo(null);
		cannotFindDialog.setResizable(false);							// user cannot resize window
		
		cannotFindLabel = new JLabel("Cannot Fine \"" +  lastFindStr   + "\" ");	// building label
		cannotFindOk = new JButton("OK");								// ok button 
		
		cannotFindOk.addActionListener(this);
		
		JPanel okButtonPanel = new JPanel( new BorderLayout());			// panel to format button on dialog window
		okButtonPanel.add(cannotFindOk, BorderLayout.CENTER);			// adding button to panel
		okButtonPanel.add(new JLabel("  "), BorderLayout.LINE_START);	// used for formating
		okButtonPanel.add(new JLabel("  "), BorderLayout.LINE_END);
		okButtonPanel.add(new JLabel(" "), BorderLayout.PAGE_START);
		okButtonPanel.add(new JLabel(" "), BorderLayout.PAGE_END);
		
		JPanel greyBottom = new JPanel(new GridLayout(1,2));			// to for bottom grey panel
		greyBottom.add(new JLabel(""));									// used for formating
		greyBottom.add(okButtonPanel);									// adding button panel to grey panel
		//greyBottom.setBackground(Color.GRAY);
		
		JPanel whiteUpper = new JPanel(new BorderLayout());				// panel to be white and hold jlabel
		whiteUpper.add(cannotFindLabel, BorderLayout.CENTER);			// adding label to panel
		whiteUpper.setBackground(Color.WHITE);							// setting color to white
		cannotFindLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel mainCannotMsgPanel = new JPanel(new BorderLayout());
		mainCannotMsgPanel.add(whiteUpper, BorderLayout.CENTER);
		mainCannotMsgPanel.add(greyBottom, BorderLayout.PAGE_END);
		
		cannotFindDialog.add(mainCannotMsgPanel);						// adding panel to main dialog window
		
		cannotFindDialog.getRootPane().setDefaultButton(cannotFindOk); 	// is pressed when user hits enter on this window
		
	}
	
	
	
	
	
	
	private void buildFindDialog(){
		findDialog = new JDialog(this, "Find", false);					// creating non-modal dialog window 
		findDialog.setSize(400,200); 									// size of dialog window
		findDialog.setResizable(false); 								// making so user cannot resize window
		findDialog.setLayout(new BorderLayout()); 						// setting dialog window to borderlayout for formatting
		findDialog.setLocationRelativeTo(this);
		
		findText = new JTextField(20);									// creating a JTextField
		findNextButton = new JButton("Find Next");
		findNextButton.setActionCommand("FindNext");
		findNextButton.setEnabled(false); 								// initially Find Next button is disabled, until user types in textfield
		findCancel = new JButton("Cancel");
		findCancel.setActionCommand("findCancel"); 						// using different action command to not confuse other cancel action event
		findMatch = new JCheckBox("Match case");						
		upButton = new JRadioButton("Up");								// JRadioButton being created
		downButton = new JRadioButton("Down");					
		butGroup = new ButtonGroup();									// buttonGroup being created
		butGroup.add(upButton);
		butGroup.add(downButton); 										// adding JRadioButtons to buttongroup
		downButton.setSelected(true);									// downButton initially selected
		
		JPanel middlePanel = new JPanel(new FlowLayout());				// used to hold label and JTextField 
		JPanel lowerPanel = new JPanel(new GridLayout(1,2));			// used to hold check box and radio buttons 
		JPanel rightPanel = new JPanel(new GridLayout(3,1));			// used to hold Both JButtons on the right side of window
		JPanel upperRightPanel = new JPanel(new BorderLayout());		// used to hold Find Next button and place in rightPanel top location
		JPanel midRightPanel = new JPanel(new BorderLayout());			// used to hold Cancel button and place in rightPanel middle location
		JPanel radioPanel = new JPanel(new GridLayout(1,2));			// used to hold radio buttons and border around buttons 
		
		findText.addActionListener(this);								// adding action listeners
		findNextButton.addActionListener(this);
		findCancel.addActionListener(this);
		findMatch.addActionListener(this);	
		upButton.addActionListener(this);
		downButton.addActionListener(this);
		
		findDialog.getRootPane().setDefaultButton(findNextButton);  	// will press on enter key
		
		JLabel findWhatLabel = new JLabel("Find what: ");
		middlePanel.add(findWhatLabel);						// find what label added to panel
		middlePanel.add(findText);										// TextField added to panel
		findDialog.add(middlePanel, BorderLayout.PAGE_START);				// adding panel 
		
		lowerPanel.add(findMatch);										// adding checkBox to panel
		radioPanel.add(upButton);
		radioPanel.add(downButton);
		radioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Direction")); // adding border and Title to radiobuttons
		lowerPanel.add(radioPanel);									// adding radioButtons to panel
		findDialog.add(lowerPanel, BorderLayout.CENTER); 				// adding panel TO find Dialog window
		
		upperRightPanel.add(findNextButton, BorderLayout.CENTER);		// adding "Find Next" button to panel
		//upperRightPanel.add(new JLabel(" "), BorderLayout.PAGE_START);	// used for formating space around buttons
		upperRightPanel.add(new JLabel(" "), BorderLayout.LINE_START);	// used for formating space around buttons
		upperRightPanel.add(new JLabel(" "), BorderLayout.LINE_END);	// used for formating space around buttons
		upperRightPanel.add(new JLabel(" "), BorderLayout.PAGE_END);	// used for formating space around buttons
		
		midRightPanel.add(findCancel, BorderLayout.CENTER);				// adding "Center" button to panel
		//midRightPanel.add(new JLabel(" "), BorderLayout.PAGE_START);	// used for formating
		midRightPanel.add(new JLabel(" "), BorderLayout.LINE_START);	// used for formating
		midRightPanel.add(new JLabel(" "), BorderLayout.LINE_END);		// used for formating
		midRightPanel.add(new JLabel(" "), BorderLayout.PAGE_END);		// used for formating
		
		rightPanel.add(upperRightPanel);								// adding to rightPanel
		rightPanel.add(midRightPanel);				
		findDialog.add(rightPanel, BorderLayout.LINE_END); 				// adding to findDialog window
		
		matchIsSelected = false;										// originally match is not selected
		upButtonOn = false;												// originally down button is selected
		caretOnLeft = false;											// originally caret will be on total right side
																		// doesnt matter location until a word is found and
																		// highlighted and user wants to change UP/Down buttons
		
		findMatch.setMnemonic(KeyEvent.VK_C);							// setting mnemonic for match case key	
		findMatch.setDisplayedMnemonicIndex(6);							
	
		findWhatLabel.setDisplayedMnemonic(KeyEvent.VK_N);				// mnemonic for label will have to attach to text field 
		findWhatLabel.setLabelFor(findText); 							// attaching label to textfield
		
		upButton.setMnemonic(KeyEvent.VK_U);						
		downButton.setMnemonic(KeyEvent.VK_D);
		
		findNextButton.setMnemonic(KeyEvent.VK_F);
	
	
		
	}
	
	
	
	
	private void buildAboutDialog(){
		aboutDialog = new JDialog(this, "About JNotepad", true);					// making aboutDialog 
		aboutDialog.setSize(400,300);									// setting size of dialog window
		aboutDialog.setLayout(new BorderLayout()); 						// set layout 
		aboutDialog.setLocationRelativeTo(this);
		
		
		okay = new JButton("Ok");										// ok button on dialog
		aboutLabel = new JLabel("(c) Eric Schenck");					
		ImageIcon icon = new ImageIcon("JNotepad.png");
		
		aboutIcon = new JLabel(icon);									// adding JNotepad.png image to label
		
		JPanel topPan = new JPanel();									// default is FlowLayout()
		JPanel lowPan = new JPanel(new GridLayout(1,3));
		JPanel buttonPan = new JPanel(new BorderLayout());
		
		topPan.setLayout(new FlowLayout());
		topPan.add(aboutIcon);											// adding Icon to dialog
		topPan.add(aboutLabel);											// adding label 
		aboutDialog.add(topPan, BorderLayout.CENTER);					// addin pane to JDialog
		
		lowPan.add(new JLabel(""));
		lowPan.add(new JLabel(""));										// used to fill up blank spots to place button better
		
		buttonPan.add(okay, BorderLayout.CENTER);
		buttonPan.add(new JLabel(" "), BorderLayout.PAGE_END);
		buttonPan.add(new JLabel("   "), BorderLayout.LINE_END);			// used for formatting of the button
		
		lowPan.add(buttonPan);												// adding okay method
		
		aboutDialog.add(topPan, BorderLayout.CENTER);
		aboutDialog.add(lowPan, BorderLayout.PAGE_END);					// adding panels to dialog window
		
		okay.addActionListener(this); 									// adding listener to button
		aboutDialog.getRootPane().setDefaultButton(okay); 				// okay pressed upon enter key
		
		
		aboutDialog.setResizable(false); 								// not allowing user to resize window
		
		
	}
	
	
	
	
	
	// used to update the filename info each time it changes. I dont delete this JDialog, just hide and show as needed.
	private void updateLastSaveMenu(){
		closeMsgLabel.setText("  Do you want to save changes to " + fileName + "?");
		
	}
	
	private void buildLastSaveMenu(){

		closeMsg = new JDialog(this, "Notepad", true);
		closeMsg.setSize(425, 175);											// setting size of msg window
		closeMsg.setLocationRelativeTo(this);
		
		msgSave = new JButton("Save");										// creating new JButtons
		msgDontSave = new JButton("Don't Save");
		msgCancel = new JButton("Cancel");
		
		msgSave.addActionListener(this);									// adding action Listeners to JButtons
		msgDontSave.addActionListener(this);
		msgCancel.addActionListener(this);
		
		closeMsg.setLayout(new BorderLayout());								// setting to borderlayout
		
		closeMsgLabel = new JLabel("  Do you want to save changes to " + fileName + "?"); //msg displayed
		
		closeMsgLabel.setFont(new Font(closeMsgLabel.getFont().getFontName(), closeMsgLabel.getFont().getStyle(), 14));
		
		closeMsgLabel.setOpaque(true);  									// needed to paint JLabel
		closeMsgLabel.setBackground(Color.WHITE);							// setting background white
		closeMsgLabel.setForeground(Color.BLUE);
		
		
		JPanel topGrid = new JPanel(new BorderLayout());					// used for button formatting, nothing else
		JLabel blank1 = new JLabel(" ");
		JLabel blank2 = new JLabel(" ");									// formatting purposes only 
		JLabel blank3 = new JLabel("  ");									
		
		topGrid.add(blank1, BorderLayout.PAGE_START);
		topGrid.add(blank2, BorderLayout.PAGE_END);							// formatting buttons 
		topGrid.add(blank3, BorderLayout.LINE_END);
		
		msgGrid = new GridLayout(1,4);										// layout for JPanel
		
		menuPane = new JPanel(msgGrid);										// JPanel used to hold buttons on msgPrompt
		menuPane.add(new JLabel(""));										// used for blank first spot
		menuPane.add(msgSave);
		menuPane.add(msgDontSave);			
		menuPane.add(msgCancel);	
		
		msgGrid.setHgap(8);												// gap between buttons
		
		topGrid.add(menuPane, BorderLayout.CENTER);							// adding buttons to Pane
		closeMsg.add(topGrid, BorderLayout.PAGE_END); 						// adding pane to JDialog
		closeMsg.add(closeMsgLabel, BorderLayout.CENTER);					// adding to JDialog
		
		closeMsg.setResizable(false); 										// not allowing user to resize dialog window
		
		closeMsg.getRootPane().setDefaultButton(msgCancel);  				// default is cancel as a incase accident press 
		
	}
	
	
	
	private void buildMenu(){
		
		menuBar = new JMenuBar();											// creating menuBar
		
		file = new JMenu("File");											// JMenu file
		file.setMnemonic(KeyEvent.VK_F);                                              
		optionNew = new JMenuItem("New", KeyEvent.VK_N);
		file.add(optionNew);												// adding menuItem to menu
		open = new JMenuItem("Open...");									// creating menuItems
		file.add(open);
		save = new JMenuItem("Save");
		file.add(save);
		saveAs = new JMenuItem("Save As...");
		file.add(saveAs);													// adding menuItem to menu
		file.addSeparator();												// adding separator
		pageSetup = new JMenuItem("Page Setup...", KeyEvent.VK_U);
		file.add(pageSetup);
		print = new JMenuItem("Print...");
		file.add(print);
		file.addSeparator();
		exit = new JMenuItem("Exit", KeyEvent.VK_X);
		file.add(exit);														// adding menuItem to menu
		menuBar.add(file);													// adding menu to menuBar
		
		optionNew.addActionListener(this); 									// adding actionListener to MenuItems
		open.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		exit.addActionListener(this);
		
		pageSetup.setEnabled(false); 										// disabling unused menuItems
		print.setEnabled(false);
		
		edit = new JMenu("Edit");											// JMenu "Edit"
		edit.setMnemonic(KeyEvent.VK_E); 									// setting mnemonic
		undo = new JMenuItem("Undo");
		edit.add(undo);
		edit.addSeparator();
		cut = new JMenuItem("Cut");											// adding menuItem to menu
		edit.add(cut);
		copy = new JMenuItem("Copy");	
		edit.add(copy);
		paste = new JMenuItem("Paste");
		edit.add(paste);
		delete = new JMenuItem("Delete");									// creating menuItem
		edit.add(delete);													// adding menuItem to menu
		edit.addSeparator();												// adding separator
		find = new JMenuItem("Find...");
		edit.add(find);
		findNext = new JMenuItem("Find Next");								// creating menuItem
		edit.add(findNext);
		replace = new JMenuItem("replace");									// creating menuItem
		edit.add(replace);													// adding menuItem to menu
		goTo = new JMenuItem("Go To...");									// creating menuIem
		edit.add(goTo);
		edit.addSeparator();
		selectAll = new JMenuItem("Select All");							// creating menuItem
		edit.add(selectAll);
		timeDate = new JMenuItem("Time/Date");
		edit.add(timeDate);
		menuBar.add(edit);													// adding menu to MenuBar
		
		cut.addActionListener(this);										// adding ActionListeners to menuItems
		copy.addActionListener(this);
		paste.addActionListener(this);
		delete.addActionListener(this);
		find.addActionListener(this);
		findNext.addActionListener(this);
		selectAll.addActionListener(this);
		timeDate.addActionListener(this);
		
		undo.setEnabled(false); 											// setting disabled since not used
		replace.setEnabled(false);
		goTo.setEnabled(false);
		
		format = new JMenu("Format");										// Format Menu
		format.setMnemonic(KeyEvent.VK_O);
		wordWrap = new JCheckBoxMenuItem("Word Wrap");							// creating menuItem
		wordWrap.setMnemonic(KeyEvent.VK_W);
		format.add(wordWrap);												// adding menuItem to menu
		font = new JMenuItem("Font...", KeyEvent.VK_F);
		format.add(font);
		menuBar.add(format);												// adding menu to MenuBar
		
		wordWrap.addActionListener(this);  									// addign actionListener to menuItems
		font.addActionListener(this);
		
	
		view = new JMenu("View");											// View Menu
		view.setMnemonic(KeyEvent.VK_V);
		statusBar = new JMenuItem("Status Bar", KeyEvent.VK_S);				// creating new menuItem
		view.add(statusBar);
		menuBar.add(view);													// adding menu to MenuBar
	
		statusBar.setEnabled(false); 										// disabling menuItem
		
		help = new JMenu("Help");											// help Menu
		help.setMnemonic(KeyEvent.VK_H);
		viewHelp = new JMenuItem("View Help", KeyEvent.VK_H);				// creating new menuItem
		help.add(viewHelp);
		help.addSeparator();
		aboutJNotepad = new JMenuItem("About JNotepad");					// creaign new menuItem
		help.add(aboutJNotepad);											// adding menuItem to menu
		menuBar.add(help);													// adding menu to menuBar
		
		aboutJNotepad.addActionListener(this); 								// adding actionListener to menuitem
		
		viewHelp.setEnabled(false); 										// disabling viewHelp
		
		
		// setting shortcuts
		optionNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N , ActionEvent.CTRL_MASK));
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O , ActionEvent.CTRL_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S , ActionEvent.CTRL_MASK));
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P , ActionEvent.CTRL_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X , ActionEvent.CTRL_MASK));	
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C , ActionEvent.CTRL_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V , ActionEvent.CTRL_MASK));
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE , 0));
		find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F , ActionEvent.CTRL_MASK));
		replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H , ActionEvent.CTRL_MASK));
		goTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G , ActionEvent.CTRL_MASK));
		selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A , ActionEvent.CTRL_MASK));
		timeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5 , 0));
		findNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		
		cut.setEnabled(false);												// initially disabled until text is selected
		copy.setEnabled(false);												// initially disabled until text is selected
		delete.setEnabled(false);											// initially disabled until text is selected
		find.setEnabled(false);												// initially disabled until text exists in document
		findNext.setEnabled(false);											// initially disabled until text exists in document
		
		
		
		
		setJMenuBar(menuBar);												// setting menubar to frame
		
		
		
		
	}
	
	
	
	
	public static void main(String[] args){
		
		SwingUtilities.invokeLater(() -> {
		new JNotepad();	});
		
	}


	
	
}
