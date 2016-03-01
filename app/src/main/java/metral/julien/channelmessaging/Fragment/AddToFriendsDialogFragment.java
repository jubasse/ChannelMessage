package metral.julien.channelmessaging.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.R;

/**
 * Created by Julien on 01/03/2016.
 */
public class AddToFriendsDialogFragment extends DialogFragment {

    private User friendToAdd;
    private Context context;
    private User self;

    public void setFriendToAdd(User friendToAdd) {
        this.friendToAdd = friendToAdd;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setSelf(User self) {
        this.self = self;
    }

    public AddToFriendsDialogFragment()
    {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ajouter un ami")
                .setMessage("Voulez vous vraiment ajouter cet utilisateur à vos amis ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}