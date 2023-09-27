class LoadingAnimation {
    String[] animationFrames = { "| Loading.", "/ Loading..", "— Loading...", "\\ Loading..." };
    Integer framesPerSecond = 3;
    Integer frameDelayMillis = 1000 / framesPerSecond;

    public void run(long durationMillis) throws InterruptedException {
        Integer totalFrames = (int) (durationMillis / frameDelayMillis);

        for (int i = 0; i < totalFrames; i++) {
            System.out.print("\r" + animationFrames[i % animationFrames.length]);
            Thread.sleep(frameDelayMillis);
        }
        System.out.println("\rDone!");
    }
}
