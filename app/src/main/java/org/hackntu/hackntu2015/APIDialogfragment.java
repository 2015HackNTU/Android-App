package org.hackntu.hackntu2015;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

public class APIDialogfragment extends DialogFragment
{
    String[] api_award={"Apple","Google","Microsoft","Tutor ABC","Alibaba","Intel","ASUS"};
    TextView apiname;
    int mNum;
    static APIDialogfragment newInstance(int num) {
        APIDialogfragment f = new APIDialogfragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mNum = getArguments().getInt("num");
//        apiname.setText(api_award[mNum]);

        View contentView = getActivity().getLayoutInflater().inflate(R.layout
                .fragment_apidialog, null);

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.apple)
                .setTitle(api_award[mNum])
                .setView(contentView)
//                .setPositiveButton(R.string.alert_dialog_ok,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                ((FragmentAlertDialog)getActivity()).doPositiveClick();
//                            }
//                        }
//                )
//                .setNegativeButton(R.string.alert_dialog_cancel,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                ((FragmentAlertDialog)getActivity()).doNegativeClick();
//                            }
//                        }
//                )
                  .setNeutralButton("close", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dismiss();
                      }
                  })
                .create();
    }






}
