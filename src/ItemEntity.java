import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.*;
import com.jme3.scene.shape.Box;
import com.jme3.util.BufferUtils;

import java.util.ArrayList;
import java.util.Arrays;

//this is the item entity object
public class ItemEntity {
    //this holds all the items
    private static ItemEntity[] items = new ItemEntity[5000]; //128000 items max
    private static int currentIndex = 0;
    private static float itemSize = 0.2f;

    private boolean up = true;
    private Vector3f pos;
    private Vector3f inert;
    private short id;
    private int index;
    private float animationHeight;
    private float animationRotation;
    private float timer = 0;

    public ItemEntity(Vector3f position, Vector3f inertia, short blockID, Node rootNode){
        inert = inertia;
        pos = position;
        id = blockID;
        animationHeight = 0;
        animationRotation = 0;
        index = currentIndex;

        Geometry geo = new Geometry("item:"+currentIndex, generateMesh(blockID));
        geo.setMaterial(Loader.getTextureAtlas());
        geo.setLocalTranslation(position);
        rootNode.attachChild(geo);

        //System.out.println(Arrays.toString(items)); don't turn this on unless you want A LOT of debug info

        items[currentIndex] = this;

        currentIndex++;
    }

    public Vector3f getPos(){
        return pos;
    }

    public static void itemsOnTick(Node rootNode){
        for (ItemEntity thisItem : items) {

            if(thisItem == null){
                return;
            }

            thisItem.timer += 0.001f;

            if (thisItem.timer > 2 && thisItem.pos.distance(Player.getPos()) < 3){

                Vector3f toPos = Player.getPos().clone();
                toPos.y += 0.5;

                if(thisItem.pos.distance(toPos) < 0.2f){
                    deleteItem(thisItem.index, rootNode);
                    continue;
                }


                thisItem.inert = toPos.subtract(thisItem.pos).normalize().mult(20f);
                Collision.applyInertia(thisItem.pos, thisItem.inert, false, itemSize, itemSize * 2f, false);
            }   else {
                Collision.applyInertia(thisItem.pos, thisItem.inert, false, itemSize, itemSize * 2f, true);
            }

            Spatial thisItemSpacial = rootNode.getChild("item:"+thisItem.index);

            thisItem.animationRotation += 0.001f;
            if(thisItem.animationRotation > FastMath.TWO_PI){
                thisItem.animationRotation -= FastMath.TWO_PI;
            }
            if(thisItem.up){
                thisItem.animationHeight += 0.0001f;
                if (thisItem.animationHeight > 0.25f){
                    thisItem.up = false;
                }
            } else {
                thisItem.animationHeight -= 0.0001f;
                if (thisItem.animationHeight <= 0f){
                    thisItem.up = true;
                }
            }


            Vector3f posMod = thisItem.pos.clone();
            posMod.y += thisItem.animationHeight;
            thisItemSpacial.setLocalTranslation(posMod);
            thisItemSpacial.setLocalRotation(new Quaternion().fromAngles(0, thisItem.animationRotation, 0));
        }
    }

    private static void deleteItem(int index, Node rootNode){
        rootNode.detachChildNamed("item:"+index);
        for (ItemEntity thisItem : items) {
            if (thisItem == null) {
                break;
            }

            if (thisItem.index > index){
                rootNode.getChild("item:"+thisItem.index).setName("item:"+(thisItem.index-1));
                items[thisItem.index-1] = thisItem;
                thisItem.index -= 1;
            }
        }
        items[currentIndex-1] = null;
        currentIndex -= 1;
    }

    private final static int[] indexes = { 0,1,2, 0,2,3 };
    private static Mesh generateMesh(short blockID){
        Mesh mesh = new Mesh();
        //initialize lists
        ArrayList<Vector3f> vertices = new ArrayList();
        ArrayList<Vector2f> texCoord = new ArrayList();
        ArrayList indexArray = new ArrayList();
        float[] textureMaps;
        int count = 0;

        //TODO front
        vertices.add(new Vector3f(itemSize, -itemSize+itemSize, -itemSize));
        vertices.add(new Vector3f(-itemSize, -itemSize+itemSize, -itemSize));
        vertices.add(new Vector3f(-itemSize, itemSize+itemSize, -itemSize));
        vertices.add(new Vector3f(itemSize, itemSize+itemSize, -itemSize));

        textureMaps = TextureCalculator.calculateTextureMap(blockID);

        texCoord.add(new Vector2f(textureMaps[0], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[3]));
        texCoord.add(new Vector2f(textureMaps[0], textureMaps[3]));

        //add the index data using the count
        for (int i = 0; i < 6; i++) {
            indexArray.add(indexes[i] + count);
        }

        count += 4;


        //TODO back
        vertices.add(new Vector3f(-itemSize, -itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(itemSize, -itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(itemSize, itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(-itemSize, itemSize+itemSize, itemSize));


        textureMaps = TextureCalculator.calculateTextureMap(blockID);

        texCoord.add(new Vector2f(textureMaps[0], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[3]));
        texCoord.add(new Vector2f(textureMaps[0], textureMaps[3]));

        //add the index data using the count
        for (int i = 0; i < 6; i++) {
            indexArray.add(indexes[i] + count);
        }
        count += 4;


        //TODO right
        vertices.add(new Vector3f(itemSize, -itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(itemSize, -itemSize+itemSize, -itemSize));
        vertices.add(new Vector3f(itemSize, itemSize+itemSize, -itemSize));
        vertices.add(new Vector3f(itemSize, itemSize+itemSize, itemSize));

        textureMaps = TextureCalculator.calculateTextureMap(blockID);

        texCoord.add(new Vector2f(textureMaps[0], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[3]));
        texCoord.add(new Vector2f(textureMaps[0], textureMaps[3]));

        //add the index data using the count
        for (int i = 0; i < 6; i++) {
            indexArray.add(indexes[i] + count);
        }

        count += 4;

        //TODO left
        vertices.add(new Vector3f(-itemSize, -itemSize+itemSize, -itemSize));
        vertices.add(new Vector3f(-itemSize, -itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(-itemSize, itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(-itemSize, itemSize+itemSize, -itemSize));

        textureMaps = TextureCalculator.calculateTextureMap(blockID);

        texCoord.add(new Vector2f(textureMaps[0], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[3]));
        texCoord.add(new Vector2f(textureMaps[0], textureMaps[3]));

        //add the index data using the count
        for (int i = 0; i < 6; i++) {
            indexArray.add(indexes[i] + count);
        }

        count += 4;


        //TODO up
        vertices.add(new Vector3f(-itemSize, itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(itemSize, itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(itemSize, itemSize+itemSize, -itemSize));
        vertices.add(new Vector3f(-itemSize, itemSize+itemSize, -itemSize));

        textureMaps = TextureCalculator.calculateTextureMap(blockID);

        texCoord.add(new Vector2f(textureMaps[0], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[3]));
        texCoord.add(new Vector2f(textureMaps[0], textureMaps[3]));

        //add the index data using the count
        for (int i = 0; i < 6; i++) {
            indexArray.add(indexes[i] + count);
        }

        count += 4;


        //TODO down

        vertices.add(new Vector3f(itemSize, -itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(-itemSize, -itemSize+itemSize, itemSize));
        vertices.add(new Vector3f(-itemSize, -itemSize+itemSize, -itemSize));
        vertices.add(new Vector3f(itemSize, -itemSize+itemSize, -itemSize));

        textureMaps = TextureCalculator.calculateTextureMap(blockID);

        texCoord.add(new Vector2f(textureMaps[0], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[2]));
        texCoord.add(new Vector2f(textureMaps[1], textureMaps[3]));
        texCoord.add(new Vector2f(textureMaps[0], textureMaps[3]));

        //add the index data using the count
        for (int i = 0; i < 6; i++) {
            indexArray.add(indexes[i] + count);
        }

        //group together the vertices
        Vector3f[] verticesV3F = vertices.toArray(new Vector3f[0]);

        //group together texture coordinates
        Vector2f[] texCoord2F = texCoord.toArray(new Vector2f[0]);

        //group together indexes
        int vertexLength = indexArray.toArray().length;

        int[] indexPrimative = new int[vertexLength];
        for (int i = 0; i < vertexLength; i++){
            indexPrimative[i] = (int)indexArray.get(i);
        }

        //update buffers
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(verticesV3F));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord2F));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indexPrimative));
        mesh.updateBound();

        return mesh;
    }


}
