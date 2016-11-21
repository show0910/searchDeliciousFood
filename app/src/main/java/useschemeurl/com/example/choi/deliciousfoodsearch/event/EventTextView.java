package useschemeurl.com.example.choi.deliciousfoodsearch.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import useschemeurl.com.example.choi.deliciousfoodsearch.R;
import useschemeurl.com.example.choi.deliciousfoodsearch.board.IconTextItem;

/**
 * Created by Choi on 2016-11-18.
 */

public class EventTextView extends LinearLayout {

    TextView titleView;
    TextView addressView;
    TextView txtShopName;
    TextView remainTime;
    CheckBox validCheckBox;
    LinearLayout layoutList;

    public EventTextView(Context context, EventTextItem aItem) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.event_board_list_view, this, true);

        titleView = (TextView) findViewById(R.id.titleView);
        addressView = (TextView) findViewById(R.id.addressView);
        txtShopName = (TextView) findViewById(R.id.txtShopName);
        validCheckBox = (CheckBox) findViewById(R.id.validCheckBox);
        layoutList = (LinearLayout) findViewById(R.id.layoutList);
        remainTime = (TextView) findViewById(R.id.remainTime);

        titleView.setText(aItem.getData(0));
        addressView.setText(aItem.getData(1));
        txtShopName.setText(aItem.getData(5));

    }

    public void setText(int index, String data) {
        if (index == 0) {
            titleView.setText(data);
        } else if (index == 1) {
            addressView.setText(data);
        } else if (index == 2) {
            txtShopName.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }


}
