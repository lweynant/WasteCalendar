package com.lweynant.wastecalendar.model;

import com.lweynant.wastecalendar.provider.DatabaseType;
import com.lweynant.wastecalendar.provider.LocalizedType;


public interface ITypeTranslator {

    public abstract DatabaseType toDatabaseFormat(LocalizedType type);

    public abstract LocalizedType fromDatabaseFormat(DatabaseType type);

}