package navicon.mju.kr.ac.naviconclientv01.Structures;

/**
 * Created by KimNamhun on 15. 11. 23..
 * 지도의 구조물에 대한 클래스이다.
 */
public class StructuresInfo {
    private int x; // 구조물 x 좌표
    private int y; // 구조물 y 좌표
    private String type; // 구조물의 타입

    public StructuresInfo(int x, int y, String type) {
        setX(x);
        setY(y);
        setType(type);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
