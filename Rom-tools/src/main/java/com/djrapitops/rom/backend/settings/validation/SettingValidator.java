package com.djrapitops.rom.backend.settings.validation;

/**
 * Interface for validating if user supplied settings are valid.
 *
 * @author Rsl1122
 */
public interface SettingValidator {

    boolean isValid(Object obj);

}
