package mortgagecalculator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/** MortgageCalculator takes three user inputs - principal, interest, and loan term, and calculates monthly mortgage.
 * It also uses one custom exception InputOutOfRangeException
 */

public class MortgageCalculator extends Application{
	GridPane root = new GridPane();
	TextField principalTextField = new TextField();		//takes principal input
	TextField interestTextField = new TextField();		//takes interest rate input
	TextField termTextField = new TextField();		//takes loan term input
	Label mortgageValue = new Label("");				//used to display mortgage result value

	double principal, interest, term;					//to hold numeric values

	@Override
	public void start(Stage primaryStage) throws Exception {
		setupScene();
		Scene scene = new Scene(root, 350, 175);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Mortgage Calculator");
		primaryStage.show();		
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setupScene() {
		Label principalLabel = new Label("Principal amount $10k-1m");
		Label interestLabel = new Label("Interest rate 0-25%");
		Label termLabel = new Label("Loan term 15-30 years");

		Button calculateButton = new Button ("Calculate");
		Button clearButton = new Button("Clear");

		root.add(principalLabel, 0, 0);
		root.add(interestLabel, 0, 1);
		root.add(termLabel, 0, 2);
		root.add(principalTextField, 1, 0);
		root.add(interestTextField, 1, 1);
		root.add(termTextField, 1, 2);
		
		root.add(mortgageValue, 0, 3, 2, 1);
		
		root.add(calculateButton, 0, 4);
		calculateButton.setPrefWidth(100);
		calculateButton.setOnAction(new CalculateHandler());

		root.add(clearButton, 1, 4);
		clearButton.setPrefWidth(100);
		clearButton.setOnAction(new ClearHandler());

		root.setHgap(5);
		root.setVgap(5);
		root.setPadding(new Insets(25,25,25,25));
	}

	private class CalculateHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//reset font colors to normal and clear previous values in mortgageValue label if any.
			principalTextField.setStyle("-fx-text-inner-color: black;");
			interestTextField.setStyle("-fx-text-inner-color: black;");
			termTextField.setStyle("-fx-text-inner-color: black;");
			mortgageValue.setText("");

			//validate input and then calculate mortgage
			if (processInput()) {
				double mortgage = calculateMortgage(principal, interest, term);
				mortgageValue.setText(String.format("Monthly mortgage is $%,.2f", mortgage));
			}
		}
	}
	
	private double calculateMortgage(double principal, double rate, double term) {
		double r = rate/1200;
		double t = -term*12;
		double m = principal * r / (1 - Math.pow(1 + r, t));
		return m;
	}

	//validates three input values. 
	//Returns false if any of the three values are invalid
	//Ensures that the numbers are of type double
	//Also ensures that the numbers are in correct range. If not, throws InputOutOfRangeException	
	
	public boolean processInput() {
		//process principal
		try {
			principal = Double.parseDouble(principalTextField.getText().trim());
			if (principal < 10000 || principal > 1000000) {  //handle it here
				principalTextField.setStyle("-fx-text-inner-color: red;");
				mortgageValue.setText("Invalid principal amount. Enter between $10k and $1m");
				return false;
			};
		} catch (NumberFormatException e) {
			principalTextField.setStyle("-fx-text-inner-color: red;");
			mortgageValue.setText("Please enter numeric value only" );
			return false;
		} 
		//process interest rate
		try {
			interest = Double.parseDouble(interestTextField.getText());
			if (interest < 0 || interest > 25) throw new InputOutOfRangeException();	 
		} catch (NumberFormatException e) {
			mortgageValue.setText("Please enter numeric value only" );
			interestTextField.setStyle("-fx-text-inner-color: red;");
			return false;
		} catch (InputOutOfRangeException e1) {
			interestTextField.setStyle("-fx-text-inner-color: red;");
			mortgageValue.setText("Invalid interest rate. Enter between 0 and 25%");
			return false;
		}	
		//process loan term
		try {
			term = Double.parseDouble(termTextField.getText());
			if (term < 15 || term > 30) throw new InputOutOfRangeException();
			return true;
		} catch (NumberFormatException e) {
			mortgageValue.setText("Please enter numeric value only" );
			mortgageValue.setStyle("-fx-text-inner-color: red;");
			return false;
		} catch (InputOutOfRangeException e1) {
			termTextField.setStyle("-fx-text-inner-color: red;");
			mortgageValue.setText("Enter numeric value between 15 and 30 years");
			return false;
		}
	}

	private class ClearHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			principalTextField.clear();
			interestTextField.clear();
			termTextField.clear();
			principalTextField.setStyle("-fx-text-inner-color: black;");
			interestTextField.setStyle("-fx-text-inner-color: black;");
			termTextField.setStyle("-fx-text-inner-color: black;");
			mortgageValue.setText("");
		}
	}
}
