import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jogamp.opengl.math.geom.AABBox;

import java.util.Arrays;

public class Player {
    private static int renderDistance = Crafter.getRenderDistance();
    private static Vector3f pos = new Vector3f(0,150,0);
    private static float eyeHeight = 1.5f;
    private static Vector3f inertia = new Vector3f(0,0,0);
    private static float height = 1.9f;
    private static float width = 0.3f;
    private static int[] currentChunk = {0,0};
    private static boolean onGround = false;
    private static boolean jumpBuffer = false;
    private static boolean mining = false;
    private static float mineTimer = 0;
    private static boolean placing = false;
    private static float placeTimer = 0;
    private static short selectedItem = 1;

    public static short getSelectedItem(){
        return selectedItem;
    }

    public static void setSelectedItem(short newItem){
        selectedItem = newItem;
    }

    public static void setMining(){
        if (mineTimer == 0) {
            mining = true;
            mineTimer = 20;
        }
    }

    public static boolean getMining(){
        return mining;
    }


    public static void setPlacing() {
        if (placeTimer == 0) {
            placing = true;
            placeTimer = 20;
        }
    }

    public static float getHeight(){
        return height;
    }
    public static float getWidth(){
        return width;
    }

    public static boolean getPlacing() {
        return placing;
    }

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



    public static void playerOnTick(){
        if(jumpBuffer){
            inertia.y += 12f;
            jumpBuffer = false;
        }
        if(mining){
            mining = false;
        }
        if(placing){
            placing = false;
        }

        if(placeTimer > 0){
            placeTimer -= 0.1f;
            if (placeTimer < 0.1){
                placeTimer = 0;
            }
        }

        if(mineTimer > 0){
            mineTimer -= 0.1f;
            if (mineTimer < 0.1){
                mineTimer = 0;
            }
        }

        onGround = Collision.applyInertia(pos, inertia, onGround, width, height,true);

        int[] current = new int[2];
        Vector3f flooredPos = pos.clone();
        flooredPos.x = FastMath.floor(flooredPos.x);
        flooredPos.z = FastMath.floor(flooredPos.z);
        current[0] = (int)(FastMath.floor(flooredPos.x / 16f));
        current[1] = (int)(FastMath.floor(flooredPos.z / 16f));
        currentChunk = current;

    }

    private static short getBlock(float x, float y, float z){
        Vector3f flooredPos = pos.clone();
        flooredPos.x = FastMath.floor(flooredPos.x + x);
        flooredPos.y = FastMath.floor(flooredPos.y + y);
        flooredPos.z = FastMath.floor(flooredPos.z + z);

        int[] current = new int[2];
        current[0] = (int)(FastMath.floor(flooredPos.x / 16f));
        current[1] = (int)(FastMath.floor(flooredPos.z / 16f));

        Vector3f realPos = new Vector3f(flooredPos.x - (16*current[0]), flooredPos.y, flooredPos.z - (16*current[1]));

        return ChunkData.getBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, current[0]+renderDistance, current[1]+renderDistance);
    }


    public static boolean isOnGround(){
        return onGround;
    }

    public static void setJumpBuffer(){
        jumpBuffer = true;
    }
}
