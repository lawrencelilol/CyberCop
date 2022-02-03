// Tiantong Li (tiantonl)

package hw3;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;

// Creates the case view window when deletes a case
public class DeleteCaseView extends CaseView{
    DeleteCaseView(String header) {
        super(header);
    }

    @Override
    Stage buildView() {
        stage.setTitle("Delete Case");
        updateButton.setText("Delete Case");

        Case c = CyberCop.currentCase;

        String title = c.getCaseTitle();
        String type = c.getCaseType();
        String number = c.getCaseNumber();
        String category = c.getCaseCategory();
        String link = c.getCaseLink();
        String note = c.getCaseNotes();

        titleTextField.setText(title);
        caseTypeTextField.setText(type);
        caseNotesTextArea.setText(note);
        caseNumberTextField.setText(number);
        categoryTextField.setText(category);
        caseLinkTextField.setText(link);
        caseDatePicker.setValue(LocalDate.now());

        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);

        stage.setScene(scene);

        return stage;
    }
}
