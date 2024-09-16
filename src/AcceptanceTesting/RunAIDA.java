package AcceptanceTesting;

import java.io.IOException;


import java.nio.file.*;

public class RunAIDA {

private String RmuttCommand;
private String RmuttDirectory;
private String NewFileAddress;
private String AIDACommand;
private String AIDADirectory;
	
	
	
	
	public RunAIDA(String RmuttDirectory,String AIDADirectory)
	{
		
		this.RmuttCommand="rmutt Main.rm > tempout/out" ;
		this.RmuttDirectory=RmuttDirectory;
		this.NewFileAddress=RmuttDirectory+"tempout\\out";
		this.AIDACommand="code_generator.bat inputFolder ";
		this.AIDADirectory=AIDADirectory;
		
		
		
	}
	
	public void create_LNC_For_AIDA() throws IOException, InterruptedException
	{
	        CreateSRFFolder CR=new CreateSRFFolder();
	        	        
			CR.createLNCFilesIntoAIDAFolder()	;
			
			
		}
	
    public void RunAIDATools() throws IOException, InterruptedException
{

        // Change directory to AIDA folder
        String folderPath = "D://Milan/RFI/AIDA-Product_Windows/standalone/";
        System.setProperty("user.dir", folderPath);

        // Run command for AIDA code generator

        String command = "code_generator.bat";
        String argument = "inputFolder"; // This is the argument for the batch file
        String directory = "D:\\Milan\\RFI\\AIDA-Product_Windows\\standalone";

 
        int exitCode=CommandExecutor.ExecuteCommandWithArgument(command, directory, argument);
        // Create ProcessBuilder to execute the command with the input folder as an argument
	
//        System.out.println("Command exited with code: " + exitCode);
        System.out.println("AIDA ran");
    	CommandExecutor.RunMessage(exitCode);

	
}

    public void CheckAidaReports(Path file) throws IOException
    {
    	String reportAddress = "D:/Milan/RFI/AIDA-Product_Windows/standalone/reports/report_inputFolder.txt";

    	
        // Switch directory back to LNCGram folder
    /*    String folderPath = "D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram";
        System.setProperty("user.dir", folderPath);
*/
        // Read and analyze the report file
        String reportContent = new String(Files.readAllBytes(Paths.get(reportAddress)));
        String fileNameWithoutExtension = ManageIO.getFileNameWithoutExtension(file);

        CreateSRFFolder CSR=new CreateSRFFolder();
        if (reportContent.contains("Test Report on inputFolder: SUCCESS")) {
        	CSR.createLNCFiles(file.toString());
            
            Path destinationFile = Paths.get("D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/out/", "Report_" + fileNameWithoutExtension+".txt");
            Files.copy(Paths.get(reportAddress), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            
            ManageIO.cutAndPasteGeneratedLNC("D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/tempout/", "D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/out/");
            
        } else {
            Path destinationFile = Paths.get("D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/outwitherror/", "Report_" + fileNameWithoutExtension+".txt");
            Files.copy(Paths.get(reportAddress), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            ManageIO.cutAndPasteGeneratedLNC("D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/tempout/", "D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/outwitherror/");

        }
    }
    
    public  void createLNCFilesIntoAIDAFolder() throws IOException, InterruptedException {
        Path sourceDir = Paths.get("D://Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/tempout");

        DirectoryStream<Path> files = Files.newDirectoryStream(sourceDir, "*.txt");

        String ldsFolderPath = "D:/Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Sheets/Stazione/LdS";
        String ldvFolderPath = "D:/Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Sheets/Stazione/LdV";
      
        // Remove previous files in LdS and LdV folders
        ManageIO.cleanDirectory(ldsFolderPath);
        ManageIO.cleanDirectory(ldvFolderPath);

//        System.out.println("Folders Cleaned");

       
        // Iterate through files and process each one
        for (Path file : files) {
     
        	
        	CreateSRFFolder.splitTextIntoAIDA(file.toString());

            
            System.out.println("LDS and LDV is created");

            
        
            RunAIDATools();
    		System.out.println("AIDA ran");
    		CheckAidaReports(file);

    	
        }
    }

    
    
}







