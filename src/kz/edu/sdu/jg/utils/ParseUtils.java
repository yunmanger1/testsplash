package kz.edu.sdu.jg.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kz.edu.sdu.jg.models.Category;
import kz.edu.sdu.jg.models.City;
import kz.edu.sdu.jg.models.Company;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class ParseUtils {
//   static Pattern CAT_RE = Pattern.compile("javascript:rubric\\('(\\d+)'\\)");
//   static Pattern COM_RE = Pattern.compile("javascript:rubrics\\('(\\d+)'\\)");
   static Pattern CAT_RE = Pattern.compile("/kz/ru/companies/\\d+/(\\d+)/");
   static Pattern COM_RE = Pattern.compile("/kz/ru/rubrics/(\\d+)/");
   static Pattern RES_RE = Pattern.compile("Результатов по Вашему запросу: <b>(\\d+)</b>");

   private static final String PHONE_SEARCH = "%s/kz/ru/phone_search/%d/%d/%s/";
   private static final String ADDRESS_SEARCH = "%s/kz/ru/address_search/%d/%d/?%s/%s/";
   private static final String NAME_SEARCH = "%s/kz/ru/name_search/%d/%d/?%s/";
   private static final String CAT_NAME_SEARCH = "%s/kz/ru/activity_search/%d/?%s/";

   static String CAT_FMT = "/kz/ru/companies/%d/%s/";
   static String COM_FMT = "/kz/ru/rubrics/%s/";
   static String DOMAIN = "http://yellow-pages.kz";

   TagNode rootNode;
   HtmlCleaner cleaner;

   public ParseUtils() {
      cleaner = new HtmlCleaner();
   }

   public ParseUtils(URL htmlPage) throws IOException {
      this();
      rootNode = cleaner.clean(htmlPage);
   }

   private int fetchCompanies(List<Company> result) {
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
      String html = UrlUtils.readURL(new URL(DOMAIN + cat.url));
      rootNode = cleaner.clean(html);
      int cnt = fetchCompanies(result);
      if (cnt >= 15) {
         Pattern c = Pattern.compile("Результатов по Вашему запросу: <b>(\\d+)</b>");
         Matcher m = c.matcher(html);
         if (m.find()) {
            int k = Integer.valueOf(m.group(1));
            int n = k / 15;
            for (int page = 2; page <= n; page++) {
               rootNode = cleaner.clean(UrlUtils.readURL(new URL(DOMAIN + cat.url + page + '/')));
               fetchCompanies(result);
            }
         }
      }

      return result;
   }

   public List<City> getAllCities() throws IOException {
      String urlAddr = DOMAIN + "/kz/ru/address_search/";
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

   public List<Category> getAlmatyCategoriesByLetter(char c) throws IOException {
      return getCityCategoriesByLetter(30, c);
   }

   public List<Category> getCityCategories(int city) throws IOException {
      String urlAddr = String.format(DOMAIN + "/kz/ru/index_search/%d/?|all|/", city);
      List<Category> result = new ArrayList<Category>();
      rootNode = cleaner.clean(new URL(urlAddr));
      fetchCategories(city, result);
      return result;
   }

   public List<Category> getCityCategoriesByLetter(int city, char c) throws IOException {
      List<Category> result = new ArrayList<Category>();
      String cc = "" + Character.toUpperCase(c);
      try {
         String urlAddr = String.format("%s/kz/ru/index_search/%d/?|%s|/", DOMAIN, city, _u(cc));
         rootNode = cleaner.clean(new URL(urlAddr));
         fetchCategories(city, result);
      } catch (UnsupportedCharsetException e) {
         e.printStackTrace();
      }
      return result;
   }

   private void fetchCategories(int city, List<Category> result) {
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
   }

   public static String _u(String s) {
      try {
         return URLEncoder.encode(s, "cp1251");
      } catch (UnsupportedEncodingException e) {
         return s;
      }
   }

   public List<Company> searchCompaniesByPhone(int city, String phone) throws IOException {
      int page = 1;
      List<Company> result = new ArrayList<Company>();
      String url = String.format(PHONE_SEARCH, DOMAIN, city, page, _u(phone));
      String html = UrlUtils.readURL(new URL(url));
      rootNode = cleaner.clean(html);
      int cnt = fetchCompanies(result);
      if (cnt >= 15) {
         Matcher m = RES_RE.matcher(html);
         if (m.find()) {
            int k = Integer.valueOf(m.group(1));
            int n = k / 15;
            for (page = 2; page <= n; page++) {
               rootNode = cleaner.clean(new URL(String.format(PHONE_SEARCH, DOMAIN, city, page, phone)));
               fetchCompanies(result);
            }
         }
      }
      return result;
   }

   public List<Company> searchCompaniesByAddress(int city, String street, String house) throws IOException {
      int page = 1;
      List<Company> result = new ArrayList<Company>();
      String url = String.format(ADDRESS_SEARCH, DOMAIN, city, page, _u(street), _u(house));
      String html = UrlUtils.readURL(new URL(url));
      rootNode = cleaner.clean(html);
      int cnt = fetchCompanies(result);
      if (cnt >= 15) {
         Matcher m = RES_RE.matcher(html);
         if (m.find()) {
            int k = Integer.valueOf(m.group(1));
            int n = k / 15;
            for (page = 2; page <= n; page++) {
               rootNode = cleaner.clean(new URL(String.format(ADDRESS_SEARCH, DOMAIN, city, page, _u(street), _u(house))));
               fetchCompanies(result);
            }
         }
      }
      return result;
   }

   public List<Company> searchCompaniesByName(int city, String name) throws IOException {
      int page = 1;
      List<Company> result = new ArrayList<Company>();
      String url = String.format(NAME_SEARCH, DOMAIN, city, page, _u(name));
      String html = UrlUtils.readURL(new URL(url));
      rootNode = cleaner.clean(html);
      int cnt = fetchCompanies(result);
      if (cnt >= 15) {
         Matcher m = RES_RE.matcher(html);
         if (m.find()) {
            int k = Integer.valueOf(m.group(1));
            int n = k / 15;
            for (page = 2; page <= n; page++) {
               rootNode = cleaner.clean(new URL(String.format(NAME_SEARCH, DOMAIN, city, page, _u(name))));
               fetchCompanies(result);
            }
         }
      }
      return result;
   }

   ///kz/ru/activity_search/30/?%C1%C0%CD%CA/
   public List<Category> searchCategoryByName(int city, String name) throws IOException {
      List<Category> result = new ArrayList<Category>();
      try {
         String urlAddr = String.format(CAT_NAME_SEARCH, DOMAIN, city, _u(name));
         rootNode = cleaner.clean(new URL(urlAddr));
         fetchCategories(city, result);
      } catch (UnsupportedCharsetException e) {
         e.printStackTrace();
      }
      return result;
   }

   public static void main(String[] args) throws IOException {
//      Properties props = System.getProperties();
//      props.put("http.proxyHost", "192.168.1.88");
//      props.put("http.proxyPort", "3128");
      ParseUtils pu = new ParseUtils();

      for (Category c : pu.searchCategoryByName(30, "БАНК")) {
         System.out.println(c.name);
      }
   }
}
