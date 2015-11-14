package navicon.mju.kr.ac.naviconclientv01.functions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KimNamhun on 15. 11. 13..
 */
public class JSONParser {

    private JSONObject json; // JSON Object 관리

    public JSONParser(String strJson) { // JSON 객체 가지고 오기
        try {
            json = new JSONObject(strJson);
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
        return null;
    }


}
