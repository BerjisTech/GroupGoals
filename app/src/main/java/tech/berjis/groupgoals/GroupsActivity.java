package tech.berjis.groupgoals;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    RecyclerView groupsRecycler;
    GroupsAdapter groupsAdapter;
    List<GroupsList> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        init_variables();
        loadGroups();
    }

    private void loadGroups() {
        listData = new ArrayList<>();
        listData.clear();
        groupsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listData.add(new GroupsList("","","","","","","",0));
        listData.add(new GroupsList("","","","","","","",0));
        listData.add(new GroupsList("","","","","","","",0));
        listData.add(new GroupsList("","","","","","","",0));
        listData.add(new GroupsList("","","","","","","",0));
        groupsAdapter = new GroupsAdapter(this, listData);
        groupsRecycler.setAdapter(groupsAdapter);
    }

    private void init_variables(){
        groupsRecycler = findViewById(R.id.groupsRecycler);
    }
}
