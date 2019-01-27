package frc.team3256.warriorlib.control;

public interface ControllerObserver {
    void setListener(XboxListenerBase xboxListenerBase);
    void update();
}