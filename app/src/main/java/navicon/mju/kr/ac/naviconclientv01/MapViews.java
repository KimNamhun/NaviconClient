package navicon.mju.kr.ac.naviconclientv01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

import navicon.mju.kr.ac.naviconclientv01.beacons.MapBeaconInfo;
import navicon.mju.kr.ac.naviconclientv01.constants.Constants;

/**
 * Created by KimNamhun on 15. 11. 19..
 * 지도를 받아서 화면에 비콘의 위치와 함께 표시해준다.
 */
public class MapViews extends View {
    private Bitmap map=null; // 비트맵 지도 변수
    private Paint mPaint; // paint 변수
    private ArrayList<MapBeaconInfo> beaconList; // 비콘 리스트 변수

    public MapViews(Context context, Bitmap bitmap, ArrayList<MapBeaconInfo> beaconList) {
        super(context);
        this.map = bitmap;// 받아온 비트맵 표시
        this.beaconList = beaconList; // 받아온 비콘리스트 표시
        mPaint = new Paint(); // 그리기 도구 준비
    }

    protected void onDraw(Canvas canvas) {
        System.out.println("MapViews() -- mapView start ::::: SUCCESS");

        double adjustedValue[]; // 변화된 가로세로 값
        if(map!=null) {
            adjustedValue = adjustValue(map.getWidth(), map.getHeight()); // 가로세로길이를 화면에 맞춰 변화시킨 값을 셋팅한다
            map = Bitmap.createScaledBitmap(map, (int) adjustedValue[0], (int) adjustedValue[1], false); // 지도를 변경 크기로 셋팅한다.
            canvas.drawBitmap(map, 0, 0, mPaint); // 지도를 그린다.
            System.out.println("MapViews() -- map loading ::::: SUCCESS");

            mPaint.setColor(Color.RED); // 그림도구 셋팅
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(3);

            for (int i = 0; i < beaconList.size(); i++) {
                Canvas offScreen = new Canvas(map); // 캔버스위에 그릴수 있게 준비
                double adjustedWidthRate = adjustedValue[0] / map.getWidth();  // 변화된 가로 비율
                double adjustedHeightRate = adjustedValue[1] / map.getHeight(); // 변화된 세로 비율
                offScreen.drawCircle((float) (beaconList.get(i).getX() + Constants.CIRCLE_RADIUS + beaconList.get(i).getX() * adjustedWidthRate), (float) (beaconList.get(i).getY() + Constants.CIRCLE_RADIUS + beaconList.get(i).getY() * adjustedHeightRate), Constants.CIRCLE_RADIUS, mPaint);

            }
        }
    }

    private double[] adjustValue(double mapWidth, double mapHeight) { // 화면값에 따라 지도의 가로세로 길이를 계산한다.
        System.out.println("MapViews() -- map width : " + mapWidth);
        System.out.println("MapViews() -- map height : " + mapHeight);
        double mapRate; // 지도의 가로 세로 비율
        double adjustedValue[] = new double[2]; // 가로 세로 변경된 값

        mapRate = calculateMapRate(mapWidth, mapHeight); // 지도의 가로 세로 배율 계산

        double tempHeight = getHeight() - mapHeight; // 늘어난 세로의 길이
        double tempValue = mapWidth + tempHeight / mapRate; //  늘어난 세로의 길이만큼 가로의 길이가 늘어남
        adjustedValue[0] = tempValue;
        adjustedValue[1] = getHeight();

        System.out.println("MapViews() -- map adjustedwidth : " + adjustedValue[0]);
        System.out.println("MapViews() -- map adjustedheight : " + adjustedValue[1]);

        return adjustedValue;
    }

    private double calculateMapRate(double mapWidth, double mapHeight) { // 지도 비율 계산
        double mapRate;

        mapRate = mapHeight/mapWidth;

        return mapRate;

    }
}
