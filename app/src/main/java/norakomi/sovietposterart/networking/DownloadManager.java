package norakomi.sovietposterart.networking;

/**
 * Created by MEDION on 15-10-2015.
 */
public class DownloadManager {

    private static DownloadManager mInstance = null;

    private DownloadManager() {
    }

    public static DownloadManager getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadManager();
        }
        return mInstance;
    }

    public void getPosterData(String url){

    }
}
