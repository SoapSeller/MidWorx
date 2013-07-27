package com.fun.midworx;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DictionaryActivity extends Activity {
    private static final Logger logger = Logger.getLogger(DictionaryActivity.class.getName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        final String word = getIntent().getExtras().getString("word");
        new DefinitionGetter(word, this).execute();
    }

    static class HeadWord {
        final String text;
        final String phonetic;
        final Uri sound;
        final String label;
        final List<Pair<String, List<String>>> meanings = new ArrayList<Pair<String, List<String>>>();

        public HeadWord(JSONObject headword, String word) throws JSONException {
            String text = word;
            String phonetic = "/" + word + "/";
            String sound = null;
            String label = "";
            JSONArray terms = headword.getJSONArray("terms");
            for (int i = 0; i < terms.length(); ++i) {
                JSONObject term = terms.getJSONObject(i);
                if (term.getString("type").equals("text")) {
                    text = term.getString("text");
                    JSONArray labels = term.getJSONArray("labels");
                    label = labels.getJSONObject(0).getString("text");
                } else if (term.getString("type").equals("phonetic")) {
                    phonetic = term.getString("text");
                } else if (term.getString("type").equals("sound")) {
                    sound = term.getString("text");
                }
            }
            JSONArray entries = headword.getJSONArray("entries");
            for (int i = 0; i < entries.length(); ++i) {
                JSONObject entry = entries.getJSONObject(i);
                if (!entry.getString("type").equals("meaning")) {
                    continue;
                }
                JSONObject term = entry.getJSONArray("terms").getJSONObject(0);
                List<String> examples = new ArrayList<String>();
                if (entry.has("entries")) {
                    JSONArray exampleEntries = entry.getJSONArray("entries");
                    for (int j = 0; j < exampleEntries.length(); ++j) {
                        JSONObject example = exampleEntries.getJSONObject(j);
                        if (!example.getString("type").equals("example")) {
                            continue;
                        }
                        examples.add(example.getJSONArray("terms").getJSONObject(0).getString("text"));
                    }
                }
                meanings.add(Pair.create(term.getString("text"), examples));
            }

            this.text = text;
            this.phonetic = phonetic;
            this.sound = Uri.parse(sound);
            this.label = label;
        }
    }

    class DefinitionGetter extends AsyncTask<Void, Void, JSONObject> {
        private static final String GOOGLE_DICT_URL =
                "http://www.google.com/dictionary/json?callback=x&q=%s&sl=en&tl=en";

        private final String word;
        private final Context context;

        private TextView wordText;
        private TextView phoneticText;
        private ImageButton pronounceButton;
        private TextView definitionText;

        DefinitionGetter(String word, Context context) {
            this.word = word;
            this.context = context;

            this.wordText = (TextView) findViewById(R.id.wordText);
            this.phoneticText = (TextView) findViewById(R.id.phoneticText);
            this.pronounceButton = (ImageButton) findViewById(R.id.pronounceButton);
            this.definitionText = (TextView) findViewById(R.id.definitionText);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                URL url = new URL(GOOGLE_DICT_URL.replace("%s", word));
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder jsonpBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    jsonpBuilder.append(line);
                }
                br.close();
                String jsonp = jsonpBuilder.toString();
                String json = jsonp.substring(2, jsonp.length()-10);
                return new JSONObject(json);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json == null) {
                // TODO(misha): display no definition found.
                return;
            }

            try {
                JSONArray primaries = json.getJSONArray("primaries");
                final List<HeadWord> headwords = new ArrayList<HeadWord>();

                for (int i = 0; i < primaries.length(); ++i) {
                    JSONObject object = primaries.getJSONObject(i);
                    if (object.getString("type").equals("headword")) {
                        headwords.add(new HeadWord(object, this.word));
                    }
                }

                wordText.setText(headwords.get(0).text);
                phoneticText.setText(headwords.get(0).phonetic);
                pronounceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MediaPlayer.create(context, headwords.get(0).sound).start();
                    }
                });

                String definitions = "";
                if (headwords.size() == 1) {
                    HeadWord headword = headwords.get(0);
                    definitions = "<font color='grey'>" + headword.label + "</font>" + "<br><ol>";
                    for (int i = 0; i < headword.meanings.size(); ++i) {
                        if (i > 1) {
                            break;
                        }
                        Pair<String, List<String>> meaning = headword.meanings.get(i);
                        definitions += "<li>" + meaning.first + ": \"" + meaning.second.get(0) + "\"</li>";
                    }
                    definitions += "</ol>";
                } else {
                    for (HeadWord headword : headwords) {
                        definitions += "<font color='grey'>" + headword.label + "</font>" + "<br>";
                        Pair<String, List<String>> meaning = headword.meanings.get(0);
                        definitions += meaning.first + ": \"" + meaning.second.get(0) + "\"<br><br>";
                    }
                }
                definitions = definitions
                        .replaceAll("x3c", "<").replaceAll("x3e", ">")
                        .replaceAll("x27", "'");
                definitionText.setText(Html.fromHtml(definitions));
            } catch (JSONException e) {
                e.printStackTrace();
            };
        }
    }
}
