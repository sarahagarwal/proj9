//Sarah Agarwal
//UID: 119641224
// I did not use any unauthorized assistance on this assignment
package spss;
import java.util.ArrayList;
import java.util.List;

//stores all the students' submissions
public class Submissions {

	//student's name
	private String name;
	
	//student's test results
	private ArrayList<Integer> testResults;
	
	//total score of the students tests
	private int score = 0;
	
	//total number of successful submissions the student made
	private int numSubmissions = 0;
	
	public Submissions(String name, List<Integer> testResults) {
		this.name = name;
		this.testResults = new ArrayList<Integer>(testResults);
		
		//finds total score of the student's tests
		for(int i = 0; i < testResults.size(); i++) {
			score += testResults.get(i);
		}
		numSubmissions = 1;
	}
	
	//returns total number of successful submissions the student made
	public int getNumSubmissions() {
		return numSubmissions;
	}
	
	
	//returns the student's test results
	public ArrayList<Integer> getTestResults() {
		return new ArrayList<Integer>(testResults);
	}
	
	//sets the score to the newScore
	public void setScore(int newScore) {
		score = newScore;
	}
	
	//sets the testResults to the newTestResults
	public void setTestResults(List<Integer> newTestResults) {
		testResults = new ArrayList<Integer>(newTestResults);
	}
	
	//increase numSubmissions by one
	public void incrementNumSubmissions() {
		numSubmissions++;
	}
	
	
}
