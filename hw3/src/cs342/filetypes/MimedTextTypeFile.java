import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;


public abstract class MimedTextTypeFile extends MimedFile implements Textual{
    MimedTextTypeFile(File file) {
        super(file);
    }
    public String getMimeTopLevelTypeName(){
        return "text";
    }
    
    /*
     * Returns an count of how many lines are in the file
     */
    public Integer getNumberOfLines() {
        Integer numLines = 1;
        try{
            Scanner in = new Scanner(file);
            while(in.hasNextLine()){
                String s = in.nextLine();
                numLines++;
            }
        } catch(FileNotFoundException e){
            System.err.println("The file could not be found");
        }
        return numLines;
    }
    
    /*
     * Returns an arraylist of each line of the textual file being read
     */
    public ArrayList<String> getLinesOfText() {
        ArrayList<String> lines = new ArrayList<>();
        try{
            Scanner in = new Scanner(file);
            while(in.hasNextLine()){
                 lines.add(in.nextLine());
            }
        } catch(FileNotFoundException e){
            System.err.println("The file could not be found");
        }
        return lines;
    }
}
