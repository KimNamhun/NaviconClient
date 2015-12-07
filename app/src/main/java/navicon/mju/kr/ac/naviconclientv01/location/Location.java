package navicon.mju.kr.ac.naviconclientv01.location;

/**
 * Created by KimNamhun on 15. 11. 30..
 * 건물의 정보를 가진 클래스이다.
 */
public class Location {

    private String floorName;
    private String buildingName;
    private String groupName;
    private int scale;
    private String buildingPhoto;
    private String buildingInfo;

    public Location(String floorName, String buildingName, String groupName, int scale, String buildingPhoto, String buildingInfo) {
        this.floorName = floorName;
        this.buildingName = buildingName;
        this.groupName = groupName;
        this.scale = scale;
        this.buildingPhoto = buildingPhoto;
        this.buildingInfo = buildingInfo;
    }

    public String getFloorName() {
        return floorName;
    }


    public String getBuildingName() {
        return buildingName;
    }


    public String getGroupName() {
        return groupName;
    }


    public int getScale() {
        return scale;
    }

    public String getBuildingInfo() {
        return buildingInfo;
    }

    public String getBuildingPhoto() {
        return buildingPhoto;
    }
}
