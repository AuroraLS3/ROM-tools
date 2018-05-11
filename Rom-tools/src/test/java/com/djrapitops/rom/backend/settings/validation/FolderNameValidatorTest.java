package com.djrapitops.rom.backend.settings.validation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FolderNameValidatorTest {

    private final SettingValidator testedValidator = new FolderNameValidator();

    @Test
    public void isValid1() {
        assertTrue(testedValidator.isValid("folder Name 3"));
    }

    @Test
    public void isValid2() {
        assertTrue(testedValidator.isValid("folderName3"));
    }

    @Test
    public void isValid3() {
        assertTrue(testedValidator.isValid("valid name"));
    }

    @Test
    public void isInvalid1() {
        assertFalse(testedValidator.isValid(" invalid"));
    }

    @Test
    public void isInvalid2() {
        assertFalse(testedValidator.isValid("invalid "));
    }

    @Test
    public void isInvalid3() {
        assertFalse(testedValidator.isValid("inva.lid"));
    }

    @Test
    public void isInvalid4() {
        assertFalse(testedValidator.isValid("/invalid"));
    }

    @Test
    public void isInvalid5() {
        assertFalse(testedValidator.isValid(6));
    }

    @Test
    public void isInvalid6() {
        assertFalse(testedValidator.isValid(""));
    }

}