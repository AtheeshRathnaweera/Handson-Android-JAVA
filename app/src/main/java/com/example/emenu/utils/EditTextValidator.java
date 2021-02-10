package com.example.emenu.utils;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class EditTextValidator {

    public static List<EditText> checkNull(List<EditText> editTextList) {
        List<EditText> nullEditTexts = new ArrayList<>();

        for (EditText editText : editTextList) {
            if (editText == null || editText.getText().toString().trim().length() == 0) {
                nullEditTexts.add(editText);
            }
        }

        return nullEditTexts;
    }

    public static List<EditText> checkInvalidEmail(List<EditText> editTextList) {
        List<EditText> invalidEditTexts = new ArrayList<>();

        for (EditText editText : editTextList) {
            if (!editText.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                invalidEditTexts.add(editText);
            }
        }

        return invalidEditTexts;
    }
}
