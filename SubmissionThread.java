//Sarah Agarwal
//UID: 119641224
// I did not use any unauthorized assistance on this assignment
package spss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubmissionThread extends Thread{

	private String fileName;

    public SubmissionThread(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        if (fileName != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Print the data as it is read (replace this with storing into SPSS object)
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + fileName);
            }
        }
    }
    
   
   
}
