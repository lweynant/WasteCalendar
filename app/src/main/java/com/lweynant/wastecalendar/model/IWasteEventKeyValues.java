package com.lweynant.wastecalendar.model;

public interface IWasteEventKeyValues {

    void writeTo(IWasteEvent event, IKeyValuesWriter writer);

    IWasteEvent readFrom(IKeyValuesReader reader);

}
