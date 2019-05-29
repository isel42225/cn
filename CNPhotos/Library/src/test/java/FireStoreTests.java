import firestore.FireStoreService;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FireStoreTests {

    private final FireStoreService firestore = new FireStoreService();
    private final String label = "tlabel";

    @Test
    public void shouldAddTestLabelWithSingletonCollectionField() {
        String testVal = "xpto";
        firestore.addData(testVal,label);
    }

    @Test
    public void shouldUpdateTestLabel() {
        String newVal = "CN";
        firestore.addData(newVal, label);
    }

    @Test
    public void shouldReturnCollectionWithTwoStrings() {
        List<String> ret = firestore.searchImage(label);
        assert(ret.size() == 2);
    }
}
