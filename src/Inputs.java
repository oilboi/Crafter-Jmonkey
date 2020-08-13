import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;

public class Inputs {

    public static void initializeKeys(InputManager inputManager, ActionListener actionListener, AnalogListener analogListener) {
        // You can map one or several inputs to one named action
        inputManager.addMapping("w", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("a", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("s", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("d", new KeyTrigger(KeyInput.KEY_D));
        // Add the names to the action listener.
        //inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(analogListener, "w", "a", "s", "d");
    }
}
