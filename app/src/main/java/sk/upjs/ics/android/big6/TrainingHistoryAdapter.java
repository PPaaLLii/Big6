//http://www.codelearn.org/android-tutorial/android-listview
//http://www.vogella.com/tutorials/AndroidListView/article.html

package sk.upjs.ics.android.big6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import sk.upjs.ics.android.util.Utils;

/**
 * Created by Pavol on 4. 5. 2015.
 */
public class TrainingHistoryAdapter extends BaseAdapter{

    private ArrayList<Training> list;
    private Context context;

    public TrainingHistoryAdapter(Context context, ArrayList<Training> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.training_list_item, parent, false);

        TextView dateView = (TextView) rowView.findViewById(R.id.trainingHistoryListViewDate);
        TextView typeView = (TextView) rowView.findViewById(R.id.trainingHistoryListViewType);
        TextView trainingView = (TextView) rowView.findViewById(R.id.trainingHistoryListViewTraining);

        Training training = list.get(position);

        StringBuilder sb = new StringBuilder();
        sb.append(training.getYear());
        sb.append(".");
        sb.append(training.getMonth());
        sb.append(".");
        sb.append(training.getDay());

        dateView.setText(sb.toString());
        typeView.setText(Utils.convertType(training.getType()));
        trainingView.setText(training.getTraining());

        return rowView;
    }
}