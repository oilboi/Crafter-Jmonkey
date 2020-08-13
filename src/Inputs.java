import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class Inputs {

    public static void initializeKeys(InputManager inputManager, ActionListener actionListener, AnalogListener analogListener) {
        // You can map one or several inputs to one named action
        inputManager.addMapping("w", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("a", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("s", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("d", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("shift", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("space", new KeyTrigger(KeyInput.KEY_SPACE));

        // Add the names to the action listener.
        //inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(analogListener, "w", "a", "s", "d", "shift", "space");
    }

    public static void handleKeys(Camera cam, float tpf, String name, float value ){
        float dir = 0;
        int run2D = 1;
        switch(name) {
            case "w":
                dir = 0;
                break;
            case "s":
                dir = FastMath.PI;
                break;
            case "a":
                dir = FastMath.HALF_PI;
                break;
            case "d":
                dir = FastMath.HALF_PI + FastMath.PI;
                break;
            default:
                run2D = 0;
                break;
        }

        switch (run2D) {
            case 1:
                //Vector3f pos = cam.getLocation();
                Vector3f pos = Player.getPos();
                float[] rot = new float[3];
                cam.getRotation().normalizeLocal().toAngles(rot);

                float x = FastMath.sin(rot[1] + dir);
                float z = FastMath.cos(-rot[1] - dir);
                pos.x += x * 20 * tpf;
                pos.z += z * 20 * tpf;

                //cam.setLocation(pos);
                Player.setPos(pos);
                break;
            case 0:
                int move = 0;
                switch (name){
                    case "space":
                        move = 1;
                        break;
                    case "shift":
                        move = -1;
                        break;
                }

                if(move==0){
                    return;
                }

                //Vector3f pos2 = cam.getLocation();
                Vector3f pos2 = Player.getPos();
                pos2.y += move * 20 * tpf;
                Player.setPos(pos2);
                break;
        }
    }
}
