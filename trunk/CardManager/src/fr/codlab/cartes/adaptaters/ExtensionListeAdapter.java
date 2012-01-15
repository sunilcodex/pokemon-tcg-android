package fr.codlab.cartes.adaptaters;


import fr.codlab.cartes.IExtensionListener;
import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.util.Rarity;
import fr.codlab.cartes.views.CardImage;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;

public class ExtensionListeAdapter extends BaseAdapter {
	private Context _context;
	private IExtensionListener _principal;
	private Extension _item;
	public CardImage _vueClic=null;
	public View v2=null;
	private SharedPreferences _shared = null;
	private int _mode;

	public ExtensionListeAdapter(IExtensionListener a, Context context, Extension item) {
		_principal=a;
		_context=context;
		_item=item;
		_shared = context.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
		_mode = _shared.getInt(MainActivity.USE, MainActivity.FR);
	}

	private Bundle createBundle(int _pos,boolean imgVue){
		Bundle objetbundle = new Bundle();
		//objetbundle.putInt("nb", _item.getCarte(_pos).getNb());
		objetbundle.putSerializable("card", _item.getCarte(_pos));
		if(imgVue)
			objetbundle.putInt("next", 1);
		objetbundle.putInt("extension", _item.getId());
		objetbundle.putString("intitule", _item.getShortName());

		return objetbundle;
	}

	public View getView(final int pos, View inView, ViewGroup parent) {
		if(inView == null){
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inView = inflater.inflate(R.layout.image_list, null);
		}
		final View v = inView;
		try{
			CardImage iv = (CardImage) v.findViewById(R.id.image);
			Bitmap _bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_item.getShortName()+"_"+_item.getCarte(pos).getCarteId()+( _mode == MainActivity.FR ? "" : "_us")+".jpg");
			if(_bmp != null)
				iv.setImageBitmap(_bmp);
			else
				iv.setImageResource(R.drawable.back);
		}
		catch(Exception e)
		{
		}

		try{
			if(v.getResources().getIdentifier(_item.getCarte(pos).getDrawableRarete() , "drawable", _context.getPackageName())>0){
				String [] _types = _item.getCarte(pos).getTypes();
				LinearLayout _vue_types = (LinearLayout)v.findViewById(R.id.vue_types);
				_vue_types.removeAllViews();


				try{
					if(v.getResources().getIdentifier(_item.getCarte(pos).getDrawableRarete() , "drawable", _context.getPackageName())>0){
						ImageView iv = new ImageView(_context);
						iv.setImageResource(v.getResources().getIdentifier(_item.getCarte(pos).getDrawableRarete() , "drawable", _context.getPackageName()));
						_vue_types.addView(iv);
					}
				}
				catch(Exception e)
				{
				}

				for(int i=0;i<_types.length;i++){
					ImageView _type = new ImageView(_context);
					_type.setImageResource(v.getResources().getIdentifier("type_"+_types[i] , "drawable", _context.getPackageName()));
					_vue_types.addView(_type);
				}
			}
		}
		catch(Exception e)
		{
		}
		final int position=pos;
		TextView bTitle = (TextView) v.findViewById(R.id.carte);
		bTitle.setText(_item.getCarte(pos).getNom());

		TextView bInfos = (TextView) v.findViewById(R.id.infos);
		bInfos.setText(_item.getCarte(pos).getInfos());

		final View sav=v;

		ImageView moins;
		ImageView plus;
		TextView editQuantite;

		moins = (ImageView)v.findViewById(R.id.carte_sub);
		plus = (ImageView)v.findViewById(R.id.carte_add);
		editQuantite=(TextView)v.findViewById(R.id.carte_possedees);

		if(_item.getCarte(position).getIsNormal()){
			editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantite(_context, Rarity.NORMAL)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, -1, Rarity.NORMAL);
					TextView editQuantite=(TextView)sav.findViewById(R.id.carte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.NORMAL)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, 1, Rarity.NORMAL);
					TextView editQuantite=(TextView)sav.findViewById(R.id.carte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.NORMAL)));
					updateAll();
				}
			});

		}else{
			moins.setVisibility(View.GONE);
			plus.setVisibility(View.GONE);
			editQuantite.setVisibility(View.GONE);
		}

		moins = (ImageView)v.findViewById(R.id.reversecarte_sub);
		plus = (ImageView)v.findViewById(R.id.reversecarte_add);
		editQuantite=(TextView)v.findViewById(R.id.reversecarte_possedees);

		if(_item.getCarte(position).getIsReverse()){
			editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantite(_context, Rarity.REVERSE)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, -1, Rarity.REVERSE);
					TextView editQuantite=(TextView)sav.findViewById(R.id.reversecarte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.REVERSE)));
					updateAll();
				}
			});
			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, 1, Rarity.REVERSE);
					TextView editQuantite=(TextView)sav.findViewById(R.id.reversecarte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.REVERSE)));
					updateAll();
				}
			});

		}else{
			moins.setVisibility(View.GONE);
			plus.setVisibility(View.GONE);
			editQuantite.setVisibility(View.GONE);
		}

		moins = (ImageView)v.findViewById(R.id.holocarte_sub);
		plus = (ImageView)v.findViewById(R.id.holocarte_add);
		editQuantite=(TextView)v.findViewById(R.id.holocarte_possedees);
		if(_item.getCarte(position).getIsHolo()){
			editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantite(_context, Rarity.HOLO)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, -1, Rarity.HOLO);
					TextView editQuantite=(TextView)sav.findViewById(R.id.holocarte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.HOLO)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, 1, Rarity.HOLO);
					TextView editQuantite=(TextView)sav.findViewById(R.id.holocarte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.HOLO)));
					updateAll();
				}
			});

		}else{
			moins.setVisibility(View.GONE);
			plus.setVisibility(View.GONE);
			editQuantite.setVisibility(View.GONE);
		}
		final int _pos=pos;

		CardImage iv = (CardImage) v.findViewById(R.id.image);
		if(iv!=null)
			iv.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {

					_principal.onClick(createBundle(_pos,true));
				}

			});
		v.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				_principal.onClick(createBundle(_pos,false));
			}	
		});


		return(v);
	}

	public void updateAll() {
		_item.updatePossessed();
		_principal.updateProgress(_item.getProgress(),_item.getCount());
		_principal.updatePossessed(_item.getPossessed());
		_principal.updated(_item.getId());
	}

	@Override
	public int getCount() {
		return _item.getCount();
	}

	@Override
	public Object getItem(int pos) {
		return _item.getCarte(pos);
	}

	@Override
	public long getItemId(int pos) {
		return _item.getCarte(pos).getId();
	}
}
