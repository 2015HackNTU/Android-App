package org.hackntu.hackntu2015;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class APIDialogfragment extends DialogFragment {
    public static final String TAG = "APIDialogFragment";
    public static final String AWARD = "award";
    public static final String IMAGE = "image";

    ApiAward award;
    AlertDialog dialog;
    Bitmap image;


    static APIDialogfragment newInstance(ApiAward award, Bitmap image) {
        APIDialogfragment f = new APIDialogfragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(AWARD, award);
        args.putParcelable(IMAGE, image);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        award = args.getParcelable(AWARD);
        image = args.getParcelable(IMAGE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView = getActivity().getLayoutInflater().inflate(R.layout
                .fragment_apidialog, null);
        TextView prize = (TextView) contentView.findViewById(R.id.api_reward);
        TextView criteria = (TextView) contentView.findViewById(R.id.api_criteria);

        prize.setText(award.prize);
        criteria.setText(award.criteria);
        dialog = new AlertDialog.Builder(getActivity())
                .setIcon(new BitmapDrawable(getResources(), image))
                .setTitle(award.companyName)
                .setView(contentView)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
        Log.i(TAG, "loading image:" + award.imageUrl);
        return dialog;
    }
}
