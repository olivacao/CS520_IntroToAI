public class Q1_4 {
    public static void main(String[] args) {
        Stationary stationary = new Stationary();

        int iteration = 100;
        int method = 2;

        double[] avgStepNaive = new double[2];
        double[] avgStep = new double[2];

        for (int i = 0; i < iteration; i++) {
            int[] stepsNaive = stationary.naiveSearchOrMotion(method);
            avgStepNaive[0] += stepsNaive[0];
            avgStepNaive[1] += stepsNaive[1];

            stationary.reset();

            int[] steps = stationary.SearchOrMotion(method);
            avgStep[0] += steps[0];
            avgStep[1] += steps[1];
            stationary.reset();
            stationary.moveTarget();
        }
        System.out.println("Naive searchOrMotion avg search: " + avgStepNaive[0] / iteration
                + " , avg motion:" + avgStepNaive[1] / iteration);
        System.out.println("searchOrMotion avg search: " + avgStep[0] / iteration
                + " , avg motion:" + avgStep[1] / iteration);
    }
}
