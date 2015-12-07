package navicon.mju.kr.ac.naviconclientv01;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import navicon.mju.kr.ac.naviconclientv01.event.EventManager;
import navicon.mju.kr.ac.naviconclientv01.structures.StructuresInfo;
import navicon.mju.kr.ac.naviconclientv01.beacons.BeaconAnimation;
import navicon.mju.kr.ac.naviconclientv01.beacons.MapBeaconInfo;
import navicon.mju.kr.ac.naviconclientv01.constants.Constants;
import navicon.mju.kr.ac.naviconclientv01.functions.ComputeScale;
import navicon.mju.kr.ac.naviconclientv01.location.Location;
import navicon.mju.kr.ac.naviconclientv01.rooms.Rooms;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by KimNamhun on 15. 11. 19..
 * 지도를 받아서 화면에 비콘의 위치와 함께 표시해준다.
 */
public class MapViews extends View{
    private Bitmap map; // 비트맵 지도 변수
    private Paint mPaint; // paint 변수

    private ArrayList<StructuresInfo> structuresList; // 구조물 리스트
    private ArrayList<Rooms> roomsList; // 호실 리스트
    private ArrayList<MapBeaconInfo> beaconList; // 비콘 리스트
    private Location location; // 위치 정보

    private int mapState; // 현재 지도 상태
    private int currentBeacon; // 현재 비콘
    private int currentDestinationBeacon; // 현재 목적지 비콘

    private BeaconAnimation beaconAnimation; // 비콘 애니메이트 변수
    private EventManager eventManager; // 이벤트 관리 매니저 변수

    private TextView buildingInfoText; // 화면 TextView 건물 정보
    private TextView scaleText; // 스케일 화면 글자
    private TextView remainDistance; // 남은 거리
    private TextView remainTime; // 남은 시간
    private EditText findDestinationEditText;

    private ImageView scale;

    private ProgressDialog dialog1; // 로딩 다이얼 로그



    // 자주 쓰는 변수들
    private double adjustedValue[] = {}; // 변화된 가로세로 값
    private double initXYValue[]; // xy변경전 값을 가지고 있는다

    Handler mHandler = new Handler() { // 로딩시간을 위한 핸들러
        public void handleMessage(Message msg) { // 로딩 캔슬
            System.out.println("MapViews() -- mapView loading cancel ::::: SUCCESS");
            dialog1.cancel();

        }
    };

    public MapViews(Context context, Bitmap bitmap, ArrayList<StructuresInfo> structuresList, ArrayList<Rooms> roomsList, ArrayList<MapBeaconInfo> beaconList, Location location, TextView buildingInfoText, TextView scaleText, TextView remainDistance, TextView remainTime, ImageView scale, EditText findDestinationEditText) {
        super(context);
        this.map = bitmap;// 받아온 비트맵 표시
        mPaint = new Paint(); // 그리기 도구 준비
        this.structuresList = structuresList;
        this.roomsList = roomsList;
        this.beaconList = beaconList;
        this.location = location;
        this.mapState = 0;
        this.currentBeacon = 0;
        this.currentDestinationBeacon = 0;
        this.beaconAnimation = null;
        this.eventManager = null;
        this.buildingInfoText = buildingInfoText;
        this.findDestinationEditText = findDestinationEditText;
        this.scaleText = scaleText;
        this.remainDistance = remainDistance;
        this.remainTime = remainTime;
        this.initXYValue = new double[2];
        this.scale = scale;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);
        double increaseRate = map.getWidth() / initXYValue[0];

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            for (int i = 0; i < roomsList.size(); i++) { // 각 방 공지 이벤트 추가
                if ((roomsList.get(i).getLeft() * increaseRate + getWidth() / 2 - adjustedValue[0] / 2 <= x && x <= (roomsList.get(i).getLeft() + roomsList.get(i).getWidth()) * increaseRate + getWidth() / 2 - adjustedValue[0] / 2) && ((roomsList.get(i).getTop() * increaseRate <= y) && (y <= (roomsList.get(i).getTop() + roomsList.get(i).getHeight()) * increaseRate))) { // 각 공지사항 클릭시
                    if (!roomsList.get(i).getInfo().equals("")) {
                        System.out.println("MapViews() -- SelectRoomInfo : " + roomsList.get(i).getRoomName());

                        createDialogBoxRoom(roomsList.get(i)).show();
                        break;
                    }
                }

            }
            if (buildingInfoText.getLeft() + buildingInfoText.getWidth() >= x && buildingInfoText.getLeft() <= x && buildingInfoText.getTop() + buildingInfoText.getHeight() / 2 >= y && buildingInfoText.getTop() - buildingInfoText.getHeight() / 2 <= y) { // 건물명 정보 클릭시
                createDialogBoxLocation(location).show();
            }
            return true;
        }

        return false;
    }

    private AlertDialog createDialogBoxRoom(Rooms room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(room.getRoomName() + " 공지사항"); // 제목
        builder.setMessage(room.getInfo()); // 메시지
        builder.setIcon(R.mipmap.infocon);

        builder.setNegativeButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
                        // 다이얼로그를 취소한다
                        dialog.cancel();
                    }
                });

        return builder.create();

    }

    private AlertDialog createDialogFinish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("새로운 지도로 이동합니다"); // 제목
        builder.setMessage("Loading..."); // 메시지


        return builder.create();

    }


    private AlertDialog createDialogBoxLocation(Location location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(location.getGroupName() + " " + location.getBuildingName()); // 제목
        builder.setMessage(location.getBuildingInfo()); // 메시지
        String url = Constants.SERVER_URL + location.getBuildingPhoto();
        BuildingMapDown buildingMapDown = new BuildingMapDown(url);
        Bitmap bitmap = null;
        try {
            bitmap = buildingMapDown.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Drawable drawable = new BitmapDrawable(bitmap);


        ImageView image = new ImageView(getContext());
        image.setBackground(drawable);
        builder.setView(image);

        builder.setNegativeButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
                        // 다이얼로그를 취소한다
                        dialog.cancel();
                    }
                });

        return builder.create();

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

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    int count =0; // 지도 바로 읽어들이지 않기 위해
    AlertDialog dialog= createDialogFinish();
    protected void onDraw(Canvas canvas) {

        if (map != null) {
            count++;
            mPaint.setColor(Color.WHITE);

            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

            if (mapState == 0) { // 지도가 로딩이 안되었을때
                //Toast.makeText(getContext(), "Loading.........", Toast.LENGTH_SHORT).show();

                dialog.show();

                if (count == 3) {


                    mPaint.setColor(Color.WHITE);

                    canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

                    System.out.println("MapViews() -- mapView start ::::: SUCCESS");
                    dialog.cancel();
                    if (map != null) {
                        initXYValue[0] = map.getWidth();
                        initXYValue[1] = map.getHeight();
                        adjustedValue = adjustValue(map.getWidth(), map.getHeight()); // 가로세로길이를 화면에 맞춰 변화시킨 값을 셋팅한다
                        map = createScaledBitmap(map, (int) adjustedValue[0], (int) adjustedValue[1], false); // 지도를 변경 크기로 셋팅한다.

                        canvas.drawBitmap(map, (int) (getWidth() / 2 - adjustedValue[0] / 2), 0, mPaint); // 지도를 그린다.
                        System.out.println("MapViews() -- map loading ::::: SUCCESS");
                    }

                    Canvas offScreen = new Canvas(map); // 캔버스위에 그릴수 있게 준비
                    Bitmap image;
                    for (int i = 0; i < structuresList.size(); i++) { // 시설물 그리기

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

                    for (int i = 0; i < roomsList.size(); i++) { // 방 그리기, 공지 그리기
                        image = BitmapFactory.decodeResource(getResources(), R.drawable.notice);
                        image = createScaledBitmap(image, Constants.NOTICE_SIZE, Constants.NOTICE_SIZE, false);
                        if (!roomsList.get(i).getInfo().equals("")) {
                            System.out.println("MapViews() -- getInfo :" + roomsList.get(i).getInfo());
                            offScreen.drawBitmap(image, (float) ((roomsList.get(i).getLeft() + (roomsList.get(i).getWidth() / 4)) * (map.getWidth() / initXYValue[0]) - image.getWidth()), (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (map.getHeight() / initXYValue[1]) - image.getHeight()), mPaint);
                        }
                        mPaint.setColor(Color.BLACK);
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setFakeBoldText(true);
                        mPaint.setTextSize(Constants.ROOM_TEXTSIZE);
                        offScreen.drawText(roomsList.get(i).getRoomName(), (float) ((roomsList.get(i).getLeft() + (roomsList.get(i).getWidth() / 4)) * (map.getWidth() / initXYValue[0])), (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (map.getHeight() / initXYValue[1])), mPaint);
                    }

                    // 건물 정보 쓰기
                    buildingInfoText.setText(location.getGroupName() + "\n" + location.getBuildingName() + "\n" + location.getFloorName());
                    // 스케일 쓰기
                    scaleText.setText(Long.toString(Math.round((ComputeScale.computeScale((double) location.getScale(), (map.getWidth() / initXYValue[0])) * scale.getWidth()))) + "m");

                    mapState = 1;
                    count = 0;
                }


            } else { // 지도가 그려진 상태에서
                if (count == Integer.MAX_VALUE) { // 혹시나 카운트가 다 차면 0으로 만들어준다.
                    count = 0;
                }
                double currentBeaconX = 0;
                double currentBeaconY = 0;

                Bitmap newMap = createScaledBitmap(map, (int) adjustedValue[0], (int) adjustedValue[1], false); // 현재 상태인 map을 더이상 건들지 않는다
                canvas.drawBitmap(newMap, (float) (getWidth() / 2 - adjustedValue[0] / 2), 0, mPaint);

                Canvas offScreen = new Canvas(newMap);
                // 현재 핀 그리기 & 목적지 그리기
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
                image = createScaledBitmap(image, beaconAnimation.getCurrentPinSize(), beaconAnimation.getCurrentPinSize(), false);

                Bitmap image2 = BitmapFactory.decodeResource(getResources(), R.drawable.destination); // 목적지
                image2 = createScaledBitmap(image2, Constants.DESTINATION_SIZE, Constants.DESTINATION_SIZE, false);




                    for (int i = 0; i < beaconList.size(); i++) { // 현재 비콘 위치 그리기
                        if (Integer.toString(this.currentBeacon).equals(beaconList.get(i).getBeaconId())) { // 현재 받은 비콘의 아이디와 비콘리스트의 아이디가 같으면 현재 비콘 위치 찍어주기

                            currentBeaconX = beaconList.get(i).getX() * newMap.getWidth() / initXYValue[0];
                            currentBeaconY = beaconList.get(i).getY() * newMap.getHeight() / initXYValue[1];


                            offScreen.drawBitmap(image, (int) (beaconList.get(i).getX() * newMap.getWidth() / initXYValue[0]), (int) ((beaconList.get(i).getY() * newMap.getHeight() / initXYValue[1])), mPaint);






                            }

                        }


                    this.remainDistance.setText("");
                    this.remainTime.setText("");

                    for (int i = 0; i < roomsList.size(); i++) { // 목적지 까지 그리기

                        double remainTime; // 목적지까지 남은시간
                        double remainDistance = 0; // 목적지까지 남은 거리

                        if (Integer.toString(this.currentDestinationBeacon).equals(roomsList.get(i).getRoomName())) {
                            float downStraightStartX = (float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2);
                            float downVerticalStartX = (float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2);
                            float downStraightStartY = (float) currentBeaconY + (float) Constants.CURRENT_PIN_SIZE + Constants.LINE_WIDTH;
                            float downVerticalStartY = (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1]));
                            float downStraightEndX = (float) (currentBeaconX + Constants.CURRENT_PIN_SIZE / 2);
                            float downVerticalEndX = (float) ((roomsList.get(i).getLeft() + (roomsList.get(i).getWidth() / 4)) * (newMap.getWidth() / initXYValue[0]) + Constants.CURRENT_PIN_SIZE / 2);
                            float downStraightEndY = (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1]) + Constants.LINE_WIDTH / 2);
                            float downVerticalEndY = (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1]));

                            float upStraightStartY = (float) (currentBeaconY - Constants.LINE_WIDTH);
                            float upVerticalStartY = (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1]));
                            float upStratightEndY = (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1]) - Constants.LINE_WIDTH / 2);
                            float upVerticalEndY = (float) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1]));
                            // 목적지 깃발 그리기
                            offScreen.drawBitmap(image2, (int) ((roomsList.get(i).getLeft() + (roomsList.get(i).getWidth() / 4)) * (newMap.getWidth() / initXYValue[0])), (int) ((roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1])) - image2.getHeight(), mPaint);
                            // 경로 그리기
                            mPaint.setColor(Color.parseColor(Constants.ROAD_COLOR));
                            mPaint.setStrokeWidth(Constants.LINE_WIDTH);
                            if (currentBeaconY < (roomsList.get(i).getTop() + (roomsList.get(i).getHeight() * 2 / 3)) * (newMap.getHeight() / initXYValue[1])) { // 아랫방향으로갈때
                                offScreen.drawLine(downStraightStartX, downStraightStartY, downStraightEndX, downStraightEndY, mPaint);
                                offScreen.drawLine(downVerticalStartX, downVerticalStartY, downVerticalEndX, downVerticalEndY, mPaint);
                                remainDistance += (Math.abs((double) (downStraightEndY - downStraightStartY)));

                            } else {
                                offScreen.drawLine(downStraightStartX, upStraightStartY, downStraightEndX, upStratightEndY, mPaint);
                                offScreen.drawLine(downVerticalStartX, upVerticalStartY, downVerticalEndX, upVerticalEndY, mPaint);
                                remainDistance += (Math.abs((double) (downStraightEndY - downStraightStartY)));
                            }

                            //남은 거리 시간 산출

                            remainDistance = (ComputeScale.computeScale(location.getScale(), newMap.getWidth() / initXYValue[0]) * remainDistance);
                            remainTime = ComputeScale.computeTime(remainDistance);
                            this.remainDistance.setText("남은 거리 : " + Math.round(remainDistance) + "m");
                            this.remainTime.setText("남은 시간 : " + Math.round(remainTime) + "s");

                            //남은 거리에 따라 도착 알림
                            alarmFinish(remainDistance);


                        }// else로 목적지가 없을때 표시할수 있음

                    }


                    int closestExit = -1;
                    double smallDistance = Double.MAX_VALUE;
                    for (int i = 0; i < structuresList.size(); i++) {
                        if (structuresList.get(i).getType().equals("exit")) {
                            if (Math.abs(currentBeaconY - structuresList.get(i).getY() * (newMap.getHeight() / initXYValue[1])) < smallDistance) {
                                smallDistance = Math.abs(currentBeaconY - structuresList.get(i).getY() * (newMap.getHeight() / initXYValue[1]));
                                closestExit = i;
                            }
                        }
                    }
                    // 화장실, 비상구 그리기
                    for (int i = 0; i < structuresList.size(); i++) {


                        if (eventManager.getToilet_switch() == 1 && structuresList.get(i).getType().equals("toilet")) { // 화장실이 켜져있으면
                            mPaint.setColor(Color.parseColor(Constants.TOILET_ROAD_COLOR));
                            mPaint.setStrokeWidth(Constants.LINE_WIDTH);

                            if (currentBeaconY < structuresList.get(i).getY() * (newMap.getHeight() / initXYValue[1])) { // 아랫방향으로갈때
                                offScreen.drawLine((float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2), (float) currentBeaconY + (float) Constants.CURRENT_PIN_SIZE + Constants.LINE_WIDTH, (float) (currentBeaconX + Constants.CURRENT_PIN_SIZE / 2), (float) (structuresList.get(i).getY() * (newMap.getHeight() / initXYValue[1]) + Constants.LINE_WIDTH / 2) + Constants.STRUCTURE_SIZE, mPaint); // 세로선
                                offScreen.drawLine((float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2), (float) (structuresList.get(i).getY() * (newMap.getHeight() / initXYValue[1])) + Constants.STRUCTURE_SIZE, (float) (structuresList.get(i).getX() * (newMap.getWidth() / initXYValue[0]) + (Constants.CURRENT_PIN_SIZE / 2)), (float) (structuresList.get(i).getY() * (newMap.getHeight() / initXYValue[1]) + Constants.STRUCTURE_SIZE), mPaint); // 가로선


                            } else {
                                offScreen.drawLine((float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2), (float) (currentBeaconY - Constants.LINE_WIDTH), (float) (currentBeaconX + Constants.CURRENT_PIN_SIZE / 2), (float) (structuresList.get(i).getY() * (newMap.getHeight() / initXYValue[1]) - (Constants.LINE_WIDTH / 2)) + Constants.STRUCTURE_SIZE, mPaint);
                                offScreen.drawLine((float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2), (float) (structuresList.get(i).getY() * (newMap.getHeight() / initXYValue[1]) + Constants.STRUCTURE_SIZE), (float) (structuresList.get(i).getX() * (newMap.getWidth() / initXYValue[0]) + (Constants.CURRENT_PIN_SIZE / 2)), (float) (structuresList.get(i).getY() * (newMap.getHeight() / initXYValue[1])) + Constants.STRUCTURE_SIZE, mPaint);

                            }


                        } else if (eventManager.getExit_switch() == 1 && closestExit != -1) { // 비상구가 켜져있으면
                            mPaint.setStrokeWidth(Constants.LINE_WIDTH);
                            mPaint.setColor(Constants.EXIT_ROAD_COLOR);

                            if (currentBeaconY < structuresList.get(closestExit).getY() * (newMap.getHeight() / initXYValue[1])) { // 아랫방향으로갈때
                                offScreen.drawLine((float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2), (float) currentBeaconY + (float) Constants.CURRENT_PIN_SIZE + Constants.LINE_WIDTH, (float) (currentBeaconX + Constants.CURRENT_PIN_SIZE / 2), (float) (structuresList.get(closestExit).getY() * (newMap.getHeight() / initXYValue[1]) + Constants.LINE_WIDTH / 2), mPaint); // 세로선
                                offScreen.drawLine((float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2), (float) (structuresList.get(closestExit).getY() * (newMap.getHeight() / initXYValue[1])), (float) (structuresList.get(closestExit).getX() * (newMap.getWidth() / initXYValue[0]) + (Constants.CURRENT_PIN_SIZE / 2)), (float) (structuresList.get(closestExit).getY() * (newMap.getHeight() / initXYValue[1])), mPaint); // 가로선

                            } else {
                                offScreen.drawLine((float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2), (float) (currentBeaconY - Constants.LINE_WIDTH), (float) (currentBeaconX + Constants.CURRENT_PIN_SIZE / 2), (float) (structuresList.get(closestExit).getY() * (newMap.getHeight() / initXYValue[1]) - (Constants.LINE_WIDTH / 2)), mPaint);
                                offScreen.drawLine((float) currentBeaconX + (float) (Constants.CURRENT_PIN_SIZE / 2), (float) (structuresList.get(closestExit).getY() * (newMap.getHeight() / initXYValue[1])), (float) (structuresList.get(closestExit).getX() * (newMap.getWidth() / initXYValue[0]) + (Constants.CURRENT_PIN_SIZE / 2)), (float) (structuresList.get(closestExit).getY() * (newMap.getHeight() / initXYValue[1])), mPaint);
                            }

                        }
                    }


                }


            }
        }


    private void alarmFinish(double remainDistance) {
        if(remainDistance < Constants.FINISH_DISTANCE) {
            Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(Constants.VIBRATOR_TIME);
            findDestinationEditText.setText("");
            currentDestinationBeacon = 0;
            Toast.makeText(getContext(), "목적지에 도착하였습니다 탐색을 종료합니다.", Toast.LENGTH_LONG).show();

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

    public void showLoadingDialog() {
        for (int i = 0; i < roomsList.size(); i++) {
            if (Integer.toString(this.currentDestinationBeacon).equals(roomsList.get(i).getRoomName())) {

                //로딩 다이얼로그 객체
                //--> import android.app.ProgressDialog;
                dialog1 = new ProgressDialog(getContext());

                //다이얼로그에 표시할 메시지
                // (이 외에 타이틀, 아이콘, 버튼 등을 추가할 수 있음)
                dialog1.setMessage("최적의 경로 탐색중");

                //다이얼로그를 화면에 표시하기
                dialog1.show();
                mHandler.sendEmptyMessageDelayed(0, Constants.LOADING_DELAY);

            }
        }



    }






}
