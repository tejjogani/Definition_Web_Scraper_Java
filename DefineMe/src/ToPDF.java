
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Iterator;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;


public class ToPDF {

    private static String FILE = "output/output.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);

    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12,
            Font.NORMAL);


    public static String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void addContent(Document document, Utilities u) throws DocumentException {
        Anchor anchor = new Anchor("Definitions produced", catFont);
        anchor.setName("Definitions");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

//add the date
        catPart.addSection("Date: " + getDate() + "\n");

//make a sub titles
        Paragraph subPara = new Paragraph("Definitions", subFont);
        Section subCatPart = catPart.addSection(subPara);

        addEmptyLine(subPara, 1);

        createList(subCatPart,u.getWords(), u.getWordsDef());

        document.add(catPart);

    }

    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private static void createList(Section subCatPart, java.util.List words, String[] wordsDef) {
        List list = new List(true, false, 10);

        int count = 0;
        Iterator<String> iter = words.iterator();
        while(iter.hasNext()){

            list.add(new ListItem(" "+ capitalize(iter.next())  + "- " + capitalize(wordsDef[count] + "\n\n"),smallBold));
            count++;
        }

        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public static String getFILE() {
        return FILE;
    }
}


