package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import useschemeurl.com.example.choi.deliciousfoodsearch.board.IconTextItem;
import useschemeurl.com.example.choi.deliciousfoodsearch.event.EventTextItem;
import useschemeurl.com.example.choi.deliciousfoodsearch.event.EventTextListAdapter;


/**
 * Created by Choi on 2016-11-18.
 */

public class EventBoard extends AppCompatActivity {

    public static final int REQUEST_CODE_INSERT = 1001;
    public static final int REQUEST_CODE_UPDATE = 1002;

    ListView listView01;
    EventTextListAdapter adapter;
    Button addListButton;
    Button delListButton;
    Button updateListButton;
    int posForUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_board);

        final ArrayList<Integer> list = new ArrayList<Integer>();

        listView01 = (ListView) findViewById(R.id.listView);
        addListButton = (Button) findViewById(R.id.addListButton);
        delListButton = (Button) findViewById(R.id.delListButton);
        updateListButton = (Button) findViewById(R.id.updateListButton);

        adapter = new EventTextListAdapter(this);

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), EventBoardAnother.class);
                intent.putExtra("usage", "insert");
                startActivityForResult(intent, REQUEST_CODE_INSERT);

            }
        });

        updateListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), EventBoardAnother.class);

                list.clear();

                for (int i = 0; i < adapter.getCount(); i++) {
                    if (((EventTextItem) adapter.getItem(i)).getSelectValid()) {
                        list.add(i);
                        posForUpdate = i;
                    }
                }

                if (list.size() != 1) {
                    Toast.makeText(getApplicationContext(), "1개의 List를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                EventTextItem item = (EventTextItem) adapter.getItem(posForUpdate);

                intent.putExtra("usage", "update");
                intent.putExtra("title", item.getData(0));
                intent.putExtra("address", item.getData(1));
                intent.putExtra("time", item.getData(2));
                intent.putExtra("contents", item.getData(3));
                intent.putExtra("hour", item.getData(4));
                intent.putExtra("shopName", item.getData(5));

                startActivityForResult(intent, REQUEST_CODE_UPDATE);
            }
        });


        delListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.clear();
                String delTitle = null;

                for (int i = 0; i < adapter.getCount(); i++) {
                    if (((EventTextItem) adapter.getItem(i)).getSelectValid()) {
                        list.add(i);
                        if (delTitle == null) {
                            delTitle = ((EventTextItem) adapter.getItem(i)).getData(0);
                        } else {
                            delTitle = delTitle + "^" + ((EventTextItem) adapter.getItem(i)).getData(0);
                        }
                    }
                }

                if (list.size() == 0) {
                    Toast.makeText(getApplicationContext(), "선택된 List가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(EventBoard.this);
                builder.setMessage("선택된 리스트를 삭제하시겠습니까??").setCancelable(false)
                        .setPositiveButton("삭제!!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adapter.delView(list);
                                listView01.setAdapter(adapter);
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_OK) {

            String[] curData = new String[6];

            curData[0] = data.getStringExtra("title");
            curData[1] = data.getStringExtra("address");
            curData[2] = data.getStringExtra("time");
            curData[3] = data.getStringExtra("contents");
            curData[4] = data.getStringExtra("hour");
            curData[5] = data.getStringExtra("shopName");

            adapter.addItem(new EventTextItem(curData));

            listView01.setAdapter(adapter);

            Toast.makeText(getApplicationContext(), "Data 입력 완료", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK) {

            String[] curData = new String[6];

            curData[0] = data.getStringExtra("title");
            curData[1] = data.getStringExtra("address");
            curData[2] = data.getStringExtra("time");
            curData[3] = data.getStringExtra("contents");
            curData[4] = data.getStringExtra("hour");
            curData[5] = data.getStringExtra("shopName");

            ((EventTextItem) adapter.getItem(posForUpdate)).setData(curData);
            ((EventTextItem) adapter.getItem(posForUpdate)).setSelectValid(false);

            listView01.setAdapter(adapter);

            Toast.makeText(getApplicationContext(), "Data 수정 완료", Toast.LENGTH_SHORT).show();
        }
    }


}
