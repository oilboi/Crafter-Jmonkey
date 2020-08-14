import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jogamp.opengl.math.geom.AABBox;

import java.util.Arrays;

public class Player {
    private static int renderDistance = Crafter.getRenderDistance();
    private static Vector3f pos = new Vector3f(0,52,0);

    private static float eyeHeight = 1.5f;

    private static Vector3f inertia = new Vector3f(0,0,0);

    private static float height = 1.9f;

    private static float width = 0.3f;

    private static int[] currentChunk = {0,0};

    private static boolean onGround = false;

    private static boolean jumpBuffer = false;


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
        inertia.y -= 50 * tpf;

        if(jumpBuffer){
            inertia.y += 12f;
            jumpBuffer = false;
        }

        applyInertia(tpf);

        int[] current = new int[2];
        Vector3f flooredPos = pos.clone();
        flooredPos.x = FastMath.floor(flooredPos.x);
        flooredPos.z = FastMath.floor(flooredPos.z);
        current[0] = (int)(FastMath.floor(flooredPos.x / 16f));
        current[1] = (int)(FastMath.floor(flooredPos.z / 16f));
        currentChunk = current;
        if(pos.y < 0){
            pos = new Vector3f(0,100,0);
        }
    }

    private static void applyInertia(float tpf){
        Vector3f newPos = pos;
        newPos.x += inertia.x * tpf;
        newPos.y += inertia.y * tpf;
        newPos.z += inertia.z * tpf;

        //System.out.println(inertia.y);

        collisionDetect(tpf, newPos);

        Vector3f inertia3 = Player.getInertia();
        inertia3.x += -inertia3.x * tpf * 10;
        inertia3.z += -inertia3.z * tpf * 10;
        Player.setInertia(inertia3);
    }

    private static void collisionDetect(float tpf, Vector3f newPos){


        //x- detection
        if (detectBlock(-width, 0.1f, 0)) {
            Vector3f temp = pos.clone();
            temp.x = FastMath.floor(temp.x - width);
            temp.y = FastMath.floor(temp.y + 0.1f);
            temp.z = FastMath.floor(temp.z);
            CustomBlockBox blockBelow = new CustomBlockBox((int) temp.x, (int) temp.y, (int) temp.z);
            CustomAABB us = new CustomAABB(pos.x, pos.y, pos.z, width, height);

            if (blockBelow.getTop() > us.getBottom() && blockBelow.getBottom() < us.getBottom()){
                if (blockBelow.getRight() > us.getLeft()) {
                    pos.x = blockBelow.getRight() + width + 0.001f;
                    inertia.x = 0;
                }
            }
        }

        //x+ detection
        if (detectBlock(width, 0.1f, 0)) {
            Vector3f temp = pos.clone();
            temp.x = FastMath.floor(temp.x+width);
            temp.y = FastMath.floor(temp.y+0.1f);
            temp.z = FastMath.floor(temp.z);
            CustomBlockBox blockBelow = new CustomBlockBox((int) temp.x, (int) temp.y, (int) temp.z);
            CustomAABB us = new CustomAABB(pos.x, pos.y, pos.z, width, height);
            if (blockBelow.getTop() > us.getBottom() && blockBelow.getBottom() < us.getBottom()) {
                if (blockBelow.getLeft() < us.getRight()) {
                    pos.x = blockBelow.getLeft() - width - 0.001f;
                    inertia.x = 0;
                }
            }
        }



        //z- detection
        if (detectBlock(0, 0.1f, -width)) {
            Vector3f temp = pos.clone();
            temp.x = FastMath.floor(temp.x);
            temp.y = FastMath.floor(temp.y + 0.1f);
            temp.z = FastMath.floor(temp.z - width);
            CustomBlockBox blockBelow = new CustomBlockBox((int) temp.x, (int) temp.y, (int) temp.z);
            CustomAABB us = new CustomAABB(pos.x, pos.y, pos.z, width, height);

            if (blockBelow.getTop() > us.getBottom() && blockBelow.getBottom() < us.getBottom()){
                if (blockBelow.getBack() > us.getFront()) {
                    pos.z = blockBelow.getBack() + width + 0.001f;
                    inertia.z = 0;
                }
            }
        }

        //z+ detection
        if (detectBlock(0, 0.1f, width)) {
            Vector3f temp = pos.clone();
            temp.x = FastMath.floor(temp.x);
            temp.y = FastMath.floor(temp.y+0.1f);
            temp.z = FastMath.floor(temp.z+width);
            CustomBlockBox blockBelow = new CustomBlockBox((int) temp.x, (int) temp.y, (int) temp.z);
            CustomAABB us = new CustomAABB(pos.x, pos.y, pos.z, width, height);
            if (blockBelow.getTop() > us.getBottom() && blockBelow.getBottom() < us.getBottom()) {
                if (blockBelow.getFront() < us.getBack()) {
                    pos.z = blockBelow.getFront() - width - 0.001f;
                    inertia.z = 0;
                }
            }
        }


        onGround = false;

        //floor detection
        if (detectBlock(0, 0, 0)) {
            Vector3f temp = pos.clone();
            temp.x = FastMath.floor(temp.x);
            temp.y = FastMath.floor(temp.y);
            temp.z = FastMath.floor(temp.z);
            CustomBlockBox blockBelow = new CustomBlockBox((int) temp.x, (int) temp.y, (int) temp.z);
            CustomAABB us = new CustomAABB(pos.x, pos.y, pos.z, width, height);

            if (blockBelow.getTop() >= us.getBottom()) {
                pos.y = blockBelow.getTop() + 0.001f;
                inertia.y = 0;
                onGround = true;
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

        Vector3f realPos = new Vector3f(flooredPos.x - (16*current[0]), flooredPos.y, flooredPos.z - (16*current[1]));

        return ChunkData.getBlock((int)realPos.x, (int)realPos.y, (int)realPos.z, current[0]+renderDistance, current[1]+renderDistance) != 0;
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

    //binary flip flop
    private static float getSign(float n){
        if(n > 0){
            return 1;
        } else if (n < 0){
            return -1;
        }
        return 0;
    }

    public static boolean isOnGround(){
        return onGround;
    }

    public static void setJumpBuffer(){
        jumpBuffer = true;
    }
}
