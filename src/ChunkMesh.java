import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import jme3tools.optimize.GeometryBatchFactory;

import java.util.ArrayList;
import java.util.Collection;


public class ChunkMesh extends Mesh{
    private static short chunkSizeX = 16;
    private static short chunkSizeY = 128;
    private static short chunkSizeZ = 16;

    public static Geometry genChunkMesh(Chunk chunk, AssetManager assetManager, int chunkX, int chunkZ){
        //set up data
        int x = 0;
        int y = 0;
        int z = 0;

        chunkX *= chunkSizeX;
        chunkZ *= chunkSizeZ;

        Collection<Geometry> meshCollection = new ArrayList();

        int count = 0;

        Quad quad = new Quad(1, 1); //The quad worker object
        for (int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++) {
            int block = chunk.getBlock(x,y,z);

            if( block > 0) {
                //TODO: optimize this further
                //TODO: put this on a short term thread to return then terminate thread
                /*
                TODO: WARNING!!
                x is pushing the object to the right
                z is in front of x
                for axis x check z
                for axis z check x
                 */
                //Material matVC = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                //matVC.setBoolean("VertexColor", true);

                //front
                if (chunk.getBlock(x,y,z+1) == 0) {
                    Geometry geo = new Geometry("OurQuad", quad); // using Quad object
                    geo.setLocalTranslation(0 + x + chunkX, 0 + y, 0 + z + chunkZ);
                    meshCollection.add(geo);
                }

                //right
                if (chunk.getBlock(x+1,y,z) == 0) {
                    Geometry geo2 = new Geometry("OurQuad", quad); // using Quad object
                    geo2.setLocalTranslation(1 + x + chunkX, 0 + y, 0 + z + chunkZ);
                    geo2.rotate(0, (float) Math.PI / 2, 0);
                    meshCollection.add(geo2);
                }

                //back
                if (chunk.getBlock(x,y,z-1) == 0) {
                    Geometry geo3 = new Geometry("OurQuad", quad); // using Quad object
                    geo3.setLocalTranslation(1 + x + chunkX, 0 + y, -1 + z + chunkZ);
                    geo3.rotate(0, (float) Math.PI, 0);
                    meshCollection.add(geo3);
                }

                //left
                if (chunk.getBlock(x-1,y,z) == 0) {
                    Geometry geo4 = new Geometry("OurQuad", quad); // using Quad object
                    geo4.setLocalTranslation(0 + x + chunkX, 0 + y, -1 + z + chunkZ);
                    geo4.rotate(0, (float) Math.PI * (float) 1.5, 0);
                    meshCollection.add(geo4);
                }

                //top
                if (chunk.getBlock(x,y+1,z) == 0) {
                    Geometry geo5 = new Geometry("OurQuad", quad); // using Quad object
                    geo5.setLocalTranslation(0 + x + chunkX, 1 + y, 0 + z + chunkZ);
                    geo5.rotate((float) Math.PI * (float) 1.5, 0, 0);
                    meshCollection.add(geo5);
                }

                //bottom
                if (chunk.getBlock(x,y-1,z) == 0) {
                    Geometry geo6 = new Geometry("OurQuad", quad); // using Quad object
                    geo6.setLocalTranslation(0 + x + chunkX, 0 + y, 0 + z + chunkZ);
                    geo6.rotate((float) Math.PI / 2, (float) Math.PI / 2, 0);
                    meshCollection.add(geo6);
                }
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

        Mesh chunky = new Mesh();

        GeometryBatchFactory.mergeGeometries(meshCollection,chunky);


        Geometry geo = new Geometry("test", chunky);


        geo.setMaterial(Loader.loadMaterial("dirt.png", assetManager));

        //chunky.scaleTextureCoordinates(new Vector2f(2,2));


        return geo;
    }
}
