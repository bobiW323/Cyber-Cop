/**
 * Andrew ID: jintaoh
 * Name: Jintao Huang
 */
package hw3;

import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ModifyCaseView extends CaseView{

    ModifyCaseView(String header) {
        super(header);
      //set scene
        this.stage.setScene(new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT));
    }

    @Override
    Stage buildView() {
        
        stage.setTitle("Modify Case");
        updateButton.setText("Modify Case");
        titleTextField.setText(CyberCop.currentCase.getCaseTitle());
        caseTypeTextField.setText(CyberCop.currentCase.getCaseType());
        int year = Integer.parseInt(CyberCop.currentCase.getCaseDate().split("-")[0]);
        int month = Integer.parseInt(CyberCop.currentCase.getCaseDate().split("-")[1]);
        int day = Integer.parseInt(CyberCop.currentCase.getCaseDate().split("-")[2]);
        caseDatePicker.setValue(LocalDate.of(year, month, day));
        caseNumberTextField.setText(CyberCop.currentCase.getCaseNumber());
        categoryTextField.setText(CyberCop.currentCase.getCaseCategory());
        caseLinkTextField.setText(CyberCop.currentCase.getCaseLink());
        caseNotesTextArea.setText(CyberCop.currentCase.getCaseNotes());
        return stage;
    }
    

}
