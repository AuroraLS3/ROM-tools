package com.djrapitops.rom.backend.settings;

import com.djrapitops.rom.backend.settings.validation.SettingValidator;
import com.djrapitops.rom.backend.settings.validation.Validators;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

/**
 * Settings enum for all settings.
 *
 * @author Rsl1122
 */
public enum Settings {

    FOLDER_ATARI_2600("Atari 2600", "atari2600", Validators.FOLDER_NAME),
    FOLDER_ATARI_5200("Atari 5200", "atari5200", Validators.FOLDER_NAME),
    FOLDER_ATARI_7800("Atari 7800", "atari7800", Validators.FOLDER_NAME),
    FOLDER_NES("NES", "nes", Validators.FOLDER_NAME),
    FOLDER_SNES("Super Nintendo", "snes", Validators.FOLDER_NAME),
    FOLDER_GENESIS("Sega Megadrive", "megadrive", Validators.FOLDER_NAME),
    FOLDER_GAME_GEAR("Game Gear", "gamegear", Validators.FOLDER_NAME),
    FOLDER_N64("Nintendo 64", "n64", Validators.FOLDER_NAME),
    FOLDER_GAMEBOY("Gameboy", "gb", Validators.FOLDER_NAME),
    FOLDER_GAMEBOY_COLOR("Gameboy Color", "gbc", Validators.FOLDER_NAME),
    FOLDER_GBA("Gameboy Advance", "gba", Validators.FOLDER_NAME),
    FOLDER_GAMECUBE("Gamecube", "gamecube", Validators.FOLDER_NAME),
    FOLDER_NINTENDO_DS("Nintendo DS", "ds", Validators.FOLDER_NAME),
    FOLDER_WII("Nintendo Wii", "wii", Validators.FOLDER_NAME),
    FOLDER_NINTENDO_3DS("Nintendo 3DS", "3ds", Validators.FOLDER_NAME),
    FOLDER_NEO_GEO("NeoGeo", "neogeo", Validators.FOLDER_NAME),
    FOLDER_PC_ENGINE("PC Engine", "pcengine", Validators.FOLDER_NAME),
    FOLDER_VIRTUAL_BOY("Virtual Boy", "virtualboy", Validators.FOLDER_NAME),
    FOLDER_PSX("Playstation 1", "psx", Validators.FOLDER_NAME),
    FOLDER_PS2("Playstation 2", "ps2", Validators.FOLDER_NAME),
    FOLDER_PSP("Playstation Portable", "psp", Validators.FOLDER_NAME),
    FOLDER_XBOX("Xbox", "xbox", Validators.FOLDER_NAME),
    FOLDER_METADATA("Unknown", "unknown", Validators.FOLDER_NAME),
    FOLDER_SEGA_CD("Sega CD", "segacd", Validators.FOLDER_NAME),
    FOLDER_VECTREX("Vectrex", "vectrex", Validators.FOLDER_NAME);

    private final String label;
    private final Serializable defaultValue;
    private final SettingValidator validator;

    Settings(String label, Serializable defaultValue, SettingValidator validator) {
        this.label = label;
        this.defaultValue = defaultValue;
        this.validator = validator;
    }

    public Serializable getDefaultValue() {
        return defaultValue;
    }

    /**
     * Gets the setting value from SettingsManager or the default value if the settings have not yet been changed.
     *
     * @return value of the setting.
     * @see SettingsManager
     */
    Serializable getValue() {
        Serializable savedValue = SettingsManager.getInstance().getValue(this);
        return savedValue != null ? savedValue : defaultValue;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Sets a new value to the SettingsManager for this Enum.
     *
     * @param value new value.
     * @see SettingsManager
     */
    public void setValue(Serializable value) {
        SettingsManager.getInstance().setValue(this, value);
    }

    /**
     * Get the setting value as a number.
     *
     * @return Numeric value of the setting.
     * @throws IllegalStateException If the value is not a number.
     */
    public long asNumber() {
        String asString = asString();
        if (StringUtils.isNumeric(asString)) {
            return NumberUtils.createLong(asString);
        }
        throw new IllegalStateException("Value is not a number!");
    }

    /**
     * Get the class of the default value for the setting.
     * <p>
     * Used for determining input for settings view.
     *
     * @return Class of the default value.
     */
    public Class<? extends Serializable> getSettingClass() {
        return defaultValue.getClass();
    }

    /**
     * Get the setting value as a String.
     *
     * @return String value of the setting.
     */
    public String asString() {
        Serializable setValue = getValue();
        if (setValue instanceof String) {
            return (String) setValue;
        }
        return setValue.toString();
    }

    public boolean isValidValue(Serializable value) {
        return validator.isValid(value);
    }

    public boolean asBoolean() {
        return "true".equalsIgnoreCase(asString());
    }
}
