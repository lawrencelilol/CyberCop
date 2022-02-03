// Tiantong Li (tiantonl)

package hw3;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DataException extends RuntimeException {

    DataException(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Data Error");
        if(message.equals("TSV file missing data")) {
            int caseNumber = TSVCaseReader.invalidCases;
            alert.setContentText(caseNumber + " cases rejected.\n The file must have cases with \n tab separated date, title, type and case number!");
            alert.showAndWait();
        } else if(message.equals("Missing Case Error")) {

            alert.setContentText("Case must have date, title, type, and number");
            alert.showAndWait();
        } else if(message.equals("Duplicate Case")) {
            alert.setContentText("Duplicate case number");
            alert.showAndWait();
        }
    }

}
