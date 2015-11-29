package navicon.mju.kr.ac.naviconclientv01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

import navicon.mju.kr.ac.naviconclientv01.Structures.StructuresInfo;
import navicon.mju.kr.ac.naviconclientv01.beacons.BeaconAnimation;
import navicon.mju.kr.ac.naviconclientv01.beacons.MapBeaconInfo;
import navicon.mju.kr.ac.naviconclientv01.constants.Constants;
import navicon.mju.kr.ac.naviconclientv01.rooms.Rooms;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by KimNamhun on 15. 11. 19..
 * 지도를 받아서 화면에 비콘의 위치와 함께 표시해준다.
 */
public class MapViews extends View {
    private Bitmap map=null; // 비트맵 지도 변수
    private Paint mPaint; // paint 변수
    private ArrayList<StructuresInfo> structuresList; // 구조물 리스트
    private ArrayList<Rooms> roomsList; // 호실 리스트
    private ArrayList<MapBeaconInfo> beaconList; // 비콘 리스트

    private int mapState = 0; // 현재 지도 다운로드 안된 상태
    private int currentBeacon = 0; // 현재 비콘
    private int currentDestinationBeacon = 0; // 현재 목적지 비콘

    private double adjustedValue[] = { }; // 변화된 가로세로 값
    private double initXYValue[] = new double[2]; // xy변경전 값을 가지고 있는다

    private BeaconAnimation beaconAnimation;

    public MapViews(Context context, Bitmap bitmap, ArrayList<StructuresInfo> structuresList, ArrayList<Rooms> roomsList, ArrayList<MapBeaconInfo> beaconList) {
        super(context);
        this.map = bitmap;// 받아온 비트맵 표시
        this.structuresList = structuresList;
        this.roomsList = roomsList;
        this.beaconList = beaconList;
        mPaint = new Paint(); // 그리기 도구 준비

    }

    public void setCurrentBeacon(int currentBeacon) {
        this.currentBeacon = currentBeacon;
    }

    public void setCurrentDestinationBeacon(int currentDestinationBeacon) {
        this.currentDestinationBeacon = currentDestinationBeacon;
    }

    public void setBeaconAnimation(BeaconAnimation beaconAnimation) {
        this.beaconAnimation = beaconAnimation;
    }

    protected void onDraw(Canvas canvas) {

        if(mapState == 0) {
            System.out.println("MapViews() -- mapView start ::::: SUCCESS");
            if (map != null) {
                initXYValue[0] = map.getWidth();
                initXYValue[1] = map.getHeight();
                adjustedValue = adjustValue(map.getWidth(), map.getHeight()); // 가로세로길이를 화면에 맞춰 변화시킨 값을 셋팅한다
                map = createScaledBitmap(map, (int) adjustedValue[0], (int) adjustedValue[1], false); // 지도를 변경 크기로 셋팅한다.
                canvas.drawBitmap(map, (int) (getWidth() / 2 - adjustedValue[0] / 2), 0, mPaint); // 지도를 그린다.
                System.out.println("MapViews() -- map loading ::::: SUCCESS");
            }

            Canvas offScreen = new Canvas(map); // 캔버스위에 그릴수 있게 준비

            for (int i = 0; i < structuresList.size(); i++) { // 시설물 그리기
                Bitmap image;
                if (structuresList.get(i).getType().equals("exit")) {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.exit);
                } else if (structuresList.get(i).getType().equals("extinguisher")) {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.extinguisher);
                } else {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.toilet);

                }
                image = createScaledBitmap(image, Constants.STRUCTURE_SIZE, Constants.STRUCTURE_SIZE, false);
                offScreen.drawBitmap(image, (int) (structuresList.get(i).getX() * map.getWidth() / initXYValue[0]), (int) (structuresList.get(i).getY() * map.getHeight() / initXYValue[1]), mPaint);
            }

            for (int i = 0; i < roomsList.size(); i++) {
                mPaint.setColor(Color.BLACK);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setFakeBoldText(true);
                mPaint.setTextSize(Constants.ROOM_TEXTSIZE);
                offScreen.drawText(roomsList.get(i).getRoomName(), (float) ((roomsList.get(i).getLeft() + (roomsList.get(i).getWidth() / 4)) * (map.getWidth() / initXYValue[0])), (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (map.getHeight() / initXYValue[1])), mPaint);
            }
            mapState = 1;
        } else { // 지도가 그려진 상태에서
                double currentBeaconX = 0;
                double currentBeaconY = 0;

                System.out.println("MapViews() -- map current loading ::::: SUCCESS");

                Bitmap newMap = createScaledBitmap(map, (int) adjustedValue[0], (int) adjustedValue[1], false); // 현재 상태인 map을 더이상 건들지 않는다
                canvas.drawBitmap(newMap, (float) (getWidth() / 2 - adjustedValue[0] / 2), 0, mPaint);

                Canvas offScreen = new Canvas(newMap);

                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
                image = createScaledBitmap(image, beaconAnimation.getCurrentPinSize() , beaconAnimation.getCurrentPinSize(), false);

                Bitmap image2 = BitmapFactory.decodeResource(getResources(), R.drawable.destination); // 목적지
                image2 = createScaledBitmap(image2, Constants.DESTINATION_SIZE, Constants.DESTINATION_SIZE, false);
                System.out.println("MapViews() -- this.currentBeacon = " + this.currentBeacon);

                for (int i = 0; i < beaconList.size(); i++) { // 현재 비콘 위치 그리기
                    if (Integer.toString(this.currentBeacon).equals(beaconList.get(i).getBeaconId())) { // 현재 받은 비콘의 아이디와 비콘리스트의 아이디가 같으면 현재 비콘 위치 찍어주기

                        offScreen.drawBitmap(image, (int) (beaconList.get(i).getX() * newMap.getWidth() / initXYValue[0]), (int) (beaconList.get(i).getY() * newMap.getHeight() / initXYValue[1]), mPaint);
                        System.out.println("MapViews() -- pinWidth : " + beaconList.get(i).getX() * newMap.getWidth() / initXYValue[0]);
                        System.out.println("MapViews() -- pinHeight : " + beaconList.get(i).getY() * newMap.getHeight() / initXYValue[1]);
                        currentBeaconX = beaconList.get(i).getX() * newMap.getWidth() / initXYValue[0];
                        currentBeaconY = beaconList.get(i).getY() * newMap.getHeight() / initXYValue[1];
                    }
                }

                for(int i =0; i< roomsList.size(); i++) { // 목적지 까지 그리기

                    if (Integer.toString(this.currentDestinationBeacon).equals(roomsList.get(i).getRoomName())) {
                        offScreen.drawBitmap(image2, (int) ((roomsList.get(i).getLeft() + (roomsList.get(i).getWidth() / 4)) * (newMap.getWidth() / initXYValue[0])), (int) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1])) - image2.getHeight(), mPaint);
                        mPaint.setColor(Color.parseColor(Constants.ROAD_COLOR));
                        mPaint.setStrokeWidth(Constants.LINE_WIDTH);
                        if(currentBeaconY < (roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1])) {
                            offScreen.drawLine((float) currentBeaconX + Constants.CURRENT_PIN_SIZE / 2, (float) currentBeaconY + Constants.CURRENT_PIN_SIZE+Constants.LINE_WIDTH, (float) currentBeaconX + Constants.CURRENT_PIN_SIZE / 2, (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1])) + Constants.LINE_WIDTH / 2, mPaint);
                            offScreen.drawLine((float) currentBeaconX + Constants.CURRENT_PIN_SIZE / 2, ((float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1]))), (float) ((roomsList.get(i).getLeft() + (roomsList.get(i).getWidth() / 4)) * (newMap.getWidth() / initXYValue[0])) + Constants.CURRENT_PIN_SIZE / 2, (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1])), mPaint);
                        } else {
                            offScreen.drawLine((float) currentBeaconX + Constants.CURRENT_PIN_SIZE / 2, (float) currentBeaconY - Constants.LINE_WIDTH, (float) currentBeaconX + Constants.CURRENT_PIN_SIZE / 2, (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1])) - Constants.LINE_WIDTH / 2, mPaint);
                            offScreen.drawLine((float) currentBeaconX + Constants.CURRENT_PIN_SIZE / 2, ((float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1]))), (float) ((roomsList.get(i).getLeft() + (roomsList.get(i).getWidth() / 4)) * (newMap.getWidth() / initXYValue[0])) + Constants.CURRENT_PIN_SIZE / 2, (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1])), mPaint);
                        }
                    } // else로 목적지가 없을때 표시할수 있음
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
