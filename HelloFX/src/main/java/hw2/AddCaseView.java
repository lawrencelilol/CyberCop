// Tiantong Li (tiantonl)

package hw2;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;


// Builds the CaseView when adding a new case
public class AddCaseView extends CaseView{
    AddCaseView(String header) {
        super(header);
    }

    // buildView() method returns a Stage with empty GUI controls for a case
    @Override
    Stage buildView() {
        stage.setTitle("Add Case");
        updateButton.setText("Add Case");
        caseDatePicker.setValue(LocalDate.now());
        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene);

        return stage;
    }
}
