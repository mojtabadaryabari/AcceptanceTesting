package AcceptanceTesting;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

public class RunRmutt {

	
	private String RmuttCommand="rmutt Main.rm > tempout/out" ;
	private String RmuttDirectory="D:\\\\Milan\\\\RFI\\\\ToolsForLNCGenarator\\\\rmutt.js\\\\LNCGram";

	

	public  void CallRmutt(int i) throws IOException, InterruptedException
	{
		String NewFileName = "out" + i + ".txt";
		this.RmuttCommand="rmutt Main.rm > tempout/" +NewFileName ;
		
		CommandExecutor CX=new CommandExecutor();
		CX.ExecuteCommandWithoutArgument(RmuttCommand,RmuttDirectory);
				
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


	
    
	private static boolean isModifier(String word) {
     return Arrays.asList("pubblico", "privato", "protetta", "pubblica", "privata", "protetto").contains(word);
 }



	
	
	
	
	
}
