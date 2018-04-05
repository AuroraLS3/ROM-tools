package com.djrapitops.rom.backend.database.table;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TableTest {

    @Test
    public void getTableColumn() {
        String column = FileTable.Col.FILE_PATH;

        String expected = FileTable.TABLE_NAME + "." + column;
        String result = Table.getTableColumn(new FileTable(null), column);
        assertEquals(expected, result);
    }
}