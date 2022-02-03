// Tiantong Li (tiantonl)

package hw2;

// Invokes CSVCaseReader when the filename is csv
// and invokes TSVCaseReader when filename is tsv

public class CaseReaderFactory {

    /*
    createCaseReader() method returns an appropriate caseReader
     */
    CaseReader createReader(String filename) {
        CaseReader c;
        if(filename.contains("tsv")) {
            c = new TSVCaseReader(filename);
        } else if(filename.contains("csv")) {
            c = new CSVCaseReader(filename);
        } else {
            c = null;
        }
        return c;
    }
}
