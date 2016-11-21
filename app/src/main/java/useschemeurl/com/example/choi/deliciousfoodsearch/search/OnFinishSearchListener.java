package useschemeurl.com.example.choi.deliciousfoodsearch.search;

import java.util.List;

/**
 * Created by Choi on 2016-11-04.
 */

public interface OnFinishSearchListener {
    public void onSuccess(List<Item> itemList);

    public void onFail();
}
