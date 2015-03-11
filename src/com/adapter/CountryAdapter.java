package com.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.country.R;
import com.model.Country;

public class CountryAdapter extends ArrayAdapter<Country> {
	private ArrayList<Country> actorList;
	private LayoutInflater vi;
	private int Resource;
	private ViewHolder holder;

	public CountryAdapter(Context context, int resource,
			ArrayList<Country> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		actorList = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.imageview = (ImageView) v.findViewById(R.id.iv_flag);
			holder.tvName = (TextView) v.findViewById(R.id.tv_title);
			holder.tvDescription = (TextView) v
					.findViewById(R.id.tv_description);
			holder.imageview.setTag(actorList.get(position).getImage());

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		if (actorList.get(position).getImage() != null) {
			new DownloadImageTask(holder).execute();
		}
		if (actorList.get(position).getName() != null) {
			holder.tvName.setText(actorList.get(position).getName());
		}
		if (actorList.get(position).getDescription() != null) {
			holder.tvDescription.setText(actorList.get(position).getDescription());
		}

		return v;

	}

	static class ViewHolder {
		public ImageView imageview;
		public TextView tvName;
		public TextView tvDescription;

	}

	// Download Image from url using AsyncTask
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ViewHolder bmImage;

		public DownloadImageTask(ViewHolder bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {

			String urldisplay = bmImage.imageview.getTag().toString();
			Bitmap mIcon = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon = BitmapFactory.decodeStream(in);

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return mIcon;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.imageview.setImageBitmap(result);

		}

	}
}