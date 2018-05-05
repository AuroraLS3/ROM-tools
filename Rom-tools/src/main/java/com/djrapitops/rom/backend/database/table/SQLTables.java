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
    private final MetadataTable metadataTable;

    VersionTable versionTable;

    public SQLTables(SQLDatabase database) {
        versionTable = new VersionTable(database);
        fileTable = new FileTable(database);
        gameTable = new GameTable(database);
        metadataTable = new MetadataTable(database);
    }

    @Override
    public void createTables() {
        int newestSchemaVersion = 1;
        boolean newDatabase = versionTable.isNewDatabase();

        fileTable.createTable();
        gameTable.createTable();
        metadataTable.createTable();
        versionTable.createTable();

        if (newDatabase) {
            versionTable.setVersion(newestSchemaVersion);
        }
        // Future schema changes go here.
        if (versionTable.getVersion() < newestSchemaVersion) {
            throw new BackendException("Failed to update database schema version");
        }
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
    public MetadataTable getMetadataTable() {
        return metadataTable;
    }
}
