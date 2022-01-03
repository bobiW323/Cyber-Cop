/**
 * Andrew ID: jintaoh
 * Name: Jintao Huang
 */
package hw3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		//write your code here
	    CaseReaderFactory filefactory = new CaseReaderFactory();
	    CaseReader cr = filefactory.createReader(filename);
	    List<Case> caselist = cr.readCases();
	    Iterator<Case> itt = caselist.iterator();
	    while(itt.hasNext()) {
	        Case cas = itt.next();
	        caseList.add(cas);
	    }
	    
	    //caseList = (ObservableList<Case>) cr.readCases();
	    Collections.sort(caseList);
	    Iterator<Case> itr = caseList.iterator();
	    while(itr.hasNext()) {
	        Case cs = itr.next();
	        caseMap.put(cs.getCaseNumber(), cs);
	    }
	    
	}
	/**
	 *  This method writes caseList elements in a TSV file. 
	 *  If the write is successful, it returns true. 
	 *  In case of IOException, it returns false.
	 * @param filename
	 */
	boolean writeCases(String filename) {
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename));){
	        Iterator<Case> cl = caseList.iterator();
	        while (cl.hasNext()) {
	            Case cs = cl.next();
	            bw.write(String.format("%s\t%s\t%s\t%s\t", cs.getCaseDate(), cs.getCaseTitle(), cs.getCaseType(), cs.getCaseNumber()));
	            if (cs.getCaseLink().equals("")) {
	                bw.write("\s\t");
	            } else bw.write(String.format("%s\t", cs.getCaseLink()));
	            if (cs.getCaseCategory().equals("")) {
                    bw.write("\s\t");
                } else bw.write(String.format("%s\t", cs.getCaseCategory()));
	            if (cs.getCaseNotes().equals("")) {
                    bw.write("\s\t");
                } else bw.write(String.format("%s\t%n", cs.getCaseNotes()));
	            
	        }
	        return true;
	    } catch (IOException e) {
	        return false;
	    }
	}
	
	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */

    void buildYearMapAndList() {
		
	    StringBuilder yearsb = new StringBuilder();
	    for (int i = 0; i < caseList.size(); i++) {
	        String yr = caseList.get(i).getCaseDate().split("-")[0];
	        if (!yearsb.toString().contains(yr))
	            yearsb.append(yr + "\n");
	    }
	    String[] yeararray = yearsb.toString().split("\n");
	    for (int i = 0; i < yeararray.length; i++) {
	        List<Case> yearlist = new ArrayList<>();
	       for(Case cs : caseList) {
	           if (cs.getCaseDate().split("-")[0].equals(yeararray[i]))
	           yearlist.add(cs);
	       }
	       yearMap.put(yeararray[i], yearlist);

	    }
	    List<String> yr = new ArrayList<>(yearMap.keySet());
	    yearList = FXCollections.observableList(yr);
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		
	    List<Case> result = new ArrayList<>();
	    Iterator<Case> itt = caseList.iterator();
	    while(itt.hasNext()) {
	        Case css = itt.next();
	        String cstitle = css.getCaseTitle();
	        String cstype = css.getCaseType();
	        String csyear = css.getCaseDate().split("-")[0];
	        String cssnumber = css.getCaseNumber();
	        if (title != null && caseType != null && year != null && caseNumber != null 
	                && cstitle.toLowerCase().contains(title.toLowerCase()) && cstype.equalsIgnoreCase(caseType) && 
	                csyear.equals(year) && cssnumber.contains(caseNumber))
	            result.add(css);
	        else if (title != null && caseType != null && year != null && caseNumber == null 
                    && cstitle.toLowerCase().contains(title.toLowerCase()) && cstype.equalsIgnoreCase(caseType) && 
                    csyear.equals(year))
	            result.add(css);
	        else if (title != null && caseType != null && year == null && caseNumber != null 
                    && cstitle.toLowerCase().contains(title.toLowerCase()) && cstype.equalsIgnoreCase(caseType) && 
                    cssnumber.contains(caseNumber))
                result.add(css);
	        else if (title != null && caseType == null && year != null && caseNumber != null 
                    && cstitle.toLowerCase().contains(title.toLowerCase()) &&  csyear.equals(year) && cssnumber.contains(caseNumber))
	            result.add(css);
	        else if (title == null && caseType != null && year != null && caseNumber != null 
                    && cstype.equalsIgnoreCase(caseType) && 
                    csyear.equals(year) && cssnumber.contains(caseNumber))
	            result.add(css);
	        else if (title == null && caseType == null && year != null && caseNumber != null 
                    && csyear.equals(year) && cssnumber.contains(caseNumber))
	            result.add(css);
	        else if (title == null && caseType != null && year == null && caseNumber != null 
                    && cstype.equalsIgnoreCase(caseType) && cssnumber.contains(caseNumber))
	            result.add(css);
	        else if (title == null && caseType != null && year != null && caseNumber == null 
                    && cstype.equalsIgnoreCase(caseType) && csyear.equals(year))
	            result.add(css);
	        else if (title != null && caseType == null && year == null && caseNumber != null 
                    && cstitle.toLowerCase().contains(title.toLowerCase()) && cssnumber.contains(caseNumber))
	            result.add(css);
	        else if (title != null && caseType == null && year != null && caseNumber == null 
                    && cstitle.toLowerCase().contains(title.toLowerCase()) && csyear.equals(year))
	            result.add(css);
	        else if (title != null && caseType != null && year == null && caseNumber == null 
                    && cstitle.toLowerCase().contains(title.toLowerCase()) && cstype.equalsIgnoreCase(caseType))
	            result.add(css);
	        else if (title != null && caseType == null && year == null && caseNumber == null 
                    && cstitle.toLowerCase().contains(title.toLowerCase()))
	            result.add(css);
	        else if (title == null && caseType != null && year == null && caseNumber == null 
                    && cstype.equalsIgnoreCase(caseType))
	            result.add(css);
	        else if (title == null && caseType == null && year != null && caseNumber == null 
                    && csyear.equals(year))
	            result.add(css);
	        else if (title == null && caseType == null && year == null && caseNumber != null 
                    && cssnumber.contains(caseNumber))
	            result.add(css);
	    }
		return result;
	}
}
