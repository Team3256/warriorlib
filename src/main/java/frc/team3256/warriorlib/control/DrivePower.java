package frc.team3256.warriorlib.control;

public class DrivePower {
	private double left;
	private double right;
	private boolean highGear;

	public DrivePower(double left, double right, boolean highGear) {
		this.left = left;
		this.right = right;
		this.highGear = highGear;
	}

	public DrivePower(double left, double right) {
		this(left, right, true);
	}

	public double getLeft() {
		return left;
	}

	public double getRight() {
		return right;
	}

	public boolean getHighGear() {
		return highGear;
	}

}
