import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class GameCamera {

    private static Quaternion tmpQuat = new Quaternion();
    private static float[] angles = new float[3];

    public static void handleCamera(Camera cam){
        cam.getRotation().toAngles(angles);
        if(angles[0]> FastMath.HALF_PI){
            angles[0]=FastMath.HALF_PI;
            cam.setRotation(tmpQuat.fromAngles(angles));
        }else if(angles[0]<-FastMath.HALF_PI){
            angles[0]=-FastMath.HALF_PI;
            cam.setRotation(tmpQuat.fromAngles(angles));
        }

        cam.setLocation(Player.getPosWithEyeHeight());
    }
}
