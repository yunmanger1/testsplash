package kz.edu.sdu.jg;

import kz.edu.sdu.jg.models.Company;
import kz.edu.sdu.jg.utils.ParseUtils;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class DetailActivity extends Activity {
   TextView title;
   ParseUtils pu;
   TextView view;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.details);

      pu = new ParseUtils();
      title = (TextView) findViewById(R.id.detailtitle);
      Company c = (Company) savedInstanceState.get("COMPANY");
      title.setText(c.name);
      view = (TextView) findViewById(R.id.detailview);
      view.setText(Html.fromHtml(c.content));
   }
}
