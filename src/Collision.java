import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class Collision {
    private static int renderDistance = Crafter.getRenderDistance();
    final private static float gameSpeed = 0.001f;
    private static boolean objectOnGround = false;
    public static boolean applyInertia(Vector3f pos,Vector3f inertia, boolean onGround, float width, float height){
        objectOnGround = onGround;
        inertia.y -= 50 * gameSpeed; //gravity

        //limit speed
        if (inertia.y <= -70f){
            inertia.y = -70f;
        } else if (inertia.y > 70f){
            inertia.y = 70f;
        }

        pos.x += inertia.x * gameSpeed;
        pos.y += inertia.y * gameSpeed;
        pos.z += inertia.z * gameSpeed;

        collisionDetect(pos, inertia, onGround, width, height);

        //apply friction
        Vector3f inertia3 = Player.getInertia();
        inertia3.x += -inertia3.x * gameSpeed * 10; // do (10 - 9.5f) for slippery!
        inertia3.z += -inertia3.z * gameSpeed * 10;
        Player.setInertia(inertia3);

        return objectOnGround;
    }

    private static void collisionDetect(Vector3f pos, Vector3f inertia, boolean onGround, float width, float height){
        objectOnGround = false;

        //get the real positions of the blocks
        Vector3f fPos = pos.clone();
        fPos.x = FastMath.floor(fPos.x);
        fPos.y = FastMath.floor(fPos.y);
        fPos.z = FastMath.floor(fPos.z);

        CustomBlockBox[] virtualBlock = new CustomBlockBox[3*3*4];
        int index = 0;

        //collect all blocks within collision index
        //todo: turn this into 1D indexing
        for (int x = -1; x <= 1; x++){
            for (int z = -1; z <= 1; z++){
                for (int y = -1; y <= 2; y++){
                    if (detectBlock(new Vector3f(fPos.x + x, fPos.y + y, fPos.z + z))) {
                        virtualBlock[index] = new CustomBlockBox((int) fPos.x + x, (int) fPos.y + y, (int) fPos.z + z);
                        index++;
                    }
                }
            }
        }

        //run through collisions
        for(int i = 0; i < index; i++){
            //virtualBlock[i]; //this is the block object
            CustomAABB us = new CustomAABB(pos.x, pos.y, pos.z, width, height);
            collide(us, virtualBlock[i], pos, inertia, width, height);
        }
    }

    //this is where actual collision events occur!
    public static void collide(CustomAABB us, CustomBlockBox block, Vector3f pos, Vector3f inertia, float width, float height){

        boolean xWithin = !(us.getLeft()   > block.getRight() || us.getRight() < block.getLeft());
        boolean yWithin = !(us.getBottom() > block.getTop()   || us.getTop()   < block.getBottom());
        boolean zWithin = !(us.getFront()  > block.getBack()  || us.getBack()  < block.getFront());

        //double check to stop clipping if not enough space
        if (xWithin && zWithin && yWithin &&
                !detectBlock(new Vector3f(block.getLeft(), block.getBottom()+1,block.getFront())) &&
                !detectBlock(new Vector3f(block.getLeft(), block.getBottom()+2,block.getFront()))) {
            //System.out.println((us.getBottom() - block.getTop()));
            //floor detection
            if (block.getTop() > us.getBottom() && inertia.y < 0 && us.getBottom() - block.getTop() > -0.15f) {
                //this is the collision debug sphere for terrain
                float oldPos = pos.y;
                pos.y = block.getTop();
                //don't move up if too high
                if (pos.y - oldPos > 1) {
                    pos.y = (int)oldPos;
                }
                inertia.y = 0;
                objectOnGround = true;
            }
        }

        //stop getting shot across the map
        if (xWithin && zWithin && yWithin  &&
                !detectBlock(new Vector3f(block.getLeft(), block.getBottom()-1,block.getFront())) &&
                !detectBlock(new Vector3f(block.getLeft(), block.getBottom()-2,block.getFront()))) {
            //head detection
            if (block.getBottom() < us.getTop() && inertia.y >= 0 && us.getTop() - block.getBottom() < 0.15f) {
                pos.y = block.getBottom() - height;
                inertia.y = 0;
            }
        }



        float averageX = FastMath.abs(((block.getLeft() + block.getRight())/2f) - pos.x);
        float averageY = FastMath.abs(((block.getBottom() + block.getTop())/2f) - pos.y);
        float averageZ = FastMath.abs(((block.getFront() + block.getBack())/2f) - pos.z);

        if (averageX > averageZ) {

            if (!detectBlock(new Vector3f(block.getLeft()+1, block.getBottom(),block.getFront()))) {
                us = new CustomAABB(pos.x, pos.y + 0.1501f, pos.z, width, height - 0.3001f);
                xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
                yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
                zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

                //x- detection
                if (xWithin && zWithin && yWithin) {
                    if (block.getRight() > us.getLeft() && inertia.x < 0) {
                        pos.x = block.getRight() + width + 0.00001f;
                        inertia.x = 0;
                    }
                }
            }

            if (!detectBlock(new Vector3f(block.getLeft()-1, block.getBottom(),block.getFront()))) {
                us = new CustomAABB(pos.x, pos.y + 0.1501f, pos.z, width, height - 0.3001f);
                xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
                yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
                zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

                //x+ detection
                if (xWithin && zWithin && yWithin) {
                    if (block.getLeft() < us.getRight() && inertia.x > 0) {
                        pos.x = block.getLeft() - width - 0.00001f;
                        inertia.x = 0;
                    }
                }
            }
        } else {
            if (!detectBlock(new Vector3f(block.getLeft(), block.getBottom(),block.getFront()+1))) {
                us = new CustomAABB(pos.x, pos.y + 0.1501f, pos.z, width, height - 0.3001f);
                xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
                yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
                zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

                //z- detection
                if (xWithin && zWithin && yWithin) {
                    if (block.getBack() > us.getFront() && inertia.z < 0) {
                        pos.z = block.getBack() + width + 0.00001f;
                        inertia.z = 0;
                    }
                }
            }
            if (!detectBlock(new Vector3f(block.getLeft(), block.getBottom(),block.getFront()-1))) {
                us = new CustomAABB(pos.x, pos.y + 0.1501f, pos.z, width, height - 0.3001f);
                xWithin = !(us.getLeft() > block.getRight() || us.getRight() < block.getLeft());
                yWithin = !(us.getBottom() > block.getTop() || us.getTop() < block.getBottom());
                zWithin = !(us.getFront() > block.getBack() || us.getBack() < block.getFront());

                //z+ detection
                if (xWithin && zWithin && yWithin) {
                    if (block.getFront() < us.getBack() && inertia.z > 0) {
                        pos.z = block.getFront() - width - 0.00001f;
                        inertia.z = 0;
                    }
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
}
