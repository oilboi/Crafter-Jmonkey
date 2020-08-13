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
    }

    public static void handleKeys(Camera cam,float tpf, String name, float value ){
        float dir = 0;
        int run2D = 1;
        switch(name) {
            case "w":
                dir = 0;
                break;
            case "s":
                dir = FastMath.PI;
                break;
            case "a":
                dir = FastMath.HALF_PI;
                break;
            case "d":
                dir = FastMath.HALF_PI + FastMath.PI;
                break;
            default:
                run2D = 0;
                break;
        }

        switch (run2D) {
            case 1:
                Vector3f pos = cam.getLocation();
                float[] rot = new float[3];
                cam.getRotation().normalizeLocal().toAngles(rot);

                float x = FastMath.sin(rot[1] + dir);
                float z = FastMath.cos(-rot[1] - dir);
                pos.x += x * 20 * tpf;
                pos.z += z * 20 * tpf;
                cam.setLocation(pos);
                break;
            case 0:
                int move = 0;
                switch (name){
                    case "space":
                        move = 1;
                        break;
                    case "shift":
                        move = -1;
                        break;
                }

                if(move==0){
                    return;
                }

                Vector3f pos2 = cam.getLocation();
                float[] rot2 = new float[3];
                cam.getRotation().normalizeLocal().toAngles(rot2);
                cam.setLocation(pos2);
                pos2.y += move * 20 * tpf;
                
                break;
        }

    }
}
