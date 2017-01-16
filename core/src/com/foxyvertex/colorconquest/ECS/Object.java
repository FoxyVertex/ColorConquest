package com.foxyvertex.colorconquest.ECS;


import com.badlogic.gdx.utils.Array;


/**
 * Created by aidan on 12/30/2016.
 */

public class Object {
    private Array<Component> enabledComponentsArray = new Array<Component>();
    private Array<Component> disabledComponentsArray = new Array<Component>();

    public Object add(Component component) {
        enabledComponentsArray.add(component);
        component.me = this;
        component.create();
        component.enable();
        return this;
    }

    public void removeAll() {
        while (enabledComponentsArray.size > 0) {
            remove(enabledComponentsArray.get(0).typeID);
        }
        while (disabledComponentsArray.size > 0) {
            remove(disabledComponentsArray.get(0).typeID);
        }
    }

    public Component remove(ComponentTypes typeID) {
        Array<Component> comps = new Array<Component>();
        comps.addAll(enabledComponentsArray);
        comps.addAll(disabledComponentsArray);
        for (int i = 0; i < comps.size; i++) {
            Component component = comps.get(i);
            if (component.typeID == typeID) {
                comps.removeIndex(i);
                component.obliterate();
                return component;
            }
        }
        return null;
    }

    public Array<Component> getComponents() {
        Array<Component> comps = new Array<Component>();
        comps.addAll(enabledComponentsArray);
        comps.addAll(disabledComponentsArray);
        return comps;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(ComponentTypes typeID) {
        Array<Component> comps = new Array<Component>();
        comps.addAll(enabledComponentsArray);
        comps.addAll(disabledComponentsArray);
        for (Component comp : comps) {
            if (comp.typeID == typeID) return (T) comp;
        }
        return null;
    }

    public void disableComponent(ComponentTypes typeID) {
        for (Component comp : enabledComponentsArray) {
            if (comp.typeID == typeID) {
                comp.disable();
            }
        }
    }

    public boolean hasComponent(ComponentTypes typeID) {
        Array<Component> comps = new Array<Component>();
        comps.addAll(enabledComponentsArray);
        comps.addAll(disabledComponentsArray);
        for (Component comp : comps) {
            if (comp.typeID == typeID) return true;
        }
        return false;
    }

    public void tick(float delta) {
        for (Component comp : enabledComponentsArray) {
            comp.tick(delta);
        }
    }

    public void obliterate() {
        removeAll();
    }
}
