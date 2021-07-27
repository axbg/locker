package locker.service.impl;

import locker.service.PasswordService;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    private static final int LOWER_CASE_CHARS = 5;
    private static final int UPPER_CASE_CHARS = 5;
    private static final int DIGIT_CHARS = 6;
    private static final int SPECIAL__CHARS = 6;

    @Override
    public String generateStrongPassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
        lowerCaseRule.setNumberOfCharacters(5);

        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
        upperCaseRule.setNumberOfCharacters(5);

        CharacterRule digitsRule = new CharacterRule(EnglishCharacterData.Digit);
        digitsRule.setNumberOfCharacters(6);

        CharacterRule specialRule = new CharacterRule(EnglishCharacterData.Special);
        specialRule.setNumberOfCharacters(6);

        return passwordGenerator.generatePassword(LOWER_CASE_CHARS + UPPER_CASE_CHARS + DIGIT_CHARS + SPECIAL__CHARS,
                lowerCaseRule, upperCaseRule, digitsRule, specialRule);
    }
}
