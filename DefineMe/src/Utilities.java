import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

//THIS IS A CLASS WITH ALL THE BASIC METHODS NEEDED TO EXTRACT INFO

public class Utilities {

    //VARIABLE INIT:
    private List<String> words = new ArrayList<String>();
    private String[] wordsDef;
    //..................................

    //Decapitalise a word
    public String decapitalize(final String line) {
        return Character.toLowerCase(line.charAt(0)) + line.substring(1);
    }
    //Make the first letter of a word capital
    public String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    //get the URL for a given word
    public static String getURL(String word){
        String urlToReturn = "https://www.google.co.in/search?q=define+" + word + "#dobs=" + word;
        return  urlToReturn;
    }
    //If the google definition is not found, this will search google without the definition link
    public String getGoogleURLForWiki(String word){
        String url = "https://www.google.co.in/search?q=" + word;
        return url;
    }
    //Get an HTML String for a given website url
    public static String parse(String url) throws IOException{
        Document doc = Jsoup.connect(url).get();
        //Elements divs = doc.select("#uid_JCdjW7_JPM_prQH7i774Bg_0");
        return doc.toString();
    }
    //Get the definition from google
    public static String getDefGoogle(String html){
        String def = "";
        int index = html.indexOf("dfn");
        if(index == -1){ //when the word doesnt exist,i.e. google deosnt have the definition, the index is -1, so this is error checking
            return "FAIL";
        }else {

            //isolate the definition
            html = html.substring(index);
            int newIndex = html.indexOf("<span>");
            int endIndex = html.indexOf("</span>");
            def = html.substring(newIndex + 6, endIndex) + "\n";
            return def;

        }


    }
    //What type of word is this? Noun, adj, etc...
    public  String getType(String html){
        int index = html.indexOf("<div class=\"lr_dct_sf_h\"");
        if(index == -1){
            return "";
        }else {
            html = html.substring(index);
            int endIndex = html.indexOf("</span>");
            if(endIndex == -1){
                return "";
            }else {
                html = html.substring(0, endIndex);
                Document doc = Jsoup.parse(html);
                html = doc.text();
                return html;
            }

        }


    }

    //The wikepeida URl for the topic closest to the search word
    public String getWikiURL(String html){
        int index = html.indexOf("<a href=\"https://en.wikipedia.org/wiki/");
        if(index == -1){
            return "No info found";
        }
        html = html.substring(index + 9);
        int endIndex = html.indexOf("\"");
        html = html.substring(0,endIndex);
        return html;
    }

    //get the main html part from wikepedia
    public String getWikiInfo(String url, String word) throws IOException{

        if(url.equals("No info found")){
            return "FAIL";
        }else {

            Document doc = Jsoup.connect(url).get();

            String html = doc.toString();
            int index = html.indexOf("<b>" + word.substring(0,3));
            if (index == -1){
                index = html.indexOf("\"<b>" + word.substring(0,3));
                if (index == -1){
                    index = html.indexOf("<b>" + decapitalize(word.substring(0,3)));
                    if(index == -1) {
                        return "FAIL";
                    }
                }
            }
            html = html.substring(index);
            int end = html.indexOf("</p>");
            html = html.substring(0, end);
            return html;
        }

    }
    //Get rid of any links, etc. and only keep the text
    public String formatWiki(String htmlContent){
        if(htmlContent.equals("FAIL")){
            return "FAIL";
        }else {
            Document doc = Jsoup.parse(htmlContent);
            htmlContent = doc.text();
            return htmlContent;
        }

    }

    //INPUT WORDS IN CONSOLE FOR DEBUGGING
    public void enterWords(){
        String input = "Y";
        String k;
        int count = 1;
        Scanner f =  new Scanner(System.in);
        while(input.startsWith("Y")){
            System.out.println("Input word number " + count + ": ");
            k = f.nextLine();
            k = capitalize(k);
            words.add(k);
            System.out.println("Are there more words?");
            input = f.nextLine().replaceAll("\\s+","").toUpperCase();
            count++;
        }
    }

    public void fillList(String FILENAME){

           BufferedReader br = null;
            FileReader fr = null;
            try {
                //br = new BufferedReader(new FileReader(FILENAME));
                fr = new FileReader(FILENAME);
                br = new BufferedReader(fr);
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    words.add(sCurrentLine);
                    //System.out.println(sCurrentLine); FOR DEBUGGING
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null)
                        br.close();
                    if (fr != null)
                        fr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }




    //Fill another array, with the size of a list, as the definitions.
    public void fillDef() throws IOException {
        wordsDef = new String[words.size()];
        int i = 0;
        Iterator<String> iter = words.iterator();

        while (iter.hasNext()) {
                String wordToProcess = iter.next();

                wordsDef[i] = getType(parse(getURL(decapitalize(wordToProcess)))) + ": " + getDefGoogle(parse(getURL(decapitalize(wordToProcess)))) ;
                if(wordsDef[i].equals(": FAIL")){
                    wordsDef[i] = formatWiki(getWikiInfo(getWikiURL(parse(getGoogleURLForWiki(wordToProcess.replaceAll("\\s+","")))), wordToProcess));
                    if (wordsDef[i].equals("FAIL")){
                        wordsDef[i] =  "NOT FOUND!";
                    }

                }
                i += 1;
                }

            }


    //FOR DEBUGGING
    public void printDef(){
        int count = 0;
        Iterator<String> iter = words.iterator();
        while(iter.hasNext()){

            System.out.println(iter.next() + "      " + wordsDef[count]);
            count++;
        }
    }
    //Accessor method
    public List<String> getWords() {
        return words;
    }
    //Accessor method
    public String[] getWordsDef() {
        return wordsDef;
    }
}
