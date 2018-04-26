package com.djrapitops.rom.backend.settings;

import com.djrapitops.rom.exceptions.BackendException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SettingsFile is used for reading and writing the Settings values to a config file.
 *
 * @author Rsl1122
 * @see Settings
 */
public class SettingsFile {

    private final File file;

    public SettingsFile(File file) {
        this.file = file;
    }

    public void save(Map<Settings, Serializable> values) throws IOException {
        List<String> lines = turnValuesToLines(values);

        Files.write(file.toPath(), lines, Charset.forName("UTF-8"));
    }

    private List<String> turnValuesToLines(Map<Settings, Serializable> values) {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<Settings, Serializable> entry : values.entrySet()) {
            Settings key = entry.getKey();
            Serializable value = entry.getValue();
            String line = key + "=" + value;
            lines.add(line);
        }
        return lines;
    }

    public Map<Settings, Serializable> load() throws IOException {
        if (!file.exists()) {
            return getDefaultValueMap();
        }
        return getValuesFromConfig();
    }

    private Map<Settings, Serializable> getValuesFromConfig() throws IOException {
        try (Stream<String> stream = Files.lines(file.toPath(), Charset.forName("UTF-8"))) {
            List<String> lines = stream.collect(Collectors.toList());

            return turnLinesToValues(lines);
        }
    }

    private Map<Settings, Serializable> turnLinesToValues(List<String> lines) {
        Map<Settings, Serializable> values = new EnumMap<>(Settings.class);
        for (String line : lines) {
            String[] keyAndValue = line.split("=", 2);
            if (keyAndValue.length < 2) {
                throw new BackendException("Configuration file has a wrong format. " +
                        "Misconfigured line " + (lines.indexOf(line) + 1) + ": " + line);
            }
            Settings key = Settings.valueOf(keyAndValue[0]);
            Serializable value = getProperObject(keyAndValue[1]);
            values.put(key, value);
        }

        return values;
    }

    private Serializable getProperObject(String string) {
        if (StringUtils.isNumeric(string)) {
            return NumberUtils.createLong(string);
        }
        return string;
    }

    private Map<Settings, Serializable> getDefaultValueMap() {
        EnumMap<Settings, Serializable> values = new EnumMap<>(Settings.class);
        for (Settings setting : Settings.values()) {
            values.put(setting, setting.getDefaultValue());
        }
        return values;
    }
}
