import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageVm {

    private static String PROJECT_ID = "g08-leic61d";
    private static String ZONE = "europe-west2-c";
    private static Compute computeService;

    static void deleteVm() throws IOException {
        Compute.Instances.Delete delete =
                computeService.instances().delete(PROJECT_ID, ZONE, "new-vm");
        delete.execute();

    }

    static void createInstance() throws IOException {
        // Create VM Instance object with the required properties.
        Instance instance = new Instance();
        instance.setName("new-vm");
        instance.setMachineType(
                "https://www.googleapis.com/compute/v1/projects/"
                        + PROJECT_ID + "/zones/"
                        + ZONE + "/machineTypes/"
                        + "f1-micro");
        // Add Network Interface to be used by VM Instance.
        instance.setNetworkInterfaces(
                Collections.singletonList(createNetworkInterface()));
// Add attached Persistent Disk to be used by VM Instance.
        instance.setDisks(Collections.singletonList(createDisk("new-vm")));
        Compute.Instances.Insert insert =
                computeService.instances().insert(PROJECT_ID, ZONE, instance);
        Operation op = insert.execute();
    }
    static NetworkInterface createNetworkInterface() {
        NetworkInterface ifc = new NetworkInterface();
        ifc.setNetwork(
                "https://www.googleapis.com/compute/v1/projects/"
                        + PROJECT_ID
                        + "/global/networks/default");
        List<AccessConfig> configs = new ArrayList<>();
        AccessConfig config = new AccessConfig();
        config.setType("ONE_TO_ONE_NAT");
        config.setName("External NAT");
        configs.add(config);
        ifc.setAccessConfigs(configs);
        return ifc;
    }
    static AttachedDisk createDisk(String instanceName) {
        AttachedDisk disk = new AttachedDisk();
        disk.setBoot(true);
        disk.setAutoDelete(true);
        disk.setType("PERSISTENT");
        AttachedDiskInitializeParams params
                = new AttachedDiskInitializeParams();
        // Assign the Persistent Disk the same name as the VM Instance.
        params.setDiskName(instanceName);

        params.setSourceImage("global/images/" + "image-with-forum-stress");
        // Specify the disk type as Standard Persistent Disk
        params.setDiskType("https://www.googleapis.com/compute/v1/projects/"
                + PROJECT_ID + "/zones/"
                + ZONE + "/diskTypes/pd-standard");
        disk.setInitializeParams(params);
        return disk;
    }
}
