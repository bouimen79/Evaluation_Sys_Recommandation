package util;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.*;

public class Util {
    public static <K, V extends Comparable<? super V>> List<K> sortByValueAndGetKeys(Map<K, V> map,
                                                                                     final boolean ascending, List<K> output) {
        List<Map.Entry<K, V>> list = new ObjectArrayList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if (ascending) {
                    return (o1.getValue()).compareTo(o2.getValue());
                } else {
                    return (o2.getValue()).compareTo(o1.getValue());
                }

            }
        });
        for (Map.Entry<K, V> entry : list) {
            output.add(entry.getKey());
        }
        return output;

    }
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, final boolean ascending) {
        List<Map.Entry<K, V>> list = new ObjectArrayList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if (ascending) {
                    return (o1.getValue()).compareTo(o2.getValue());
                } else {
                    return (o2.getValue()).compareTo(o1.getValue());
                }

            }
        });

        Map<K, V> result = new Object2ObjectLinkedOpenHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Sort the entries of a map based on their values and return the only the
     * keys as a list
     *
     * @param <K>
     *            -
     * @param <V>
     *            -
     * @param map
     *            -
     * @param ascending
     *            -
     * @param output
     *            -
     * @return a list of the key, sorted based on their values
     */
    public static <K, V extends Comparable<? super V>> List<K> sortByValueAndGetKeys(Map<K, V> map,
                                                                                     final boolean ascending, List<K> output, int maxRecListSize) {
        List<Map.Entry<K, V>> list = new ObjectArrayList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if (ascending) {
                    return (o1.getValue()).compareTo(o2.getValue());
                } else {
                    return (o2.getValue()).compareTo(o1.getValue());
                }

            }
        });

        int sizeList = 0;
        for (Map.Entry<K, V> entry : list) {
            output.add(entry.getKey());
            sizeList++;
            if (sizeList >= maxRecListSize) {
                break;
            }
        }

        return output;
    }
}
