import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Language {
    private final HashMap<Character, Double> frequencies;
    public final String languageName;
    public Language(String filepath) {
        frequencies = loadFromFile(filepath);
        languageName = getNameWithoutExt(filepath);
    }

    public double difference(HashMap<Character, Double> source) {
        var alphabet = addKeysets(frequencies.keySet(), source.keySet());
        var sum = 0.0;
        for (var letter: alphabet) {
            var diff = frequencies.getOrDefault(letter, 0.0) - source.getOrDefault(letter, 0.0);
            sum += Math.pow(diff, 2);
        }
        return Math.pow(sum, 0.5);
    }

    private static HashMap<Character, Double> loadFromFile(String filepath) {
        var output = new HashMap<Character, Double>();
        var file = new File(filepath);
        try {
            var scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                var elems = scanner.nextLine().split(" ");
                var letter = elems[0].charAt(0);
                var freq = Double.parseDouble(elems[1]);
                output.put(letter, freq);
            }
        } catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.printf("Failed to load file %s\n", filepath);
        }
        return output;
    }

    private static <T> HashSet<T> addKeysets(Set<T> a, Set<T> b) {
        var output = new HashSet<>(a);
        output.addAll(b);
        return output;
    }

    private static String getNameWithoutExt(String filename) {
        var path = Paths.get(filename);
        var name = path.getFileName().toString();
        var dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            return name.substring(0, dotIndex);
        }
        return name;
    }
}
