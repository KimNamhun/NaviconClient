package navicon.mju.kr.ac.naviconclientv01.rooms;

/**
 * Created by KimNamhun on 15. 11. 23..
 * 해당 방의 정보이다.
 */
public class Rooms {
    private String roomName = "";
    private int top = 0;
    private int left = 0;
    private int width = 0;
    private int height = 0;
    private String info = "";

    public Rooms(String roomName, int top, int left, int width, int height, String info) {
        setRoomName(roomName);
        setTop(top);
        setLeft(left);
        setWidth(width);
        setHeight(height);
        setInfo(info);
    }



    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
