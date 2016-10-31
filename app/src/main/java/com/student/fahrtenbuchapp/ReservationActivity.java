package com.student.fahrtenbuchapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.student.fahrtenbuchapp.com.student.fahrtenbuchapp.models.Model;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {

    private Session session;
    private ListView listViewMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        /*
        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }



        listViewMovies = (ListView) findViewById(R.id.lvMovies);

        new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt"); */

    }

    /*  public void logout() {
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(ReservationActivity.this, LoginActivity.class));
    }


    class JSONTask extends AsyncTask<String , String, List<Model>> {

        @Override
        protected List<Model> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                String finalJSONText = stringBuffer.toString();
                JSONObject parentObject = new JSONObject(finalJSONText);
                JSONArray parentArray = parentObject.getJSONArray("movies");

                List<Model> movieModelList = new ArrayList<>();
                for(int i=0; i<parentArray.length(); i++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Model movieModel = new Model();
                    movieModel.setMovie(finalObject.getString("movie"));
                    movieModel.setYear(finalObject.getInt("year"));
                    movieModel.setRating((float) finalObject.getDouble("rating"));
                    movieModel.setDirector(finalObject.getString("director"));
                    movieModel.setDuration(finalObject.getString("duration"));
                    movieModel.setTagline(finalObject.getString("tagline"));
                    movieModel.setImage(finalObject.getString("image"));
                    movieModel.setStory(finalObject.getString("story"));

                    List<Model.Cast> castList = new ArrayList<>();
                    for(int j=0; j<finalObject.getJSONArray("cast").length(); j++)
                    {
                        Model.Cast cast = new Model.Cast();
                        cast.setName(finalObject.getJSONArray("cast").getJSONObject(j).getString("name"));
                        castList.add(cast);
                    }

                    movieModel.setCastList(castList);
                    movieModelList.add(movieModel);
                }
                return movieModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
                try {
                    if(bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Model> result) {
            super.onPostExecute(result);

            MovieAdapter movieAdapter = new MovieAdapter(getApplicationContext(), R.layout.row, result);
            listViewMovies.setAdapter(movieAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            int end = spanString.length();
            spanString.setSpan(new RelativeSizeSpan(1.5f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(spanString);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.refresh:
                new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");

            case R.id.logout:
                logout();
                return true;

            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                builder.setTitle("Exit App?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    public class MovieAdapter extends ArrayAdapter {

        private List<Model> movieModelList;
        private int resource;
        private LayoutInflater inflater;

        public MovieAdapter(Context context, int resource, List<Model> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.row, null);
            }

            ImageView imageViewMovieIcon;
            TextView tvMovie;
            TextView tvTagline;
            TextView tvYear;
            TextView tvDuration;
            TextView tvDirector;
            TextView tvCast;
            TextView tvStory;
            RatingBar ratingBarMovie;

            imageViewMovieIcon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            tvMovie = (TextView) convertView.findViewById(R.id.textViewMovie);
            tvTagline = (TextView) convertView.findViewById(R.id.textViewTagline);
            tvYear = (TextView) convertView.findViewById(R.id.textViewYear);
            tvDuration = (TextView) convertView.findViewById(R.id.textViewDuration);
            tvDirector = (TextView) convertView.findViewById(R.id.textViewDirector);
            tvCast = (TextView) convertView.findViewById(R.id.textViewCast);
            tvStory = (TextView) convertView.findViewById(R.id.textViewStory);
            ratingBarMovie = (RatingBar) convertView.findViewById(R.id.ratingBar);

            tvMovie.setText(movieModelList.get(position).getMovie());
            tvTagline.setText(movieModelList.get(position).getTagline());
            tvYear.setText("Year: " + movieModelList.get(position).getYear());
            tvDuration.setText("Duration: " + movieModelList.get(position).getDuration());
            tvDirector.setText("Director: " + movieModelList.get(position).getDirector());
            tvStory.setText(movieModelList.get(position).getStory());

            StringBuffer stringBuffer = new StringBuffer();
            for (Model.Cast cast : movieModelList.get(position).getCastList()) {

                stringBuffer.append(cast.getName() + ", ");
            }

            tvCast.setText(stringBuffer);

            ratingBarMovie.setRating(movieModelList.get(position).getRating()/2);

            return convertView;
        }
    }

    */
}


