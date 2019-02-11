package frc.team3256.warriorlib.control;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

public class XboxControllerObserver implements ControllerObserver {
    private XboxListenerBase xboxListenerBase;
    private XboxController xboxController;

    private int previousPOV;

    public XboxControllerObserver(int port) {
        this.xboxController = new edu.wpi.first.wpilibj.XboxController(port);
    }

    public XboxControllerObserver(int port, XboxListenerBase xboxListenerBase) {
        this.xboxListenerBase = xboxListenerBase;
        this.xboxController = new edu.wpi.first.wpilibj.XboxController(port);
        previousPOV = 0;
    }

    public void setListener(XboxListenerBase listener) {
        xboxListenerBase = listener;
    }

    public void setRumble(double strength) {
        xboxController.setRumble(GenericHID.RumbleType.kLeftRumble, strength);
        xboxController.setRumble(GenericHID.RumbleType.kRightRumble, strength);
    }

    public void update() {
        if (xboxController == null) {
            System.out.println("No controller listener set, add one using .setListener()");
            return;
        }
        // A
        if (xboxController.getAButtonPressed()) {
            xboxListenerBase.onAPressed();
        }
        if (xboxController.getAButtonReleased()) {
            xboxListenerBase.onAReleased();
        }
        // B
        if (xboxController.getBButtonPressed()) {
            xboxListenerBase.onBPressed();
        }
        if (xboxController.getBButtonReleased()) {
            xboxListenerBase.onBReleased();
        }
        // X
        if (xboxController.getXButtonPressed()) {
            xboxListenerBase.onXPressed();
        }
        if (xboxController.getXButtonReleased()) {
            xboxListenerBase.onXReleased();
        }
        // Y
        if (xboxController.getYButtonPressed()) {
            xboxListenerBase.onYPressed();
        }
        if (xboxController.getXButtonReleased()) {
            xboxListenerBase.onYReleased();
        }
        // Select
        if (xboxController.getBackButtonPressed()) {
            xboxListenerBase.onSelectedPressed();
        }
        if (xboxController.getBackButtonReleased()) {
            xboxListenerBase.onSelectedReleased();
        }
        // Start
        if (xboxController.getStartButtonPressed()) {
            xboxListenerBase.onStartPressed();
        }
        if (xboxController.getStartButtonReleased()) {
            xboxListenerBase.onStartReleased();
        }
        // Left Shoulder
        if (xboxController.getBumperPressed(GenericHID.Hand.kLeft)) {
            xboxListenerBase.onLeftShoulderPressed();
        }
        if (xboxController.getBumperPressed(GenericHID.Hand.kLeft)) {
            xboxListenerBase.onLeftShoulderReleased();
        }
        // Right Shoulder
        if (xboxController.getBumperPressed(GenericHID.Hand.kRight)) {
            xboxListenerBase.onRightShoulderPressed();
        }
        if (xboxController.getBumperPressed(GenericHID.Hand.kRight)) {
            xboxListenerBase.onRightShoulderReleased();
        }
        // Left Joystick
        if (xboxController.getStickButtonPressed(GenericHID.Hand.kLeft)) {
            xboxListenerBase.onLeftJoystickPressed();
        }
        if (xboxController.getStickButtonReleased(GenericHID.Hand.kLeft)) {
            xboxListenerBase.onLeftJoystickReleased();
        }
        xboxListenerBase.onLeftJoystick(xboxController.getX(GenericHID.Hand.kLeft), -xboxController.getY(GenericHID.Hand.kLeft));
        // Right Joystick
        if (xboxController.getStickButtonPressed(GenericHID.Hand.kRight)) {
            xboxListenerBase.onRightJoystickPressed();
        }
        if (xboxController.getStickButtonReleased(GenericHID.Hand.kRight)) {
            xboxListenerBase.onRightJoystickReleased();
        }
        xboxListenerBase.onRightJoyStick(xboxController.getX(GenericHID.Hand.kRight), -xboxController.getY(GenericHID.Hand.kRight));
        // Left Trigger
        xboxListenerBase.onLeftTrigger(xboxController.getTriggerAxis(GenericHID.Hand.kLeft));
        // Right Trigger
        xboxListenerBase.onRightTrigger(xboxController.getTriggerAxis(GenericHID.Hand.kRight));

        int pov = xboxController.getPOV();
        // Call released function if no button on the dpad is pressed
        if (pov == -1) {
            switch (previousPOV) {
                case 0:
                    xboxListenerBase.onUpDpadReleased();
                    break;
                case 180:
                    xboxListenerBase.onDownDpadReleased();
                    break;
                case 270:
                    xboxListenerBase.onLeftDpadReleased();
                    break;
                case 90:
                    xboxListenerBase.onRightDpadReleased();
                    break;
            }
            previousPOV = -1;
            return;
        }
        switch (pov) {
            case 0:
                xboxListenerBase.onUpDpadPressed();
                break;
            case 180:
                xboxListenerBase.onDownDpadPressed();
                break;
            case 270:
                xboxListenerBase.onLeftDpadPressed();
                break;
            case 90:
                xboxListenerBase.onRightDpadPressed();
                break;
        }
        previousPOV = pov;
    }
}
