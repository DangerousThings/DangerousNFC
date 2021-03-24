package com.dangerousthings.nfc.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dangerousthings.nfc.R;
import com.dangerousthings.nfc.enums.MainActionBarState;
import com.dangerousthings.nfc.interfaces.IMainActionBar;

import java.util.Objects;

public class MainFragment extends Fragment
{
    IMainActionBar actionBarInterface;
    Button mToggleReadButton;
    Button mToggleSyncButton;
    ImageView mAnimationView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        ImageButton mDrawerButton = view.findViewById(R.id.main_button_drawer_toggle);
        mToggleReadButton = view.findViewById(R.id.main_button_toggle_read);
        mToggleSyncButton = view.findViewById(R.id.main_button_toggle_sync);
        mDrawerButton.setOnClickListener(v -> actionBarInterface.drawerButtonClicked());
        mToggleReadButton.setOnClickListener(v -> onToggleReadPressed());
        mToggleSyncButton.setOnClickListener(v -> onToggleSyncPressed());
        mAnimationView = view.findViewById(R.id.image_scan_animation);

        setUpScanAnimation();
    }

    private void onToggleReadPressed()
    {
        mToggleReadButton.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.left_pill_primary_dark));
        mToggleSyncButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.right_pill_primary));
        mToggleReadButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.White));
        mToggleSyncButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));

        actionBarInterface.mainActionToggled(MainActionBarState.ReadPayload);
    }

    private void onToggleSyncPressed()
    {
        mToggleSyncButton.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.right_pill_primary_dark));
        mToggleReadButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.left_pill_primary));
        mToggleReadButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
        mToggleSyncButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.White));

        actionBarInterface.mainActionToggled(MainActionBarState.SyncImplant);
    }

    public void setActionBarInterface(IMainActionBar mainActionBar)
    {
        this.actionBarInterface = mainActionBar;
    }

    private void setUpScanAnimation()
    {
        final AnimatedVectorDrawableCompat animation = AnimatedVectorDrawableCompat.create(Objects.requireNonNull(getActivity()), R.drawable.avd_scan);
        assert animation != null;
        animation.setTint(ContextCompat.getColor(getActivity(), R.color.White));
        mAnimationView.setImageDrawable(animation);
        final Handler handler = new Handler(Looper.getMainLooper());
        animation.registerAnimationCallback(new Animatable2Compat.AnimationCallback()
        {
            @Override
            public void onAnimationEnd(Drawable drawable)
            {
                handler.post(animation::start);
                super.onAnimationEnd(drawable);
            }
        });
        animation.start();
    }
}