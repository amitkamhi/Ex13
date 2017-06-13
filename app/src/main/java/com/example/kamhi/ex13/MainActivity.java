package com.example.kamhi.ex13;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;

import com.example.kamhi.ex13.data.DatabaseHelper;
import com.example.kamhi.ex13.data.Item;

import java.util.HashSet;

import uz.shift.colorpicker.LineColorPicker;

public class MainActivity extends Activity{

    GridView gv = null;
    MyAdapter adapter = null;
    Button btSortByNumber = null;
    Button btSortByColor = null;
    Button btShuffle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = (GridView)findViewById(R.id.gameTable);
        this.adapter = new MyAdapter(this, R.layout.item);
        gv.setAdapter(adapter);

        btShuffle = (Button)findViewById(R.id.shuffle);
        btSortByNumber = (Button)findViewById(R.id.number);
        btSortByColor = (Button)findViewById(R.id.color);


    }

    public void openNewItemDialog(){
        HashSet<Integer> existingNumbers = this.adapter.getExistingNumbers();
        String[] stringArray = new String[MyAdapter.MAX_NUMBERS + 1 -existingNumbers.size()];
        int i=0,n=0;
        while (n <=MyAdapter.MAX_NUMBERS){

            if (!existingNumbers.contains(n))
                stringArray[i++] = Integer.toString(n);
            n++;
        }
        Bundle bndl = new Bundle();
        bndl.putStringArray("numbers", stringArray);
        NewItemDlg dlg = new NewItemDlg(bndl);
        dlg.setCancelable(false);
        dlg.show(getFragmentManager(), "");
    }

    public void sort(View v){
        switch(v.getId()){
            case R.id.number:
                adapter.sortItems(DatabaseHelper.SORT_BY_NUMS);
                break;
            case R.id.color:
                adapter.sortItems(DatabaseHelper.SORT_BY_COLORS);
                break;
            case R.id.shuffle:
                adapter.sortItems(DatabaseHelper.SHUFFLE);
                break;
        }
    }

    private class NewItemDlg extends DialogFragment{

        private LineColorPicker colorPicker;

        private NumberPicker num;
        String[] stringArray=null;

        private String[] pallete = new String[] { "#b8c847", "#67bb43", "#41b691",
                "#4182b6", "#4149b6", "#7641b6", "#b741a7", "#c54657", "#d1694a" };

        private int[] palleteInt = new int[] { 0xffb8c847, 0xff67bb43, 0xff41b691,
                0xff4182b6, 0xff4149b6, 0xff7641b6, 0xffb741a7, 0xffc54657, 0xffd1694a };

        public NewItemDlg() {}//to let android auto-restart the fragment when rotating

        public NewItemDlg(Bundle args) {
            this.setArguments(args);}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final View dlgView = getActivity().getLayoutInflater().inflate(R.layout.new_item, null);

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity())
                            .setView(dlgView)
                            .setTitle("New Item")
                            .setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismiss();
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int index = num.getValue();

                                    Item item = new Item(Integer.parseInt(stringArray[index]),colorPicker.getColor());
                                    adapter.addNewItem(item);
                                    dismiss();
                                }
                            });

            colorPicker = (LineColorPicker) dlgView.findViewById(R.id.colorPicker);
            num =(NumberPicker) dlgView.findViewById(R.id.numberPicker);

            int[] colors = generateColors(Color.BLUE, Color.RED, 10);
//			colorPicker.setColors(colors);
            colorPicker.setColors(palleteInt);
            colorPicker.setSelectedColorPosition(0);
            this.stringArray = getArguments().getStringArray("numbers");//new String[10];
            num.setDividerDrawable(null);
            num.setMaxValue(stringArray.length-1);
            num.setMinValue(0);

            num.setDisplayedValues(stringArray);
            return builder.create();
        }

        private int[] generateColors(int from, int to, int count) {
            int[] palette = new int[count];

            float[] hsvFrom = new float[3];
            float[] hsvTo = new float[3];

            Color.colorToHSV(from, hsvFrom);
            Color.colorToHSV(to, hsvTo);

            float step = (hsvTo[0] - hsvFrom[0]) / count;

            for (int i = 0; i < count; i++) {
                palette[i] = Color.HSVToColor(hsvFrom);

                hsvFrom[0] += step;
            }

            return palette;
        }
    }
}
