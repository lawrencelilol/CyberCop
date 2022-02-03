package com.example.hellofx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileUtilities {
    public StringBuilder readFile(String filename) {
        File file = new File(filename);
        StringBuilder fileContent = new StringBuilder();

        try {
            Scanner input = new Scanner(file);
            while(input.hasNextLine()) {
                fileContent.append(input.nextLine() + "\n");
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return fileContent;
    }
}
