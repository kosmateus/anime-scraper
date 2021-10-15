package pl.anime.scraper.utils.collections;

import java.util.Map;

public class MapUtils {

    public static <K, V>  void putIfNotNull(Map<K, V> map, K key, V value) {
        if (key != null && value != null) {
            map.put(key, value);
        }
    }
}
