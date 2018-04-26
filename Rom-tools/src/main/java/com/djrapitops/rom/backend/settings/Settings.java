package com.djrapitops.rom.backend.settings;

import java.io.Serializable;

/**
 * Settings enum for all settings.
 *
 * @author Rsl1122
 */
public enum Settings {

    FOLDER_ATARI_2600("atari2600"),
    FOLDER_ATARI_7800("atari7800"),
    FOLDER_NES("nes"),
    FOLDER_SNES("snes"),
    FOLDER_GENESIS("megadrive"),
    FOLDER_GAME_GEAR("gamegear"),
    FOLDER_N64("n64"),
    FOLDER_GAMEBOY("gb"),
    FOLDER_GAMEBOY_COLOR("gbc"),
    FOLDER_GBA("gba"),
    FOLDER_GAMECUBE("gamecube"),
    FOLDER_NINTENDO_DS("ds"),
    FOLDER_WII("wii"),
    FOLDER_NINTENDO_3DS("3ds"),
    FOLDER_NEO_GEO("neogeo"),
    FOLDER_PC_ENGINE("pc engine"),
    FOLDER_VIRTUAL_BOY("virtual boy"),
    FOLDER_PSX("psx"),
    FOLDER_PS2("ps2"),
    FOLDER_PSP("psp"),
    FOLDER_XBOX("xbox"),
    FOLDER_METADATA("unknown"),
    FOLDER_SEGA_CD("segacd"),
    FOLDER_VECTREX("vectrex");

    private final Serializable defaultValue;

    Settings(Serializable defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Serializable getDefaultValue() {
        return defaultValue;
    }

    Serializable getValue() {
        Serializable value = SettingsManager.getInstance().getValue(this);
        return value != null ? value : defaultValue;
    }

    public double getNumber() {
        Serializable value = getValue();
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Long) {
            return (Long) value;
        }
        throw new IllegalStateException("Value is not a number!");
    }

    public Class<? extends Serializable> getSettingClass() {
        return defaultValue.getClass();
    }

    public String getString() {
        Serializable value = getValue();
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }
}
