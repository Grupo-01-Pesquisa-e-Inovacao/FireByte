public class LoadingAnimation {
    private String[] animationFrames = { "| Loading.", "/ Loading..", "— Loading...", "\\ Loading..." };
    private int framesPerSecond = 3;
    private long frameDelayMillis = 1000 / framesPerSecond;

    public void run() throws InterruptedException {
        int totalFrames = 20;

        for (int i = 0; i < totalFrames; i++) {
            System.out.print("\r" + animationFrames[i % animationFrames.length]);
            Thread.sleep(frameDelayMillis);
        }
        System.out.println("\rDone!");
    }
}