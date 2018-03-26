package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.Tables;
import com.djrapitops.rom.exceptions.BackendException;

/**
 * SQL Database implementation for Tables interface.
 *
 * @author Rsl1122
 */
public class SQLTables implements Tables {

    private final FileTable fileTable;
    private final GameTable gameTable;
    private final GamesToFilesTable gamesToFilesTable;
    private final MetadataTable metadataTable;

    private final VersionTable versionTable;

    public SQLTables(SQLDatabase database) {
        versionTable = new VersionTable(database);
        fileTable = new FileTable(database);
        gameTable = new GameTable(database);
        gamesToFilesTable = new GamesToFilesTable(database);
        metadataTable = new MetadataTable(database);
    }

    @Override
    public void createTables() throws BackendException {
        fileTable.createTable();
        gameTable.createTable();
        gamesToFilesTable.createTable();
        metadataTable.createTable();
        versionTable.createTable();
    }

    @Override
    public FileTable getFileTable() {
        return fileTable;
    }

    @Override
    public GameTable getGameTable() {
        return gameTable;
    }

    @Override
    public GamesToFilesTable getGamesToFilesTable() {
        return gamesToFilesTable;
    }

    @Override
    public MetadataTable getMetadataTable() {
        return metadataTable;
    }
}
