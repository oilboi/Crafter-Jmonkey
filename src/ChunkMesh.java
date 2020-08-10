import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import jme3tools.optimize.GeometryBatchFactory;

import java.util.ArrayList;
import java.util.Collection;


public class ChunkMesh extends Mesh{
    private static short chunkSizeX = 16;
    private static short chunkSizeY = 128;
    private static short chunkSizeZ = 16;

    public static Geometry genChunkMesh(Chunk chunk, AssetManager assetManager){//, Node rootNode){
        //set up data
        int x = 0;
        int y = 0;
        int z = 0;

        Collection<Geometry> meshCollection = new ArrayList();

        for (int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++) {

            int block = chunk.getBlock(x,y,z);
            if( block > 0) {
                //front
                Quad quad = new Quad(1, 1); // replace the definition of Vertex and Textures Coordinates plus indexes
                Geometry geo = new Geometry("OurQuad", quad); // using Quad object
                //Material mat = new Material(assetManager,
                //        "Common/MatDefs/Misc/Unshaded.j3md");
                //mat.setColor("Color", ColorRGBA.White);
                //geo.setMaterial(mat);
                geo.setLocalTranslation(0 + x, 0 + y, 0 + z);
                meshCollection.add(geo);

                //right
                Quad quad2 = new Quad(1, 1); // replace the definition of Vertex and Textures Coordinates plus indexes
                Geometry geo2 = new Geometry("OurQuad", quad2); // using Quad object
//            Material mat2 = new Material(assetManager,
//                    "Common/MatDefs/Misc/Unshaded.j3md");
//            mat2.setColor("Color", ColorRGBA.Gray);
//            geo2.setMaterial(mat2);
                geo2.setLocalTranslation(1 + x, 0 + y, 0 + z);
                geo2.rotate(0, (float) Math.PI / 2, 0);
                meshCollection.add(geo2);

                //back
                Quad quad3 = new Quad(1, 1); // replace the definition of Vertex and Textures Coordinates plus indexes
                Geometry geo3 = new Geometry("OurQuad", quad3); // using Quad object
//            Material mat3 = new Material(assetManager,
//                    "Common/MatDefs/Misc/Unshaded.j3md");
//            mat3.setColor("Color", ColorRGBA.Green);
//            geo3.setMaterial(mat3);
                geo3.setLocalTranslation(1 + x, 0 + y, -1 + z);
                geo3.rotate(0, (float) Math.PI, 0);
                meshCollection.add(geo3);

                //left
                Quad quad4 = new Quad(1, 1); // replace the definition of Vertex and Textures Coordinates plus indexes
                Geometry geo4 = new Geometry("OurQuad", quad4); // using Quad object
//            Material mat4 = new Material(assetManager,
//                    "Common/MatDefs/Misc/Unshaded.j3md");
//            mat4.setColor("Color", ColorRGBA.Pink);
//            geo4.setMaterial(mat4);
                geo4.setLocalTranslation(0 + x, 0 + y, -1 + z);
                geo4.rotate(0, (float) Math.PI * (float) 1.5, 0);
                meshCollection.add(geo4);

                //top
                Quad quad5 = new Quad(1, 1); // replace the definition of Vertex and Textures Coordinates plus indexes
                Geometry geo5 = new Geometry("OurQuad", quad5); // using Quad object
//            Material mat5 = new Material(assetManager,
//                    "Common/MatDefs/Misc/Unshaded.j3md");
//            mat5.setColor("Color", ColorRGBA.Yellow);
//            geo5.setMaterial(mat5);
                geo5.setLocalTranslation(0 + x, 1 + y, 0 + z);
                geo5.rotate((float) Math.PI * (float) 1.5, 0, 0);
                meshCollection.add(geo5);

                //bottom
                Quad quad6 = new Quad(1, 1); // replace the definition of Vertex and Textures Coordinates plus indexes
                Geometry geo6 = new Geometry("OurQuad", quad6); // using Quad object
//            Material mat6 = new Material(assetManager,
//                    "Common/MatDefs/Misc/Unshaded.j3md");
//            mat6.setColor("Color", ColorRGBA.Red);
//            geo6.setMaterial(mat6);
                geo6.setLocalTranslation(0 + x, 0 + y, 0 + z);
                geo6.rotate((float) Math.PI / 2, (float) Math.PI / 2, 0);
                meshCollection.add(geo6);
            }
            y++;
            if( y > chunkSizeY - 1){
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

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geo.setMaterial(mat);

        return geo;
    }
}
