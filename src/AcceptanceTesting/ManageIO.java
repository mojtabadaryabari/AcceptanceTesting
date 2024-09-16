package AcceptanceTesting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;

public final class ManageIO {

	
	  public static void cleanDirectory(String folderPath) throws IOException {
	        File folder = new File(folderPath);
	        if (!folder.exists()) {
	            folder.mkdirs();
	        }

	        for (File file : Objects.requireNonNull(folder.listFiles())) {
	            if (file.isFile()) {
	       //         System.out.println(file.getAbsolutePath());
	                file.delete();
	         //       System.out.println("removed");
	            }
	        }
	    }

	  public static void cutAndPasteGeneratedLNC(String sourceFolder, String destinationFolder) throws IOException {
	    
		  

	        
	        
	        try {
	            Path sourceDir = Paths.get(sourceFolder);
	            Path destinationDir = Paths.get(destinationFolder);


	            // Find the single file in the source directory
	            Path fileToMove = Files.list(sourceDir).filter(Files::isRegularFile).findFirst().orElseThrow(() -> new Exception("No file found in the source folder"));

	            // Move the file to the destination directory
	            Path destinationPath = destinationDir.resolve(fileToMove.getFileName());
	            Files.move(fileToMove, destinationPath, StandardCopyOption.REPLACE_EXISTING);

	        } catch (Exception e) {
	            System.err.println("Error occurred: " + e.getMessage());
	            e.printStackTrace();
	        }
	        
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


	  public static void removeLdvDFile() throws IOException {
	        String dizionarioFile = "D://Milan/RFI/AIDA-Product_Windows/standalone/inputFolder/Dizionario.rfisrf_dictionary";
	        String templateFile = "D://Milan/RFI/AIDA-Product_Windows/standalone/template/Dizionario.rfisrf_dictionary";
	        
	        Files.copy(Paths.get(templateFile), Paths.get(dizionarioFile), StandardCopyOption.REPLACE_EXISTING);
	    }
	  
	  public static void writeFile(String filePath, String content) throws IOException {
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	            writer.write(content);
	        }
	    }

/*	  public static String getFileNameWithoutExtension(String filePath) {
	        return new File(filePath).getName().replaceFirst("[.][^.]+$", "");
	    }*/
	  
	  public static String getFileNameWithoutExtension(Path file) {
	        String fileName = file.getFileName().toString();
	        int dotIndex = fileName.lastIndexOf('.');
	        if (dotIndex != -1) {
	            return fileName.substring(0, dotIndex);
	        }
	        return fileName;
	    }

	  public static void FoldedrCopy(String source,String target)
	  { 
      
      Path sourceDir = Paths.get(source);
      Path targetDir = Paths.get(target);

      try {
    	  
    	  
    	   if (Files.exists(targetDir)) {
               deleteDirectory(targetDir);
           }
    	   
          Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
              @Override
              public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                  Path targetPath = targetDir.resolve(sourceDir.relativize(dir));
                  if (!Files.exists(targetPath)) {
                      Files.createDirectory(targetPath);
                  }
                  return FileVisitResult.CONTINUE;
              }

              @Override
              public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                  Files.copy(file, targetDir.resolve(sourceDir.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                  return FileVisitResult.CONTINUE;
              }
          });
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
		  
	  private static void deleteDirectory(Path dir) throws IOException {
	        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
	            @Override
	            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	                Files.delete(file);
	                return FileVisitResult.CONTINUE;
	            }

	            @Override
	            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
	                Files.delete(dir);
	                return FileVisitResult.CONTINUE;
	            }
	        });
	    }
	    

	  
}
