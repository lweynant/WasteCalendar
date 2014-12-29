package com.lweynant.wastecalendar.model;

import com.lweynant.wastecalendar.provider.DatabaseType;
import com.lweynant.wastecalendar.provider.WasteProviderMetaData.WasteEventTableMetaData;

public class WasteEventKeyValues implements IWasteEventKeyValues {
    private IWasteEventFactory factory;

    public WasteEventKeyValues(IWasteEventFactory f) {
        this.factory = f;

    }

    @Override
    public void writeTo(IWasteEvent event, IKeyValuesWriter writer) {
        writer.put(WasteEventTableMetaData.WASTE_EVENT_TYPE, factory.toDatabaseFormat(event.type()).value());
        writer.put(WasteEventTableMetaData.WASTE_EVENT_COLLECTED, event.isCollected() ? 1 : 0);
        IDate collectionDate = event.collectionDate();
        writer.put(WasteEventTableMetaData.WASTE_EVENT_YEAR, collectionDate.year());
        writer.put(WasteEventTableMetaData.WASTE_EVENT_MONTH, collectionDate.month());
        writer.put(WasteEventTableMetaData.WASTE_EVENT_DAY, collectionDate.dayOfMonth());
    }

    @Override
    public IWasteEvent readFrom(IKeyValuesReader reader) {
        IDate date = readDateFrom(reader);
        DatabaseType type = readEventFrom(reader);
        IWasteEvent event = factory.createWasteEvent(type, date);
        event.setCollected(readCollectedFrom(reader));
        return event;
    }

    private boolean readCollectedFrom(IKeyValuesReader reader) {
        return reader.getInt(WasteEventTableMetaData.WASTE_EVENT_COLLECTED) == 0 ? false : true;
    }

    private DatabaseType readEventFrom(IKeyValuesReader reader) {
        return new DatabaseType(reader.getString(WasteEventTableMetaData.WASTE_EVENT_TYPE));
    }

    private IDate readDateFrom(IKeyValuesReader reader) {
        Date date = new Date(reader.getInt(WasteEventTableMetaData.WASTE_EVENT_YEAR),
                reader.getInt(WasteEventTableMetaData.WASTE_EVENT_MONTH),
                reader.getInt(WasteEventTableMetaData.WASTE_EVENT_DAY));
        return date;
    }


}
