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
	    String SienaSRF2Json = "D:\\Milan\\GeneratoreSiena_v1.0.4\\SRF2Json";
	    String SienaGenerator = "D:\\Milan\\GeneratoreSiena_v1.0.4\\input";
			
	        
		RunAIDA R=new RunAIDA(directoryPath,AIDAPath);
	
	    RunRmutt RR=new RunRmutt();
		
	    
		for (int i = 1; i < 5; i++) {
		try {
			    
			System.out.println("\n\n Project Out"+i+" is created.");	
				RR.CallRmutt(i);
		        
		        		

				RR.EditAcessModifiers();
		       // System.out.println("Acess Modifiers Changed");

		        
		    	R.create_LNC_For_AIDA();	
	    		
	    
	  	        DirectoryStream<Path> files = Files.newDirectoryStream(sourceDir, "*.txt");

	  	      for (Path file : files) {	
		        R.RunAIDATools();  
	    		

		        R.CheckAidaReports(file);
	  	      }

	  	      RunSienaTools RS=new RunSienaTools(AIDAPath, SienaSRF2Json);
	  	      RS.Run();
    	  	  
  
    	  	  
	  	      
	    		
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
        
	    }
	
	 
}
