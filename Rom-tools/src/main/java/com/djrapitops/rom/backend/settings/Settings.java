package com.djrapitops.rom.backend.settings;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

/**
 * Settings enum for all settings.
 *
 * @author Rsl1122
 */
public enum Settings {

    FOLDER_ATARI_2600("Atari 2600", "atari2600"),
    FOLDER_ATARI_7800("Atari 7800", "atari7800"),
    FOLDER_NES("NES", "nes"),
    FOLDER_SNES("Super Nintendo", "snes"),
    FOLDER_GENESIS("Sega Megadrive", "megadrive"),
    FOLDER_GAME_GEAR("Game Gear", "gamegear"),
    FOLDER_N64("Nintendo 64", "n64"),
    FOLDER_GAMEBOY("Gameboy", "gb"),
    FOLDER_GAMEBOY_COLOR("Gameboy Color", "gbc"),
    FOLDER_GBA("Gameboy Advance", "gba"),
    FOLDER_GAMECUBE("Gamecube", "gamecube"),
    FOLDER_NINTENDO_DS("Nintendo DS", "ds"),
    FOLDER_WII("Nintendo Wii", "wii"),
    FOLDER_NINTENDO_3DS("Nintendo 3DS", "3ds"),
    FOLDER_NEO_GEO("NeoGeo", "neogeo"),
    FOLDER_PC_ENGINE("PC Engine", "pcengine"),
    FOLDER_VIRTUAL_BOY("Virtual Boy", "virtualboy"),
    FOLDER_PSX("Playstation 1", "psx"),
    FOLDER_PS2("Playstation 2", "ps2"),
    FOLDER_PSP("Playstation Portable", "psp"),
    FOLDER_XBOX("Xbox", "xbox"),
    FOLDER_METADATA("Unknown", "unknown"),
    FOLDER_SEGA_CD("Sega CD", "segacd"),
    FOLDER_VECTREX("Vectrex", "vectrex");

    private final String label;
    private final Serializable defaultValue;

    Settings(String label, Serializable defaultValue) {
        this.label = label;
        this.defaultValue = defaultValue;
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

    public boolean asBoolean() {
        return "true".equalsIgnoreCase(asString());
    }
}
