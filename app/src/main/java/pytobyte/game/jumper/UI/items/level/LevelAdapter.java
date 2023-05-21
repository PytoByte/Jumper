package pytobyte.game.jumper.UI.items.level;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import pytobyte.game.jumper.R;
import pytobyte.game.jumper.UI.Sounds;
import pytobyte.game.jumper.UI.activities.MainActivity;

import java.util.List;

public class LevelAdapter  extends RecyclerView.Adapter<LevelAdapter.ViewHolder> implements Sounds {
    private final LayoutInflater inflater;
    private final List<Level> levels;
    private final Fragment fragment;
    MediaPlayer mPlayer;

    public LevelAdapter(Context context, List<Level> levels, Fragment fragment) {
        this.levels = levels;
        this.inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        mPlayer = initSound(fragment.getActivity(), R.raw.button_sound);
    }

    @Override
    public LevelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_item_level, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LevelAdapter.ViewHolder holder, int position) {
        Level level = levels.get(position);
        holder.name.setText(level.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences("level", MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = sharedPreferences.edit();
                prefEditor.putString("name", level.getName());
                prefEditor.putString("mode", "company");
                prefEditor.apply();
                startGame();

                playSound(mPlayer, fragment.getActivity());
            }
        });
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.name);
        }
    }

    private void startGame() {
        if (fragment.requireActivity() instanceof MainActivity) {
            findNavController(fragment).navigate(R.id.action_mainFragment_to_gameUIFragment);
        }
    }
}
