package kz.edu.sdu.jg.utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kz.edu.sdu.jg.models.Category;
import kz.edu.sdu.jg.models.City;
import kz.edu.sdu.jg.models.Company;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class ParseUtils {
   static Pattern CAT_RE = Pattern.compile("javascript:rubric\\('(\\d+)'\\)");
   static Pattern COM_RE = Pattern.compile("javascript:rubrics\\('(\\d+)'\\)");
   static String CAT_FMT = "/kz/ru/companies/%d/%s/";
   static String COM_FMT = "/kz/ru/rubrics/%s/";

   TagNode rootNode;
   HtmlCleaner cleaner;

   public ParseUtils() {
      cleaner = new HtmlCleaner();
   }

   public ParseUtils(URL htmlPage) throws IOException {
      this();
      rootNode = cleaner.clean(htmlPage);
   }

   public int fetchCompaniesByURL(List<Company> result) {
      int count = 0;
      List<TagNode> options = rootNode.getElementListHavingAttribute("href", true);
      for (TagNode opt : options) {
         String href = opt.getAttributeByName("href");
         Matcher m = COM_RE.matcher(href);
         if (m.matches()) {
            Company tmp = new Company();
            String sid = m.group(1);
            tmp.id = Integer.valueOf(sid);
            tmp.url = String.format(COM_FMT, sid);
            tmp.content = opt.getParent().getText().toString();

            TagNode parent = opt.getParent();
            if (parent != null) {
               String text = parent.getText().toString();
               int pos = text.indexOf("...");
               if (pos != -1) {
                  tmp.name = text.substring(0, pos).trim();
               }
               String cnt = cleaner.getInnerHtml(parent);
               pos = cnt.indexOf("...</a>");
               if (pos != -1) {
                  cnt = cnt.substring(pos + 19);
               }
               tmp.content = cnt;
            }
            count += 1;
            result.add(tmp);
         }
      }
      return count;
   }

   public List<Company> getCategoryDetail(Category cat) throws IOException {
      List<Company> result = new ArrayList<Company>();
      String html = UrlUtils.readURL(new URL("http://yellow-pages.kz" + cat.url));
      rootNode = cleaner.clean(html);
      int cnt = fetchCompaniesByURL(result);
      if (cnt >= 15) {
         Pattern c = Pattern.compile("Результатов по Вашему запросу: <b>(\\d+)</b>");
         Matcher m = c.matcher(html);
         if (m.find()) {
            int k = Integer.valueOf(m.group(1));
            int n = k / 15;
            for (int page = 2; page <= n; page++) {
               rootNode = cleaner.clean(UrlUtils.readURL(new URL("http://yellow-pages.kz" + cat.url + page + '/')));
               fetchCompaniesByURL(result);
            }
         }
      }

      return result;
   }

   public List<City> getAllCities() throws IOException {
      String urlAddr = "http://yellow-pages.kz/kz/ru/address_search/";
      List<City> result = new ArrayList<City>();
      rootNode = cleaner.clean(new URL(urlAddr));
      List<TagNode> options = rootNode.getElementListByName("option", true);
      for (TagNode opt : options) {
         String id = opt.getAttributeByName("value");
         City tmp = new City();
         tmp.id = Integer.valueOf(id);
         tmp.name = opt.getText().toString();
         result.add(tmp);
      }
      return result;
   }

   public List<Category> getAlmatyCategories() throws IOException {
      return getCityCategories(30);
   }

   public List<Category> getCityCategories(int city) throws IOException {
      String urlAddr = String.format("http://yellow-pages.kz/kz/ru/index_search/%d/?|all|/", city);
      List<Category> result = new ArrayList<Category>();
      rootNode = cleaner.clean(new URL(urlAddr));
      List<TagNode> links = rootNode.getElementListHavingAttribute("href", true);
      for (TagNode link : links) {
         String href = link.getAttributeByName("href");
         Matcher m = CAT_RE.matcher(href);
         if (m.matches()) {
            Category tmp = new Category();
            String sid = m.group(1);
            tmp.id = Integer.valueOf(sid);
            tmp.url = String.format(CAT_FMT, city, sid);
            tmp.name = link.getText().toString();
            result.add(tmp);
         }
      }
      return result;
   }

   public static void main(String[] args) throws IOException {
      Properties props = System.getProperties();
      props.put("http.proxyHost", "192.168.1.88");
      props.put("http.proxyPort", "3128");
      ParseUtils pu = new ParseUtils();
      Category cat = new Category();
      cat.url = "/kz/ru/companies/30/00056/";
//      root
      for (Company c : pu.getCategoryDetail(cat)) {
         System.out.println(c.name);
      }
   }
}
