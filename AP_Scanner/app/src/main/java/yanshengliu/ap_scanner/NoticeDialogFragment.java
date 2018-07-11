package yanshengliu.ap_scanner;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

/**
 * Created by ylxh5 on 8/9/2017.
 */

public class NoticeDialogFragment extends DialogFragment{

    public interface NoticeDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener =(NoticeDialogListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" must implement NoticeDialogListener.");
        }
    }

}
