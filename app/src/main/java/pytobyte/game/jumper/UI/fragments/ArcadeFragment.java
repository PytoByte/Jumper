package pytobyte.game.jumper.UI.fragments;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pytobyte.game.jumper.R;
import pytobyte.game.jumper.UI.activities.MainActivity;
import pytobyte.game.jumper.databinding.FragmentArcadeBinding;

public class ArcadeFragment extends Fragment {

    FragmentArcadeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_arcade, container, false);
        binding = FragmentArcadeBinding.bind(view);

        binding.play.setOnClickListener(v -> play());

        return view;
    }

    public void play() {
        if (requireActivity() instanceof MainActivity) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("level", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            prefEditor.putString("mode", "arcade");
            prefEditor.apply();
            findNavController(getParentFragment()).navigate(R.id.action_mainFragment_to_gameUIFragment);
        }
    }
}