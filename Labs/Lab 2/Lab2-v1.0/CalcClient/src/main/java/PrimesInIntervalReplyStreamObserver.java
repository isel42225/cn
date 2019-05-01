import calcstubs.Prime;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class PrimesInIntervalReplyStreamObserver implements StreamObserver<Prime> {

    private final List<Prime> primes;
    private final int start;
    private final int end;

    public PrimesInIntervalReplyStreamObserver(int start, int end) {
        this.start = start;
        this.end = end;
        primes = new ArrayList<>();
    }

    @Override
    public void onNext(Prime value) {
        primes.add(value);
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println(
                String.format("Primes between %d - %d : %s",start,end, primes));
    }
}
