/**
 * Andrew ID: jintaoh
 * Name: Jintao Huang
 */
package hw3;

import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddCaseView extends CaseView{

    AddCaseView(String header) {
        super(header);
        //set scene
        this.stage.setScene(new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT));
    }

    @Override
    Stage buildView() {
        updateButton.setText("Add Case");
        caseDatePicker.setValue(LocalDate.now());
        return stage;
    }

}
