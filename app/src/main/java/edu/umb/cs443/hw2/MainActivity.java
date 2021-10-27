package edu.umb.cs443.hw2;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {

    GridView gridView;

    private static int w=5,curx,cury;

    private Random r=new Random();

    static String[] tiles = new String[w*w];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView1);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, tiles);

        gridView.setAdapter(adapter);

        init();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                    Toast.makeText(getApplicationContext(),
                        (CharSequence) (new Integer(position).toString()),
                        Toast.LENGTH_SHORT).show();

            }


        });

    }

    public void reset(View view){
        init();
    }

    void init(){
        for(int i=0;i<tiles.length;i++) tiles[i]=" ";

        curx=r.nextInt(w);
        cury=r.nextInt(w);

        tiles[cury*w+curx]="O";

        ((ArrayAdapter)gridView.getAdapter()).notifyDataSetChanged();
    }


}
