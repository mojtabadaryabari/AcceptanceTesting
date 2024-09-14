package AcceptanceTesting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.net.ssl.SSLEngine;

import java.nio.file.*;

public class RunRmutt {

	private String RmuttCommand;
	private String RmuttDirectory;
	private String NewFileAddress;
	private String AIDACommand;
	private String AIDADirectory;
	
	
	
	
	public RunRmutt(String RmuttDirectory,String AIDADirectory)
	{
		
		this.RmuttCommand="rmutt Main.rm > tempout/out" ;
		this.RmuttDirectory=RmuttDirectory;
		this.NewFileAddress=RmuttDirectory+"tempout\\out";
		this.AIDACommand="code_generator.bat inputFolder ";
		this.AIDADirectory=AIDADirectory;
		
		
		
	}
	
	
	public void create_LNC_For_AIDA() throws IOException, InterruptedException
	{
		for (int i = 1; i < 2; i++) {
			CallRmutt(i);
	        System.out.println("Rmutt called");

			EditAcessModifiers();
	        System.out.println("Type changed");

			createLNCFilesIntoAIDAFolder()	;
			
			
		}
		
		
	}
		
	public  String CallRmutt(int i) throws IOException, InterruptedException
	{
		String NewFileName = "out" + i + ".txt";
		this.RmuttCommand="rmutt Main.rm > tempout/" +NewFileName ;
		
		CommandExecutor CX=new CommandExecutor();
		CX.ExecuteCommandWithoutArgument(RmuttCommand,RmuttDirectory);
		
	
		
		return NewFileAddress;
	}
	
	
	
	public String EditAcessModifiers()
	{
		  
	       Path sourceDir = Paths.get(RmuttDirectory+"\\tempout\\");
	        
	        try {
	            DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir, "*.txt");
	            for (Path file : stream) {
	                String newtxt = "";
	                String codeContent = new String(Files.readAllBytes(file));
	                codeContent = codeContent.replace("\n", "*Enter*");
	                String[] findte = codeContent.split(Pattern.quote("//***************************************************"));
	                for (int i = 0; i < findte.length; i++) {
	                    String s = findte[i];
	                    int index = s.indexOf("macro");
	                    if (index > 0) {
	                        String definitionPartClass = findte[i].substring(0, index);
	                        String[] textWords = definitionPartClass.split("\\s+");
	                        String nextword = "";
	                        for (int j = 0; j < textWords.length - 1; j++) {
	                            nextword = "";
	                            if (isModifier(textWords[j])) {
	                                if ("visibile".equals(textWords[j + 1])) {
	                                    nextword = textWords[j + 2];
	                                } else {
	                                    nextword = textWords[j + 1];
	                                }
	                                boolean exist = isUsedInOtherFiles(nextword, findte, i);
	                                if (exist && !"stato".equals(nextword)) {
	                                    textWords[j] = "pubblico";
	                                }
	                            }
	                        }
	                        definitionPartClass = String.join(" ", textWords);
	                        findte[i] = "/*[*/\n//***************************************************\n" 
	                                    + definitionPartClass.replace("*Enter*", "\n") 
	                                    + findte[i].substring(index).replace("*Enter*", "\n");
	                        newtxt += findte[i];
	                    }
	                }

	                Files.write(file, newtxt.getBytes());
	     
	                
	                
	            } 
	        }
	            catch (IOException e) {
	                e.printStackTrace();
	            }
		
		
		return "";
	}

	private static String getFileNameWithoutExtension(Path file) {
	        String fileName = file.getFileName().toString();
	        int dotIndex = fileName.lastIndexOf('.');
	        if (dotIndex != -1) {
	            return fileName.substring(0, dotIndex);
	        }
	        return fileName;
	    }
	  
    public  void CreateLNCFiles() throws IOException {
    
    	Path sourceDir = Paths.get(RmuttDirectory+"\\out\\");
    	Path RmuttDir = Paths.get(RmuttDirectory);

        try (Stream<Path> files = Files.list(sourceDir)) {
            files.filter(file -> file.toString().endsWith(".txt"))
                 .forEach(file -> {
                	    try {
                	        String fileNameWithoutExtension = getFileNameWithoutExtension(file);
                            Path folderPath = Paths.get("output", fileNameWithoutExtension);
                            Files.createDirectories(RmuttDir.resolve(folderPath));

                            String name = nameOfClassText(Files.readString(file));

                            name = name.substring(0,name.length()-1);
                            
                           // System.out.println(RmuttDirectory+folderPath+name+ ".rfisrf_definition");
                            // Properly resolve paths using Path.resolve()
                            Path outputFile1Path = RmuttDir.resolve(folderPath).resolve(name + ".rfisrf_definition");
                            Path outputFile2Path = RmuttDir.resolve(folderPath).resolve(name + ".rfisrf_sheet");

            
                            // For demonstration, just creating empty files here
                
                         // Perform the split text operation on the provided filepath
                           splitText(file);
                         
                          
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 });
        }
    }
	
	       
	private static boolean isModifier(String word) {
	        return Arrays.asList("pubblico", "privato", "protetta", "pubblica", "privata", "protetto").contains(word);
	    }

	private static boolean isUsedInOtherFiles(String nextword, String[] findte, int currentClass) {
		    for (int i = 0; i < findte.length; i++) {
		        if (i != currentClass) {
		            if (findte[i].contains(nextword)) {
		                return true;
		            }
		        }
		    }
		    return false;
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
    
    public static String nameOfClass(String inputFilePath) throws IOException {
        String codeContent = new String(Files.readAllBytes(Paths.get(inputFilePath)));

        if (codeContent.contains("LDS")) {
            // Pattern for "della classe"
            String pattern = Pattern.quote("della classe ") + "(.*?)" + Pattern.quote("\n");
            Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(codeContent);
            if (matcher.find()) {
                return matcher.group(1).trim();
            } else {
                return "";
            }
        } else if (codeContent.contains("LDV")) {
            // Pattern for "della classe di vista"
            String pattern = Pattern.quote("della classe di vista ") + "(.*?)" + Pattern.quote("\n");
            Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(codeContent);
            if (matcher.find()) {
                return matcher.group(1).trim();
            } else {
                return "";
            }
        }

        return "";
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

            
            name = name.substring(0,name.length()-1);
            
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

    
    
    public static void splitTextIntoAIDA(String inputFile) throws IOException {
        removeLdsDFile();
        removeLdvDFile();

        System.out.println("Folders Cleaned");

        String codeContent = new String(Files.readAllBytes(Paths.get(inputFile)));
        codeContent = codeContent.replace("valore  Fal /*39,*/", "valore  False /*39,*/");

        String[] files = codeContent.split(Pattern.quote("//***************************************************"));

        System.out.println("files.length "+files.length);

        
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
                writeFile(ldvOutputFile1Path, textPart1);
                writeFile(ldvOutputFile2Path, textPart2);
            } else {
                writeFile(ldsOutputFile1Path, textPart1);
                writeFile(ldsOutputFile2Path, textPart2);
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

    private static String getFileNameWithoutExtension(String filePath) {
        return new File(filePath).getName().replaceFirst("[.][^.]+$", "");
    }

  /*  private static String getFileExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf("."));
    }
*/
    private static void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
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
    
    
    public static void removeLdsDFile() throws IOException {
        String dizionarioFile = "D://Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Dizionario.rfisrf_dictionary";
        List<String> lines = Files.readAllLines(Paths.get(dizionarioFile));
        int startIndex = 0;
        int endIndex = 0;

        for (int j = 0; j < lines.size(); j++) {
            if (lines.get(j).contains("LdS")) {
                startIndex = j;
            } else if (lines.get(j).contains("LdV")) {
                endIndex = j - 1;
                break;
            }
        }

        if (startIndex > 0 && endIndex > 0) {
            lines.subList(startIndex + 1, endIndex + 1).clear();
        }

        Files.write(Paths.get(dizionarioFile), lines);
    }

    // Method to replace Dizionario file with a template
    public static void removeLdvDFile() throws IOException {
        String dizionarioFile = "D://Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Dizionario.rfisrf_dictionary";
        String templateFile = "D://Milan/RFI/AIDA-Product_Windows/standalone/template/Dizionario.rfisrf_dictionary";
        
        Files.copy(Paths.get(templateFile), Paths.get(dizionarioFile), StandardCopyOption.REPLACE_EXISTING);
    }

    // Other helper methods...

    public  void createLNCFilesIntoAIDAFolder() throws IOException, InterruptedException {
        Path sourceDir = Paths.get("D://Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/tempout");

        DirectoryStream<Path> files = Files.newDirectoryStream(sourceDir, "*.txt");

        String ldsFolderPath = "D:/Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Sheets/Stazione/LdS";
        String ldvFolderPath = "D:/Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Sheets/Stazione/LdV";
        String reportAddress = "D:/Milan/RFI/AIDA-Product_Windows/standalone/reports/report_inputFolder.txt";

        // Remove previous files in LdS and LdV folders
        cleanDirectory(ldsFolderPath);
        cleanDirectory(ldvFolderPath);

        System.out.println("Folders Cleaned");

        
        // Iterate through files and process each one
        for (Path file : files) {
     
        	
            splitTextIntoAIDA(file.toString());

            
            System.out.println("LDS and LDV is created");

            
            // Change directory to AIDA folder
            String folderPath = "D://Milan/RFI/AIDA-Product_Windows/standalone/";
            System.setProperty("user.dir", folderPath);

            
            // Run command for AIDA code generator
            
        
                String command = "code_generator.bat";
    	        String argument = "inputFolder"; // This is the argument for the batch file
    	        String directory = "D:\\Milan\\RFI\\AIDA-Product_Windows\\standalone";

    	        CommandExecutor EX=new CommandExecutor();
    	        int exitCode=EX.ExecuteCommandWithArgument(command, directory, argument);
    	        // Create ProcessBuilder to execute the command with the input folder as an argument
    		
    	        System.out.println("Command exited with code: " + exitCode);
    		
    		System.out.println("AIDA ran");

    		
            // Switch directory back to LNCGram folder
            folderPath = "D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram";
            System.setProperty("user.dir", folderPath);

            // Read and analyze the report file
            String reportContent = new String(Files.readAllBytes(Paths.get(reportAddress)));
            if (reportContent.contains("Test Report on inputFolder: SUCCESS")) {
                createLNCFiles(file.toString());
                
                cutAndPaste(file.toString(), "D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/out");
                Path destinationFile = Paths.get("D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/output/", "Report_" + file.getFileName());
                
//                Files.copy(Paths.get(reportAddress), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                
            } else {
                cutAndPaste(file.toString(), "D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/outwitherror");
                Path destinationFile = Paths.get("D:/Milan/RFI/ToolsForLNCGenarator/rmutt.js/LNCGram/outwitherror/", "Report_" + file.getFileName());
                Files.copy(Paths.get(reportAddress), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    
    private static void cleanDirectory(String folderPath) throws IOException {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
                file.delete();
                System.out.println("removed");
            }
        }
    }


    private static void cutAndPaste(String inputFile, String destinationFolder) throws IOException {
        Path sourcePath = Paths.get(inputFile);
        Path destinationPath = Paths.get(destinationFolder, sourcePath.getFileName().toString());
           
        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
 
    
    
    public  void createLNCFiles(String filepath) throws IOException {
       
    	Path sourceDir = Paths.get(RmuttDirectory+"\\out\\");
    	Path RmuttDir = Paths.get(RmuttDirectory);

    	
        DirectoryStream<Path> files = Files.newDirectoryStream(sourceDir, "*.txt");

        for (Path file : files) {
            String fileNameWithoutExtension = getFileNameWithoutExtension(file.toString());

            // Define output paths
            Path folderPath = Paths.get(RmuttDirectory+"\\output\\"+fileNameWithoutExtension);
            
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            String name = nameOfClass(file.toString());
        
            Path outputFile1Path = RmuttDir.resolve(folderPath).resolve(name + ".rfisrf_definition");
            Path outputFile2Path = RmuttDir.resolve(folderPath).resolve(name + ".rfisrf_sheet");

            
            
            // Convert filepath to Path before passing to splitText
            Path inputPath = Paths.get(filepath); // Conversion from String to Path
            splitText(inputPath); // Pass Path object to splitText method
              }
    }
    
    
}







