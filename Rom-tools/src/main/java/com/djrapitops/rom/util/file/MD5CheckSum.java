package com.djrapitops.rom.util.file;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Utility for creating MD5 CheckSums of file contents.
 * <p>
 * Users Apache Commons Codec.
 *
 * @author Rsl1122
 */
public class MD5CheckSum {

    private final File file;

    public MD5CheckSum(File file) {
        this.file = file;
    }

    /**
     * Creates MD5 Hash
     *
     * @return MD5 Hash of the bytes in the file.
     * @throws IOException If File can not be operated on.
     */
    public String toHash() throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            return DigestUtils.md5Hex(in);
        }
    }
}
