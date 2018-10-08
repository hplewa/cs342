package edu.uic.cs342;

import edu.uic.cs342.SentenceParserException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class SentenceParser {

  /**
   * Parses a given text and splits it into sentences.
   *
   * @param text A text to split into sentences.
   *
   * @return A list of Strings, each being a full English sentence.
   *
   * @throws SentenceParserException Generic exception class to indicate
   *                                 something went wrong during parsing
   *                                 the text into sentences.
   */
  public List<String> asList(String text) throws SentenceParserException {
    //Removing unneeded whitespace
    text = text.trim();

    //Tokenizing bothersome punctuation.
    //Replacing ellipses 
    text = text.replaceAll("\\.{3}", "hp_ellipse");

    //Replacing nested periods, question, and exclamation marks
    text = text.replaceAll("\\.(?=[‘’'\"]\\s[a-z])", "hp_per_in_quote");
    text = text.replaceAll("\\?(?=[‘’'\"]\\s[a-z])", "hp_quest_in_quote");
    text = text.replaceAll("!(?=[‘’'\"]\\s[a-z])", "hp_exclam_in_quote");

    //Iteratively reading the text to parse it
    Scanner sc = new Scanner(text);
    ArrayList<String> sentences = new ArrayList<>();
    String buildSentence = new String();

    //Counter to keep track of whether we are nested in a quote
    int inQuote = 0;

    //An end of sentence is -- . ! ? which may be preceded or succeeded by a quote
    Pattern terminators = Pattern.compile("[‘’'\"]?(--|\\.|!|\\?)[‘’'\"]?");

    //A quote opens with a letter succeeding it
    Pattern openQuote = Pattern.compile("[{<\\[\\(‘’'\"]\\w");

    /* 
      A quote ends when a lowercase letter or number is succeeded by a terminator and 
      quote mark in some order
    */
    Pattern closeQuote = Pattern.compile("[a-z0-9][\\.!?]?(--[}>\\]\\)‘’'\"]|(,[}>\\]\\)‘’'\"])|}|>|\\]|\\)|‘|’|'|\")[\\.!?]?");

    //Mapping of common abbreviations which should not be counted as terminators
    Pattern invalidEnd = Pattern.compile("(Mr\\.)|(Ms\\.)|(Mrs\\.)|(Dr.)|(Ph\\.D\\.)|(PhD\\.)|(Prof\\.)|(Col\\.)|(Rev\\.)|(\\.\\))|(!\\))|(\\?\\))|(\\.])|(!])|(\\?])|(\\.})|(!})|(\\?}|(\\.\\>)|(!\\>)|(\\?>))");

    //Looping through the entire text, word by word
    while(sc.hasNext()) {    
      String cur = sc.next();
      cur.trim();
      Matcher openQ = openQuote.matcher(cur);
      Matcher closeQ = closeQuote.matcher(cur);
      buildSentence = buildSentence + cur + " ";

      /*
        If we find an open quote, then a counter keeping track of what level of 
        nesting the reader is in increments. If a closing quote is found
        the counter decrements.
      */
      if(openQ.find() == true) {
        inQuote++;
      }
      if(closeQ.find() == true && inQuote > 0) {
        inQuote--;
      }

      //A new sentence can only be added if we are not in nested quotes
      if(inQuote == 0) {
        Matcher term = terminators.matcher(cur);
        //If there is a terminator in the word
        if(term.find() == true) {
          Matcher invalid = invalidEnd.matcher(cur);
          //If there is not an abbreviation
          if(invalid.find() == false) {
            buildSentence = buildSentence.replaceAll("hp_ellipse","...");
            buildSentence = buildSentence.replaceAll("hp_per_in_quote", ".");
            buildSentence = buildSentence.replaceAll("hp_quest_in_quote", "?");
            buildSentence = buildSentence.replaceAll("hp_exclam_in_quote", "!");
            sentences.add(new String(buildSentence));
            buildSentence = new String();
          } 
        }
      }
    }
    sc.close();

    List<String> parsedText = sentences.stream()
                                        .map(x -> x.trim())
                                        .collect(Collectors.toList());

    return parsedText;
  }
}