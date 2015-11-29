package navicon.mju.kr.ac.naviconclientv01.constants;



/**
 * Created by KimNamhun on 15. 11. 12..
 * 프로그램에 이용되는 주요 상수에 대해 정의 되어 있다.
 */
public abstract class Constants {
    public static final int REQUEST_ENABLE_BT = 1000; // 블루투스 상수
    public static final int SHORTEST_BEACON_DISTANCE=5; // 지도 탐색을 위한 비콘 탐지가능 거리
    public static final float SHORTEST_CURRENT_BEACON_DISTANCE=1.5f; //현재 위치 탐색을 위한 비콘 거리
    public static final int STRUCTURE_SIZE = 100; // 구조물 크기
    public static final float ROOM_TEXTSIZE = 50.0f; // 룸 텍스트 사이즈
    public static final int CURRENT_PIN_SIZE = 60; // 현재 위치 핀 사이즈
    public static final int DESTINATION_SIZE = 120; // 목적지 사이즈
    public static final float LINE_WIDTH = 15.0f; // 길찾기 라인 두께
    public static final String ROAD_COLOR="#3F9BFE"; // 길 색깔 (하늘계열 파랑)
    public static final int PIN_CHANGE_COUNT = 8; // 핀 변화 주기
    public static final int MAX_PIN_SIZE = 67; // 최대 핀 사이즈
    public static final int VARIABLE_PIN_SIZE = 1; // 핀 사이즈 변화 크기
    public static final int MIN_PIN_SIZE = 60; // 최소 변화 핀 사이즈

    public static final String SERVER_URL = "http://117.17.158.95:8080/navicon/"; // 서버 주소
    public static final String SERVER_MAPDATA_URL = "mobile/getLocationInformation.do?beaconName="; // 서버 맵 데이터 JSON 주소
}
