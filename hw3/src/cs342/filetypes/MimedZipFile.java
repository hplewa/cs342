// https://docs.oracle.com/javase/8/docs/api/java/io/File.html
import java.io.File;
// https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html
import java.io.IOException;
// https://docs.oracle.com/javase/8/docs/api/java/util/Enumeration.html
import java.util.Enumeration;
// https://docs.oracle.com/javase/8/docs/api/java/util/zip/ZipFile.html
import java.util.zip.ZipFile;
// https://docs.oracle.com/javase/8/docs/api/java/util/zip/ZipEntry.html
import java.util.zip.ZipEntry;
// https://docs.oracle.com/javase/8/docs/api/java/util/zip/ZipException.html
import java.util.zip.ZipException;

/**
 * Class for representing Text files (mime type application/zip).
 */
public class MimedZipFile extends MimedApplicationFile implements Multipart{

  MimedZipFile(File file) {
    super(file);
  }
    public String getMimeSubTypeName() {
        return "zip";
    }
    
    public Integer getNumParts() {
        int p = 0;
        
        ZipFile z;
        try{
            z = new ZipFile(file);
        } catch(IOException e) {
            System.err.println("Encountered an error reading this zip file");
            return 0;
        }
        try{
            p = z.size();
        } catch(IllegalStateException e) {
            System.err.println("The file was prematurely closed");
        }
        
        return p;
    }
}
