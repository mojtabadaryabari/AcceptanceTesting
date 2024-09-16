package AcceptanceTesting;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {
    public static final String RED = "\033[0;31m";      // Red text
    public static final String GREEN = "\033[0;32m";    // Green text
    public static final String RESET = "\033[0m";       // Reset color

    

	  public static int ExecuteCommandWithoutArgument(String command, String directory) throws IOException, InterruptedException {
	        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
	        builder.directory(new File(directory));
	        builder.redirectErrorStream(true);
	        Process process = builder.start();
	        return process.waitFor();
	    }

	  public static int ExecuteCommandWithArgument(String command, String directory,String Argument) throws IOException, InterruptedException {
	     
	  
		    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command, Argument);
	        builder.directory(new File(directory));
	        builder.redirectErrorStream(true); // Redirect error stream to standard output

	        try {
	            // Start the process
	            Process process = builder.start();

	            // Read process output
	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line;

	            while ((line = reader.readLine()) != null) {
	        //        System.out.println(line);
	            }
	            
	            // Wait for the process to finish and get the exit code
	            int exitCode = process.waitFor();
	            return exitCode;

	  }
	       
	        	catch (IOException | InterruptedException e) {
    	            e.printStackTrace();
    	            return 0;
    	        }
	        	 
	        	
			

	  
}

	  public static void  RunMessage(int exitCode)
	  {
		  
		  if (exitCode==0)
		  {
			  System.out.println(GREEN+"SUCCESS"+RESET);
		  }
		  else
		  {
			  System.out.println(RED+"ERROR !!!"+RESET);
			  
		  }
		  
	  }

}

	  

