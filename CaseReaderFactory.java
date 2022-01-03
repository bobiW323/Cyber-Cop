/**
 * Andrew ID: jintaoh
 * Name: Jintao Huang
 */
package hw3;

public class CaseReaderFactory {
    CaseReader createReader(String filename) {
    CaseReader cr = null; 
        if(filename.contains(".csv"))
             cr = new CSVCaseReader(filename);
        else if (filename.contains(".tsv"))
             cr = new TSVCaseReader(filename);
        return cr;
    }

}
