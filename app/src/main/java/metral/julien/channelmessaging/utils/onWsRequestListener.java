package metral.julien.channelmessaging.Utils;

public interface onWsRequestListener {
    void onCompleted(String response);
    void onError(String error);
}
