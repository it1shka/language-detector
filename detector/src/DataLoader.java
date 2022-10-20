import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class DataLoader {
    public static ArrayList<Language> loadLanguages(String directory) throws Exception {
        var dir = new File(directory);
        if (!dir.isDirectory()) {
            throw new Exception("Can load languages only from directory");
        }
        var languages = new ArrayList<Language>();;
        for (var file: Objects.requireNonNull(dir.listFiles())) {
            var filename = file.getPath();
            if (!fileExtension(filename).equals(".freqs")) {
                continue;
            }
            var language = new Language(filename);
            languages.add(language);
        }
        return languages;
    }

    public static HashMap<Character, Double> loadSourceFile(String filename) throws FileNotFoundException {
        var file = new File(filename);
        var scanner = new Scanner(file);
        var counter = new HashMap<Character, Integer>();
        var totalLetters = 0;
        while (scanner.hasNext()) {
            var cur = scanner.next().charAt(0);
            if (!Character.isLetter(cur)) continue;
            counter.merge( Character.toLowerCase(cur), 1, Integer::sum);
            totalLetters++;
        }
        var output = new HashMap<Character, Double>();
        for (var entry: counter.entrySet()) {
            var key = entry.getKey();
            var value = (double)entry.getValue() * 100.0 / (double)totalLetters;
            output.put(key, value);
        }
        return output;
    }

    private static String fileExtension(String filename) {
        var dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0) {
            return filename.substring(dotIndex);
        }
        return "";
    }
}
