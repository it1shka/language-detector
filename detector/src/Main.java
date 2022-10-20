public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Expected some text input file as an argument");
            System.exit(1);
        }

        var input = args[0];
        try {
            var currentFrequencies = DataLoader.loadSourceFile(input);
            var languageDataSource = ".language_data";
            if (args.length >= 2) {
                languageDataSource = args[1];
            } else {
                System.out.printf("Using default location for language data: %s\n", languageDataSource);
            }
            var languages = DataLoader.loadLanguages(languageDataSource);
            if (languages.size() == 0) {
                System.out.println("No languages were provided");
                System.exit(1);
            } else {
                System.out.printf("%d languages were provided\n", languages.size());
            }

            var closestLanguage = "alien";
            var minimalDiff = Double.POSITIVE_INFINITY;
            for (var language: languages) {
                var currentDiff = language.difference(currentFrequencies);
                if (minimalDiff > currentDiff) {
                    minimalDiff = currentDiff;
                    closestLanguage = language.languageName;
                }
                System.out.printf("%s: difference %f\n", language.languageName, currentDiff);
            }
            System.out.printf("Closest language: %s with minimal difference %f\n", closestLanguage, minimalDiff);

        } catch (Exception e) {
            System.out.printf("Error occured: %s\n", e.getMessage());
            e.printStackTrace();
        }
    }

}