public class MonitorState {
    private int imgThreshold = 3;
    private int nOfVms = 1;
    private int currImgLoad = 0;

    public int getImgThreshold() {
        return imgThreshold;
    }

    public void setImgThreshold(int imgThreshold) {
        this.imgThreshold = imgThreshold;
    }

    public int getnOfVms() {
        return nOfVms;
    }

    public void setnOfVms(int nOfVms) {
        this.nOfVms = nOfVms;
    }

    public int getCurrImgLoad() {
        return currImgLoad;
    }

    public void setCurrImgLoad(int currImgLoad) {
        this.currImgLoad = currImgLoad;
    }
}
