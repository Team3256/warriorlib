package frc.team3256.warriorlib.math;

public class Vector {
	public double x, y, z;
	private double velocity;
	private double curvature;
	private double distance;

	public Vector() {

	}

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		velocity = 0;
		curvature = 0;
		distance = 0;
	}

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0;
		velocity = 0;
		curvature = 0;
		distance = 0;
	}

	public Vector(Vector v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.velocity = v.velocity;
		this.curvature = v.curvature;
		this.distance = v.distance;
	}

	public static Vector add(Vector a, Vector b, Vector target) {
		if (target == null) {
			target = new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
		} else {
			target.set(a.x + b.x, a.y + b.y, a.z + b.z);
		}
		return target;
	}

	public static Vector add(Vector a, Vector b) {
		return add(a, b, null);
	}

	public static Vector sub(Vector a, Vector b, Vector target) {
		if (target == null) {
			target = new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
		} else {
			target.set(a.x - b.x, a.y - b.y, a.z - b.z);
		}
		return target;
	}

	public static Vector sub(Vector a, Vector b) {
		return sub(a, b, null);
	}

	public static Vector mult(Vector a, double n, Vector target) {
		if (target == null) {
			target = new Vector(a.x * n, a.y * n, a.z * n);
		} else {
			target.set(a.x * n, a.y * n, a.z * n);
		}
		return target;
	}

	public static Vector mult(Vector a, double n) {
		return mult(a, n, null);
	}

	public static Vector mult(Vector a, Vector b, Vector target) {
		if (target == null) {
			target = new Vector(a.x * b.x, a.y * b.y, a.z * b.z);
		} else {
			target.set(a.x * b.x, a.y * b.y, a.z * b.z);
		}
		return target;
	}

	public static Vector mult(Vector a, Vector b) {
		return mult(a, b, null);
	}

	public static Vector div(Vector a, float n, Vector target) {
		if (target == null) {
			target = new Vector(a.x / n, a.y / n, a.z / n);
		} else {
			target.set(a.x / n, a.y / n, a.z / n);
		}
		return target;
	}

	public static Vector div(Vector a, float n) {
		return div(a, n, null);
	}

	public static Vector div(Vector a, Vector b, Vector target) {
		if (target == null) {
			target = new Vector(a.x / b.x, a.y / b.y, a.z / b.z);
		} else {
			target.set(a.x / b.x, a.y / b.y, a.z / b.z);
		}
		return target;
	}

	static public double dist(Vector a, Vector b) {
		double dx = a.x - a.x;
		double dy = a.y - b.y;
		double dz = a.z - b.z;
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
	}

	static public double dot(Vector a, Vector b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	static public double angleBetween(Vector a, Vector b) {
		double dot = dot(a, b);
		double amag = Math.sqrt(Math.pow(a.x, 2) + Math.pow(a.y, 2) + Math.pow(a.z, 2));
		double bmag = Math.sqrt(Math.pow(b.x, 2) + Math.pow(b.y, 2) + Math.pow(b.z, 2));
		return Math.acos(dot / (amag * bmag));
	}

	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getCurvature() {
		return curvature;
	}

	public void setCurvature(double curvature) {
		this.curvature = curvature;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Vector duplicate() {
		return new Vector(x, y, z);
	}

	public double norm() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	public void add(Vector v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void sub(Vector a) {
		x -= a.x;
		y -= a.y;
		z -= a.z;
	}

	public void sub(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	public void mult(double n) {
		x *= n;
		y *= n;
		z *= n;
	}

	public void mult(Vector a) {
		x *= a.x;
		y *= a.y;
		z *= a.z;
	}

	public void div(double n) {
		x /= n;
		y /= n;
		z /= n;
	}

	public void div(Vector a) {
		x /= a.x;
		y /= a.y;
		z /= a.z;
	}

	public Vector div(Vector a, Vector b) {
		return div(a, b, null);
	}

	public double dist(Vector v) {
		double dx = x - v.x;
		double dy = y - v.y;
		double dz = z - v.z;
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
	}

	public double dot(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public double dot(double x, double y, double z) {
		return this.x * x + this.y * y + this.z * z;
	}

	public void normalize() {
		double m = norm();
		if (m != 0 && m != 1) {
			div(m);
		}
	}

	public Vector normalize(Vector target) {
		if (target == null) {
			target = new Vector();
		}
		double m = norm();
		if (m > 0) {
			target.set(x / m, y / m, z / m);
		} else {
			target.set(x, y, z);
		}
		return target;
	}

	public void limit(float max) {
		if (norm() > max) {
			normalize();
			mult(max);
		}
	}

	@Override
	public String toString() {
		return "x: " + x + ", y: " + y;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Vector))
			return false;
		Vector vector = (Vector) object;
		return vector.x == x && vector.y == y && vector.z == z && vector.curvature == curvature && vector.velocity == velocity && vector.distance == distance;
	}
}
