package projektkurs.lib;

import java.util.ArrayList;
import java.util.HashMap;

import projektkurs.io.storage.SaveData;
import projektkurs.raster.extra.ExtraInformation;
import projektkurs.raster.extra.ExtraInformationChest;
import projektkurs.raster.extra.ExtraInformationDoor;
import projektkurs.raster.extra.ExtraInformationFire;
import projektkurs.util.Init;
import projektkurs.util.Logger;
import projektkurs.util.Pair;
import projektkurs.util.ReflectionUtil;

/**
 * Alle ExtraInformationstypen.
 */
public final class ExtraInformationen {

    /**
     * Mappings.
     */
    public static final HashMap<String, Class<? extends ExtraInformation>> MAPPINGS = new HashMap<String, Class<? extends ExtraInformation>>();

    /**
     * Das Pair, das alle ExtraInformationen enthaelt.
     *
     * @return Pair
     */
    public static Pair<String, ArrayList<String>> getPair() {
        return new Pair<String, ArrayList<String>>("info.extras", new ArrayList<String>(MAPPINGS.keySet()));
    }

    /**
     * Initialisiert alle ExtraInformationstypen.
     */
    @Init
    public static void init() {
        registerExtraInformation(ExtraInformationDoor.class);
        registerExtraInformation(ExtraInformationFire.class);
        registerExtraInformation(ExtraInformationChest.class);
    }

    /**
     * Laedt einen ExtraInformation aus einem SaveData-Objekt.
     *
     * @param data
     *            SaveData
     * @return ExtraInformation
     */
    public static ExtraInformation loadExtraInformation(SaveData data) {
        ExtraInformation extra = null;
        try {
            extra = ReflectionUtil.newInstance(MAPPINGS.get(data.getString(Strings.EXTRA_ID)));
            extra.load(data);
        } catch (Throwable t) {
            Logger.logThrowable("Unable to load ExtraInformation from " + data, t);
        }
        return extra;
    }

    /**
     * Speichert eine ExtraInformation in einer SaveData.
     *
     * @param extra
     *            ExtraInformation
     * @return SaveData
     */
    public static SaveData writeExtraInformation(ExtraInformation extra) {
        SaveData data = new SaveData();
        try {
            data.set(Strings.EXTRA_ID, extra.getInternalName());
            extra.write(data);
        } catch (Throwable t) {
            Logger.logThrowable("Unable to save ExtraInformation '" + extra + "' to SaveData", t);
        }
        return data;
    }

    /**
     * Registriert ein Mapping.
     *
     * @param cls
     *            ExtraInformations-Klasse
     */
    private static void registerExtraInformation(Class<? extends ExtraInformation> cls) {
        if (cls != null && !MAPPINGS.containsKey(cls.getName())) {
            MAPPINGS.put(cls.getName(), cls);
        } else {
            Logger.warn("Unable to register ExtraInformation", cls);
            throw new IllegalArgumentException("Unable to register ExtraInformation " + cls);
        }
    }

    /**
     * Nicht instanziierbar.
     */
    private ExtraInformationen() {
    }
}
