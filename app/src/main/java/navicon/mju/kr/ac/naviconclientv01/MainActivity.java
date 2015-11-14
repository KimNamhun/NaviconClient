package navicon.mju.kr.ac.naviconclientv01;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.wizturn.sdk.central.Central;
import com.wizturn.sdk.central.CentralManager;
import com.wizturn.sdk.peripheral.Peripheral;
import com.wizturn.sdk.peripheral.PeripheralScanListener;

import navicon.mju.kr.ac.naviconclientv01.beacons.MapBeacon;
import navicon.mju.kr.ac.naviconclientv01.constants.Constants;

/*

MainActivity 주로 비콘의 신호를 다루는 API관련 코드이다.

Author : KimNamhun
 */
public class MainActivity extends Activity {

    private CentralManager centralManager; // 비콘 매니저
    private MapBeacon mapBeacon; // 맵 비콘 관리자

    private ImageView mapView; // 지도 view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(); // 비콘 탐지 구동을 위한 초기 셋팅
        start(); // 비콘 탐지 구동

        setContentView(R.layout.activity_main);
        getView(); // xml view 객체 인식

    }


    private void init() { // 비콘 탐지 구동을 위한 초기 셋팅
        setCentralManager(); // 비콘 탐지를 위한 매니저 셋팅 메소드
        terminateIfNotBLE(); // BLE가 아니면 종료
        turnOnBluetooth(); // 블루투스를 켠다
    }

    private void setCentralManager() { // 비콘 탐지를 위한 매니저 셋팅 메소드
        mapBeacon = new MapBeacon();
        centralManager = CentralManager.getInstance();
        centralManager.init(getApplicationContext());
        centralManager.setPeripheralScanListener(new PeripheralScanListener() {
            @Override
            public void onPeripheralScan(Central central, final Peripheral peripheral) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        findShortestBeacon(peripheral, mapBeacon); // 가장 가까운 비콘의 지도값을 찾는다

                    }
                });
            }
        });
    }

    private void findShortestBeacon(Peripheral peripheral, MapBeacon mapBeacon) {

        if (peripheral.getDistance() < Constants.SHORTEST_BEACON_DISTANCE) { // 지정한 거리내에 들어오는 비콘을 최단거리 비콘 major(위치번호)로 설정한다
            if (mapBeacon.getShortestBeacon() != peripheral.getMajor()) { // 현재 위치의 지도와 새로 만난 지도가 다르면 지도를 갱신한다.
                    mapBeacon.setShortestBeacon(peripheral.getMajor());
                    ServerMapJSONSearch serverHttpManager = new ServerMapJSONSearch(mapView,Constants.SERVER_URL + Constants.SERVER_MAPDATA_URL + mapBeacon.getShortestBeacon());

                    serverHttpManager.execute();
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

    private void getView() {
        mapView = (ImageView)findViewById(R.id.mapView);
    }




}
