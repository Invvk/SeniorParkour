package io.github.invvk.seniorparkour.database;

import io.github.invvk.seniorparkour.database.manager.IDataManager;
import io.github.invvk.seniorparkour.database.storage.IStorage;
import io.github.invvk.seniorparkour.database.storage.MySQLStorage;
import lombok.Getter;

public class StorageManager {

    @Getter private final IStorage storage;

    public StorageManager() {
        this.storage = new MySQLStorage();
        this.storage.init();
    }

    public void close() {
        if (this.storage != null)
            this.storage.close();
    }

    public IDataManager getDataManager() {
        return this.storage.getDataManager();
    }
}
