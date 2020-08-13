import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import java.util.Arrays;

public class Player {
    private static Vector3f pos = new Vector3f(0,45,0);
    private static float eyeHeight = 1.5f;

    private static Vector3f inertia = new Vector3f(0,0,0);
    private static float height = 1.9f;
    private static float width = 0.3f;

    private static int[] currentChunk = {0,0};

    public static Vector3f getPos() {
        return pos;
    }

    public static Vector3f getPosWithEyeHeight(){
        return new Vector3f(pos.x, pos.y + eyeHeight, pos.z);
    }

    public static void setPos(Vector3f pos) {
        Player.pos = pos;
    }

    public static Vector3f getInertia(){
        return inertia;
    }

    public static void setInertia(Vector3f inertia) {
        Player.inertia = inertia;
    }

    public static void playerOnTick(float tpf){
        Vector3f currentPos = pos;
        currentPos.x += inertia.x * tpf;
        currentPos.y += inertia.y * tpf;
        currentPos.z += inertia.z * tpf;
        setPos(currentPos);
        Vector3f inertia3 = Player.getInertia();
        inertia3.x += -inertia3.x * tpf * 10;
        inertia3.y += -inertia3.y * tpf * 10;
        inertia3.z += -inertia3.z * tpf * 10;
        Player.setInertia(inertia3);

        int[] current = new int[2];

        Vector3f flooredPos = currentPos.clone();
        flooredPos.x = FastMath.floor(flooredPos.x);
        flooredPos.z = FastMath.floor(flooredPos.z);
        current[0] = (int)(FastMath.floor(flooredPos.x / 16f));
        current[1] = (int)(FastMath.floor(flooredPos.z / 16f));
        currentChunk = current;
        //System.out.println(Arrays.toString(current));
        //System.out.println(FastMath.floor(pos.x) + " " + FastMath.floor(pos.z));
    }


    //private static void collisionDetect(
}
