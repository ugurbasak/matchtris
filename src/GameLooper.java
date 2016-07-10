public class GameLooper implements Runnable {

    private MatchTris myMidlet;
    private Game myGame;
    private Thread myThread;
    public boolean isRunnning = false;

    public GameLooper(MatchTris myMidlet, Game myGame) {
        this.myGame = myGame;
        this.myMidlet = myMidlet;
        isRunnning = true;
        myThread = new Thread(this);
        myThread.start();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(20);
                if (isRunnning)
                    myGame.repaint(0, 0, 128, 128);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
