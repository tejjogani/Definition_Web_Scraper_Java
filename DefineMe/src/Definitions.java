import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;


public class Definitions{
    public static void main(String[] args) {
        try {
            Utilities k =  new Utilities(); //create an object of utilities to use the methods and fill list
            k.fillList("InputFiles/words.txt");
            k.fillDef();
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(ToPDF.getFILE()));
            document.open();
            ToPDF.addContent(document, k);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}