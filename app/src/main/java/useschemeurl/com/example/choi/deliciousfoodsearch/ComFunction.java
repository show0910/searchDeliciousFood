package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Choi on 2016-11-04.
 */

public class ComFunction {

    public String delSpace(String text) {

        String newText = "";
        String word;
        int num = text.length();

        char[] leng = text.toCharArray();

        for (int i = 0; i < num; i++) {
            if (text.charAt(i) != ' ') {
                word = Character.toString((leng[i]));
                newText = newText + word;
            }
        }

        return newText;
    }

}
