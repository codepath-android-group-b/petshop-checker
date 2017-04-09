package lin.leila.petshopinspector.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.keep.Keep;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;

/**
 * Created by javiosyc on 2017/4/9.
 */

public class CustomerPickImageDialog extends CustomerPickImageBaseDialog {
    public CustomerPickImageDialog() {
    }

    public static CustomerPickImageDialog newInstance(PickSetup setup) {
        CustomerPickImageDialog frag = new CustomerPickImageDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("SETUP_TAG", setup);
        frag.setArguments(bundle);
        return frag;
    }

    public static CustomerPickImageDialog build(PickSetup setup, IPickResult pickResult) {
        CustomerPickImageDialog d = newInstance(setup);
        d.setOnPickResult(pickResult);
        return d;
    }

    public static CustomerPickImageDialog build(IPickResult pickResult) {
        return build(new PickSetup(), pickResult);
    }

    public static CustomerPickImageDialog build(PickSetup setup) {
        return build(setup, (IPickResult) null);
    }

    public static CustomerPickImageDialog build() {
        return build();
    }

    public CustomerPickImageDialog show(FragmentActivity fragmentActivity) {
        return this.show(fragmentActivity.getSupportFragmentManager());
    }

    public CustomerPickImageDialog show(FragmentManager fragmentManager) {
        super.show(fragmentManager, DIALOG_FRAGMENT_TAG);
        return this;
    }

    public void onCameraClick() {
        this.launchCamera();
    }

    public void onGalleryClick() {
        this.launchGallery();
    }

    public CustomerPickImageDialog setOnClick(IPickClick onClick) {
        return (CustomerPickImageDialog) super.setOnClick(onClick);
    }

    public CustomerPickImageDialog setOnPickResult(IPickResult onPickResult) {
        return (CustomerPickImageDialog) super.setOnPickResult(onPickResult);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {
            if (resultCode == -1) {
                this.showProgress(true);
                this.getAsyncResult().execute(new Intent[]{data});
            } else {
                this.dismissAllowingStateLoss();
            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 99) {
            boolean granted = true;
            int[] var5 = grantResults;
            int var6 = grantResults.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                Integer i = Integer.valueOf(var5[var7]);
                granted = granted && i.intValue() == 0;
            }

            if (granted) {
                if (!this.launchSystemDialog()) {
                    this.launchCamera();
                }
            } else {
                this.dismissAllowingStateLoss();
            }

            Keep.with(this.getActivity()).askedForPermission();
        }

    }

}
