package AcceptanceTesting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RunSienaTools {
	
	private String AidaPath;
	private String SienaPath;
	

	
	
	
	public RunSienaTools(String source,String target)
	{
		this .AidaPath=source;
		this .SienaPath=target;
		
	}
	
	public void Run() throws IOException, InterruptedException
	{
		ManageIO.FoldedrCopy(AidaPath+"\\inputFolder\\Sheets",SienaPath+"\\Logica\\SRF");
		
		Path AIDADizionarioPath = Paths.get(AidaPath+"\\inputFolder\\Dizionario.rfisrf_dictionary");
	    Path SienaDizionarioPath = Paths.get(SienaPath+"\\Logica");
	        
	       
	        
	    Files.copy(AIDADizionarioPath, SienaDizionarioPath.resolve(AIDADizionarioPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
	       
	    
	    String command = 
	            "jdk-11.0.9\\bin\\java.exe"+
	            " -jar"+
	            " srf2json-2.1.7-jar-with-dependencies.jar"+ 
	            " Logica"+  
	            " Logica\\test.json"
	        ;
	    
	    int exitCode= CommandExecutor.ExecuteCommandWithoutArgument(command,SienaPath);
	    
	  System.out.println("Siena Tools ran");
	  CommandExecutor.RunMessage(exitCode);
		
	}
	

}
