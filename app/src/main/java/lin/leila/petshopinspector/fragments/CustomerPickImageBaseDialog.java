//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package lin.leila.petshopinspector.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vansuita.pickimage.R.id;
import com.vansuita.pickimage.R.layout;
import com.vansuita.pickimage.R.string;
import com.vansuita.pickimage.async.AsyncImageResult;
import com.vansuita.pickimage.async.AsyncImageResult.OnFinish;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageBaseDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;
import com.vansuita.pickimage.resolver.IntentResolver;
import com.vansuita.pickimage.util.Util;

public abstract class CustomerPickImageBaseDialog extends DialogFragment implements IPickClick {
    protected static final String SETUP_TAG = "SETUP_TAG";
    public static final String DIALOG_FRAGMENT_TAG = PickImageBaseDialog.class.getSimpleName();
    private PickSetup setup;
    private IntentResolver resolver;
    private boolean showCamera = true;
    private boolean showGallery = true;
    private CardView card;
    private LinearLayout llButtons;
    private TextView tvTitle;
    private TextView tvCamera;
    private TextView tvGallery;
    private TextView tvCancel;
    private TextView tvProgress;
    private View vFirstLayer;
    private View vSecondLayer;
    private Boolean validProviders = null;
    private IPickResult onPickResult;
    private IPickClick onClick;
    private OnCancelClickListener cancelClick;

    public void setOnCancelClickListener( OnCancelClickListener cancelClick) {
        this.cancelClick = cancelClick;
    }

    public interface OnCancelClickListener {
        void onCancelClick();
    }

    private OnClickListener listener = new OnClickListener() {
        public void onClick(View view) {
            if (view.getId() == id.cancel) {
                CustomerPickImageBaseDialog.this.cancelClick.onCancelClick();
            } else if (view.getId() == id.camera) {
                CustomerPickImageBaseDialog.this.onClick.onCameraClick();
            } else if (view.getId() == id.gallery) {
                CustomerPickImageBaseDialog.this.onClick.onGalleryClick();
            }
        }
    };

    public CustomerPickImageBaseDialog() {
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout.dialog, (ViewGroup) null, false);
        this.onAttaching();
        this.onInitialize();
        if (this.isValidProviders()) {
            this.onBindViewsHolders(view);
            if (!this.launchSystemDialog()) {
                this.onBindViews(view);
                this.onBindViewListeners();
                this.onSetup();
            }

            return view;
        } else {
            return this.delayedDismiss();
        }
    }

    private void onAttaching() {
        if (this.onClick == null) {
            if (this.getActivity() instanceof IPickClick) {
                this.onClick = (IPickClick) this.getActivity();
            } else {
                this.onClick = this;
            }
        }

        if (this.onPickResult == null && this.getActivity() instanceof IPickResult) {
            this.onPickResult = (IPickResult) this.getActivity();
        }

    }

    protected void onInitialize() {
        if (this.getDialog().getWindow() != null) {
            this.getDialog().getWindow().requestFeature(1);
            this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        this.setup = (PickSetup) this.getArguments().getSerializable("SETUP_TAG");
        this.resolver = new IntentResolver(this.getActivity(), this.setup);
    }

    private boolean isValidProviders() {
        if (this.validProviders == null) {
            this.validProviders = Boolean.valueOf(true);
            this.showCamera = EPickType.CAMERA.inside(this.setup.getPickTypes()) && (this.onClick == null || this.resolver.isCamerasAvailable() && !this.resolver.wasCameraPermissionDeniedForever());
            this.showGallery = EPickType.GALLERY.inside(this.setup.getPickTypes());
            if (!this.showCamera && !this.showGallery) {
                Error e = new Error(this.getString(string.no_providers));
                this.validProviders = Boolean.valueOf(false);
                if (this.onPickResult == null) {
                    throw e;
                }

                this.onError(e);
            }
        }

        return this.validProviders.booleanValue();
    }

    private void onBindViewsHolders(View v) {
        this.card = (CardView) v.findViewById(id.card);
        this.vFirstLayer = v.findViewById(id.first_layer);
        this.vSecondLayer = v.findViewById(id.second_layer);
    }

    private void onBindViews(View v) {
        this.llButtons = (LinearLayout) v.findViewById(id.buttons_holder);
        this.tvTitle = (TextView) v.findViewById(id.title);
        this.tvCamera = (TextView) v.findViewById(id.camera);
        this.tvGallery = (TextView) v.findViewById(id.gallery);
        this.tvCancel = (TextView) v.findViewById(id.cancel);
        this.tvProgress = (TextView) v.findViewById(id.loading_text);
    }

    private void onBindViewListeners() {
        this.tvCancel.setOnClickListener(this.listener);
        this.tvCamera.setOnClickListener(this.listener);
        this.tvGallery.setOnClickListener(this.listener);
    }

    private void onSetup() {
        if (this.setup.getBackgroundColor() != 17170443) {
            this.card.setCardBackgroundColor(this.setup.getBackgroundColor());
            if (this.showCamera) {
                Util.background(this.tvCamera, Util.getAdaptiveRippleDrawable(this.setup.getBackgroundColor()));
            }

            if (this.showGallery) {
                Util.background(this.tvGallery, Util.getAdaptiveRippleDrawable(this.setup.getBackgroundColor()));
            }
        }

        this.tvTitle.setTextColor(this.setup.getTitleColor());
        if (this.setup.getButtonTextColor() != 0) {
            this.tvCamera.setTextColor(this.setup.getButtonTextColor());
            this.tvGallery.setTextColor(this.setup.getButtonTextColor());
        }

        if (this.setup.getProgressTextColor() != 0) {
            this.tvProgress.setTextColor(this.setup.getProgressTextColor());
        }

        if (this.setup.getCancelTextColor() != 0) {
            this.tvCancel.setTextColor(this.setup.getCancelTextColor());
        }

        if (this.setup.getCameraButtonText() != null) {
            this.tvCamera.setText(this.setup.getCameraButtonText());
        }

        if (this.setup.getGalleryButtonText() != null) {
            this.tvGallery.setText(this.setup.getGalleryButtonText());
        }

        this.tvCancel.setText(this.setup.getCancelText());
        this.tvTitle.setText(this.setup.getTitle());
        this.tvProgress.setText(this.setup.getProgressText());
        this.showProgress(false);
        Util.gone(this.tvCamera, !this.showCamera);
        Util.gone(this.tvGallery, !this.showGallery);
        this.llButtons.setOrientation(this.setup.getButtonOrientation() == LinearLayout.HORIZONTAL ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        Util.setIcon(this.tvCamera, this.setup.getCameraIcon(), this.setup.getIconGravity());
        Util.setIcon(this.tvGallery, this.setup.getGalleryIcon(), this.setup.getIconGravity());
        Util.setDimAmount(this.setup.getDimAmount(), this.getDialog());
    }

    protected void showProgress(boolean show) {
        Util.gone(this.card, false);
        Util.gone(this.vFirstLayer, show);
        Util.gone(this.vSecondLayer, !show);
    }

    protected void launchCamera() {
        if (this.resolver.requestCameraPermissions(this)) {
            this.resolver.launchCamera(this);
        }

    }

    protected void launchGallery() {
        this.resolver.launchGallery(this);
    }

    protected boolean launchSystemDialog() {
        if (this.setup.isSystemDialog()) {
            this.card.setVisibility(View.GONE);
            if (this.showCamera) {
                if (this.resolver.requestCameraPermissions(this)) {
                    this.resolver.launchSystemChooser(this);
                }
            } else {
                this.resolver.launchSystemChooser(this);
            }

            return true;
        } else {
            return false;
        }
    }

    protected CustomerPickImageBaseDialog setOnPickResult(IPickResult onPickResult) {
        this.onPickResult = onPickResult;
        return this;
    }

    protected CustomerPickImageBaseDialog setOnClick(IPickClick onClick) {
        this.onClick = onClick;
        return this;
    }

    protected AsyncImageResult getAsyncResult() {
        return (new AsyncImageResult(this.getActivity(), this.setup)).setOnFinish(new OnFinish() {
            public void onFinish(PickResult pickResult) {
                if (CustomerPickImageBaseDialog.this.onPickResult != null) {
                    CustomerPickImageBaseDialog.this.onPickResult.onPickResult(pickResult);
                }

                CustomerPickImageBaseDialog.this.dismissAllowingStateLoss();
            }
        });
    }

    public Context getContext() {
        Object context = super.getContext();
        if (context == null && this.resolver != null) {
            context = this.resolver.getActivity();
        }

        return (Context) context;
    }

    private View delayedDismiss() {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                CustomerPickImageBaseDialog.this.dismiss();
            }
        }, 20L);
        return new View(this.getContext());
    }

    protected void onError(Error e) {
        if (this.onPickResult != null) {
            this.onPickResult.onPickResult((new PickResult()).setError(e));
            this.dismissAllowingStateLoss();
        }

    }
}
