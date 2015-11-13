package navicon.mju.kr.ac.naviconclientv01.constants;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by KimNamhun on 15. 11. 12..
 */
public class Constants {

    public static final int REQUEST_ENABLE_BT = 1000; // 블루투스 상수
    public static final int SHORTEST_BEACON_DISTANCE=3; // 지도 탐색을 위한 비콘 탐지가능 거리
    public static final String SERVER_URL = "http://117.17.158.95:8080/navicon/"; // 서버 주소
    public static final String SERVER_MAPDATA_URL = "mobile/getLocationInformation.do?beaconName="; // 서버 맵 데이터 JSON 주소

    public static int CURRENT_SHORTEST_BEACON_MAJOR = 00; // 최단거리 비콘 위치 Major 값
    public static String JSON_CURRENT_DATA = ""; // JSON 현재 객체 값
    public static String MAP_URL = ""; // 현재 맵 위치 주소
    public static Bitmap CURRENT_MAP = null;

}
