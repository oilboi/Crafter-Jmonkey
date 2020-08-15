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

    public static void playerOnTick(float tpf, Node rootNode){
        inertia.y -= 50 * tpf; //gravity

        if(jumpBuffer){
            inertia.y += 10f;//12f;
            jumpBuffer = false;
        }

        applyInertia(tpf, rootNode);

        int[] current = new int[2];
        Vector3f flooredPos = pos.clone();
        flooredPos.x = FastMath.floor(flooredPos.x);
        flooredPos.z = FastMath.floor(flooredPos.z);
        current[0] = (int)(FastMath.floor(flooredPos.x / 16f));
        current[1] = (int)(FastMath.floor(flooredPos.z / 16f));
        currentChunk = current;
        //returns player back to origin point
        if(pos.y < 0){
            pos = new Vector3f(0,100,0);
        }
        //this is for debugging
//        if(pos.y < -3){
//            inertia.y = 0;
//        }
    }

    private static void applyInertia(float tpf, Node rootNode){
        Vector3f newPos = pos.clone();
        newPos.x += inertia.x * tpf;
        newPos.y += inertia.y * tpf;
        newPos.z += inertia.z * tpf;

        //System.out.println(inertia.y);

        collisionDetect(tpf, newPos, rootNode);

        pos = newPos;

        //apply friction
        Vector3f inertia3 = Player.getInertia();
        inertia3.x += -inertia3.x * tpf * 10; // do (10 - 9.5f) for slippery!
        inertia3.z += -inertia3.z * tpf * 10;
        Player.setInertia(inertia3);
    }

    private static void collisionDetect(float tpf, Vector3f newPos, Node rootNode){
        onGround = false;

        //get the real positions of the blocks
        Vector3f fPos = pos.clone();
        fPos.x = FastMath.floor(fPos.x);
        fPos.y = FastMath.floor(fPos.y);
        fPos.z = FastMath.floor(fPos.z);

        //System.out.println(new CustomBlockBox((int)fPos.x, (int)fPos.y, (int)fPos.z).getBasePos());

        CustomBlockBox[] virtualBlock = new CustomBlockBox[3*3*4];
        int index = 0;
        //collect all blocks within collision index
        //todo: turn this into 1D indexing
        //System.out.println();
        for (int x = -1; x <= 1; x++){
            for (int z = -1; z <= 1; z++){
                for (int y = -1; y <= 2; y++){
                    if (detectBlock(new Vector3f(fPos.x + x, fPos.y + y, fPos.z + z))) {
                        virtualBlock[index] = new CustomBlockBox((int) fPos.x + x, (int) fPos.y + y, (int) fPos.z + z);
                        index++;
                        //System.out.println((fPos.x + x) + " " + (fPos.y + y) + " " + (fPos.z+z));
                    }
                }
            }
        }
        //run through collisions
        for(int i = 0; i < index; i++){
            //virtualBlock[i]; //this is the block object
            CustomAABB us = new CustomAABB(newPos.x, newPos.y, newPos.z, width, height);
            collide(us, virtualBlock[i], newPos, rootNode);
        }
    }

    //this is where actual collision events occur!
    public static void collide(CustomAABB us, CustomBlockBox block, Vector3f newPos, Node rootNode){

        boolean xWithin = !(us.getLeft()   > block.getRight() || us.getRight() < block.getLeft());
        boolean yWithin = !(us.getBottom() > block.getTop()   || us.getTop()   < block.getBottom());
        boolean zWithin = !(us.getFront()  > block.getBack()  || us.getBack()  < block.getFront());

        //System.out.println(block.getLeft() + " " + block.getBottom() + " " + block.getFront());

        if (xWithin && zWithin && yWithin) {
            //floor detection
            if (block.getTop() > us.getBottom() && inertia.y < 0 && us.getBottom() - block.getTop() > -0.01f) {
                //this is the collision debug sphere for terrain
                float oldPos = newPos.y;
                newPos.y = block.getTop();
                //don't move up if too high
                if (newPos.y - oldPos > 1) {
                    newPos.y = (int)oldPos;
                }
                rootNode.getChild("collision").setLocalTranslation((block.getRight() + block.getLeft()) / 2f, block.getTop(), (block.getFront() + block.getBack()) / 2f);
                inertia.y = 0;
                onGround = true;
            }
        }

        float averageX = FastMath.abs(((block.getLeft() + block.getRight())/2f) - newPos.x);
        float averageY = FastMath.abs(((block.getBottom() + block.getTop())/2f) - newPos.y);
        float averageZ = FastMath.abs(((block.getFront() + block.getBack())/2f) - newPos.z);

        if (averageX > averageZ) {
            us = new CustomAABB(newPos.x, newPos.y+0.1f, newPos.z, width, height);
            xWithin = !(us.getLeft()   > block.getRight() || us.getRight() < block.getLeft());
            yWithin = !(us.getBottom() > block.getTop()   || us.getTop()   < block.getBottom());
            zWithin = !(us.getFront()  > block.getBack()  || us.getBack()  < block.getFront());

            //x- detection
            if (xWithin && zWithin && yWithin) {
                if (block.getRight() > us.getLeft() && inertia.x < 0) {
                    rootNode.getChild("collision").setLocalTranslation((block.getRight() + block.getLeft()) / 2f, block.getTop(), (block.getFront() + block.getBack()) / 2f);
                    newPos.x = block.getRight() + width + 0.00001f;
                    inertia.x = 0;
                }
            }

            us = new CustomAABB(newPos.x, newPos.y + 0.1f, newPos.z, width, height);
            xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
            yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
            zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

            //x+ detection
            if (xWithin && zWithin && yWithin) {
                if (block.getLeft() < us.getRight() && inertia.x > 0) {
                    rootNode.getChild("collision").setLocalTranslation((block.getRight() + block.getLeft()) / 2f, block.getTop(), (block.getFront() + block.getBack()) / 2f);
                    newPos.x = block.getLeft() - width - 0.00001f;
                    inertia.x = 0;
                }
            }
        } else {
            us = new CustomAABB(newPos.x, newPos.y + 0.1f, newPos.z, width, height);
            xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
            yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
            zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

            //z- detection
            if (xWithin && zWithin && yWithin) {
                if (block.getBack() > us.getFront() && inertia.z < 0) {
                    rootNode.getChild("collision").setLocalTranslation((block.getRight() + block.getLeft()) / 2f, block.getTop(), (block.getFront() + block.getBack()) / 2f);
                    newPos.z = block.getBack() + width + 0.00001f;
                    inertia.z = 0;
                }
            }

            us = new CustomAABB(newPos.x, newPos.y + 0.1f, newPos.z, width, height);
            xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
            yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
            zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

            //z+ detection
            if (xWithin && zWithin && yWithin) {
                if (block.getFront() < us.getBack() && inertia.z > 0) {
                    rootNode.getChild("collision").setLocalTranslation((block.getRight() + block.getLeft()) / 2f, block.getTop(), (block.getFront() + block.getBack()) / 2f);
                    newPos.z = block.getFront() - width - 0.00001f;
                    inertia.z = 0;
                }
            }
        }
    }

    private static boolean detectBlock(Vector3f flooredPos){
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
