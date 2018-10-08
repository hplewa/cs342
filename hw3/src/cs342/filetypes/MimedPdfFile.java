// https://docs.oracle.com/javase/8/docs/api/java/io/File.html
import java.io.File;
// https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html
import java.io.IOException;
// https://pdfbox.apache.org/docs/2.0.5/javadocs/org/apache/pdfbox/pdmodel/PDDocument.html
import org.apache.pdfbox.pdmodel.PDDocument;
// https://pdfbox.apache.org/docs/2.0.5/javadocs/org/apache/pdfbox/pdmodel/PDPage.html
import org.apache.pdfbox.pdmodel.PDPage;
// https://pdfbox.apache.org/docs/2.0.5/javadocs/org/apache/pdfbox/pdmodel/common/PDRectangle.html
import org.apache.pdfbox.pdmodel.common.PDRectangle;
// https://pdfbox.apache.org/docs/2.0.5/javadocs/org/apache/pdfbox/pdmodel/encryption/InvalidPasswordException.html
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
// https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html
// (not needed by the compiler, but provided as a nudge to the programmer :)
import java.lang.Math;

//// https://docs.oracle.com/javase/8/docs/api/javax/imageio/ImageIO.html
//import javax.imageio.ImageIO;
//// https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html
//import java.awt.image.BufferedImage;
//// https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html
//import java.io.IOException;

/**
 * Class for representing Text files (mime type application/pdf).
 */
public class MimedPdfFile extends MimedApplicationFile implements Multipart, Graphical {

  MimedPdfFile(File file) {
    super(file);
  }
    public String getMimeSubTypeName() {
        return "pdf";
    }
    
    public Integer getNumParts() {
        int numParts = 0;
        try{
            PDDocument pdf = PDDocument.load(file);
            numParts = pdf.getNumberOfPages();
            pdf.close();
        }catch(InvalidPasswordException e) {
            System.err.println("This pdf requires a password");
        }catch(IOException e) {
            System.err.println("Could not read the file");
            System.exit(1);
        }
        return numParts;
    }

    public Integer getHeight() {
        int h = 0;
        try{
            PDDocument pdf = PDDocument.load(file);
            PDPage page = pdf.getPage(0);
            PDRectangle box = page.getBBox();
            h = (int)box.getHeight();
            pdf.close();

        } catch(IOException e) {
            System.err.println("The file could not be read");
            System.exit(1);
        } catch(NullPointerException e) {
            System.err.println("The file could not be read");
            System.exit(1);
        }
        return h;
    }

    public Integer getWidth() {
        int w = 0;
        try{
            PDDocument pdf = PDDocument.load(file);
            PDPage page = pdf.getPage(0);
            PDRectangle box = page.getBBox();
            w = (int)box.getWidth();
            pdf.close();

        } catch(IOException e) {
            System.err.println("The file could not be read");
            System.exit(1);
        } catch(NullPointerException e) {
            System.err.println("The file could not be read");
            System.exit(1);
        }

        return w;
    }
}
