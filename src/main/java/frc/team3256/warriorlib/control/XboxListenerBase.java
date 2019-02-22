package frc.team3256.warriorlib.control;

import edu.wpi.first.wpilibj.XboxController;

public abstract class XboxListenerBase {
    private XboxControllerObserver xboxControllerObserver;

    public void setController(XboxControllerObserver xboxControllerObserver) {
        this.xboxControllerObserver = xboxControllerObserver;
    }

    public XboxControllerObserver getController() {
        return xboxControllerObserver;
    }

    public abstract void onAPressed();
    public abstract void onBPressed();
    public abstract void onXPressed();
    public abstract void onYPressed();

    public abstract void onAReleased();
    public abstract void onBReleased();
    public abstract void onXReleased();
    public abstract void onYReleased();

    public abstract void onLeftDpadPressed();
    public abstract void onRightDpadPressed();
    public abstract void onUpDpadPressed();
    public abstract void onDownDpadPressed();

    public abstract void onLeftDpadReleased();
    public abstract void onRightDpadReleased();
    public abstract void onUpDpadReleased();
    public abstract void onDownDpadReleased();

    public abstract void onSelectedPressed();
    public abstract void onStartPressed();

    public abstract void onSelectedReleased();
    public abstract void onStartReleased();

    public abstract void onLeftShoulderPressed();
    public abstract void onRightShoulderPressed();

    public abstract void onLeftShoulderReleased();
    public abstract void onRightShoulderReleased();

    public abstract void onLeftTrigger(double value);
    public abstract void onRightTrigger(double value);

    public abstract void onLeftJoystick(double x, double y);
    public abstract void onRightJoystick(double x, double y);

    public abstract void onLeftJoystickPressed();
    public abstract void onRightJoystickPressed();

    public abstract void onLeftJoystickReleased();
    public abstract void onRightJoystickReleased();
}

