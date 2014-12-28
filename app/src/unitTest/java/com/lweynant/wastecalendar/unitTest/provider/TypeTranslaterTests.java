package com.lweynant.wastecalendar.unitTest.provider;

import android.test.suitebuilder.annotation.SmallTest;

import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.provider.DatabaseType;
import com.lweynant.wastecalendar.provider.LocalizedType;
import com.lweynant.wastecalendar.provider.TypeTranslator;

import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeTranslaterTests  extends TestCase{

	private ILocalizer localizer;

	public void setUp() throws Exception {
		localizer = mock(ILocalizer.class);
		when(localizer.string(anyInt())).thenReturn("unstubbed");
	}

	@SmallTest
	public void translateToDatabaseFormatOnExistingType(){
		LocalizedType[] localizedTypes = {new LocalizedType("local-type")};
		DatabaseType[] databaseTypes = {new DatabaseType("db-type")};
		TypeTranslator sut = new TypeTranslator(localizedTypes, databaseTypes);
		DatabaseType type = sut.toDatabaseFormat(localizedTypes[0]);
		assertThat(type, is(databaseTypes[0]));
	}
	@SmallTest
	public void translateFromDatabaseFormatOnExistingType(){
		LocalizedType[] localizedTypes = {new LocalizedType("local-type")};
		DatabaseType[] databaseTypes = {new DatabaseType("db-type")};
		TypeTranslator sut = new TypeTranslator(localizedTypes, databaseTypes);
		LocalizedType type = sut.fromDatabaseFormat(databaseTypes[0]);
		assertThat(type, is(localizedTypes[0]));
	}
	
}
