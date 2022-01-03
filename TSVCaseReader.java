/**
 * Andrew ID: jintaoh
 * Name: Jintao Huang
 */
package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TSVCaseReader extends CaseReader{

    TSVCaseReader(String filename) {
        super(filename);
        
    }

    @Override
    List<Case> readCases() {
        List<Case> caseList = new ArrayList<>();
        int count = 0;
        try {
            Scanner tsvfile = new Scanner(new File(filename));
            while(tsvfile.hasNextLine()) {
                String[] tsvline = tsvfile.nextLine().split("\t");
                if (tsvline[0].trim().equals("") || tsvline[1].trim().equals("") || tsvline[2].trim().equals("") || tsvline[3].trim().equals("")) {
                    count++;
                } else {
                Case c1= new Case(tsvline[0], tsvline[1], tsvline[2], tsvline[3], tsvline[4], tsvline[5], tsvline[6]);
                caseList.add(c1);
                }
            }
            if (count > 0) {
                String message = count + " cases rejected\n" + "The file must have cases with\n" + "tab separated data. title, type, and case number!";
                throw new DataException(message);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DataException d) {
        }
        return caseList;
    }

}
