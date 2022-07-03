package com.example.drrbni.Fragments.BottomNavigationScreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drrbni.Adapters.NotificationAdapter;
import com.example.drrbni.Models.Notification;
import com.example.drrbni.ViewModels.MyListener;
import com.example.drrbni.ViewModels.NotificationViewModel;
import com.example.drrbni.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationViewModel notificationViewModel;
    private FirebaseAuth auth;
    private NotificationAdapter adapter;

    public NotificationsFragment() {
    }

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        auth = FirebaseAuth.getInstance();
        load();

        notificationViewModel.getNotificationsByUid(auth.getCurrentUser().getUid(), new MyListener<List<Notification>>() {
            @Override
            public void onValuePosted(List<Notification> value) {
                if (getActivity() == null) return;
                stopLoad();
                if (value.isEmpty()) {
                    binding.notificationsList.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                } else {
                    binding.notificationsList.setVisibility(View.VISIBLE);
                    binding.noData.setVisibility(View.GONE);
                }
                adapter = new NotificationAdapter(value, notificationViewModel, auth.getCurrentUser().getUid());
                initRV();
            }
        }, new MyListener<Boolean>() {
            @Override
            public void onValuePosted(Boolean value) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void initRV() {
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        binding.notificationsList.setLayoutManager(lm);
        binding.notificationsList.setHasFixedSize(true);
        binding.notificationsList.setAdapter(adapter);
    }

    public void load() {
        binding.shimmerView.setVisibility(View.VISIBLE);
        binding.shimmerView.startShimmerAnimation();
        binding.linear.setVisibility(View.GONE);
    }

    public void stopLoad() {
        binding.shimmerView.setVisibility(View.GONE);
        binding.shimmerView.stopShimmerAnimation();
        binding.linear.setVisibility(View.VISIBLE);
    }
}