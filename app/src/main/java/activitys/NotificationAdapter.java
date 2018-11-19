package activitys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emsphere.commando.emspherevms.R;

import java.util.ArrayList;

/**
 * Created by commando1 on 8/22/2017.
 */

public class NotificationAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NotificationPojo> mDataSource;


    public NotificationAdapter(Context context, ArrayList<NotificationPojo> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_mobile, parent, false);

        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.label);
        TextView bodyTextView =
                (TextView) rowView.findViewById(R.id.body);
        // 1
        NotificationPojo recipe = (NotificationPojo) getItem(position);

// 2
        titleTextView.setText(recipe.Title);
        bodyTextView.setText(recipe.body);

        return rowView;
    }
}
