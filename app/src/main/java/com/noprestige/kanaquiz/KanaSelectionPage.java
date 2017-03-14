package com.noprestige.kanaquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KanaSelectionPage extends Fragment
{
    private static final String ARG_PAGE_NUMBER = "position";

    public KanaSelectionPage() {}

    public static KanaSelectionPage newInstance(int id)
    {
        KanaSelectionPage screen = new KanaSelectionPage();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, id);
        screen.setArguments(args);
        return screen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        switch(getArguments().getInt(ARG_PAGE_NUMBER, -1))
        {
            case 0:
                return inflater.inflate(R.layout.fragment_kana_selection_hiragana, container, false);
            case 1:
                return inflater.inflate(R.layout.fragment_kana_selection_katakana, container, false);
            default:
                return null;
        }
    }
}