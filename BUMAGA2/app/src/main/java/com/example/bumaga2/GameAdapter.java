package com.example.bumaga2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class GameAdapter extends BaseAdapter {
    private Context context;
    private List<Game> gameList;

    public GameAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_game, parent, false);
        }

        Game game = gameList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView developerTextView = convertView.findViewById(R.id.developerTextView);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        titleTextView.setText(game.getTitle());
        developerTextView.setText(game.getDeveloper());
        imageView.setImageBitmap(game.getImage());

        return convertView;
    }
}