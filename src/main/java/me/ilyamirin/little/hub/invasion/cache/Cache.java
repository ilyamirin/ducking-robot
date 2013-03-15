package me.ilyamirin.little.hub.invasion.cache;

/**
 *
 * @author ilamirin
 */
public class Cache {

    public static Cache build() {
        return new Cache();
    }

    public <T> T get(String key, Class<T> claz) {
        return null;
    }

    public void put(String key, Object value) {
    }

    public boolean contains(String key) {
        return false;
    }
}
