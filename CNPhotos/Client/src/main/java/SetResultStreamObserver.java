import io.grpc.stub.StreamObserver;
import monitorstubs.SetResult;

public class SetResultStreamObserver implements StreamObserver<SetResult> {
    @Override
    public void onNext(SetResult setResult) {
        boolean result = setResult.getSuccess();
        if(result){
            System.out.println("Success!!");
        }
        else{
            System.out.println("There was an error");
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
