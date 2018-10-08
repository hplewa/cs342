// https://docs.oracle.com/javase/8/docs/api/java/io/File.html
import java.io.File;
// https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html
import java.io.IOException;
// https://docs.oracle.com/javase/8/docs/api/java/util/zip/ZipFile.html
import java.util.zip.ZipFile;
// https://docs.oracle.com/javase/8/docs/api/java/util/zip/ZipException.html
import java.util.zip.ZipException;

/**
 * Class for representing Text files (mime type application/x-java-archive).
 */
public class MimedJarFile extends MimedApplicationFile implements Multipart {

  MimedJarFile(File file) {
    super(file);
  }
    public String getMimeSubTypeName() {
        return "jar";
    }
    
    public Integer getNumParts() {
        int p = 0;
        
        try{
            ZipFile z = new ZipFile(file);
            
            p = z.size();
        } catch(IOException e) {
            System.err.println("The jar file could not be read");
        }
        
        return p;
    }
}
