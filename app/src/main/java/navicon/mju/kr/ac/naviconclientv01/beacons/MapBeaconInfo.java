package navicon.mju.kr.ac.naviconclientv01.beacons;

/**
 * Created by KimNamhun on 15. 11. 21..
 * 비콘의 정보를 관리하는 클래스이다.
 */
public class MapBeaconInfo {
    private int x; // 비콘 x좌표
    private int y; // 비콘 y좌표

    public MapBeaconInfo(int x, int y) {
        setX(x);
        setY(y);
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
