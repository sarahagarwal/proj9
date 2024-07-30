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

//The SPSS class serves as a comprehensive tool for managing student submissions and
//analyzing test scores. Upon instantiation, the class initializes with the specified
//number of tests and sets up data structures to store student information and submissions. 
//It offers methods to add new students, count the total number of students, and record 
//submissions, ensuring the validity of input data efficiency. Additionally, the class
//includes functionalities to process individual submission data, calculate student scores,
//count student submissions, and evaluate student performance based on test results.

public class SPSS {

    private int numTests; // Number of tests

    // List to store names of all students
    private ArrayList<String> students;

    // List to store names of students that made at least one successful submission
    private ArrayList<String> studentNames;

    // List to store information for every student that makes a submission
    private ArrayList<Submissions> allSubmissions;

    // Constructor
    public SPSS(int numTests) {
        // Initialize number of tests
        if (numTests <= 0) {
            this.numTests = 1;
        } else {
            this.numTests = numTests;
        }

        // Initialize lists
        students = new ArrayList<String>();
        allSubmissions = new ArrayList<Submissions>();
        studentNames = new ArrayList<String>();
    }

    //This method adds a new student to the SPSS object. It checks if the 
    //provided student name is not null and not already in the list of
    ///students. If the name is valid and not a duplicate, it adds the student
    //to the list.
    public boolean addStudent(String newStudentName) {
        boolean result = false;

        // Check for invalid parameters
        if (newStudentName != null && !newStudentName.isEmpty()) {
            // Check if the student already exists
            boolean found = students.contains(newStudentName);

            // Add the student if it doesn't already exist
            if (!found) {
                students.add(newStudentName);
                result = true;
            }
        }
        return result;
    }

//This method returns the total number of students currently stored in the SPSS object.
    public int numStudents() {
        return students.size();
    }

//This method adds a submission for a given student. It verifies that the test 
    //results list is not null, contains the correct number of test scores, and 
    //the student name is valid and exists in the list of students. It then adds
    //the submission to the SPSS object, updating existing submissions if necessary.
    public boolean addSubmission(String name, List<Integer> testResults) {
        boolean isValid = false;

        // Check for valid parameters
        if (testResults != null && !testResults.isEmpty() && testResults.size() == 
        		numTests && name != null
                && students.contains(name)) {

            int score = 0;

            // Calculate total score
            for (int i = 0; i < testResults.size(); i++) {
                score += testResults.get(i);
            }

            // Check if the student has previously made any valid submissions
            boolean foundSubmission = studentNames.contains(name);
            int index = -1;

            // If the student has made a previous submission and the new score is higher than the previous score,
            // update the submission; otherwise, ignore the submission
            if (foundSubmission) {
                index = studentNames.indexOf(name);
                int currentScore = score(name);

                if (score > currentScore) {
                    allSubmissions.get(index).setTestResults(testResults);
                }

                allSubmissions.get(index).incrementNumSubmissions();
                isValid = true;
            }

            // Add the submission if no previous submission has been made and everything else is valid
            if (!foundSubmission) {
                boolean negValue = false;

                // Check if testResults has any negative elements
                for (int i = 0; i < testResults.size(); i++) {
                    if (testResults.get(i) < 0) {
                        negValue = true;
                        break;
                    }
                }

                // Add submission if testResults has no negative elements
                if (!negValue) {
                    allSubmissions.add(new Submissions(name, testResults));
                    studentNames.add(name);
                    isValid = true;
                }
            }
        }
        return isValid;
    }

    //This method reads submissions concurrently from multiple files. It creates 
    //a new thread for each file provided in the list of file names, where each
    //thread reads the content of a file, processes the submissions, and adds them 
    //to the SPSS object. It ensures concurrent execution using multithreading and
    //thread joining.
    public boolean readSubmissionsConcurrently(List<String> fileNames) {
    	
        boolean result = false;


    	if (fileNames != null && !fileNames.isEmpty()) {

            // Create and start a thread for each filename
            List<Thread> threads = new ArrayList<>();
            
            for (String fileName : fileNames) {
            	
                if (fileName != null) {

                    Thread thread = new Thread(null, null, fileName, 0) {
                        {
                            try {
                                // Read the content of the file and process submissions
                                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    // Process each line to extract submission data and add to SPSS object
                                    processSubmissionLine(line);
                                }
                                reader.close();
                            } catch (IOException e) {
                                // Handle file I/O errors
                                System.err.println("Error");
                            }
                        }
                    };
                    thread.start(); // Start the thread
                    threads.add(thread); // Add the thread to the list
                    result = true; // Set result to true since at least one thread is started
                }
            }
            
            // Wait for all threads to finish
            for (Thread thread : threads) {
                try {
                    thread.join(); // Wait for the thread to finish
                } catch (InterruptedException e) {
                    System.err.println("Error joining thread: " + e.getMessage());
                }
            }
        }

            
        
        return result; // Return true if at least one thread is started
    }


//This method processes a single line of submission data from a file. It extracts 
    //the student name and test scores from the line and adds the submission to
    //the SPSS object using the addSubmission method.
    private void processSubmissionLine(String line) {
        // Create a Scanner to read the line
        Scanner input = new Scanner(line);

        // Read the student name
        String studentName = input.next();

        // Extract test scores
        List<Integer> testScores = new ArrayList<>();
        while (input.hasNextInt()) {
            int score = input.nextInt();
            testScores.add(score);
        }

        // Close the scanner
        input.close();

        // Add the submission to the SPSS object
        boolean result = addSubmission(studentName, testScores);
        if (!result) {
            // Handle if the submission could not be added
            System.err.println("Error adding submission for student: " + studentName);
        }
    }

//This method calculates the total score for a given student based on their
    //submissions. It checks if the student name is valid and exists in the 
    //list of students, then calculates the total score by summing the scores
    //from all submissions made by that student.
    public int score(String name) {
        int score = -1;

        // Check for valid parameters
        if (name != null && !name.isEmpty() && students.contains(name)) {
            score = -1;

            int index = -1;

            // Find the total score of the student's best submission if the student has made at least one successful submission
            if (studentNames.contains(name)) {
                index = studentNames.indexOf(name);
                score = 0;
                for (int i = 0; i < allSubmissions.get(index).getTestResults().size(); i++) {
                    score += allSubmissions.get(index).getTestResults().get(i);
                }
            } else {
                // Total score is 0 if student has made no successful submissions
                score = 0;
            }
        }
        return score;
    }

// This method returns the total number of submissions made by a given student. 
    //It checks if the student name is valid and exists in the list of students,
    //then retrieves the number of submissions made by that student.
    public int numSubmissions(String name) {
    	
  	int result = -1;
  	
  	//checks that all the parameters are valid
  	if (name != null && !name.isEmpty() && students.contains(name)) {
  		result = 0;
  	
	    	int index = -1;
	        
	    	//finds the student's total number of successful submissions if it
	    	//has made atleast one successful submission
	        if (studentNames.indexOf(name) != -1) {
	        	index = studentNames.indexOf(name);
	            result = allSubmissions.get(index).getNumSubmissions();
	        }
  	}
  	
  	return result;
  }

    //This method returns the total number of submissions made by all
    //students combined. It iterates through all submissions in the SPSS object
    //and calculates the total count.
  public int numSubmissions() {
  	
  	int total = 0;
  	
  	for (int i = 0; i < allSubmissions.size(); i++) {
  		
  		total += allSubmissions.get(i).getNumSubmissions();
  	}
  	
  	return total;
  }

  //This method determines if a student's performance is satisfactory based 
  //on the number of tests passed. It checks if the student name is valid and 
  //exists in the list of students, then calculates the percentage of tests
  //passed and determines if it meets the satisfactory criteria.
  public boolean satisfactory(String name) {
  	
  	boolean result = false;
  	
  	//checks that all the parameters are valid
  	if(name != null && !name.isEmpty() && students.contains(name)) {
  		
 
	    	int index = -1;
	    	
	    	//checks that the student has made atleast one successful submission
	    	if(studentNames.indexOf(name) != -1) {
	        	index = studentNames.indexOf(name);
				int num = 0;
				int size = allSubmissions.get(index).getTestResults().size();
				
				//finds the number of tests that passed
				for(int j = 0; j < size; j++) {
					if(allSubmissions.get(index).getTestResults().get(j) > 0) {
						num++;
					}
				}
	    			
				//checks if half or more of the tests passed
				if(num >= size/2) {
					result = true;
				}
	    	}
  		
  	}
  	return result;
  	
  }

  
  //This method checks if a student received extra credit for submitting all tests 
  //correctly. It verifies if the student name is valid and exists in the list of
  //students, then checks if the student made only one successful submission and
  //all test scores were positive. If both conditions are met, it returns true
  //indicating the student received extra credit.
  public boolean gotExtraCredit(String name) {
  	
  	boolean result = false;
  	
  	//checks that all the parameters are valid
  	if(name != null && !name.isEmpty() && students.contains(name)) {
  	
	    	int index = -1;
	    	
	    	//checks that the student has made atleast one successful submission

	    	if(studentNames.indexOf(name) != -1) {
	        	index = studentNames.indexOf(name);
	        	
		    	//checks that the student has made only one successful submission

				if(allSubmissions.get(index).getNumSubmissions() == 1) {
					int num = 0;
	    			int size = allSubmissions.get(index).getTestResults().
	    					size();
	    			
	    			//finds the number of tests that passed

	    			for(int j = 0; j < size; j++) {
	    				if(allSubmissions.get(index).getTestResults().
	    						get(j) > 0) {
	    					num++;
	    				}
	        		}
	    			//checks if all of the tests passed 
	    			if(num == size) {
	    				result = true;
	    			}
	    			
	    		}
	    	}
  	}
  	return result;

  }
}
