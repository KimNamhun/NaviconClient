package navicon.mju.kr.ac.naviconclientv01.beacons;

/**
 * Created by KimNamhun on 15. 11. 14..
 */
public class MapBeacon { // 지도 비콘을 관리한다.
    private int shortestBeacon;

    public MapBeacon() {
        this.shortestBeacon = 00;
    }

    public int getShortestBeacon() {
        return shortestBeacon;
    }

    public void setShortestBeacon(int shortestBeacon) {
        this.shortestBeacon = shortestBeacon;
    }
}
