package easybackup.place.ispl;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;

import static easybackup.place.ispl.utils.EasyBackUpUtils.rootPath;

public class WebViewActivity extends Activity {
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.setVisibility(View.VISIBLE);

        List<ContactDetailsModel> selectedContatList = new ArrayList<ContactDetailsModel>();
        selectedContatList = null;
        selectedContatList = EasyBackUpUtils.getContactDetailsList();

        String htmlDocument =
                "<html><body><h1>My Contacts</h1><br><br>" +
                        "<table border=1>" +
                        "<tr>" +
                        "<th>Iamge</th>" +
                        "<th>Name</th>" +
                        "<th>Mobile</th>" +
                        "<th>Home</th>" +
                        "<th>Work</th>" +
                        "<th>Email Home</th>" +
                        "<th>Email Work</th>" +
                        "<th>Job Title</th>" +
                        "<th>Birthdate</th>" +
                        "</tr>";

        for (ContactDetailsModel model : selectedContatList) {
          //  EasyBackUpUtils.v("PdfName : " + model.getContactName());
            htmlDocument += "<tr>" +
                    "<td>" + model.getContactImage() + "</td>" +
                    "<td>" + model.getContactName() + "</td>" +
                    "<td>" + model.getContactMobileNumber() + "</td>" +
                    "<td>" + model.getContactHomeNumber() + "</td>" +
                    "<td>" + model.getContactWorkNumber() + "</td>" +
                    "<td>" + model.getContactEmailHome() + "</td>" +
                    "<td>" + model.getContactEmailWork() + "</td>" +
                    "<td>" + model.getContactTitle() + "</td>" +
                    "<td>" + model.getContactBirthdate() + "</td>" +
                    "</tr>";
            EasyBackUpUtils.v("Show Contact" + htmlDocument);
        }
        htmlDocument += "</table></body></html>";

        try {
            Document document = new Document(PageSize.LETTER);
            final String vfile = "MyContacts.pdf";
            String path = rootPath + File.separator + vfile;
            PdfWriter.getInstance(document, new FileOutputStream(path,true));
            document.open();
//            document.addAuthor("Real Gagnon");
//            document.addCreator("Real's HowTo");
//            document.addSubject("Thanks for your support");
//            document.addCreationDate();
//            document.addTitle("Please read this");

            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(htmlDocument));
            document.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        webView.loadDataWithBaseURL("", htmlDocument, "text/html", "UTF-8", "");
    }
}
