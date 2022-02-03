// Tiantong Li (tiantonl)

package hw3;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "CyberCop.html"; //local HTML
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
		primaryStage.setTitle(APP_TITLE);
		cyberCopRoot = ccView.setupScreen();
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getClassLoader().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		//write your code here
		ccView.saveFileMenuItem.setDisable(true);
		ccView.closeFileMenuItem.setDisable(true);
		ccView.addCaseMenuItem.setDisable(true);
		ccView.modifyCaseMenuItem.setDisable(true);
		ccView.deleteCaseMenuItem.setDisable(true);

		ccView.openFileMenuItem.setOnAction(new OpenFileMenuItemHandler());
		ccView.saveFileMenuItem.setOnAction(new SaveFileMenuItemHandler());
		ccView.caseCountChartMenuItem.setOnAction(new CaseCountChartMenuItemHandler());

		ccView.closeFileMenuItem.setOnAction(new CloseFileMenuItemHandler());
		ccView.searchButton.setOnAction(new SearchButtonHandler());
		ccView.clearButton.setOnAction(new ClearButtonHandler());

		// Exits the application
		ccView.exitMenuItem.setOnAction(actionEvent -> Platform.exit());

		Collections.reverse(ccModel.caseList);

		// Add a listener when the user clicks on the table chooses a case as currentCase
		ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
			if(newValue != null) {
				currentCase = newValue;
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0,4));
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());

				//The following is some helper code to display web-pages.
				if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
					URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
					ccView.webEngine.load(currentCase.getCaseLink());
				} else {
					URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				}
			}
		}));
	}

	/* Opens dialog box for user to select a case file.
	Invokes ccModel's readCases() and buildMapAndList() methods to populate caseList and yearList.
	Select first record in caseTableView AND MAKES currentCase point to it.
	It updates stage title and messageLabel
	*/
	public class OpenFileMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("All","*.*"));

			File file;
			if((file = fileChooser.showOpenDialog(stage)) != null) {
				ccView.isFileOpen.setValue(true);
				ccView.closeFileMenuItem.setDisable(false);
				ccView.openFileMenuItem.setDisable(true);
				TSVCaseReader.invalidCases = 0;

				ccModel.readCases(file.getAbsolutePath());
				ccModel.buildYearMapAndList();
				Collections.sort(ccModel.yearList);
				Collections.reverse(ccModel.caseList);

				// Set initial view of Open File
				currentCase = ccModel.caseList.get(0);
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0,4));
				ccView.yearComboBox.setItems(ccModel.yearList);
				ccView.caseTableView.setItems(ccModel.caseList);

				//Update stage title and message label
				String fileName = file.getName();
				stage.setTitle("Cyber Cop: " + fileName);
				ccView.messageLabel.setText(ccModel.caseList.size() +" cases");

				//set HTML
				if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
					URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				} else if (currentCase.getCaseLink().toLowerCase().trim().startsWith("http")){  //if external link
					ccView.webEngine.load(currentCase.getCaseLink());
				} else {
					URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				}

				if(ccView.isFileOpen.getValue()) {
					ccView.addCaseMenuItem.setDisable(false);
					ccView.modifyCaseMenuItem.setDisable(false);
					ccView.deleteCaseMenuItem.setDisable(false);
					ccView.saveFileMenuItem.setDisable(false);

					ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
					ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
					ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());
				} else {
					ccView.addCaseMenuItem.setDisable(true);
					ccView.modifyCaseMenuItem.setDisable(true);
					ccView.deleteCaseMenuItem.setDisable(true);
				}
			}
		}
	}

	// Clears all the GUI elements and sets isFileOpen false
	public class CloseFileMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// Clear all GUI elements
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.caseNotesTextArea.clear();
			ccView.yearComboBox.valueProperty().set(null);
			ccView.caseTableView.getItems().clear();

			URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);
			ccView.webEngine.load(url.toExternalForm());

			//Update stage title and message label
			stage.setTitle(APP_TITLE);
			ccView.isFileOpen.setValue(false);
			ccView.closeFileMenuItem.setDisable(true);
			ccView.openFileMenuItem.setDisable(false);
			ccView.saveFileMenuItem.setDisable(true);

			ccView.addCaseMenuItem.setDisable(true);
			ccView.modifyCaseMenuItem.setDisable(true);
			ccView.deleteCaseMenuItem.setDisable(true);

		}
	}


	public class SearchButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			List<Case> foundCases = new ArrayList<>();
			String title = ccView.titleTextField.getText();
			String type = ccView.caseTypeTextField.getText();
			String year = ccView.yearComboBox.getValue();
			String number = ccView.caseNumberTextField.getText();
			foundCases = ccModel.searchCases(title,type,year,number);


			if(foundCases.size() == 0) {
				ObservableList<Case> fc = FXCollections.observableArrayList(foundCases);
				ccView.caseTableView.setItems(fc);
				ccView.titleTextField.setText("");
				ccView.caseTypeTextField.setText("");
				ccView.caseNumberTextField.setText("");
				ccView.caseNotesTextArea.setText("");
				ccView.messageLabel.setText("Found 0 case");
				
			} else {
				currentCase = foundCases.get(0);
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0,4));

				ccView.messageLabel.setText(foundCases.size() +" cases");

				if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
					URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				} else if (currentCase.getCaseLink().toLowerCase().trim().startsWith("http")){  //if external link
					ccView.webEngine.load(currentCase.getCaseLink());
				} else {
					URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				}

				ObservableList<Case> fc = FXCollections.observableArrayList(foundCases);
				ccView.caseTableView.setItems(fc);
			}
		}
	}

	// Clears the data entered in titleTextField, caseTypeTextField, yearComboBox and caseNumberTextField
	public class ClearButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.yearComboBox.valueProperty().set(null);
		}
	}

	// A handler for Add case, Modify case, Delete case
	// Depending on which menu item was chosen, it creates an instance of Addview, modifyView, or DeleteView
	// Also creates an instance of AddButtonHandler, ModifyButtonHandler or DeleteButtonHandler, and binds it
	// to caseView's updateButton.
	// The clearButton clears all data from the view
	// The closeButton closes its stage
	public class CaseMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			MenuItem choice = (MenuItem)event.getSource();
			String choiceText = choice.getText();

			if(choiceText.equals("Add case")) {
				caseView = new AddCaseView("Add Case");
				stage = caseView.buildView();
				caseView.stage.show();

				caseView.updateButton.setOnAction(new AddButtonHandler());
				ccView.messageLabel.setText(ccModel.caseList.size() + "cases");
				caseView.closeButton.setOnAction(actionEvent -> caseView.stage.close());
				caseView.clearButton.setOnAction(actionEvent ->{caseView.titleTextField.clear();
					caseView.caseTypeTextField.clear();
					caseView.caseDatePicker.setValue(null);
					caseView.caseNumberTextField.clear();
					caseView.categoryTextField.clear();
					caseView.caseLinkTextField.clear();
					caseView.caseNotesTextArea.clear();});

			} else if(choiceText.equals("Modify case")) {
				caseView = new ModifyCaseView("Modify Case");
				stage = caseView.buildView();
				caseView.stage.show();

				String title = currentCase.getCaseTitle();
				String date = currentCase.getCaseDate();
				String type = currentCase.getCaseType();
				String number = currentCase.getCaseNumber();
				String category = currentCase.getCaseCategory();
				String link = currentCase.getCaseLink();
				String note = currentCase.getCaseNotes();

				caseView.titleTextField.setText(title);
				caseView.caseTypeTextField.setText(type);
				caseView.caseNotesTextArea.setText(note);
				caseView.caseNumberTextField.setText(number);
				caseView.categoryTextField.setText(category);
				caseView.caseLinkTextField.setText(link);
				caseView.caseDatePicker.setValue(LocalDate.parse(date));

				caseView.updateButton.setOnAction(new ModifyButtonHandler());
				caseView.closeButton.setOnAction(actionEvent -> caseView.stage.close());

				caseView.clearButton.setOnAction(actionEvent ->{caseView.titleTextField.clear();
					caseView.caseTypeTextField.clear();
					caseView.caseDatePicker.setValue(null);
					caseView.caseNumberTextField.clear();
					caseView.categoryTextField.clear();
					caseView.caseLinkTextField.clear();
					caseView.caseNotesTextArea.clear();});

			} else if(choiceText.equals("Delete case")) {
				caseView = new DeleteCaseView("Delete Case");
				stage = caseView.buildView();
				caseView.stage.show();

				caseView.updateButton.setOnAction(new DeleteButtonHandler());
				caseView.closeButton.setOnAction(actionEvent -> caseView.stage.close());

				caseView.clearButton.setOnAction(actionEvent ->{caseView.titleTextField.clear();
					caseView.caseTypeTextField.clear();
					caseView.caseDatePicker.setValue(null);
					caseView.caseNumberTextField.clear();
					caseView.categoryTextField.clear();
					caseView.caseLinkTextField.clear();
					caseView.caseNotesTextArea.clear();});
			}
		}
	}

	// Add the new case to caseList and messageLabel is updated
	public class AddButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			String title = caseView.titleTextField.getText();
			String date = caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String type = caseView.caseTypeTextField.getText();
			String number = caseView.caseNumberTextField.getText();
			String category = caseView.categoryTextField.getText();
			String link = caseView.caseLinkTextField.getText();
			String note = caseView.caseNotesTextArea.getText();

			Case newCase = new Case(date, title, type, number, link, category, note);

			// Get all the unique case number
			StringBuilder uniqueCaseNumber = new StringBuilder();
			for(Case c: ccModel.caseList) {
				if(!uniqueCaseNumber.toString().contains(c.getCaseNumber())) {
					uniqueCaseNumber.append(c.getCaseNumber() + "\n");
				}
			}

			// Check to see if the newCase has missing values or duplicate case number
			// else add the new case to caseList
			try{
				if(newCase.getCaseDate().trim().equals("") || newCase.getCaseTitle().trim().equals("") || newCase.getCaseType().trim().equals("")
						|| newCase.getCaseNumber().trim().equals("")) {
					throw new DataException("Missing Case Error");

				} else if (uniqueCaseNumber.toString().contains(newCase.getCaseNumber())) {
					throw new DataException("Duplicate Case");
				} else {
					// Add new Case to caseList
					if(!ccModel.caseList.contains(newCase)) {
						ccModel.caseList.add(newCase);
					}
				}
			} catch (DataException e1) {
				System.out.println("Data Exception While Adding");
			}


			// Get all the unique years
			StringBuilder uniqueYearLists = new StringBuilder();
			for(String year: ccModel.yearList) {
				if(!uniqueYearLists.toString().contains(year)) {
					uniqueYearLists.append(year);
				}
			}
			if(!uniqueYearLists.toString().contains(date.substring(0,4))) {
				ccModel.yearList.add(date.substring(0,4));
			}
			ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
		}
	}

	// Takes the data from all GUI controls and updates currentCase's properties
	// that they are updated in the main screen's view
	public class ModifyButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			String title = caseView.titleTextField.getText();
			String date = caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String type = caseView.caseTypeTextField.getText();
			String number = caseView.caseNumberTextField.getText();
			String category = caseView.categoryTextField.getText();
			String link = caseView.caseLinkTextField.getText();
			String note = caseView.caseNotesTextArea.getText();

			Case modifiedCase = new Case(date,title,type,number,link,category,note);

			// Get all the unique case number
			StringBuilder uniqueCaseNumber = new StringBuilder();
			for(Case c: ccModel.caseList) {
				if(!uniqueCaseNumber.toString().contains(c.getCaseNumber())) {
					uniqueCaseNumber.append(c.getCaseNumber() + "\n");
				}
			}

			try{
				if(modifiedCase.getCaseDate().trim().equals("") || modifiedCase.getCaseTitle().trim().equals("") || modifiedCase.getCaseType().trim().equals("")
						|| modifiedCase.getCaseNumber().trim().equals("")) {
					throw new DataException("Missing Case Error");
				} if (uniqueCaseNumber.toString().contains(number) && !number.equals(currentCase.getCaseNumber())) {
					throw new DataException("Duplicate Case");
				} else {
					// Update caseList
					int index = ccModel.caseList.indexOf(currentCase);
					ccModel.caseList.set(index,modifiedCase);
				}
			} catch (DataException e) {
				System.out.println("Data Exception While Modifying");
			}



			// update yearList
			StringBuilder uniqueYearLists = new StringBuilder();
			for(String year: ccModel.yearList) {
				if(!uniqueYearLists.toString().contains(year)) {
					uniqueYearLists.append(year);
				}
			}
			if(!uniqueYearLists.toString().contains(date.substring(0,4))) {
				ccModel.yearList.add(date.substring(0,4));
			}
		}
	}

	// Removes the currentCase from caseMap and caseList and updates messageLabel.
	public class DeleteButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			Case temp = ccModel.caseList.get(ccModel.caseList.size() - 1);
			ccModel.caseList.remove(currentCase);
			ccModel.caseMap.remove(currentCase);
			ccModel.yearList.remove(currentCase.getCaseDate().substring(0,4));
			ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
			currentCase = temp;
		}
	}

	// Opens the file-dialog box in DEFAULT_PATH for user to enter a filename for the file in which to save the data.
	// It then invokes ccModel's writeCase() method.
	// If the writeCase() method returns true, it displays the "filename saved" message on messageLabel.

	public class SaveFileMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent actionEvent) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save file");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));

			File file = fileChooser.showSaveDialog(stage);

			String fileName = file.getAbsolutePath();

			boolean check = ccModel.writeCases(fileName);

			if(check) {
				ccView.messageLabel.setText(file.getName() + " saved");
			}
		}
	}

	public class CaseCountChartMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent actionEvent) {
			ccView.showChartView(ccModel.yearMap);
		}
	}
}

