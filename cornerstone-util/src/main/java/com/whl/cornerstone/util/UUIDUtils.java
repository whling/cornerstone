package com.whl.cornerstone.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by whling on 2018/4/10.
 */
public class UUIDUtils {

    public static final char DEFAULT_PREFIX_CHAR = 'o';

    public static String toSequenceUUID(String prefix) {
        return toSequenceUUID(prefix, System.currentTimeMillis());
    }

    public static String toSequenceUUID(String prefix, long timestamp) {
        char use_prefix = 'o';
        if (StringUtils.isNotBlank(prefix)) {
            use_prefix = prefix.charAt(0);
        }
        return toSequenceUUID(use_prefix, timestamp);
    }

    public static String toSequenceUUID(long timestamp) {
        return toSequenceUUID('o', timestamp);
    }

    public static String toSequenceUUID(long mostSigBits, long leastSigBits) {
        return toSequenceUUID('o', System.currentTimeMillis(), mostSigBits, leastSigBits);
    }

    public static String toSequenceUUID(long timestamp, long mostSigBits, long leastSigBits) {
        return toSequenceUUID('o', timestamp, mostSigBits, leastSigBits);
    }

    public static String toSequenceUUID(char prefix, long timestamp) {
        UUID u = UUID.randomUUID();
        return toSequenceUUID(prefix, timestamp, u.getMostSignificantBits(), u
                .getLeastSignificantBits());
    }

    public static String toSequenceUUID(char prefix, long timestamp, long mostSigBits, long leastSigBits) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(prefix);

        sb.append(BaseConvert.compressNumber(timestamp, 5));
        String m = BaseConvert.compressNumber(mostSigBits, 6);
        sb.append(m);
        if (m.length() < 11) {
            sb.append("_");
        }
        sb.append(BaseConvert.compressNumber(leastSigBits, 6));
        int len = 32 - sb.length();
        if (len > 0) {
            sb.append("________________________________", 0, len);
        }
        return sb.toString();
    }

    public static String toSequenceUUID() {
        return toSequenceUUID(System.currentTimeMillis());
    }

    public static long extractTimestamp(String sequenceUUID) {
        return BaseConvert.decompressNumber(sequenceUUID.substring(1, 10), 5);
    }

    public static long extractMostSignificantBits(String sequenceUUID) {
        int endIndex = sequenceUUID.indexOf('_', 10);
        if (endIndex < 10) {
            endIndex = 21;
        }
        return BaseConvert.decompressNumber(sequenceUUID.substring(10, endIndex), 6);
    }

    public static long extractLeastSignificantBits(String sequenceUUID) {
        int startIndex = sequenceUUID.indexOf('_', 10) + 1;
        if (startIndex < 10) {
            startIndex = 21;
        }
        int endIndex = sequenceUUID.indexOf('_', startIndex);
        if (endIndex < 21) {
            endIndex = 32;
        }
        return BaseConvert.decompressNumber(sequenceUUID.substring(startIndex, endIndex), 6);
    }

    public static void main(String[] strings) {
        Map map = new ConcurrentHashMap();
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 200; i++)
            executorService.submit(new Runnable() {
                public void run() {
                    while (true) {
                        String sequenceUUID = UUIDUtils.toSequenceUUID();
                        boolean containsKey = map.containsKey(sequenceUUID);
                        if (!containsKey) {
                            map.put(sequenceUUID, sequenceUUID);
                            System.out.println(map.size());
                        } else {
                            System.out.println(sequenceUUID + " exists");
                        }
                    }
                }
            });
    }

    public static class BaseConvert {
        static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-', '~'};

        public static String compressNumber(long number, int shift) {
            char[] buf = new char[64];
            int charPos = 64;
            int radix = 1 << shift;
            long mask = radix - 1;
            do {
                charPos--;
                buf[charPos] = digits[(int) (number & mask)];
                number >>>= shift;
            } while (number != 0L);
            return new String(buf, charPos, 64 - charPos);
        }

        public static long decompressNumber(String decompStr, int shift) {
            long result = 0L;
            for (int i = decompStr.length() - 1; i >= 0; i--) {
                if (i == decompStr.length() - 1) {
                    result += getCharIndexNum(decompStr.charAt(i));
                } else {
                    for (int j = 0; j < digits.length; j++) {
                        if (decompStr.charAt(i) == digits[j])
                            result += (j << shift * (decompStr.length() - 1 - i));
                    }
                }
            }
            return result;
        }

        private static long getCharIndexNum(char ch) {
            int num = ch;
            if ((num >= 48) && (num <= 57))
                return num - 48;
            if ((num >= 97) && (num <= 122))
                return num - 87;
            if ((num >= 65) && (num <= 90))
                return num - 29;
            if (num == 43)
                return 62L;
            if (num == 47) {
                return 63L;
            }
            return 0L;
        }
    }
}
