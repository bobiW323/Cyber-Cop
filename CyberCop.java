/**
 * Andrew ID: jintaoh
 * Name: Jintao Huang
 */
package hw3;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.

	public static void main(String[] args) {
		launch(args);
	}

	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
	    
	    currentCase = ccView.caseTableView.getSelectionModel().getSelectedItem();
        //update currentCase with the selected row in case table view
        ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue != null) {
                for (Case item : ccView.caseTableView.getItems()) {
                    if (item.getCaseTitle().equalsIgnoreCase(newValue.getCaseTitle())) {
                        ccView.titleTextField.setText(newValue.getCaseTitle());
                        ccView.caseTypeTextField.setText(newValue.getCaseType());
                        ccView.yearComboBox.setValue(newValue.getCaseDate().split("-")[0]);
                        ccView.caseNumberTextField.setText(newValue.getCaseNumber());
                        ccView.caseNotesTextArea.setText(newValue.getCaseNotes());
                        currentCase = newValue;
                    }
                }
            }            
            if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
                URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
                if (url != null) ccView.webEngine.load(url.toExternalForm());
            } else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
                ccView.webEngine.load(currentCase.getCaseLink());
            } else {
                URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
                if (url != null) ccView.webEngine.load(url.toExternalForm());
            }
            
            
        });
	    
		//binds all GUI components to their handlers
	    ccView.openFileMenuItem.setOnAction(new OpenFileMenuItemHandler());
	    ccView.saveFileMenuItem.setOnAction(new SaveFileMenuItemHandler());
	    ccView.closeFileMenuItem.setOnAction(new CloseFileMenuItemHandler());
	    ccView.exitMenuItem.setOnAction(new ExitMenuItemHandler());
	    ccView.searchButton.setOnAction(new SearchButtonHandler());
	    ccView.clearButton.setOnAction(new ClearButtonHandler());
	    ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
	    ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
	    ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());
	    ccView.caseCountChartMenuItem.setOnAction(new CaseCountChartMenuItemHandler());
	    
	    //binds disableProperty of menu items and text-fields with ccView.isFileOpen so that they are enabled as needed
	    ccView.openFileMenuItem.disableProperty().bind(ccView.isFileOpen);
	    ccView.closeFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.saveFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.titleTextField.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.caseTypeTextField.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.yearComboBox.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.caseNumberTextField.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.searchButton.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.clearButton.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.addCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.modifyCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.deleteCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
	    ccView.caseCountChartMenuItem.disableProperty().bind(ccView.isFileOpen.not());
	  
	}
	class OpenFileMenuItemHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
            
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Select file");
            filechooser.setInitialDirectory(new File(DEFAULT_PATH));
            
            filechooser.getExtensionFilters().addAll(new ExtensionFilter("TSV Files", "*.tsv"), new ExtensionFilter("CSV Files", "*.csv"), new ExtensionFilter("All Files", "*.*"));
            ccView.isFileOpen.setValue(true);
            File datafile = null;
            if ((datafile = filechooser.showOpenDialog(stage)) != null) {
                
            ccModel.readCases(datafile.getAbsolutePath());
            ccModel.buildYearMapAndList();
            currentCase = ccModel.caseList.get(0);
            ccView.titleTextField.setText(currentCase.getCaseTitle());
            ccView.caseTypeTextField.setText(currentCase.getCaseType());
            ccView.yearComboBox.setValue(currentCase.getCaseDate().split("-")[0]);
            ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
            ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
            ccView.caseTableView.setItems(ccModel.caseList);
            ccView.yearComboBox.setItems(ccModel.yearList);
            
            stage.setTitle("Cyber Cop: " + datafile.getName());
            ccView.messageLabel.setText(ccModel.caseList.size()+" cases");
            
            }
        }
	    
	}
	class SaveFileMenuItemHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file");
            fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                ccModel.writeCases(file.getName());
                if (ccModel.writeCases(file.getName()) == true) {
                   ccView.messageLabel.setText(file.getName()+ " saved");
                }
            }
        }
        
    }
	class CloseFileMenuItemHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
            ccView.titleTextField.clear();          
            ccView.caseTypeTextField.clear();
            ccView.yearComboBox.getItems().clear();
            ccView.caseNumberTextField.clear();
            ccView.caseTableView.getItems().clear();
            ccView.caseNotesTextArea.clear();
            ccView.isFileOpen.setValue(false);
        }
	    
	}
	class ExitMenuItemHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
            Platform.exit();
            
        }
	    
	}
	class SearchButtonHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
        List<Case> resultlist = ccModel.searchCases(ccView.titleTextField.getText(), ccView.caseTypeTextField.getText(), ccView.yearComboBox.getValue(), ccView.caseNumberTextField.getText());   
        if (resultlist.size() > 0) {
        ccModel.caseList = FXCollections.observableList(resultlist);
        currentCase = resultlist.get(0);
        ccView.titleTextField.setText(currentCase.getCaseTitle());
        ccView.caseTypeTextField.setText(currentCase.getCaseType());
        ccView.yearComboBox.setValue(currentCase.getCaseDate().split("-")[0]);
        ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
        ccView.messageLabel.setText(resultlist.size() + " cases");
        ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
        
        ccView.caseTableView.setItems(ccModel.caseList);
            }
        else {
            currentCase = ccModel.caseList.get(0);
            ccView.titleTextField.setText(currentCase.getCaseTitle());
            ccView.caseTypeTextField.setText(currentCase.getCaseType());
            ccView.yearComboBox.setValue(currentCase.getCaseDate().split("-")[0]);
            ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
            ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
            ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
            
            ccView.caseTableView.setItems(ccModel.caseList);
            
            }
        }
       
            
	}
	    

	class ClearButtonHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
            ccView.titleTextField.setText(null);
            ccView.caseTypeTextField.setText(null);
            ccView.yearComboBox.setValue(null);
            ccView.caseNumberTextField.setText(null);
            ccView.caseNotesTextArea.setText(null);
            ccView.messageLabel.setText("0 cases");
        }
	    
	}
	
	class CaseMenuItemHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
            //switch eventhandlers according to the source menu item
            if (arg0.getSource().equals(ccView.addCaseMenuItem)) { 
                    caseView = new AddCaseView("Add Case");
                    caseView.updateButton.setOnAction(new AddButtonHandler());
            }
            else if (arg0.getSource().equals(ccView.modifyCaseMenuItem)) {     
                    caseView = new ModifyCaseView("Modify Case");               
                    caseView.updateButton.setOnAction(new ModifyButtonHandler());
            }
            else if (arg0.getSource().equals(ccView.deleteCaseMenuItem)) {                
                    caseView = new DeleteCaseView("Delete Case");
                    caseView.updateButton.setOnAction(new DeleteButtonHandler());
            }
            
            caseView.clearButton.setOnAction((event1) -> {
                caseView.titleTextField.setText(null);
                caseView.caseDatePicker.setValue(null);
                caseView.caseTypeTextField.setText(null);
                caseView.caseNumberTextField.setText(null);
                caseView.categoryTextField.setText(null);
                caseView.caseLinkTextField.setText(null);
                caseView.caseNotesTextArea.setText(null);
            });
            caseView.closeButton.setOnAction((event2) -> {
                caseView.stage.close();
            });                    
            caseView.buildView().show();
            
                      
        }
	    
	}
	class CaseCountChartMenuItemHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
            ccView.showChartView(ccModel.yearMap);
        }
	    
	}
	
	class AddButtonHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
            //reject cases with some null values
            try {
            if (caseView.titleTextField.getText().equals("") || caseView.caseTypeTextField.getText().equals("")
                    || caseView.caseNumberTextField.getText().equals("")) {
                String message = "Case must have date, title, type, and number";
                throw new DataException(message);
            }
            } catch (DataException d){
                return;
            }
            //reject cases with existing case number 
            try {
            String newNumber = caseView.caseNumberTextField.getText().trim();
            Iterator<Case> cs = ccModel.caseList.iterator();
            while (cs.hasNext()) {
                 String caseNum = cs.next().getCaseNumber();
                 if (caseNum.equals(newNumber)) {
                     String message = "Duplicate case number";
                     throw new DataException(message); 
                 }     
             }
            } catch (DataException d){
                return;
            }
            //create new case 
           Case newcase = new Case(caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 
                   caseView.titleTextField.getText(),
                   caseView.caseTypeTextField.getText(),
                   caseView.caseNumberTextField.getText(),
                   caseView.caseLinkTextField.getText(),
                   caseView.categoryTextField.getText(),
                   caseView.caseNotesTextArea.getText());
           ccModel.caseList.add(newcase);
           ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
           
            
        }
	    
	}
	
	class ModifyButtonHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
            try {
                if (caseView.titleTextField.getText().equals("") || caseView.caseTypeTextField.getText().equals("")
                        || caseView.caseNumberTextField.getText().equals("")) {
                    String message = "Case must have date, title, type, and number";
                    throw new DataException(message); 
                }
            } catch (DataException d){
                return;
            }
            try {
                String newNumber = caseView.caseNumberTextField.getText().trim();
                Iterator<Case> cs = ccModel.caseList.iterator();
                while (cs.hasNext()) {
                     String caseNum = cs.next().getCaseNumber();
                     if (caseNum.equals(newNumber)) {
                         String message = "Duplicate case number";
                         throw new DataException(message); 
                     }     
                 } 
            } catch (DataException d){
                return;
            }
            currentCase.setCaseDate(caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            currentCase.setCaseTitle(caseView.titleTextField.getText());
            currentCase.setCaseType(caseView.caseTypeTextField.getText());
            currentCase.setCaseNumber(caseView.caseNumberTextField.getText());
            currentCase.setCaseLink(caseView.caseLinkTextField.getText());
            currentCase.setCaseCategory(caseView.categoryTextField.getText());
            currentCase.setCaseNotes(caseView.caseNotesTextArea.getText());
            
        }
	    
	}
	
	class DeleteButtonHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent arg0) {
            ccModel.caseList.remove(currentCase);
            ccModel.caseMap.remove(currentCase.getCaseNumber(),currentCase);
            ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
        }
	    
	}
}

    

