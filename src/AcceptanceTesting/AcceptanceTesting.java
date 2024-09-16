package AcceptanceTesting;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AcceptanceTesting {
	
	
	public static void main(String[] args) {

		// Read from Config file
	   	
	    String directoryPath = "D:\\Milan\\RFI\\ToolsForLNCGenarator\\rmutt.js\\LNCGram";
	    String AIDAPath = "D:\\Milan\\RFI\\AIDA-Product_Windows\\standalone";
		Path sourceDir = Paths.get("D://Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/tempout");

	        
		RunAIDA R=new RunAIDA(directoryPath,AIDAPath);
	
	    RunRmutt RR=new RunRmutt();
		
	    
		for (int i = 1; i < 5; i++) {
		try {
			
				RR.CallRmutt(i);
		        System.out.println("Rmutt called");

				RR.EditAcessModifiers();
		        System.out.println("Type changed");

		        
		    	R.create_LNC_For_AIDA();	
	    		
	    
	  	        DirectoryStream<Path> files = Files.newDirectoryStream(sourceDir, "*.txt");

	  	      for (Path file : files) {	
		        R.RunAIDATools();  
	    		System.out.println("AIDA ran");

		        R.CheckAidaReports(file);
	  	      }
	    		
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
        
	    }
	
	 
}
