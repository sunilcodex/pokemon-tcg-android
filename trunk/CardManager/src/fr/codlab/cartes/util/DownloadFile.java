package fr.codlab.cartes.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


import android.os.AsyncTask;
import android.util.Log;

class DownloadFile extends AsyncTask<String, Double, Long>{
	private URL _url;
	private long total;
	private int phase = 0;
	private String _tmp;
	private final static String _location = "/sdcard/"; 
	private IDownloadFile _listener;
	
	private DownloadFile(){
		super();
	}
	
	DownloadFile(IDownloadFile listener, String tmp){
		super();
		_tmp = tmp;
		_listener = listener;
	}
	
	//@Override
	protected Long doInBackground(String... url) {
		int count;
		try {
			phase = 0;
			_url = new URL(url[0]);
			URLConnection conexion = _url.openConnection();
			conexion.connect();
			// this will be useful so that you can show a tipical 0-100% progress bar
			int lenghtOfFile = conexion.getContentLength();

			// downlod the file
			InputStream input = new BufferedInputStream(_url.openStream());
			OutputStream output = new FileOutputStream("/sdcard/card_images.zip");

			byte data[] = new byte[1024];

			total= 0;

			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress(((int)((total*50./lenghtOfFile)*1000))*1./1000);
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();

			String _zipfile = "/sdcard/card_images.zip";

			ZipFile zipInFile = null;
			total = 0;
			total += count;

			phase = 1;

			try
			{
				zipInFile = new ZipFile(_zipfile);
				lenghtOfFile = zipInFile.size();
				for (Enumeration<? extends ZipEntry> entries = zipInFile.entries(); entries.hasMoreElements();)
				{
					ZipEntry zipMediaEntry = entries.nextElement();
					Log.d("-",zipMediaEntry.getName());
					if (zipMediaEntry.isDirectory())
					{
						File mediaDir = new File(_location+zipMediaEntry.getName());
						mediaDir.mkdirs();
					}
					else
					{
						BufferedInputStream bisMediaFile = null;
						FileOutputStream fosMediaFile = null; 
						BufferedOutputStream bosMediaFile = null;
						try
						{
							String strFileName = _location+zipMediaEntry.getName();
							File uncompressDir = new File(strFileName).getParentFile();
							uncompressDir.mkdirs();

							bisMediaFile = new BufferedInputStream(zipInFile.getInputStream(zipMediaEntry));
							fosMediaFile = new FileOutputStream(strFileName);
							bosMediaFile = new BufferedOutputStream(fosMediaFile);

							int counter;
							byte _data[] = new byte[2048];

							while ((counter = bisMediaFile.read(_data, 0, 2048)) != -1) 
							{
								bosMediaFile.write(_data, 0, counter);
							}
						}
						catch (Exception ex)
						{
							throw ex;
						}
						finally
						{
							if (bosMediaFile != null)
							{
								bosMediaFile.flush();
								bosMediaFile.close();
							}
							if (bisMediaFile != null)
								bisMediaFile.close();
						}
					}
					publishProgress((50000 + (int)(((total*50.)/lenghtOfFile)*1000))*1./1000);
					total++;
				}
			}
			catch (Exception ex)
			{
				throw ex;
			}
			finally
			{
				if (zipInFile != null)
					zipInFile.close();
				File flZipToDelete = new File(_zipfile);
				if(flZipToDelete.exists())
					flZipToDelete.delete();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}

	public void onProgressUpdate(Double... args){
		_listener.receiveProgress((phase == 0) ? "% phase 1/2 (50%)" : "% phase 2/2 (50%)", args[0]);
	}

	protected void onPostExecute(Long result) {
		_listener.onPost(result);
	}
}
