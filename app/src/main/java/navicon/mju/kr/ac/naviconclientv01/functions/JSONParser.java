package navicon.mju.kr.ac.naviconclientv01.functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import navicon.mju.kr.ac.naviconclientv01.beacons.MapBeaconInfo;

/**
 * Json을 읽어와서 필요한 내용을 얻을때 쓰는 오퍼레이션이 담긴 클래스이다.
 */
public class JSONParser {

    private JSONObject json; // JSON Object 관리

    public JSONParser(String strJson) { // JSON 객체 가지고 오기
        try {
            json = new JSONObject(strJson);
            System.out.println("JSONParser() -- " + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String findMapURL() { // JSON DATA에서 지도 URL을 반환한다.
        try {
            return json.getString("map");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // 지도가 URL이 없음
    }

    public ArrayList<MapBeaconInfo> findBeaconList() { // json에서 비콘리스트를 찾아 (x,y) 좌표 위치를 리스트로 반환한다
        ArrayList<MapBeaconInfo> beaconList = new ArrayList<>();
        try {
            JSONArray arrResults = json.getJSONArray("beaconList");
            int iCount = arrResults.length();
            for(int i=0; i<iCount; ++i)
            {
                JSONObject beaconItem = arrResults.getJSONObject(i);
                System.out.println("JSONParser() -- findBeaconList() count : " + iCount);
                System.out.println("JSONParser() -- findBeaconList() x : " + beaconItem.getInt("x"));
                System.out.println("JSONParser() -- findBeaconList() y : " + beaconItem.getInt("y"));
                beaconList.add(i,new MapBeaconInfo(beaconItem.getInt("x"), beaconItem.getInt("y")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return beaconList;
    }


}
