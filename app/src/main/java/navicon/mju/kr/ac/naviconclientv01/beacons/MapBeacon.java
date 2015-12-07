package navicon.mju.kr.ac.naviconclientv01.beacons;

/**
 * Created by KimNamhun on 15. 11. 14..
 * 가장 가까운 위치에 있는 비콘의 지도 major관련 클래스이다.
 */
public class MapBeacon { // 지도 비콘을 관리한다.
    private int shortestBeacon; // 가장 가까운 곳에 있는 비콘의 정보이다.
    private int currentDestinationBeacon; // 현재 목적지 비콘의 정보이다.

    public MapBeacon() {
        this.shortestBeacon = 0;
        this.currentDestinationBeacon = 0000; // 에딧 변경시 문제점 때문에 0000 지정
    }

    public int getCurrentDestinationBeacon() {
        return currentDestinationBeacon;
    }

    public void setCurrentDestinationBeacon(int currentDestinationBeacon) {
        this.currentDestinationBeacon = currentDestinationBeacon;
    }

    public int getShortestBeacon() {
        return shortestBeacon;
    }

    public void setShortestBeacon(int shortestBeacon) {
        this.shortestBeacon = shortestBeacon;
    }


}
