package utils;
import java.lang.Comparable;
import java.lang.Object;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.Optional;

// not complete, but complete enough for MandelbrotSort
public final class Complex extends Object
implements Comparable<Complex> {
    //private static final long serialVersionUID = 5600679651837541971L;
    public double real;
    public Imaginary imaginary;


    public static final Class<Complex>  TYPE = (Class<Complex>) Complex.class;

    public Complex() {
        this.real = 0d;
        this.imaginary = new Imaginary(0);
    }
    public Complex(double real) {
        this.real = real;
        this.imaginary = new Imaginary(0);
    }
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = new Imaginary(imaginary);
    }
    public Complex(double real, Imaginary imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static Complex valueOf(double real) {
        return new Complex(real, 0);
    }

    public static Complex valueOf(double real, double imaginary) {
        return new Complex(real, imaginary);
    }

    public Complex neg() {
        return new Complex(-this.real, this.imaginary.unaryMinus());
    }

    public int sign() {
        return this.div(this.abs()).compareTo(0);
    }

    public Complex sqrt() {
        double Hyp = Math.hypot(this.real, this.imaginary.value);
        return new Complex(
            Math.sqrt((Hyp + this.real) / 2),
            Math.sqrt((Hyp - this.real) / 2)
        );
    }

    public double abs() {
        return Math.hypot(this.real, this.imaginary.value);
    }

    public Complex log() {
        return new Complex(Math.log(this.abs()), this.arg());
    }

    /**
     * Helped me understand the basics of power.
     * @return {@code e}<sup>{@code <this>}</sup>.
     */
    public Complex exp() {
        double eX = Math.exp(this.real);
        return new Complex(eX*Math.sin(this.imaginary.value), eX*Math.cos(this.imaginary.value));
    }

    /**
     * Initial plan was approximating it with weird sqrt shenanigans.
     * Glad I took the polar route, as it actually works.
     * @param Exponent
     * @return {@code <this>}<sup>pow</sup>
     */
    public Complex pow(double pow) {
        double theta = Math.atan(this.imaginary.value / this.real), // Reverse calculate the theta
               r = this.abs(); // Get r^2 = a^2+(real)b^2
        if (Double.isNaN(theta) || Double.isNaN(r) || Double.isNaN(this.real) || Double.isNaN(this.imaginary.value)) {
            return new Complex(0, 0);
        }
        return new Complex(Math.cos(pow * theta), Math.sin(pow * theta)).mult(Math.pow(r, pow)); // Need an epsilon for this.
    }

    public double arg() {
        return Math.atan2(this.imaginary.value, this.real);
    }

    public Complex add(Complex complex) {
        return new Complex(this.real+complex.real, this.imaginary.plus(complex.imaginary));
    }
    public Complex add(double val) {
        return new Complex(this.real+val, this.imaginary);
    }
    public Complex add(Imaginary imag) {
        return new Complex(this.real, this.imaginary.plus(imag));
    }

    public Complex sub(Complex complex) {
        return new Complex(this.real-complex.real, this.imaginary.minus(complex.imaginary));
    }
    public Complex sub(double val) {
        return new Complex(this.real-val, this.imaginary);
    }
    public Complex sub(Imaginary imag) {
        return new Complex(this.real, this.imaginary.minus(imag));
    }

    public Complex mult(double num) {
        return new Complex(
            (this.real * num),
            (this.imaginary.times(num))
        );
    }
    public Complex mult(Complex complex) {
        return new Complex(
            (this.real * complex.real) -
            (this.imaginary.value * complex.imaginary.value),
            (this.real * this.imaginary.value) +
            (complex.real * complex.imaginary.value)
        );
    }
    public Complex mult(Imaginary imaginary) {
        return this.mult(new Complex(0, imaginary));
    }

    /**
     * An interface for dividing {@code <this>} by another complex
     * number, through the conjugate method.
     * @param The complex quotient
     * @return {@code <this> / <complex>}
     */
    public Complex div(Complex complex) {
        double conjugate = ((complex.real * complex.real) - (complex.imaginary.value * complex.imaginary.value));
        return new Complex(
            (this.real * complex.real - (this.imaginary.value * complex.imaginary.value)) / conjugate,
            (this.imaginary.value * complex.real - (this.real * complex.imaginary.value)) / conjugate
        );
    }
    /**
     * An interface for dividing {@code <this>} by an imaginary.
     * @param The imaginary quotient
     * @return {@code <this> / <real>}
     */
    public Complex div(Imaginary imaginary) {
        return this.div(new Complex(0, imaginary));
    }
    /**
     * A method for dividing a {@code <this>} by a real.
     * @param The quotient
     * @return {@code <this> / <real>}
     */
    public Complex div(double val) {
        return new Complex(
            (this.real / val),
            (this.imaginary.div(val))
        );
    }
    /**
     * An interface for dividing a real by {@code <this>}.
     * @param The number to divide by {@code <this>}
     * @return {@code <real> / <this>}
     */
    public Complex divReal(double val) {
        return new Complex(val, 0).div(this);
    }

    public long longValue() {
        return (long) this.real;
    }

    public long longValueExact() {
        if (this.real > 0x7FFFFFFFFFFFFFFFL || this.real < 0x8000000000000000L) {
            throw new ArithmeticException("Out of range for exact values");
        }
        return (long) this.real;
    }

    public int intValue() {
        return (int) this.real;
    }

    public float floatValue() {
        return (float) this.real;
    }

    public float floatValueExact() {
        if (this.real > Float.MAX_VALUE || this.real < Float.MIN_VALUE) {
            throw new ArithmeticException("Out of range for exact values");
        }
        return (float) this.real;
    }

    public double doubleValue() {
        return this.real;
    }

    public double doubleValueExact() {
        return this.real;
    }

    public int intValueExact() {
        if (this.real > 0x7FFFFFFF || this.real < 0x80000000) {
            throw new ArithmeticException("Out of range for exact values");
        }
        return (int) this.real;
    }

    public int compareTo(double val) {
        int imagComp = this.imaginary.compareTo(0);
        int compare = Double.compare(this.real, val);
        if (compare == 0) {
            return imagComp;
        }
        return compare;
    }
    public int compareTo(Complex complex) {
        int imagComp = this.imaginary.compareTo(complex.imaginary);
        int compare = Double.compare(this.real, complex.real);
        if (compare == 0) {
            return imagComp;
        }
        return compare;
    }
    public boolean equals(double number) {
        return this.real == number && this.imaginary.value == 0;
    }
    public boolean equals(Complex complex) {
        return this.real == complex.real && this.imaginary.value == complex.imaginary.value;
    }
    public boolean equals(Imaginary imaginary) {
        return this.real == 0 && this.imaginary.value == imaginary.value;
    }
    public boolean equals(Object object) {
        return Objects.equals(this, object);
    }
    public int compareTo(Imaginary imaginary) {
        int imagComp = this.imaginary.compareTo(imaginary);
        int compare = Double.compare(this.real, 0);
        if (compare == 0) {
            return imagComp;
        }
        return compare;
    }

    public int hashCode() {
        return Objects.hash(this.real, this.imaginary);
    }
    public String toString() {
        return this.real + " + " + this.imaginary;
    }


    public Optional<Complex> describeConstable() {
        return Optional.of(this);
    }

    public Complex resolveConstantDesc(MethodHandles.Lookup lookup) {
        return this;
    }
}
