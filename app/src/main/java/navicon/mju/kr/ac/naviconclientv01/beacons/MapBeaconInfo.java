package navicon.mju.kr.ac.naviconclientv01.beacons;

/**
 * Created by KimNamhun on 15. 11. 21..
 * 비콘의 정보를 관리하는 클래스이다.
 */
public class MapBeaconInfo {
    private String beaconId; // 비콘 이름
    private int x; // 비콘 x좌표
    private int y; // 비콘 y좌표


    public MapBeaconInfo(String beaconId, int x, int y) {
        setBeaconId(beaconId);
        setX(x);
        setY(y);
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
