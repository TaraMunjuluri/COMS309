package onetomany.BadWords;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Service
public class ProfanityFilterService {

    private final Set<String> profaneWords = new HashSet<>();

    public ProfanityFilterService() {
        loadProfanityList();
    }

    private void loadProfanityList() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/profanity.txt")))) {
            String word;
            while ((word = reader.readLine()) != null) {
                profaneWords.add(word.toLowerCase());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load profanity list", e);
        }
    }

    public boolean containsProfanity(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        String lowerCaseText = text.toLowerCase();
        for (String badWord : profaneWords) {
            if (lowerCaseText.contains(badWord)) {
                return true;
            }
        }
        return false;
    }

    public String censorProfanity(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String censoredText = text;
        for (String badWord : profaneWords) {
            censoredText = censoredText.replaceAll("(?i)" + badWord, "****");
        }
        return censoredText;
    }
}
