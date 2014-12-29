package com.lweynant.wastecalendar.provider;

public class TypeTranslator {

    private LocalizedType[] localizedTypes;
    private DatabaseType[] databaseTypes;

    public TypeTranslator(LocalizedType[] localizedTypes,
                          DatabaseType[] databaseTypes) {
        this.localizedTypes = localizedTypes;
        this.databaseTypes = databaseTypes;
    }


    public DatabaseType toDatabaseFormat(LocalizedType type) {
        for (int i = 0; i < localizedTypes.length; i++) {
            LocalizedType l = localizedTypes[i];
            if (l.equals(type)) {
                return databaseTypes[i];
            }
        }
        return null;
    }

    public LocalizedType fromDatabaseFormat(DatabaseType type) {
        for (int i = 0; i < databaseTypes.length; i++) {
            DatabaseType l = databaseTypes[i];
            if (l.equals(type)) {
                return localizedTypes[i];
            }
        }
        return null;
    }

}
