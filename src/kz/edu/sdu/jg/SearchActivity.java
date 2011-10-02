package kz.edu.sdu.jg;

import java.io.IOException;
import java.util.List;

import kz.edu.sdu.jg.models.Category;
import kz.edu.sdu.jg.models.Company;
import kz.edu.sdu.jg.utils.ParseUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class SearchActivity extends Activity {
   static final int COMPANY_TYPE = 1;
   static final int CATEGORY_TYPE = 2;
   Spinner spinner;
   EditText text;
   ParseUtils pu;
   ListView resultlist;
   List result = null;
   int type;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.search);

      pu = new ParseUtils();
      spinner = (Spinner) findViewById(R.id.searchspinner);
      text = (EditText) findViewById(R.id.searchText);
      resultlist = (ListView) findViewById(R.id.resultlist);
      ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.searchtypes, android.R.layout.simple_spinner_item);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner.setAdapter(adapter);
      resultlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

         @Override
         public void onItemSelected(AdapterView< ? > parent, View view, int pos, long id) {
            if (type == COMPANY_TYPE) {
               Intent i = new Intent(view.getContext(), DetailActivity.class);
               Company c = (Company) result.get(pos);
               i.putExtra("COMPANY", c);
               startActivity(i);
            } else if (type == CATEGORY_TYPE) {
               Intent i = new Intent(view.getContext(), CategoryActivity.class);
               Category c = (Category) result.get(pos);
               i.putExtra("CATEGORY", c);
               startActivity(i);
            }
         }

         @Override
         public void onNothingSelected(AdapterView< ? > arg0) {
            // TODO Auto-generated method stub

         }

      });

      Button b = (Button) findViewById(R.id.searchbutton);
      b.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
            resultlist.setAdapter(new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, new String[0]));
            int ALMATY = 30;
            int k = (int) spinner.getSelectedItemId();
            String value = text.getText().toString();
            try {
               switch (k) {
                  case 0 :
                     result = pu.searchCompaniesByName(ALMATY, value);
                     type = COMPANY_TYPE;
                     break;
                  case 1 :
                     String[] t = value.split(",");
                     String street = t[0];
                     String house = "";
                     if (t.length > 1) {
                        house = t[1];
                     }
                     result = pu.searchCompaniesByAddress(ALMATY, street, house);
                     type = COMPANY_TYPE;
                     break;
                  case 2 :
                     result = pu.searchCompaniesByPhone(ALMATY, value);
                     type = COMPANY_TYPE;
                     break;
                  case 3 :
                     result = pu.searchCategoryByName(ALMATY, value);
                     type = CATEGORY_TYPE;
                     break;

                  default :
                     break;
               }
            } catch (IOException e) {
               Log.wtf("onSearch", e);
            }
            if (result != null && result.size() > 0) {
               String[] strings = new String[result.size()];
               int g = 0;
               for (Object o : result) {
                  if (o instanceof Company) {
                     strings[g++] = ((Company) o).name;
                  } else {
                     strings[g++] = ((Category) o).name;
                  }
               }
               resultlist.setAdapter(new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, strings));
               resultlist.setTextFilterEnabled(true);
            }
         }
      });
   }
}
