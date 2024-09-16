package AcceptanceTesting;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateSRFFolder {

	
	
      String RmuttDirectory = "D:\\Milan\\RFI\\ToolsForLNCGenarator\\rmutt.js\\LNCGram";
	 
    
	   public  void createLNCFilesIntoAIDAFolder() throws IOException, InterruptedException {
	        Path sourceDir = Paths.get("D://Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/tempout");

	        DirectoryStream<Path> files = Files.newDirectoryStream(sourceDir, "*.txt");

	        String ldsFolderPath = "D:/Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Sheets/Stazione/LdS";
	        String ldvFolderPath = "D:/Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Sheets/Stazione/LdV";
	      
	        // Remove previous files in LdS and LdV folders
	        ManageIO.cleanDirectory(ldsFolderPath);
	        ManageIO.cleanDirectory(ldvFolderPath);

//	        System.out.println("Folders Cleaned");

	        
	        // Iterate through files and process each one
	        for (Path file : files) {
	     
	        	
	            splitTextIntoAIDA(file.toString());

	            
	            System.out.println("LDS and LDV is created");


	          

	    	
	        }
	    }


	   
	   public static void splitTextIntoAIDA(String inputFile) throws IOException {
		    

	    	
	        ManageIO.removeLdsDFile();
	        ManageIO.removeLdvDFile();

	        
	        
//	        System.out.println("Folders Cleaned");

	        String codeContent = new String(Files.readAllBytes(Paths.get(inputFile)));
	        codeContent = codeContent.replace("valore  Fal /*39,*/", "valore  False /*39,*/");

	        String[] files = codeContent.split(Pattern.quote("//***************************************************"));

	        

	        
	        for (int i = 1; i < files.length; i++) {
	            int splitPoint = files[i].indexOf("Scheda di classe");

	/*            String fileNameWithoutExtension = getFileNameWithoutExtension(inputFile);
	            String fileExtension = getFileExtension(inputFile);
	*/
	            String name = nameOfClassText(files[i]);
	            String ldsFolderPath = "D://Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Sheets/Stazione/LdS/";
	            String ldvFolderPath = "D://Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Sheets/Stazione/LdV/";

	            String ldsOutputFile1Path = ldsFolderPath + name + ".rfisrf_definition";
	            String ldsOutputFile2Path = ldsFolderPath + name + ".rfisrf_sheet";
	            String ldvOutputFile1Path = ldvFolderPath + name + ".rfisrf_ldv_definition";
	            String ldvOutputFile2Path = ldvFolderPath + name + ".rfisrf_ldv_sheet";

	            // Split the code into two parts
	            String textPart1 = files[i].substring(0, splitPoint);
	            String textPart2 = files[i].substring(splitPoint);
	            textPart2 = textPart2.replace("Scheda di classe LDV_", "Scheda della classe  di vista LDV_");
	            textPart2 = textPart2.replace("Scheda di classe LDS_LDV_Start", "Scheda della classe  di vista LDS_LDV_Start");
	            textPart1 = textPart1.replace("della classe LDS_LDV_Start", "della classe di vista LDS_LDV_Start");

	            if (textPart1.contains("LDV")) {
	            	ManageIO.writeFile(ldvOutputFile1Path, textPart1);
	            	ManageIO.writeFile(ldvOutputFile2Path, textPart2);
	            } else {
	            	ManageIO.writeFile(ldsOutputFile1Path, textPart1);
	            	ManageIO.writeFile(ldsOutputFile2Path, textPart2);
	            }

	            // Add filename into Dizionario.rfisrf_dictionary file
	            String dizionarioFile = "D://Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Dizionario.rfisrf_dictionary";
	            boolean LDS = true;

	            if (name.contains("LDV")) {
	                updateDizionarioFile(dizionarioFile, "LdV", name, i);
	                LDS = false;
	            } else if (name.contains("LDS") && LDS) {
	                updateDizionarioFile(dizionarioFile, "LdS", name, i);
	            }
	        }
	    }


	    private static String nameOfClassText(String codeContent) {
	        if (codeContent.contains("LDS")) {
	            String patternString = Pattern.quote("della classe ") + "(.*?)" + Pattern.quote("\n");
	            Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
	            Matcher matcher = pattern.matcher(codeContent);
	            if (matcher.find()) {
	                return matcher.group(1);
	            } else {
	                return "";
	            }
	        } else if (codeContent.contains("LDV")) {
	            String patternString = Pattern.quote("della classe di vista ") + "(.*?)" + Pattern.quote("\n");
	            Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
	            Matcher matcher = pattern.matcher(codeContent);
	            if (matcher.find()) {
	                return matcher.group(1);
	            } else {
	                return "";
	            }
	        }
	        return "";
	    }
	    

	    private static void updateDizionarioFile(String dizionarioFile, String type, String name, int index) throws IOException {
	        List<String> lines = Files.readAllLines(Paths.get(dizionarioFile));
	        int startIndex = 0;

	        for (int j = 0; j < lines.size(); j++) {
	            if (lines.get(j).contains(type)) {
	                startIndex = j;
	                break;
	            }
	        }

	        lines.add(startIndex + index, "\t\t\t" + name + '{' + name + '}' + "\n");
	        Files.write(Paths.get(dizionarioFile), lines);
	    }
   
	    public  void createLNCFiles(String filepath) throws IOException {
	        
	    	Path sourceDir = Paths.get(RmuttDirectory+"\\tempout\\");
	  //  	Path RmuttDir = Paths.get(RmuttDirectory);

	    	
	        DirectoryStream<Path> files = Files.newDirectoryStream(sourceDir, "*.txt");

	        for (Path file : files) {
	            String fileNameWithoutExtension = ManageIO.getFileNameWithoutExtension(file);

	            // Define output paths
	            Path folderPath = Paths.get(RmuttDirectory+"\\output\\"+fileNameWithoutExtension);
	            
	          
	            if (!Files.exists(folderPath)) {
	                Files.createDirectories(folderPath);
	            }

	//            String name = nameOfClassText(file.toString());
	        
	      /*      
	            Path outputFile1Path = RmuttDir.resolve(folderPath).resolve(name + ".rfisrf_definition");
	            Path outputFile2Path = RmuttDir.resolve(folderPath).resolve(name + ".rfisrf_sheet");
*/
	            
	            
	            // Convert filepath to Path before passing to splitText
	            Path inputPath = Paths.get(filepath); // Conversion from String to Path
	            splitText(inputPath); // Pass Path object to splitText method
	              }
	    }
 
	   
	    private  void splitText(Path inputFile) throws IOException {
	        


	        // Read file content
	        String codeContent = Files.readString(inputFile);
	       codeContent = codeContent.replace("valore  Fal /*39,*/", "valore  False /*39,*/");

	        // Split the content
	        String[] files = codeContent.split(Pattern.quote("//***************************************************"));

	        for (int i = 1; i < files.length; i++) {
	            int splitPoint = files[i].indexOf("Scheda di classe");

	            // Extract file name without extension
	            String fileNameWithoutExtension = inputFile.getFileName().toString();
	            int dotIndex = fileNameWithoutExtension.lastIndexOf('.');
	            if (dotIndex != -1) {
	                fileNameWithoutExtension = fileNameWithoutExtension.substring(0, dotIndex);
	            }

	            // Define paths
	            
	            Path folderPath = Paths.get(RmuttDirectory+"//output", fileNameWithoutExtension);
	            Path ldsFolder = folderPath.resolve("LdS");
	            Path ldvFolder = folderPath.resolve("LdV");

	            
	            // Create directories if they don't exist
	            if (i == 1) {
	            	
	                Files.createDirectories(folderPath);
	                Files.createDirectories(ldsFolder);
	                Files.createDirectories(ldvFolder);
	             }

	            String name = nameOfClassText(files[i]);

	            
	            name = name.substring(0,name.length());
	            
	            // System.out.println(RmuttDirectory+folderPath+name+ ".rfisrf_definition");
	             // Properly resolve paths using Path.resolve()
	                       
	             
	             
	            Path ldsOutputFile1Path = ldsFolder.resolve(name + ".rfisrf_definition");
	            Path ldsOutputFile2Path = ldsFolder.resolve(name + ".rfisrf_sheet");
	            Path ldvOutputFile1Path = ldvFolder.resolve(name + ".rfisrf_ldv_definition");
	            Path ldvOutputFile2Path = ldvFolder.resolve(name + ".rfisrf_ldv_sheet");

	            // Split the code into two parts
	            String textPart1 = files[i].substring(0, splitPoint);
	            String textPart2 = files[i].substring(splitPoint);

	            textPart2 = textPart2.replace("Scheda di classe LDV_", "Scheda della classe  di vista LDV_");
	            textPart2 = textPart2.replace("Scheda di classe LDS_LDV_Start", "Scheda della classe  di vista LDS_LDV_Start");
	            textPart1 = textPart1.replace("della classe LDS_LDV_Start", "della classe di vista LDS_LDV_Start");

	            // Write to files based on the content
	            if (textPart1.contains("LDV")) {
	                Files.writeString(ldvOutputFile1Path, textPart1);
	                Files.writeString(ldvOutputFile2Path, textPart2);
	            } else {
	                Files.writeString(ldsOutputFile1Path, textPart1);
	                Files.writeString(ldsOutputFile2Path, textPart2);
	            }
	        }
	    }

	    
}
