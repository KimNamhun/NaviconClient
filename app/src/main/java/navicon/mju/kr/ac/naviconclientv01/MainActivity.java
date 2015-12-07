package navicon.mju.kr.ac.naviconclientv01;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wizturn.sdk.central.Central;
import com.wizturn.sdk.central.CentralManager;
import com.wizturn.sdk.peripheral.Peripheral;
import com.wizturn.sdk.peripheral.PeripheralScanListener;

import java.util.concurrent.ExecutionException;

import navicon.mju.kr.ac.naviconclientv01.beacons.BeaconAnimation;
import navicon.mju.kr.ac.naviconclientv01.beacons.MapBeacon;
import navicon.mju.kr.ac.naviconclientv01.constants.Constants;
import navicon.mju.kr.ac.naviconclientv01.event.EventManager;

/*

MainActivity 주로 비콘의 신호를 다루는 API관련 코드이다.

Author : KimNamhun

*/

public class MainActivity extends Activity {

    // 비콘 최적화 알고리즘(YP)
    int count =0;
    int beaconArray[][] = new int[2][Constants.BEACON_ARRAY_COUNT];
    int beaconDistance[][] = new int[3][Constants.BEACON_ARRAY_COUNT];
    private int currentBeaconAddress; // 이전 비콘 관리 변수

    // 일반적인 MainActivty 소속 프러퍼티
    private CentralManager centralManager; // 비콘 매니저
    private MapBeacon mapBeacon; // 맵 비콘 관리자
    private MapViews mapView; // 현재 맵뷰

    private RelativeLayout viewArea; // 커스텀뷰 표현 부분
    private EditText findDestinationEditText; // EditText 표현 부분
    private TextView buildingInfoText; // 현재 건물 정보 텍스트
    private ImageView scale;
    private TextView scaleText; // 축척 길이
    private TextView remainDistance; // 남은 거리 길이
    private TextView remainTime; // 남은 시간
    private ImageView toilet_button; // 화장실 찾기 버튼
    private ImageView exit_button; // 출구 찾기 버튼

    private int destinationBeacon = 0; // 입력받은 목적지 비콘

    private BeaconAnimation beaconAnimation; // 비콘 애니메이션 관리 변수
    private EventManager eventManager; // 이벤트 관리 변수

    @Override
    public void onBackPressed() {
        System.exit(0);
    } // 뒤로 누르면 시스템 종료

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getView();
        addListener();

        init(); // 비콘 탐지 구동을 위한 초기 셋팅
        start(); // 비콘 탐지 구동

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 스크린 켜진 상태로 유지
    }

    private void getView() { // xml 개체를 객체화 한다.
        viewArea = (RelativeLayout)findViewById(R.id.viewArea);
        findDestinationEditText = (EditText)findViewById(R.id.findDestinationEditText);
        buildingInfoText = (TextView)findViewById(R.id.buildingInfoText);
        scaleText = (TextView)findViewById(R.id.scaleText);
        scale = (ImageView)findViewById(R.id.scale);
        remainDistance = (TextView)findViewById(R.id.remainDistance);
        remainTime = (TextView)findViewById(R.id.remainTime);
        toilet_button = (ImageView)findViewById(R.id.toilet_button);
        exit_button = (ImageView)findViewById(R.id.exit_button);
        eventManager = new EventManager();
    }

    private void addListener() { // 개체에 대해 이벤트를 단다.
        toilet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventManager.getToilet_switch() == 0) {
                    toilet_button.setImageResource(R.drawable.toiletclick);
                    eventManager.setToilet_switch(1);
                } else {
                    eventManager.setToilet_switch(0);
                    toilet_button.setImageResource(R.drawable.toiletbutton);
                }

            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventManager.getExit_switch() == 0) {
                    exit_button.setImageResource(R.drawable.exitclick);
                    eventManager.setExit_switch(1);
                } else {
                    eventManager.setExit_switch(0);
                    exit_button.setImageResource(R.drawable.exitbutton);
                }
            }
        });
    }

    private void init() { // 비콘 탐지 구동을 위한 초기 셋팅

        setCentralManager(); // 비콘 탐지를 위한 매니저 셋팅 메소드
        terminateIfNotBLE(); // BLE가 아니면 종료
        turnOnBluetooth(); // 블루투스를 켠다

    }

    private void setCentralManager() { // 비콘 탐지를 위한 매니저 셋팅 메소드
        mapBeacon = new MapBeacon(); // 비콘의 상태 기록
        beaconAnimation = new BeaconAnimation();//비콘 애니메이션 변화
        centralManager = CentralManager.getInstance();
        centralManager.init(getApplicationContext());
        centralManager.setPeripheralScanListener(new PeripheralScanListener() {
            @Override
            public void onPeripheralScan(Central central, final Peripheral peripheral) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        findShortestBeacon(peripheral, mapBeacon); // 가장 가까운 비콘의 지도값을 찾는다
                        currentBeaconSet(peripheral, mapBeacon); // 변화에 의한 지도 변화를 표시한다.

                    }
                });
            }
        });
    }
    private void findShortestBeacon(Peripheral peripheral, MapBeacon mapBeacon) {

        if (peripheral.getDistance() < Constants.SHORTEST_CURRENT_BEACON_DISTANCE) { // 지정한 거리내에 들어오는 비콘을 최단거리 비콘 major(위치번호)로 설정한다
            if (mapBeacon.getShortestBeacon() != peripheral.getMajor()) { // 현재 위치의 지도와 새로 만난 지도가 다르면 지도를 갱신한다.
                System.out.println("MainActivity() -- ChangeMajor ::::: SUCCESS");
                mapBeacon.setShortestBeacon(peripheral.getMajor());
                ServerMapJSONSearch serverHttpManager = new ServerMapJSONSearch(Constants.SERVER_URL + Constants.SERVER_MAPDATA_URL + mapBeacon.getShortestBeacon());
                try {
                    mapView = new MapViews(MainActivity.this, serverHttpManager.execute().get(), serverHttpManager.getStructuresList(), serverHttpManager.getRoomsList(), serverHttpManager.getBeaconList(), serverHttpManager.getLocation(), buildingInfoText, scaleText, remainDistance, remainTime, scale, findDestinationEditText);
                    viewArea.addView(mapView);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void currentBeaconSet(Peripheral peripheral, MapBeacon mapBeacon) {

        beaconAnimation.plusBeaconAnmationCount(); // 비콘 애니메이션 설정
        beaconAnimation.plusCurrentPinSize();

        if(mapView!=null) {
            mapView.setBeaconAnimation(beaconAnimation);

            if (!findDestinationEditText.getText().toString().equals("")) { // 입력 된 내용이 있는지 상시 확인
                destinationBeacon = Integer.parseInt(findDestinationEditText.getText().toString());
            }


            if (Constants.SHORTEST_CURRENT_BEACON_DISTANCE > peripheral.getDistance()) {
                if (destinationBeacon != mapBeacon.getCurrentDestinationBeacon()) { // 목적지가 바뀌면
                    mapBeacon.setCurrentDestinationBeacon(destinationBeacon); // 현재 목적지가 바뀜을 셋팅한다.
                    mapView.setCurrentDestinationBeacon(destinationBeacon); // 맵뷰에 목적지 비콘을 넘긴다.
                    mapView.showLoadingDialog(); // 로딩기능
                    System.out.println("MainActivity() -- Destination Change ::::: SUCCESS");
                }

                mapView.setEventManager(eventManager); // 화장실, 비상구를 눌렀는지 안눌렀는지 확인한다.

                int smallBeaconMinor = checkLocation(peripheral); // 비콘 수신 신호 최적화 알고리즘!
                if (mapView != null && smallBeaconMinor != 0) {
                    mapView.setCurrentBeacon(smallBeaconMinor);

                }
                if (mapView != null)
                    mapView.invalidate(); // 화면 다시 그림

            }
        }

    }

    private int checkLocation(Peripheral peripheral) {

        if(count < Constants.BEACON_ARRAY_COUNT-1) {
            if(peripheral.getMinor() != 0) {
                beaconArray[0][count] = peripheral.getMinor();
                beaconArray[1][count] = (int) peripheral.getDistance();
                count++;
            }

            return 0;
        } else {
            int beaconCount = 1;
            beaconDistance[0][0] = beaconArray[0][0];
            beaconDistance[2][0] = 1;
            for(int i=0; i<Constants.BEACON_ARRAY_COUNT; i++){
               boolean check = false;
                for(int j=0; j<Constants.BEACON_ARRAY_COUNT; j++){
                    if(beaconDistance[0][j] == beaconArray[0][i]){
                        check = true;
                        break;
                    }
                }
                if(!check){
                    beaconDistance[0][beaconCount] = beaconArray[0][i];
                    beaconCount ++;
                }
            }
            for (int i = 0; i < beaconCount; i++) {
                for (int j = 0; j < Constants.BEACON_ARRAY_COUNT; j++) {
                    if (beaconDistance[0][i] == beaconArray[0][j]) {
                        beaconDistance[1][i] += beaconArray[1][j];
                        beaconDistance[2][i]++;
                    }
                }
            }
            int smallDistance[] = new int[2];
            smallDistance[1] = Integer.MAX_VALUE;

            int temp=0;
            for(int i=0; i<beaconCount; i++){
                if(beaconDistance[2][i]> temp){
                    temp = beaconDistance[2][i];
                    smallDistance[1] = beaconDistance[2][i];
                    smallDistance[0] = beaconDistance[0][i];
                }
            }
            if(smallDistance[0] == 0 ){
                count = 0;
                beaconArray = new int[2][Constants.BEACON_ARRAY_COUNT];
                beaconDistance = new int[3][Constants.BEACON_ARRAY_COUNT];
                System.out.println("MainActivity() -- Destination Change : currentBeacon" + smallDistance[0]);
                return currentBeaconAddress;
            }else {
                count = 0;
                beaconArray = new int[2][Constants.BEACON_ARRAY_COUNT];
                beaconDistance = new int[3][Constants.BEACON_ARRAY_COUNT];

                currentBeaconAddress = smallDistance[0];
                return smallDistance[0];

            }
        }
    }

    private void terminateIfNotBLE() { // BLE가 아니면 종료
        if(!centralManager.isBLESupported()) {
            Toast.makeText(this, "비콘이 탐지 되지 않습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void turnOnBluetooth() { // 블루투스를 켠다
        if(!centralManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
        }
    }

    private void start() {
        centralManager.startScanning(); // 비콘 탐지 구동

    }
}
