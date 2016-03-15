package metral.julien.channelmessaging.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class AddToFriendsDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener listener;
    private Context context;

    public AddToFriendsDialogFragment setContext(Context context) {
        this.context = context;
        return this;
    }

    public AddToFriendsDialogFragment setListener(DialogInterface.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public AddToFriendsDialogFragment()
    {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Ajouter un ami / géoloc. message")
                .setItems(new String[]{
                        "Ajouter en ami",
                        "Géoloc. message",
                        "Fermer"
                }, listener)
                .setIcon(android.R.drawable.ic_dialog_dialer);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}