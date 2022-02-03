// Tiantong Li (tiantonl)

package hw2;

import java.util.*;

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
		CaseReaderFactory caseReaderFactory = new CaseReaderFactory();
		CaseReader caseReader = caseReaderFactory.createReader(filename);
		caseList = FXCollections.observableArrayList(caseReader.readCases());
		Collections.sort(caseList);
		for (Case c: caseList) {
			caseMap.put(c.getCaseNumber(),c);
		}
	}

	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		//write your code here
		for(Case c: caseList) {
			String year = c.getCaseDate().substring(0,4);
			if(!yearMap.containsKey(year)) {
				List<Case> yearCases = new ArrayList<>();
				yearCases.add(c);
				yearMap.put(year, yearCases);
			} else {
				yearMap.get(year).add(c);
			}
		}
		yearList = FXCollections.observableArrayList(new ArrayList<>(yearMap.keySet()));
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		//write your code here
		List<Case> foundCases = new ArrayList<>();
		for(Case c: caseList) {
			String caseTitle = c.getCaseTitle().toLowerCase(Locale.ROOT);
			String type = c.getCaseType().toLowerCase(Locale.ROOT);
			String caseYear = c.getCaseDate().substring(0, 4).toLowerCase(Locale.ROOT);
			String number = c.getCaseNumber().toLowerCase(Locale.ROOT);

			StringBuilder cc = new StringBuilder();
			cc.append(caseTitle+"\n").append(type+"\n").append(caseYear+"\n").append(number+"\n");

			if(title == null) {
				title = " ";
			}
			if(caseType == null){
				caseType = " ";
			}
			if(year == null){
				year = " ";
			}
			if(caseNumber == null){
				caseNumber = " ";
			}
			if(cc.toString().contains(title.toLowerCase(Locale.ROOT))&&cc.toString().contains(caseType.toLowerCase(Locale.ROOT))&&
			cc.toString().contains(year.toLowerCase(Locale.ROOT)) && cc.toString().contains(caseNumber.toLowerCase(Locale.ROOT))) {
				foundCases.add(c);
			}
		}
		return foundCases;
	}
}
