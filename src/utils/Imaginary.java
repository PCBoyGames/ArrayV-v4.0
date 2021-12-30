package utils;
import java.lang.Object;
import java.util.Objects;


public final class Imaginary extends Object {
	public double value;
	public Imaginary() {
		this.value = 0d;
	}
	public Imaginary(double value) {
		this.value = value;
	}
	public Imaginary plus(Imaginary that) {
		return new Imaginary(this.value+that.value);
	}
	public Complex plus(double num) {
		return new Complex(num, this);
	}
	public Imaginary minus(Imaginary that) {
		return new Imaginary(this.value-that.value);
	}
	public Complex minus(double num) {
		return new Complex(-num, this);
	}
	public Complex pow(double num) {
		return new Complex(0, this).pow(num);
	}
	public double times(Imaginary that) {
		return -(this.value * that.value);
	}
	public Imaginary times(double val) {
		return new Imaginary(this.value * val);
	}
	public double div(Imaginary that) {
		return (this.value / that.value);
	}
	public Imaginary divReal(double number) {
		return new Imaginary(-(number / this.value));
	}
	public Imaginary div(double val) {
		return new Imaginary(this.value / val);
	}
	public Imaginary unaryMinus() {
		return new Imaginary(-this.value);
	}
	public String toString() {
		return this.value + "i";
	}
	public int compareTo(Imaginary that) {
		double compare = this.value - that.value;
		return compare < 0 ? -1 : compare == 0 ? 0 : 1;
	}
	public int compareTo(double imaginaryReal) {
		double compare = this.value - imaginaryReal;
		return compare < 0 ? -1 : compare == 0 ? 0 : 1;
	}
	public int hashCode() {
		return Objects.hash(this.value);
	}
	/*public boolean compareToUsing(Imaginary that, ComparableUsing.Operator TYPE) {
		switch(TYPE) {
			case GT:
				return this.value > that.value;
			case GE:
				return this.value >= that.value;
			case LT:
				return this.value < that.value;
			case LE:
				return this.value <= that.value;
			case EQ:
				return this.value == that.value;
			case NE:
			default:
				return this.value != that.value;
		}
	}*/
	
	public boolean equals(Imaginary that) {
		return this.value == that.value;
	}
}
