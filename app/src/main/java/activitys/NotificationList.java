package activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.emsphere.commando.emspherevms.Login;
import com.emsphere.commando.emspherevms.R;

/**
 * Created by commando1 on 8/22/2017.
 */

public class NotificationList extends AppCompatActivity {

    ListView listView;
  Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_noti);
        listView = (ListView) findViewById(R.id.listview);
        toolbar = (Toolbar) findViewById(R.id.toolbar_Notification);
        //setActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // Log.e("data", Login.arrayList.toString());
        NotificationAdapter adapter = new NotificationAdapter(this, Login.arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),Home.class);
                intent.putExtra("name",Login.arrayList.get(position).getName());
                intent.putExtra("mobile_no",Login.arrayList.get(position).getMobile_no());
                intent.putExtra("representing",Login.arrayList.get(position).getRepresenting());
                intent.putExtra("purpose",Login.arrayList.get(position).getPurpose());
                intent.putExtra("notification_id",Login.arrayList.get(position).getNotification_id());
                intent.putExtra("vstr_entry_id",Login.arrayList.get(position).getVisitor_entry_id());

                startActivity(intent);
                //Toast.makeText(getApplicationContext(),""+Login.arrayList.get(position).toString(),Toast.LENGTH_LONG).show();

            }
        });
    }
}
