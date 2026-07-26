class Foo {

    private boolean firstDone = false;
    private boolean secondDone = false;

    public Foo() {

    }

    public synchronized void first(Runnable printFirst) {
        printFirst.run();
        firstDone = true;
        notifyAll();
    }

    public synchronized void second(Runnable printSecond) throws InterruptedException {
        while (!firstDone) {
            wait();
        }

        printSecond.run();   
        secondDone = true;
        notifyAll();
    }

    public synchronized void third(Runnable printThird) throws InterruptedException {
        while (!secondDone) {
            wait();
        }

        printThird.run();
    }
}


