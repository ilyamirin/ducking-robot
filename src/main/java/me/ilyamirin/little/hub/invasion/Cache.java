package me.ilyamirin.little.hub.invasion;

import java.util.SortedMap;
import org.apache.jdbm.DB;
import org.apache.jdbm.DBMaker;

/**
 *
 * @author ilamirin
 */
public class Cache {

    private static DB db;
    private static SortedMap<String, Object> map;

    {
        db = DBMaker.openFile("nanopod")
                .enableEncryption("shlasashaposhossse", true)
                .make();
        map = db.getTreeMap("cache");
        if (map == null) {
            map = db.createTreeMap("cache");
        }
    }

    public <T> T get(String key, Class<T> claz) {
        return (T) map.get(key);
    }

    public void put(String key, Object value) {
        map.put(key, value);
        db.commit();
    }

    public boolean contains(Object key) {
        return map.containsKey(key);
    }
}
