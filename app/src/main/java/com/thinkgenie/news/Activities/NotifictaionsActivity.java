package com.thinkgenie.news.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thinkgenie.news.Adapters.NotificationsAdapter;
import com.thinkgenie.news.Model.NotifictaionsModel;
import com.thinkgenie.news.R;

import java.util.ArrayList;
import java.util.List;

public class NotifictaionsActivity extends AppCompatActivity {
    NotificationsAdapter notificationsAdapter;
    private List newsdataList = new ArrayList<>();
    RecyclerView recyclerView;
TextView news_toolbarTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifictaions);
        notificationsAdapter = new NotificationsAdapter(newsdataList, this);
        recyclerView = findViewById(R.id.recyclerview_notfctns);
        news_toolbarTv = findViewById(R.id.news_toolbarTv);
        news_toolbarTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten=new Intent(NotifictaionsActivity.this, BottomNavigationActivity.class);
                startActivity(inten);
            }
        });
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(notificationsAdapter);
        notifictaionsDataApi();
    }

    private void notifictaionsDataApi() {
        NotifictaionsModel data = new NotifictaionsModel("julian ", "Liked your Article"
                , "5 Mins Ago");
        newsdataList.add(data);
        data = new NotifictaionsModel("julian ",
                "Comment on your article", "5 Mins Ago");
        newsdataList.add(data);
        data = new NotifictaionsModel("john ",
                "Foe 2 UNESCO", "5 Mins Ago");
        newsdataList.add(data);
        data = new NotifictaionsModel("mike ",
                "Foe 2 UNESCO", "1 hr Ago");
        newsdataList.add(data);
        data = new NotifictaionsModel("Blair",
                "Foe 2 UNESCO", "5 Mins Ago");
        data = new NotifictaionsModel("Blair",
                "Foe 2 UNESCO", "5 Mins Ago");
        data = new NotifictaionsModel("Blair",
                "Foe 2 UNESCO", "5 Mins Ago");
        data = new NotifictaionsModel("Blair",
                "Foe 2 UNESCO", "5 Mins Ago");
        data = new NotifictaionsModel("Blair",
                "Foe 2 UNESCO", "5 Mins Ago");
        data = new NotifictaionsModel("Blair",
                "Foe 2 UNESCO", "5 Mins Ago");
        data = new NotifictaionsModel("Blair",
                "Foe 2 UNESCO", "5 Mins Ago");
        data = new NotifictaionsModel("Blair",
                "Foe 2 UNESCO", "5 Mins Ago");
        newsdataList.add(data);

    }


}