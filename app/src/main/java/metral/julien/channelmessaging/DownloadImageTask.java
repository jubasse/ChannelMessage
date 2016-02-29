package metral.julien.channelmessaging;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import metral.julien.channelmessaging.utils.ImageRounder;

/**
 * Created by Julien on 29/02/2016.
 */
public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
    private String url;
    private String fileName;
    private ImageView imageView;
    private Context context;

    public DownloadImageTask(String fileName,String url, ImageView imageView,Context context) {
        this.url = url;
        this.fileName = fileName;
        this.imageView = imageView;
        this.context = context;
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            saveToInternalStorage(myBitmap, this.fileName);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String fileName) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("ChannelMessaging", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, fileName + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }


    @Override
    protected Bitmap doInBackground(Void... params) {
        return getBitmapFromURL(url);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Bitmap rounded = ImageRounder.getRoundedCornerBitmap(result,50);
                imageView.setImageBitmap(rounded);
    }
}
