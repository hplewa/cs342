// https://docs.oracle.com/javase/8/docs/api/java/io/File.html
import java.io.File;

public abstract class MimedFile {

  protected File file;

  MimedFile(File file) {
    this.file = file;
  }

  /**
   * Returns the full mime type for the file type being represented.
   * For example, if a zip file is being represented, which has mime type
   * "application/zip", this method should return "application".
   *
   * You should not make any changes to this method definition.
   */
  final public String getFullMimeType() {
    return this.getMimeTopLevelTypeName() + "/" + this.getMimeSubTypeName();
  }
  
  /**
   * Returns the top level mime type.  For example, zip files have the mimetype
   * "application/zip".  In this case, this method should return the string
   * "application".
   */
    public abstract String getMimeTopLevelTypeName();


  /**
   * Returns the sub-type portion of this files mimetype.  For example,
   * given a zip file with the mimetype "application/zip", this method
   * should return "zip".
   */
    public abstract String getMimeSubTypeName();


  /**
   * This method should return the name of the file loaded.  This *should
   * not* include any path information.  For example, if this file was
   * loaded from "/home/user/file.txt", this method should return "file.txt".
   */
  public String getFileName() {
    return file.getName();
  }

  /**
   * Returns the size of the file being represented, as a number of bytes.
   */
  public Long getFileSize() {
    return file.length();
  }
}
