package useschemeurl.com.example.choi.deliciousfoodsearch.board;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import useschemeurl.com.example.choi.deliciousfoodsearch.NoticeBoardAnother;

/**
 * Created by Choi on 2016-11-08.
 */

public class IconTextListAdapter extends BaseAdapter {

    private Context mContext;
    private List<IconTextItem> mItems = new ArrayList<IconTextItem>();

    public IconTextListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addItem(IconTextItem it) {
        mItems.add(it);
    }

    public void setmItems(List<IconTextItem> mItems) {
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    public ArrayList<String> getAllTitle() {

        ArrayList<String> titleList = new ArrayList<String>();

        for (IconTextItem item : mItems) {
            titleList.add(item.getData(0));
        }

        return titleList;
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).ismSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }


    public void delView(ArrayList<Integer> delList) {
        for (int i = 0; i < delList.size(); i++) {
            mItems.remove(delList.get(i) - i);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final IconTextView itemView;

        if (convertView == null) {
            itemView = new IconTextView(mContext, mItems.get(position));
        } else {
            itemView = (IconTextView) convertView;
            itemView.setIcon(mItems.get(position).getIcon());
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setRatingPoint(mItems.get(position).getmPoint());
            itemView.setImage(mItems.get(position).getmImagePath(), mItems.get(position).getDegree());
        }

        if (mItems.get(position).ismSelectValid()) {
            itemView.setBackgroundColor(Color.parseColor("#EAEAEA"));
            itemView.validCheckBox.setChecked(true);
        } else {
            if (position % 2 == 0) {
                itemView.setBackgroundColor(Color.parseColor("#D9E5FF"));
                itemView.validCheckBox.setChecked(false);
            } else if (position % 2 == 1) {
                itemView.setBackgroundColor(Color.parseColor("#B2CCFF"));
                itemView.validCheckBox.setChecked(false);
            }
        }

        itemView.mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeBoardAnother.class);


                IconTextItem item = (IconTextItem) getItem(position);

                intent.putExtra("usage", "view");
                intent.putExtra("title", item.getData(0));
                intent.putExtra("contents", item.getData(1));
                intent.putExtra("point", item.getmPoint());
                intent.putExtra("imagePath", item.getmImagePath());

                mContext.startActivity(intent);
            }
        });

        itemView.layoutList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeBoardAnother.class);

                IconTextItem item = (IconTextItem) getItem(position);

                intent.putExtra("usage", "view");
                intent.putExtra("title", item.getData(0));
                intent.putExtra("contents", item.getData(1));
                intent.putExtra("point", item.getmPoint());
                intent.putExtra("imagePath", item.getmImagePath());

                mContext.startActivity(intent);
            }
        });

        itemView.validCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    itemView.setBackgroundColor(Color.parseColor("#EAEAEA"));
                    mItems.get(position).setmSelectValid(true);
                } else {
                    if (position % 2 == 0) {
                        itemView.setBackgroundColor(Color.parseColor("#D9E5FF"));
                        mItems.get(position).setmSelectValid(false);
                    } else if (position % 2 == 1) {
                        itemView.setBackgroundColor(Color.parseColor("#B2CCFF"));
                        mItems.get(position).setmSelectValid(false);
                    }
                }
            }
        });

        return itemView;
    }
}
