public class Q2_2 {
    public static void main(String[] args) {
        Moving moving = new Moving();

        int iteration = 1000;
        int method = 1;

        double[] avgStepNaive = new double[2];
        double[] avgStep = new double[2];

        for (int i = 0; i < iteration; i++) {
            int[] stepsNaive = moving.naiveSearchOrMotion(method);
            avgStepNaive[0] += stepsNaive[0];
            avgStepNaive[1] += stepsNaive[1];

            moving.reset();

            int[] steps = moving.SearchOrMotion(method);
            avgStep[0] += steps[0];
            avgStep[1] += steps[1];
            moving.reset();
        }
        System.out.println("Naive searchOrMotion avg search: " + avgStepNaive[0] / iteration
                + " , avg motion:" + avgStepNaive[1] / iteration);
        System.out.println("searchOrMotion avg search: " + avgStep[0] / iteration
                + " , avg motion:" + avgStep[1] / iteration);
    }
}
