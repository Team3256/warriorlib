package frc.team3256.warriorlib.control;

public class DrivePower {
	private double leftX;
	private double leftY;
	private double rightX;
	private double rightY;
	private boolean highGear;

	public DrivePower(double leftX, double leftY, double rightX, double rightY, boolean highGear) {
		this.leftX = leftX;
		this.leftY = leftY;
		this.rightX = rightX;
		this.rightY = rightY;
		this.highGear = highGear;
	}

	public DrivePower(double leftY, double rightX, boolean highGear) {
		this(0.0, leftY, rightX, 0.0, highGear);
	}

	public DrivePower(double leftY, double rightX) {
		this(0.0, leftY, rightX, 0.0, true);
	}

	public double getLeftX() {
		return leftX;
	}

	public double getLeftY() {
		return leftY;
	}

	public double getRightX() {
		return rightX;
	}

	public double getRightY() {
		return rightY;
	}

	public boolean getHighGear() {
		return highGear;
	}

}
