package me.ashram.packagegen.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ashram on 21.12.2015.
 */
public class FinderData {
    private volatile int delimiter = 0;
    private volatile int size = 0;
    private volatile int directionFinderAddress = 0;
    private volatile int pauseInMillis = 0;

    private Map<BeaconId, Integer> rssiValues;


    public FinderData() {
        rssiValues = new HashMap<>();
        for (BeaconId id : BeaconId.values()) {
            rssiValues.put(id, 0);
        }
    }

    public FinderData(int delimiter, int size, int pauseInMillis) {
        this();
        this.delimiter = delimiter;
        this.size = size;
        this.pauseInMillis = pauseInMillis;
    }


    public synchronized int getDelimiter() {
        return delimiter;
    }

    public synchronized void setDelimiter(int delimiter) {
        this.delimiter = delimiter;
    }

    public synchronized int getSize() {
        return size;
    }


    public synchronized void setSize(int size) {
        this.size = size;
    }

    public synchronized int getDirectionFinderAddress() {
        return directionFinderAddress;
    }

    public synchronized void setDirectionFinderAddress(int directionFinder) {
        this.directionFinderAddress = directionFinder;
    }

    public synchronized int getPauseInMillis() {
        return pauseInMillis;
    }

    public synchronized void setPauseInMillis(int pauseInMillis) {
        this.pauseInMillis = pauseInMillis;
    }

    public synchronized int getRssiValueById(BeaconId id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null");
        }
        int value = 0;
        value = rssiValues.get(id);
        return value;
    }

    public synchronized void setRssiByBeaconId(BeaconId id, int value) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null");
        }

        if ((value < 0) || (value > 255)) {
            throw new IllegalArgumentException("value not in range");
        }
        rssiValues.put(id, value);
    }

    public synchronized Set<BeaconId> getBeaconIds() {
        return rssiValues.keySet();
    }

    public synchronized int getBeaconAddressById(BeaconId id) {
        if (id == null) {
            throw new IllegalArgumentException("Can not be null");
        }
        return id.getIntAddress();
    }

    public enum BeaconId {
        BEACON_01, BEACON_02, BEACON_03, BEACON_04, BEACON_05, BEACON_06, BEACON_07, BEACON_08,
        BEACON_09, BEACON_0A, BEACON_0B, BEACON_0C;

        public static BeaconId fromSting(String value) {
            if (value == null) {
                throw new IllegalArgumentException("Could not be null");
            }
            BeaconId beaconId = null;
            switch (value) {
                case "01":
                    beaconId = BEACON_01;
                    break;
                case "02":
                    beaconId = BEACON_02;
                    break;
                case "03":
                    beaconId = BEACON_03;
                    break;
                case "04":
                    beaconId = BEACON_04;
                    break;
                case "05":
                    beaconId = BEACON_05;
                    break;
                case "06":
                    beaconId = BEACON_06;
                    break;
                case "07":
                    beaconId = BEACON_07;
                    break;
                case "08":
                    beaconId = BEACON_08;
                    break;
                case "09":
                    beaconId = BEACON_09;
                    break;
                case "0A":
                    beaconId = BEACON_0A;
                    break;
                case "0B":
                    beaconId = BEACON_0B;
                    break;
                case "0C":
                    beaconId = BEACON_0C;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid data bits value: ");
            }
            return beaconId;
        }

        public int getIntAddress() {
            return Integer.valueOf(this.name().substring(7), 16);
        }
    }
}