package com.djrapitops.rom.backend.settings.validation;

/**
 * SettingValidator for folder names.
 *
 * @author Rsl1122
 */
public class FolderNameValidator implements SettingValidator {

    @Override
    public boolean isValid(Object obj) {
        return obj instanceof String
                && ((String) obj).length() > 0
                // Regex that only allows letters and numbers separated with single space characters
                // not starting or ending with a space.
                && ((String) obj).matches("(\\w+ ?\\w+?)*");
    }
}
