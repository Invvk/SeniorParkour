package io.github.invvk.seniorparkour.database.storage;

import io.github.invvk.seniorparkour.database.manager.IDataManager;

public interface IStorage {

    void init();

    void close();

    IDataManager getDataManager();

}
