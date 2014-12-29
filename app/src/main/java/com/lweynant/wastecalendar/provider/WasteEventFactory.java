package com.lweynant.wastecalendar.provider;

import com.lweynant.wastecalendar.R;
import com.lweynant.wastecalendar.model.IDate;
import com.lweynant.wastecalendar.model.ILocalizer;
import com.lweynant.wastecalendar.model.IWasteEvent;
import com.lweynant.wastecalendar.model.IWasteEventFactory;
import com.lweynant.wastecalendar.model.Waste;

public class WasteEventFactory implements IWasteEventFactory {

    final private ILocalizer localizer;
    final private TypeTranslator translator;
    static final protected DatabaseType PMD = new DatabaseType("PMD");
    static final protected DatabaseType BrownBag = new DatabaseType("Restafval");
    static final protected DatabaseType Paper = new DatabaseType("Papier");
    static final protected DatabaseType GFT = new DatabaseType("GFT");
    static final protected DatabaseType Waste = new DatabaseType("Afval");


    public WasteEventFactory(ILocalizer localizer) {
        this.localizer = localizer;
        this.translator = new TypeTranslator(getPossibleTypes(), getPossibleDatabaseTypes());
    }

    @Override
    public LocalizedType[] getPossibleTypes() {
        return new LocalizedType[]{localizedType(R.string.paper),
                localizedType(R.string.pmd),
                localizedType(R.string.gft),
                localizedType(R.string.brown_bag)};
    }

    private DatabaseType[] getPossibleDatabaseTypes() {
        return new DatabaseType[]{Paper, PMD, GFT, BrownBag};
    }


    @Override
    public IWasteEvent createWasteEvent(DatabaseType type, IDate date) {
        int typeId;
        int imageId;
        if (isPMD(type)) {
            typeId = R.string.pmd;
            imageId = R.drawable.pmd;
        } else if (isPaper(type)) {
            typeId = R.string.paper;
            imageId = R.drawable.papier;
        } else if (isGFT(type)) {
            typeId = R.string.gft;
            imageId = R.drawable.gft;
        } else if (isBrownBag(type)) {
            typeId = R.string.brown_bag;
            imageId = R.drawable.restafval;
        } else {
            typeId = R.string.unknown;
            imageId = R.drawable.ic_launcher;
        }
        return new Waste(localizedType(typeId), imageId, date);
    }

    /* (non-Javadoc)
     * @see com.lweynant.wastecalendar.provider.IWasteEventFactory#createWasteEvent(com.lweynant.wastecalendar.provider.LocalizedType, com.lweynant.wastecalendar.model.IDate)
     */
    @Override
    public IWasteEvent createWasteEvent(LocalizedType type, IDate date) {
        return createWasteEvent(translator.toDatabaseFormat(type), date);
    }

    private boolean isBrownBag(DatabaseType type) {
        return is(BrownBag, type);
    }

    private boolean isPMD(DatabaseType type) {
        return is(PMD, type);
    }

    private boolean isPaper(DatabaseType type) {
        return is(Paper, type);
    }

    private boolean isGFT(DatabaseType type) {
        return is(GFT, type);
    }

    private boolean is(DatabaseType db, DatabaseType type) {
        return db.value().equalsIgnoreCase(type.value());
    }

    private LocalizedType localizedType(int id) {
        return new LocalizedType(localizer.string(id));
    }

    @Override
    public DatabaseType toDatabaseFormat(LocalizedType type) {
        return translator.toDatabaseFormat(type);
    }

    @Override
    public LocalizedType fromDatabaseFormat(DatabaseType type) {
        return translator.fromDatabaseFormat(type);
    }

}
