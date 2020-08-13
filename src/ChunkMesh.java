import com.jme3.app.state.RootNodeAppState;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import jdk.swing.interop.SwingInterOpUtils;
import jme3tools.optimize.GeometryBatchFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class ChunkMesh extends Mesh{
    private final static short chunkSizeX = 16;
    private final static short chunkSizeY = 128;
    private final static short chunkSizeZ = 16;
    private static int renderDistance = Crafter.getRenderDistance();
    private final static int[] indexes = { 0,1,2, 0,2,3 };

    public static void genChunkMesh(Chunk chunk, AssetManager assetManager, int chunkX, int chunkZ, Node rootNode, boolean updating){

        if (rootNode.getChild(chunkX+" "+chunkZ) != null){
            rootNode.detachChildNamed(chunkX+" "+chunkZ);
            //System.out.println("node " + chunkX + " " + chunkZ + " is detached");
        }

//        long startTime = System.currentTimeMillis();
        //set up data
        int x = 0;
        int y = 0;
        int z = 0;

        int count = 0;

        int chunkLocationX = chunkX * chunkSizeX;
        int chunkLocationZ = chunkZ * chunkSizeZ;

        Mesh mesh = new Mesh();

        //initialize lists
        ArrayList<Vector3f> vertices = new ArrayList();
        ArrayList<Vector2f> texCoord = new ArrayList();
        ArrayList indexArray = new ArrayList();

        short neighborBlock;
        float[] textureMaps;
        short blockID;
        for (int w = 0; w < (chunkSizeX * chunkSizeY * chunkSizeZ); w++) {

            blockID = chunk.getBlock(x,y,z, chunkX, chunkZ);
            if (blockID != 0) {
/////////////////////////////////////////////////////////////////////////////////////

                //TODO front
                neighborBlock = chunk.getBlock(x, y, z - 1, chunkX, chunkZ);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 0 + y, 0 + z + chunkLocationZ));
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 0 + y, 0 + z + chunkLocationZ));

                    vertices.add(new Vector3f(0 + x + chunkLocationX, 1 + y, 0 + z + chunkLocationZ));
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 1 + y, 0 + z + chunkLocationZ));

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
                }

/////////////////////////////////////////////////////////////////////////////////////

                //TODO back
                neighborBlock = chunk.getBlock(x, y, z + 1, chunkX, chunkZ);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 0 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 0 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 1 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 1 + y, 1 + z + chunkLocationZ));


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
                }
/////////////////////////////////////////////////////////////////////////////////////

                //TODO right
                neighborBlock = chunk.getBlock(x + 1, y, z, chunkX, chunkZ);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 0 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 0 + y, 0 + z + chunkLocationZ));
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 1 + y, 0 + z + chunkLocationZ));
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 1 + y, 1 + z + chunkLocationZ));

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
                }
/////////////////////////////////////////////////////////////////////////////////////

                //TODO left
                neighborBlock = chunk.getBlock(x - 1, y, z, chunkX, chunkZ);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 0 + y, 0 + z + chunkLocationZ));
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 0 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 1 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 1 + y, 0 + z + chunkLocationZ));

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
                }
/////////////////////////////////////////////////////////////////////////////////////

                //TODO up
                neighborBlock = chunk.getBlock(x, y + 1, z, chunkX, chunkZ);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 1 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 1 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 1 + y, 0 + z + chunkLocationZ));
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 1 + y, 0 + z + chunkLocationZ));

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
                }
/////////////////////////////////////////////////////////////////////////////////////

                //TODO down
                neighborBlock = chunk.getBlock(x, y - 1, z, chunkX, chunkZ);
                if (neighborBlock == 0 && y != 0) {
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 0 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 0 + y, 1 + z + chunkLocationZ));
                    vertices.add(new Vector3f(0 + x + chunkLocationX, 0 + y, 0 + z + chunkLocationZ));
                    vertices.add(new Vector3f(1 + x + chunkLocationX, 0 + y, 0 + z + chunkLocationZ));

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
                }
/////////////////////////////////////////////////////////////////////////////////////

            }
            y++;
            if( y > chunkSizeY - 1 ){
                y = 0;
                x++;
                if( x > chunkSizeX - 1 ){
                    x = 0;
                    z++;
                }
            }
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

        //turn geometry to an object and apply textureAtlas
        Geometry geo = new Geometry(chunkX+" "+chunkZ, mesh);
        geo.setMaterial(Loader.getTextureAtlas());

        rootNode.attachChild(geo);

        if(!updating) {
            updateNeighbors(assetManager, chunkX, chunkZ, rootNode);
        }
    }

    public static void updateNeighbors( AssetManager assetManager, int chunkX, int chunkZ, Node rootNode){
        for (int x = -1; x < 1; x++){
            for (int z = -1; z < 1; z++){
                if (FastMath.abs(x) + FastMath.abs(z) == 1) {
                    if (ChunkData.chunkExists(chunkX + x + renderDistance, chunkZ + z + renderDistance)) {


                        Chunk chunky = ChunkData.getChunkData(chunkX + x, chunkZ + z);
                        //System.out.println(chunkX + x + " | " + chunkZ + z + " is updating!");


                        ChunkMesh.genChunkMesh(chunky, assetManager, chunkX + x, chunkZ + z, rootNode, true);
                    }
                }
            }
        }
    }
}
