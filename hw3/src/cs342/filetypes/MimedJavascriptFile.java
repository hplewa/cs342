// https://docs.oracle.com/javase/8/docs/api/java/io/File.html
import java.io.File;
// https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
import java.util.ArrayList;

/**
 * Class for representing Text files (mime type application/x-javascript).
 */
public class MimedJavascriptFile extends MimedTextTypeFile {

  MimedJavascriptFile(File file) {
    super(file);
  }
    
    public String getMimeSubTypeName() {
        return "js";
    }
}
