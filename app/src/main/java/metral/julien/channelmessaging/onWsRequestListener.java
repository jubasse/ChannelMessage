package metral.julien.channelmessaging;

/**
 * Created by Julien on 02/02/2016.
 */
public interface onWsRequestListener {
    public void onCompleted(String response);
    public void onError(String error);
}
