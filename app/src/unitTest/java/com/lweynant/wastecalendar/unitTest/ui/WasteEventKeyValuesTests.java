package com.lweynant.wastecalendar.unitTest.ui;

import android.test.suitebuilder.annotation.SmallTest;

import com.lweynant.wastecalendar.model.Date;
import com.lweynant.wastecalendar.model.IKeyValuesReader;
import com.lweynant.wastecalendar.model.IKeyValuesWriter;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.IWasteEventFactory;
import com.lweynant.wastecalendar.model.Waste;
import com.lweynant.wastecalendar.model.WasteEventKeyValues;
import com.lweynant.wastecalendar.provider.DatabaseType;
import com.lweynant.wastecalendar.provider.LocalizedType;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WasteEventKeyValuesTests extends TestCase {

    class KeyValues implements IKeyValuesReader, IKeyValuesWriter {
        HashMap<String, Integer> integers = new HashMap<String, Integer>();
        HashMap<String, String> strings = new HashMap<String, String>();

        @Override
        public void put(String key, String value) {
            strings.put(key, value);
        }

        @Override
        public void put(String key, int value) {
            integers.put(key, value);
        }

        @Override
        public String getString(String key) {
            return strings.get(key);
        }

        @Override
        public int getInt(String key) {
            return integers.get(key);
        }

    }

    private DatabaseType databaseType;
    private LocalizedType localizedType;
    private IWasteEventFactory factory;
    private Date date;
    private Waste wasteEvent;

    public void setUp() throws Exception {
        databaseType = new DatabaseType("db-type");
        localizedType = new LocalizedType("local-type");
        date = new Date(2000, Calendar.AUGUST, 23);
        wasteEvent = new Waste(localizedType, 2, date);
        factory = mock(IWasteEventFactory.class);
        when(factory.toDatabaseFormat(localizedType)).thenReturn(databaseType);
        when(factory.fromDatabaseFormat(databaseType)).thenReturn(localizedType);
        when(factory.createWasteEvent(databaseType, date)).thenReturn(wasteEvent);
        when(factory.createWasteEvent(localizedType, date)).thenReturn(wasteEvent);
    }

    @SmallTest
    public void testWriteFollowedByReadGivesTheSameWasteEvent() {
        WasteEventKeyValues sut = new WasteEventKeyValues(factory);
        IWasteEvent writtenEvent = wasteEvent;
        KeyValues kv = new KeyValues();
        sut.writeTo(writtenEvent, kv);

        IWasteEvent readEvent = sut.readFrom(kv);
        assertThat(readEvent, is(writtenEvent));
    }

    @SmallTest
    public void testWrittenCollectedEventIsReadAsCollectedEvent() {
        WasteEventKeyValues sut = new WasteEventKeyValues(factory);
        IWasteEvent writtenEvent = wasteEvent;
        writtenEvent.setCollected(true);
        KeyValues kv = new KeyValues();
        sut.writeTo(writtenEvent, kv);

        IWasteEvent readEvent = sut.readFrom(kv);
        assertThat(readEvent, is(writtenEvent));
        assertThat(readEvent.isCollected(), is(true));
    }

}
