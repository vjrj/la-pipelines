package au.org.ala.pipelines.vocabulary;

import au.org.ala.pipelines.util.Stemmer;
import lombok.extern.slf4j.Slf4j;
import org.gbif.kvs.geocode.LatLng;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Vocab {

    private static Vocab vocab;
    private Map<String,String[]> terms = new HashMap<>();

    public static Vocab loadVocabFromFile(String filePath){
        if(vocab == null){
            try {
                vocab = new Vocab();
                Stemmer stemmer = new Stemmer();
                Files.lines(new File(filePath).getAbsoluteFile().toPath())
                        .map(s -> s.trim())
                        .forEach(l -> {
                            String[] ss = l.split("\t");
                            String key = stemmer.stem(ss[0].toLowerCase());

                            vocab.terms.put(key, Arrays.copyOfRange(ss, 1, ss.length-1));

                        });
                log.info(vocab.terms.size() +" vocabs/records have been loaded.");

            }catch(Exception e){
                log.warn(e.getMessage());
            }
        }
        return vocab;
    }

    public KeyValuePair<String, String[]> matchTerm(String string2match){
        Stemmer stemmer = new Stemmer();
        String stringToUse = stemmer.stem(string2match.toLowerCase());
        String[] result = terms.get(stringToUse);
        if (result != null)
            return new KeyValuePair<>(string2match, result);
        else
            return null;
    }

    public boolean matched(String string2Match){
        if (matchTerm(string2Match) == null)
            return false;
        else
            return true;
    }

  /*  def matchTerm(string2Match:String) : Option[Term] = {
        if(string2Match != null){
            //strip whitespace & strip quotes and fullstops & uppercase
            val stringToUse = string2Match.replaceAll(regexNorm, "").toLowerCase
            val stemmed = Stemmer.stem(stringToUse)

            //println("string to use: " + stringToUse)
            all.foreach(term => {
            //println("matching to term " + term.canonical)
            if(term.canonical.equalsIgnoreCase(stringToUse))
                return Some(term)
            if(term.variants.contains(stringToUse) || term.variants.contains(stemmed)){
                return Some(term)
            }
      })
        }
        None
    }*/




}