import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class Player {
    private static Vector3f pos = new Vector3f(0,128,0);
    private static float eyeHeight = 1.5f;

    public static Vector3f getPos() {
        return pos;
    }

    public static Vector3f getPosWithEyeHeight(){
        return new Vector3f(pos.x, pos.y + eyeHeight, pos.z);
    }

    public static void setPos(Vector3f pos) {
        Player.pos = pos;
    }
    
    public static void playerOnTick(Camera cam){

    }
}
