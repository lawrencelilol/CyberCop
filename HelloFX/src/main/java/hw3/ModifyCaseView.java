// Tiantong Li (tiantonl)

package hw3;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;

// Build the caseview window when modifies a old case

public class ModifyCaseView extends CaseView{

    ModifyCaseView(String header) {
        super(header);
    }

    @Override
    Stage buildView() {
        stage.setTitle("Modify Case");
        updateButton.setText("Modify Case");
        caseDatePicker.setValue(LocalDate.now());
        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene);
        return stage;
    }
}
