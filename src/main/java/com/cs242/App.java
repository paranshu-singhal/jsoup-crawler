package com.cs242;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.json.simple.JSONObject;

import org.json.JSONObject;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */

    static List<String> frontier = new ArrayList<>();
    static List<Integer> hashcodes = new ArrayList<>();
    static PrintStream o;
    static File file;

    static JSONObject json = new JSONObject();

    public static void main(String[] args) {
        System.out.println("Hello World!");

        String seed = "https://www.ucr.edu/";
        frontier.add(seed);
        hashcodes.add(seed.hashCode());

        try{
            file = new File("/Users/paranshusinghal/Desktop/data2.txt");
            o = new PrintStream(file);
            crawl();
            //for(int i=0; i<50; i++){
            } catch(Exception e){
                e.printStackTrace();
            }  
            finally {
                o.close();
            }
        }
        
    public static void crawl(){
    
        try{
            while(file.length() < 20971520 && !frontier.isEmpty()){  //20MB

                String href = frontier.get(0);
                System.out.println(href);

                JSONObject j = new JSONObject();
                j.accumulate("URL", href);
                frontier.remove(0);
                //hashcodes.remove(0);

                Document doc = Jsoup.connect(href).get();

                j.accumulate("TITLE", doc.title());
                j.accumulate("TEXT", doc.text());
                json.append("ARRAY", j);
                o.println(j.toString());

                Elements links = doc.select("a[href]");
                for (Element headline : links) {
                    String link = headline.attr("href");
                    if(link.length()>4 && link.substring(0, 4).compareTo("http")==0){
                        if(!hashcodes.contains(link.hashCode())){
                            frontier.add(link);
                            hashcodes.add(link.hashCode());
                        }
                        
                    }
                }
            }
        
        } catch(Exception e){
            frontier.remove(0);
            hashcodes.remove(0);
            crawl();
            e.printStackTrace();
        }  
        finally {
            o.close();
        }
    }
}

