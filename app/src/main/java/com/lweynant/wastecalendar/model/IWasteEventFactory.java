package com.lweynant.wastecalendar.model;

import com.lweynant.wastecalendar.provider.DatabaseType;
import com.lweynant.wastecalendar.provider.LocalizedType;

public interface IWasteEventFactory extends ITypeTranslator {

    public abstract LocalizedType[] getPossibleTypes();

    public abstract IWasteEvent createWasteEvent(DatabaseType type, IDate date);

    public abstract IWasteEvent createWasteEvent(LocalizedType type, IDate date);

}