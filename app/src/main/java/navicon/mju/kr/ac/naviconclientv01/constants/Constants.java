package navicon.mju.kr.ac.naviconclientv01.constants;



/**
 * Created by KimNamhun on 15. 11. 12..
 */
public abstract class Constants {
    public static final int REQUEST_ENABLE_BT = 1000; // 블루투스 상수
    public static final int SHORTEST_BEACON_DISTANCE=3; // 지도 탐색을 위한 비콘 탐지가능 거리
    public static final String SERVER_URL = "http://117.17.158.95:8080/navicon/"; // 서버 주소
    public static final String SERVER_MAPDATA_URL = "mobile/getLocationInformation.do?beaconName="; // 서버 맵 데이터 JSON 주소
}
