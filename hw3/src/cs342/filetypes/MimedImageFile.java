import java.io.File;

// https://docs.oracle.com/javase/8/docs/api/javax/imageio/ImageIO.html
import javax.imageio.ImageIO;
// https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html
import java.awt.image.BufferedImage;
// https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html
import java.io.IOException;

public abstract class MimedImageFile extends MimedFile implements Graphical {
    MimedImageFile(File file) {
        super(file);
    }
    public String getMimeTopLevelTypeName(){
        return "image";
    }
    
    public Integer getHeight() {
        int h = 0;
        try{
            BufferedImage image = ImageIO.read(file);
            h = image.getHeight();
        } catch(IOException e) {
            System.err.println("The image could not be read");
            System.exit(1);
        }
        return h;
    }
    
    public Integer getWidth() {
        int w = 0;
        try{
            BufferedImage image = ImageIO.read(file);
            w = image.getWidth();
        } catch(IOException e) {
            System.err.println("The image could not be read");
            System.exit(1);
        }
        return w;
    }
}
