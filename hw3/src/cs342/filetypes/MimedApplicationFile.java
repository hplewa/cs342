import java.io.File;

public abstract class MimedApplicationFile extends MimedFile {
    MimedApplicationFile(File file) {
        super(file);
    }
    public String getMimeTopLevelTypeName(){
        return "application";
    }
}
