package java.se.umu.mobileapp.eshu0006.thirtydicegame.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.se.umu.mobileapp.eshu0006.thirtydicegame.R;
import java.se.umu.mobileapp.eshu0006.thirtydicegame.model.OneGameRoundScore;
import java.util.List;

/**
 * Class defining the row layout and binding it to the main activity.
 */
public class CustomAdapter extends BaseAdapter{
    private Activity activity;
    private List<OneGameRoundScore> gameScoreOptions;
    private LayoutInflater layoutInflater;

    /**
     * Constructor: creating an instance of CustomAdapter.
     * @param activity The activity to get the LayoutInflater object from.
     * @param gameScoreOptions The rows for all game score options.
     */
    public CustomAdapter(Activity activity, List<OneGameRoundScore> gameScoreOptions) {
        this.activity = activity;
        this.gameScoreOptions = gameScoreOptions;
        this.layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount(){
        return gameScoreOptions.size();
    }

    @Override
    public Object getItem(int i){
        return i;
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    /**
     * Static class to guarantee that instances
     * of this class cannot be created.
     */
    static class OneRowView {
        TextView textView;
        ImageView imageView;
    }

    @Override
    /**
     * Method defining the row layout and data binding it the inflated activity.
     * The layoutInflater is used to instantiate the contents of the layout XML file
     * into its corresponding View object.
     */
    public View getView (int position, View convertView, ViewGroup parent){
        OneRowView oneRow;
        if (convertView == null) {
            // Inflating the xml, taking its content and building the view object from it, which gives a view
            convertView = layoutInflater.inflate(R.layout.one_row, parent, false);
            oneRow = new OneRowView();
            oneRow.textView = (TextView) convertView.findViewById(R.id.one_row_text_view);
            oneRow.imageView = (ImageView) convertView.findViewById(R.id.one_row_image_view);
            convertView.setTag(oneRow);
        }
        else
            oneRow = (OneRowView) convertView.getTag();

        checkBoxesForScoreOptions(position, oneRow);
        return convertView;
    }

    /**
     * Method checking the box for the current specified score option.
     * It the score option has been played, it has a green check. If the
     * score option is currently marked, before ending the round and permanently
     * checking as played, the preliminary check is black. If the score
     * option has not been played and is not being checked, during the current round,
     * the check box will be white and empty.
     *
     * @param position The index for the current game score option
     * @param oneRow The static oneRowView class
     */
    private void checkBoxesForScoreOptions (int position, OneRowView oneRow) {
        OneGameRoundScore oneGameScoreOption = gameScoreOptions.get(position);
        oneRow.textView.setText(oneGameScoreOption.getName());

        if (oneGameScoreOption.roundHasBeenPlayed()) {
            oneRow.imageView.setBackgroundResource(R.drawable.checked_green); // If the game score option has been played
            oneGameScoreOption.setSelected(false);
        }
        else if (oneGameScoreOption.isSelected())
            oneRow.imageView.setBackgroundResource(R.drawable.checked);       // If the game score option is preliminary checked, not played yet
        else
            oneRow.imageView.setBackgroundResource(R.drawable.unchecked);     // If the game score option is not played

    }

}
