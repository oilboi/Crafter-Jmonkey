import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jogamp.opengl.math.geom.AABBox;

import java.util.Arrays;

public class Player {
    private static int renderDistance = Crafter.getRenderDistance();
    private static Vector3f pos = new Vector3f(0,200,0);
    private static float eyeHeight = 1.5f;
    private static Vector3f inertia = new Vector3f(0,0,0);
    private static float height = 1.9f;
    private static float width = 0.3f;
    private static int[] currentChunk = {0,0};
    private static boolean onGround = false;

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
        inertia.y -= 55 * tpf;
        applyInertia(tpf);

        int[] current = new int[2];
        Vector3f flooredPos = pos.clone();
        flooredPos.x = FastMath.floor(flooredPos.x);
        flooredPos.z = FastMath.floor(flooredPos.z);
        current[0] = (int)(FastMath.floor(flooredPos.x / 16f));
        current[1] = (int)(FastMath.floor(flooredPos.z / 16f));

        collisionDetect();

        currentChunk = current;


        //System.out.println(Arrays.toString(current));
        //System.out.println(FastMath.floor(pos.x) + " " + FastMath.floor(pos.z));
    }

    private static void applyInertia(float tpf){
        Vector3f currentPos = pos;
        currentPos.x += inertia.x * tpf;
        currentPos.y += inertia.y * tpf;
        currentPos.z += inertia.z * tpf;
        setPos(currentPos);
        Vector3f inertia3 = Player.getInertia();
        inertia3.x += -inertia3.x * tpf * 10;
        //inertia3.y += -inertia3.y * tpf * 10;
        inertia3.z += -inertia3.z * tpf * 10;
        Player.setInertia(inertia3);
    }

    private static void collisionDetect(){
        onGround = false;
        //detect ground collision
        if (detectBlock(width, 0, width) || detectBlock(width,0, -width) || detectBlock(-width,0,width) || detectBlock(-width,0,-width)) {
            while (!(!detectBlock(width, 0, width) && !detectBlock(width, 0, -width) && !detectBlock(-width, 0, width) && !detectBlock(-width, 0, -width))) {
                pos.y += 0.001;
                inertia.y = 0;
                onGround = true;
            }
        }

        //hit head on ceilings todo: probably needs a lockout when onGround is true
        if (detectBlock(width, height, width) || detectBlock(width,height, -width) || detectBlock(-width,height,width) || detectBlock(-width,height,-width)) {
            while (!(!detectBlock(width, height, width) && !detectBlock(width, height, -width) && !detectBlock(-width, height, width) && !detectBlock(-width, height, -width))) {
                pos.y -= 0.001;
                inertia.y = 0;
            }
        }





    }

    private static boolean detectBlock(float x, float y, float z){
        Vector3f flooredPos = pos.clone();
        flooredPos.x = FastMath.floor(flooredPos.x + x);
        flooredPos.y = FastMath.floor(flooredPos.y + y);
        flooredPos.z = FastMath.floor(flooredPos.z + z);

        int[] current = new int[2];
        current[0] = (int)(FastMath.floor(flooredPos.x / 16f));
        current[1] = (int)(FastMath.floor(flooredPos.z / 16f));

        //System.out.println(Arrays.toString(current));

        Vector3f realPos = new Vector3f(flooredPos.x - (16*current[0]), flooredPos.y, flooredPos.z - (16*current[1]));

        return ChunkData.getBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, current[0]+renderDistance, current[1]+renderDistance) != 0;
    }

    //binary flip flop
    private static float getSign(float n){
        if(n > 0){
            return 1;
        } else if (n < 0){
            return -1;
        }
        return 0;
    }
}
