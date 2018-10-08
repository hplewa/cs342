import edu.uic.cs342.SentenceParser;
import edu.uic.cs342.SentenceParserException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class SentenceParserTest {

  static Charset TEST_CHARSET = StandardCharsets.UTF_8;

  /**
   * Reads a file into a String, normalizing the line endings to unix endings.
   *
   * @param pathName The path to file on disk, in the tests/data subdirectory.
   *
   * @return The contents of file at the given path, as a String.
   *
   * @throws IOException If there is an error reading the file from disk.
   */
  static String fileToString(String pathName) throws IOException {
    Path filePath = FileSystems.getDefault().getPath("tests", "data", pathName);
    return Files.readAllLines(filePath, SentenceParserTest.TEST_CHARSET)
        .stream()
        .map(x -> x.trim())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Turns a list of strings into a single string, by joining with "\n".
   *
   * @param inputList A list of strings.
   *
   * @return The strings in the provided list, joined with unix new line
   *         characters.
   */
  static String listToString(List<String> inputList) {
    return inputList.stream().collect(Collectors.joining("\n"));
  }

  /**
   * Runs a test text through the SentenceParser class, joined as a String.
   *
   * @param pathName The path to file on disk, in the tests/data subdirectory.
   *
   * @return The result of parsing the test text through the SentenceParser,
   *         returned as a single string, with each given sentence on
   *         a new line.
   *
   * @throws IOException If there is an error reading the file from disk.
   *
   * @throws SentenceParserException Thrown when some error occured during
   *                                 parsing a text into sentences.
   */
  static String fileToTestResult(String pathName) throws IOException, SentenceParserException {

    String fileContents = SentenceParserTest.fileToString(pathName);

    SentenceParser parser = new SentenceParser();
    List<String> testResult = parser.asList(fileContents);
    return SentenceParserTest.listToString(testResult);
  }

  /**
   * Simplest test case, with sentences ending with "."s.
   */
  @Test
  public void toSentencesExample1Test() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("1-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("1-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }

  /**
   * Testing if ellipses are handled correctly. 
   * ... does not constitute an end of sentence
   * .... should be an end of sentence
   */
  @Test
  public void ellipsesExampleTest() throws IOException, SentenceParserException {
    String correctResult = SentenceParserTest.fileToString("2-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("2-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }
  
  /**
   * Testing sentences ending in question marks or exclamation marks
   */
  @Test
  public void questionExclamatoryMarkSentences() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("3-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("3-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }

  /**
   * Testing sentences ending in a quote
   * Albert said, "Hi."    Should count as one sentence
   */
  @Test
  public void quotationSentences() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("4-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("4-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }

  /**
   * Testing if sentences will break at a title abbreviation (Mr. Ms. Mrs. Ph.D.)
   */
  @Test
  public void titleAbbreviation() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("5-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("5-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }

  /**
   * Testing if a conversation will be split properly
   * This involves many quotes in unusual positions, and nested sentences
   */
  @Test
  public void characterDialogue() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("6-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("6-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }

  /**
   * Testing if a sentence with a pause in the middle will be handled
   * Edge case for the uncommon --
   */
  @Test
  public void midSentencePause() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("7-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("7-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }

  /**
   * Testing if a sentence with a quote that ends in a terminator, while
   * the parent sentence continues will handle properly
   * Albert said, "Hi." then he left. Should be one sentence.
   */
  @Test
  public void endOfQuoteTerminator() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("8-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("8-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }

  /**
   * Testing if a sentence with a contraction will be handled
   * Using quotes to abbreviate may be interpreted to be the
   * start of a quote, which might never end.
   */
  @Test
  public void contraction() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("9-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("9-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }

  /**
   * Testing if interrobangs (?!) and triple exclamation marks will be handled
   */
  @Test
  public void specialTerminators() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("10-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("10-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }

  /**
   * Testing if sentences nested in parenthesis will remain part of the parent 
   * sentence
   */
  @Test
  public void sentenceInParentheses() throws IOException, SentenceParserException {

    String correctResult = SentenceParserTest.fileToString("11-correct.txt");
    String testResult = SentenceParserTest.fileToTestResult("11-test.txt");

    Assert.assertEquals(correctResult, testResult);
  }


}