package navicon.mju.kr.ac.naviconclientv01.functions;


/**
 * Created by KimNamhun on 15. 11. 30..
 * 축척 계산 기능 함수 클래스
 */
public class ComputeScale {


    public static double computeScale(double width, double resizeRate) {


        double mobileScale = width * resizeRate; // mobile scale(1m당)
        return 1/mobileScale;
    }

    public static double computeTime(double remainDistance) {
        double moveDistanceSec = 3600.0 / 4000.0;
        return moveDistanceSec * remainDistance;
    }

}
