// Tiantong Li (tiantonl)

package hw2;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//Implements readCases method and returns caseList based on the tsv file

public class TSVCaseReader extends CaseReader{
    TSVCaseReader(String filename) {
        super(filename);
    }

    @Override
    List<Case> readCases() {
        List<Case> caseList = new ArrayList<>();
        StringBuilder fileContent = new StringBuilder();
        String[] lines;
        int lineCounts = 0;

        try {
            Scanner input = new Scanner(new File(filename));
            while(input.hasNextLine()) {
                fileContent.append(input.nextLine() + "\n");
                lineCounts ++;
            }
            lines = new String[lineCounts];
            lines = fileContent.toString().split("\n");

            for (int i = 0; i < lines.length; i++) {
                String[] elements = lines[i].split("\t");
                Case c = new Case(elements[0],elements[1],elements[2],elements[3],elements[4],elements[5],elements[6]);
                caseList.add(c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return caseList;
    }
}
