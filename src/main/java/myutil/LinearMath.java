package myutil;

public class LinearMath {

    private LinearMath() {
    }

    public static double[] getCrossProduct(double[] A, double[] B) {
        double[] outVector = new double[3];

        outVector[0] = (A[1] * B[2]) - (A[2] * B[1]);
        outVector[1] = (A[2] * B[0]) - (A[0] * B[2]);
        outVector[2] = (A[0] * B[1]) - (A[1] * B[0]);

        return outVector;
    }

    public static double getVectorModule(double[] vec) {
        return Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]);
    }
}
