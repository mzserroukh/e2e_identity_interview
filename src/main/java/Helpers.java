import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    public List<String> getAllNumberPlates(final String numberPlatesPattern, final String fileName) {
        List<String> foundPlates = new ArrayList<>();
        String filePath = String.format("%s/src/main/resources/%s", System.getProperty("user.dir"), fileName);

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(numberPlatesPattern);

        // Read the file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Find matches in the line
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {

                    // Add the matching number plate to the list
                    foundPlates.add(matcher.group());
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        return foundPlates;
    }

    public String returnOutputSentenceBasedOnNumberPlates(final String numberPlate, final String fileName) {
        String filePath = String.format("%s/src/main/resources/%s", System.getProperty("user.dir"), fileName);

        // Read the file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith(numberPlate)) {
                    return line;  // Return the matching line
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String formatPatternNumberPlate(String numberPlate) {

        // remove all spaces first
        String numberPlateWithoutSpace = numberPlate.replaceAll(" ", "");
        return numberPlateWithoutSpace.substring(0, 4) + " " + numberPlateWithoutSpace.substring(4);
    }
}
