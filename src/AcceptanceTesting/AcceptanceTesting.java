package AcceptanceTesting;

import java.io.IOException;

public class AcceptanceTesting {
	
	
	public static void main(String[] args) {

		// Read from Config file
	   	
	    String directoryPath = "D:\\Milan\\RFI\\ToolsForLNCGenarator\\rmutt.js\\LNCGram";
	    String AIDAPath = "D:\\Milan\\RFI\\AIDA-Product_Windows\\standalone";

	        
		RunRmutt R=new RunRmutt(directoryPath,AIDAPath);
		//String NewFileAddress=R.CallRmutt();
		//System.out.println("\n New File generated is saved in "+NewFileAddress);
		//R.EditAcessModifiers();
		try {
			R.create_LNC_For_AIDA();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
	    }
	
	 
}
