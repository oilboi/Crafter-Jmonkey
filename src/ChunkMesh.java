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

    private final static int[] indexes = { 0,1,2, 0,2,3 };

    public static Geometry genChunkMesh(Chunk chunk, AssetManager assetManager, int chunkX, int chunkZ){
//        long startTime = System.currentTimeMillis();
        //set up data
        int x = 0;
        int y = 0;
        int z = 0;

        int count = 0;

        chunkX *= chunkSizeX;
        chunkZ *= chunkSizeZ;

        Mesh mesh = new Mesh();

        //initialize lists
        ArrayList<Vector3f> vertices = new ArrayList();
        ArrayList<Vector2f> texCoord = new ArrayList();
        ArrayList indexArray = new ArrayList();

        short neighborBlock;
        float[] textureMaps;
        short blockID;
        for (int w = 0; w < (chunkSizeX * chunkSizeY * chunkSizeZ); w++) {

            blockID = chunk.getBlock(x,y,z);
            if (blockID != 0) {
/////////////////////////////////////////////////////////////////////////////////////

                //TODO front
                neighborBlock = chunk.getBlock(x, y, z - 1);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(1 + x + chunkX, 0 + y, 0 + z + chunkZ));
                    vertices.add(new Vector3f(0 + x + chunkX, 0 + y, 0 + z + chunkZ));

                    vertices.add(new Vector3f(0 + x + chunkX, 1 + y, 0 + z + chunkZ));
                    vertices.add(new Vector3f(1 + x + chunkX, 1 + y, 0 + z + chunkZ));

                    textureMaps = TextureCalculator.calculateTextureMap(blockID);

                    texCoord.add(new Vector2f(textureMaps[0], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 1));
                    texCoord.add(new Vector2f(textureMaps[0], 1));

                    //add the index data using the count
                    for (int i = 0; i < 6; i++) {
                        indexArray.add(indexes[i] + count);
                    }

                    count += 4;
                }

/////////////////////////////////////////////////////////////////////////////////////

                //TODO back
                neighborBlock = chunk.getBlock(x, y, z + 1);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(0 + x + chunkX, 0 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(1 + x + chunkX, 0 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(1 + x + chunkX, 1 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(0 + x + chunkX, 1 + y, 1 + z + chunkZ));


                    textureMaps = TextureCalculator.calculateTextureMap(blockID);

                    texCoord.add(new Vector2f(textureMaps[0], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 1));
                    texCoord.add(new Vector2f(textureMaps[0], 1));

                    //add the index data using the count
                    for (int i = 0; i < 6; i++) {
                        indexArray.add(indexes[i] + count);
                    }
                    count += 4;
                }
/////////////////////////////////////////////////////////////////////////////////////

                //TODO right
                neighborBlock = chunk.getBlock(x + 1, y, z);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(1 + x + chunkX, 0 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(1 + x + chunkX, 0 + y, 0 + z + chunkZ));
                    vertices.add(new Vector3f(1 + x + chunkX, 1 + y, 0 + z + chunkZ));
                    vertices.add(new Vector3f(1 + x + chunkX, 1 + y, 1 + z + chunkZ));

                    textureMaps = TextureCalculator.calculateTextureMap(blockID);

                    texCoord.add(new Vector2f(textureMaps[0], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 1));
                    texCoord.add(new Vector2f(textureMaps[0], 1));

                    //add the index data using the count
                    for (int i = 0; i < 6; i++) {
                        indexArray.add(indexes[i] + count);
                    }

                    count += 4;
                }
/////////////////////////////////////////////////////////////////////////////////////

                //TODO left
                neighborBlock = chunk.getBlock(x - 1, y, z);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(0 + x + chunkX, 0 + y, 0 + z + chunkZ));
                    vertices.add(new Vector3f(0 + x + chunkX, 0 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(0 + x + chunkX, 1 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(0 + x + chunkX, 1 + y, 0 + z + chunkZ));

                    textureMaps = TextureCalculator.calculateTextureMap(blockID);

                    texCoord.add(new Vector2f(textureMaps[0], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 1));
                    texCoord.add(new Vector2f(textureMaps[0], 1));

                    //add the index data using the count
                    for (int i = 0; i < 6; i++) {
                        indexArray.add(indexes[i] + count);
                    }

                    count += 4;
                }
/////////////////////////////////////////////////////////////////////////////////////

                //TODO up
                neighborBlock = chunk.getBlock(x, y + 1, z);
                if (neighborBlock == 0) {
                    vertices.add(new Vector3f(0 + x + chunkX, 1 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(1 + x + chunkX, 1 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(1 + x + chunkX, 1 + y, 0 + z + chunkZ));
                    vertices.add(new Vector3f(0 + x + chunkX, 1 + y, 0 + z + chunkZ));

                    textureMaps = TextureCalculator.calculateTextureMap(blockID);

                    texCoord.add(new Vector2f(textureMaps[0], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 1));
                    texCoord.add(new Vector2f(textureMaps[0], 1));

                    //add the index data using the count
                    for (int i = 0; i < 6; i++) {
                        indexArray.add(indexes[i] + count);
                    }

                    count += 4;
                }
/////////////////////////////////////////////////////////////////////////////////////

                //TODO down
                neighborBlock = chunk.getBlock(x, y - 1, z);
                if (neighborBlock == 0 && y != 0) {
                    vertices.add(new Vector3f(1 + x + chunkX, 0 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(0 + x + chunkX, 0 + y, 1 + z + chunkZ));
                    vertices.add(new Vector3f(0 + x + chunkX, 0 + y, 0 + z + chunkZ));
                    vertices.add(new Vector3f(1 + x + chunkX, 0 + y, 0 + z + chunkZ));

                    textureMaps = TextureCalculator.calculateTextureMap(blockID);

                    texCoord.add(new Vector2f(textureMaps[0], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 0));
                    texCoord.add(new Vector2f(textureMaps[1], 1));
                    texCoord.add(new Vector2f(textureMaps[0], 1));

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


        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(verticesV3F));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord2F));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indexPrimative));
        mesh.updateBound();

        Geometry geo = new Geometry("OurMesh", mesh); // using our custom mesh object
        geo.setMaterial(Loader.getTextureAtlas());

        return geo;
    }
}
