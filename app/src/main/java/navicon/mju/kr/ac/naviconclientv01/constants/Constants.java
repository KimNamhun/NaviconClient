package navicon.mju.kr.ac.naviconclientv01.constants;


import android.graphics.Color;

/**
 * Created by KimNamhun on 15. 11. 12..
 * 프로그램에 이용되는 주요 상수에 대해 정의 되어 있다.
 */
public abstract class Constants {
    public static final int REQUEST_ENABLE_BT = 1000; // 블루투스 상수
    public static final float SHORTEST_CURRENT_BEACON_DISTANCE=10.0f; //현재 위치 탐색을 위한 비콘 거리
    public static final int STRUCTURE_SIZE = 50; // 구조물 크기
    public static final float ROOM_TEXTSIZE = 20.0f; // 룸 텍스트 사이즈
    public static final int CURRENT_PIN_SIZE = 30; // 현재 위치 핀 사이즈
    public static final int DESTINATION_SIZE = 60; // 목적지 사이즈
    public static final float LINE_WIDTH = 7.5f; // 길찾기 라인 두께
    public static final String ROAD_COLOR="#3F9BFE"; // 길 색깔 (하늘계열 파랑)
    public static final int PIN_CHANGE_COUNT = 8; // 핀 변화 주기
    public static final int MAX_PIN_SIZE = 37; // 최대 핀 사이즈
    public static final int VARIABLE_PIN_SIZE = 1; // 핀 사이즈 변화 크기
    public static final int MIN_PIN_SIZE = 30; // 최소 변화 핀 사이즈
    public static final int LOADING_DELAY = 400; // 로딩 딜레이 시간 길찾기 신뢰도 증가(심리적)
    public static final int MAIN_LOADING_TIME = 1500; // 메인 로딩 시간
    public static final int NOTICE_SIZE = 25; // Notice 크기
    public static final int BEACON_ARRAY_COUNT = 30; // 신호 받아서 계산하는 갯수
    public static final int FINISH_DISTANCE = 10; // 종료 알림 남은 거리
    public static final int VIBRATOR_TIME = 400; // 종료 진동시간
    public static final String TOILET_ROAD_COLOR = "#F14545"; // 화장실 길 색깔
    public static final int EXIT_ROAD_COLOR = Color.GREEN; // 비상구 길 색깔

    public static final String SERVER_URL = "http://117.17.158.95:8080/navicon/"; // 서버 주소
    public static final String SERVER_MAPDATA_URL = "mobile/getLocationInformation.do?beaconName="; // 서버 맵 데이터 JSON 주소
}
