// Tiantong Li (tiantonl)

package hw3;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Implements readCases method and returns caseList based on the tsv file

public class TSVCaseReader extends CaseReader{

    static int invalidCases = 0;

    TSVCaseReader(String filename) {
        super(filename);
    }
    @Override
    List<Case> readCases() {
        List<Case> caseList = new ArrayList<>();
        StringBuilder fileContent = new StringBuilder();
        String[] lines;
        int lineCounts = 0;
        int countInvalid = 0;

        try {
            Scanner input = new Scanner(new File(filename));
            while(input.hasNextLine()) {

                fileContent.append(input.nextLine() + "\n");
                lineCounts ++;
            }
            lines = new String[lineCounts];
            lines = fileContent.toString().split("\n");

            for (int i = 0; i < lines.length; i++) {
                // support king does not have enough number of elements and needs to fix it
                String[] elements = lines[i].split("\t");

                if(elements[0].trim().equals("") || elements[1].trim().equals("")  || elements[2].trim().equals("") || elements[3].trim().equals("")) {
                    invalidCases++;
                    countInvalid++;
                } else {
                    Case c = new Case(elements[0],elements[1],elements[2],elements[3],elements[4],elements[5],elements[6]);
                    caseList.add(c);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try{
            if(countInvalid > 0) throw new DataException("TSV file missing data");
        } catch (DataException e1) {
            System.out.println("Date Exception while reading TSV file");
        }

        return caseList;
    }

}
