import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import jdk.swing.interop.SwingInterOpUtils;
import jme3tools.optimize.GeometryBatchFactory;

import java.util.ArrayList;
import java.util.Collection;


public class ChunkMesh extends Mesh{
    private final static short chunkSizeX = 16;
    private final static short chunkSizeY = 128;
    private final static short chunkSizeZ = 16;

    public static Geometry genChunkMesh(Chunk chunk, AssetManager assetManager, int chunkX, int chunkZ){
//        long startTime = System.currentTimeMillis();
        //set up data
        int x = 0;
        int y = 0;
        int z = 0;

        chunkX *= chunkSizeX;
        chunkZ *= chunkSizeZ;

        Quad quad = new Quad(1, 1); //The quad worker object
        Collection<Geometry> meshCollection = new ArrayList();

        Geometry geo; // using Quad object

        for (int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++) {
            int block = chunk.getBlock(x,y,z);

            if( block > 0) {
                //TODO: optimize this further
                //TODO: put this on a short term thread to return then terminate thread
                //front


                if (chunk.getBlock(x,y,z+1) == 0) {
                    geo = new Geometry("OurQuad", quad);
                    geo.setLocalTranslation(0 + x + chunkX, 0 + y, 0 + z + chunkZ);
                    meshCollection.add(geo);
                }


                //right
                if (chunk.getBlock(x+1,y,z) == 0) {
                    geo = new Geometry("OurQuad", quad);
                    geo.setLocalTranslation(1 + x + chunkX, 0 + y, 0 + z + chunkZ);
                    geo.rotate(0,  FastMath.HALF_PI, 0);
                    meshCollection.add(geo);
                }

                //back
                if (chunk.getBlock(x,y,z-1) == 0) {
                    geo = new Geometry("OurQuad", quad);
                    geo.setLocalTranslation(1 + x + chunkX, 0 + y, -1 + z + chunkZ);
                    geo.rotate(0,  FastMath.PI, 0);
                    meshCollection.add(geo);
                }

                //left
                if (chunk.getBlock(x-1,y,z) == 0) {
                    geo = new Geometry("OurQuad", quad);
                    geo.setLocalTranslation(0 + x + chunkX, 0 + y, -1 + z + chunkZ);
                    geo.rotate(0,  FastMath.PI + FastMath.HALF_PI, 0);
                    meshCollection.add(geo);
                }

                //top
                if (chunk.getBlock(x,y+1,z) == 0) {
                    geo = new Geometry("OurQuad", quad);
                    geo.setLocalTranslation(0 + x + chunkX, 1 + y, 0 + z + chunkZ);
                    geo.rotate( FastMath.PI  + FastMath.HALF_PI, 0, 0);
                    meshCollection.add(geo);
                }

                //bottom
                if (chunk.getBlock(x,y-1,z) == 0) {
                    geo = new Geometry("OurQuad", quad);
                    geo.setLocalTranslation(0 + x + chunkX, 0 + y, 0 + z + chunkZ);
                    geo.rotate(FastMath.HALF_PI, FastMath.HALF_PI, 0);
                    meshCollection.add(geo);
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

        Mesh finalizedChunkMesh = new Mesh();

        GeometryBatchFactory.mergeGeometries(meshCollection,finalizedChunkMesh);

        geo = new Geometry("test", finalizedChunkMesh);

        geo.setMaterial(Loader.loadMaterial("dirt.png", assetManager));


        finalizedChunkMesh = null;
        meshCollection = null;
        quad = null;

        System.gc();

        //chunky.scaleTextureCoordinates(new Vector2f(2,2));

        //System.gc();
//        long endTime = System.currentTimeMillis();
//        double timeElapsed = (double)(endTime - startTime)/1000;
//        System.out.println("Mesh gen time: " + timeElapsed + " seconds");

        return geo;
    }
}
