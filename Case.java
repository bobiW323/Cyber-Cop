/**
 * Andrew ID: jintaoh
 * Name: Jintao Huang
 */
package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case>{
    private StringProperty caseDate = new SimpleStringProperty();
    private StringProperty caseTitle = new SimpleStringProperty();
    private StringProperty caseType = new SimpleStringProperty();
    private StringProperty caseNumber = new SimpleStringProperty();
    private StringProperty caseLink = new SimpleStringProperty();
    private StringProperty caseCategory = new SimpleStringProperty();
    private StringProperty caseNotes = new SimpleStringProperty();
    
    
    Case(String caseDate, String caseTitle, String caseType, String caseNumber, String caseLink, String caseCategory, String caseNotes){
        this.caseDate.set(caseDate);
        this.caseTitle.set(caseTitle);
        this.caseType.set(caseType);
        this.caseNumber.set(caseNumber);
        this.caseLink.set(caseLink);
        this.caseCategory.set(caseCategory);
        this.caseNotes.set(caseNotes);
    }
    
    public String getCaseDate() {
        return caseDate.get();
    }
    public void setCaseDate(String date) {
       this.caseDate.set(date); 
    }
    public final StringProperty caseDateProperty() {
        return caseDate;
    }
    
    public String getCaseTitle() {
        return caseTitle.get();
    }
    public void setCaseTitle(String title) {
        this.caseTitle.set(title);
    }
    public final StringProperty caseTitleProperty() {
        return caseTitle;
    }
    
    public String getCaseType() {
        return caseType.get();
    }
    public void setCaseType(String type) {
        this.caseType.set(type);;
    }
    public final StringProperty caseTypeProperty() {
        return caseType;
    }
    
    public String getCaseNumber() {
        return caseNumber.get();
    }
    public void setCaseNumber(String number) {
        this.caseNumber.set(number);;
    }
    public final StringProperty caseNumberProperty() {
        return caseNumber;
    }
    
    public String getCaseLink() {
        return caseLink.get();
    }
    public void setCaseLink(String link) {
        this.caseLink.set(link);;
    }
    public final StringProperty caseLinkProperty() {
        return caseLink;
    }
    
    public String getCaseCategory() {
        return caseCategory.get();
    }
    public void setCaseCategory(String category) {
        this.caseCategory.set(category);;
    }
    public final StringProperty caseCategoryProperty() {
        return caseCategory;
    }
    
    public String getCaseNotes() {
        return caseNotes.get();
    }
    public void setCaseNotes(String notes) {
        this.caseNotes.set(notes);
    }
    public final StringProperty caseNotesProperty() {
        return caseNotes;
    }

    @Override
    public String toString() {
        return caseNumber.get();
    }
    @Override
    public int compareTo(Case casee) {
        return casee.caseDate.get().compareTo(this.caseDate.get());
    }
    
    
}
