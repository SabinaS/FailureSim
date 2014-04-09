import java.util.*;
import java.math.*;
import java.lang.*;
import java.io.*;

public class FailueSimulation {

	public static void main (String[] args) throws java.lang.Exception
	{
		get_mttf_no_extras(); 	//used for case with no spare cores 
		get_mttf_with_extras(8,2); 	//used for cases with spare cores 
	}
	
	public static void get_mttf_no_extras(){
		int[] trial_results = new int[1000]; 
		int x = 0; //trials left
		boolean survived = false; 
		
		while(x<1000){ 
			int t = 0; //time passed
			while(t<100){
				double prob = 99.5; 
				survived = core_survived(prob); 
				if (survived == true){ 
					t++; 
				}
				else{
					trial_results[x] = t; 
					break; 
				}
			}
			x++; 
		}
		
		average(trial_results);
		
	}//end method
	
	public static void get_mttf_with_extras(int n, int m){
		int[] trial_results = new int[1000]; 
		int x = 0; //trials left
		boolean survived = false;
		int original_n = n; 
		
		while(x<1000){
			int t = 0; 		//time passed; used for case b
			int temp_n = n; 
			int temp_m = m; 
			int prob_t = 1; 	//used for case a
			while(t<100){
				double prob = get_failure_probability(true, t);
				survived = core_survived(prob); 
				if (survived == true){ 
					if(t==99){
						trial_results[x] = 99;
						break; 
					}
					else{
						t++;
					}
				}
				else{ //failed
					if(temp_m != 0){
						temp_m--; 
						t++; 
					}
					else{
						temp_n--; 
						prob_t ++; 
						if(checkTotalFailure(temp_n, temp_m, original_n) == true){
							trial_results[x] = t;
							break; 
						}
						
					}
				}//end out else 
			}//end inner while
			x++; 
		}//end outer while 
		
		average(trial_results); 
		
	}//end method
	
	public static double get_failure_probability(boolean wearout, int t){
		double newpercent = 0; 
		if (wearout == false){ 
			double percent = (Math.exp(-(0.05 * t))); //returns a percent, from 0 to 1
			newpercent = percent* 100.0; 
		}
		else{
			double newLambda = (t * (0.05*0.05)); 
			double percent = (Math.exp(-(newLambda * t))); //returns a percent, from 0 to 1
			newpercent = percent* 100.0;
		}
		return newpercent;
	}//end method 
	
	public static boolean core_survived(double percent){
		boolean survived = false; 
		double random = (Math.random() * 100); 
		if ( (random < percent) || (random == percent)) {
                		survived = true; 
            	}
       		return survived; 
	}//end method 
	
	public static boolean checkTotalFailure(int n, int m, int original_n){
		if( (m+n) < (original_n-1) || (m+n) == (original_n-1)){
			return true; 
		}
		else{
			return false; 
		}
	}//end method
	
	public static double average(int[] trials){
		double avg = 0; 
		double sum = 0; 
 
		for(int i = 0; i< trials.length; i++){
			int x = trials[i]; 
			sum = sum+x; 
		}
		
		avg = sum / (double)trials.length; 
		return avg; 
	}//end method 

}//end class 

