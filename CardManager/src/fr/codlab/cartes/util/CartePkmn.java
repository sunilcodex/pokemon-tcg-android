package fr.codlab.cartes.util;

import java.io.Serializable;
import java.util.ArrayList;

import fr.codlab.cartes.attributes.Attaque;
import fr.codlab.cartes.attributes.PokeBody;
import fr.codlab.cartes.attributes.PokePower;
import fr.codlab.cartes.bdd.SGBD;
import android.content.Context;

public class CartePkmn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5371807270118129999L;
	private int _extension;
	private int _idImage;
	private boolean _visible;
	private String _nom=null;
	private String _nomPkmn=null;
	private String _rarete=null;
	private int _carteId=0;
	private String _specialId=null;
	private int _pv;
	private int _quantite_normal;
	private int _quantite_reverse;
	private int _quantite_holo;
	private int _id;
	private String [] _type;
	private ArrayList<Attaque> _attaques;
	private PokePower _pokepower;
	private PokeBody _pokebody;
	private int _retraite;
	private String _faiblesses;
	private String _resistances;
	private String _numero;
	private String _description;
	private boolean _isHolo;
	private boolean _isReverse;
	
	public CartePkmn(int extension){

		_faiblesses="";
		_resistances="";
		_extension=extension;
		_description="";
		_pv=0;
		_quantite_normal=-1;
		_quantite_reverse=-1;
		_quantite_holo=-1;
		_visible=true;
		_retraite=0;
		_rarete="aucune";
		_type = new String[1];
		_type[0]="type";
		_numero="";
		_attaques = new ArrayList<Attaque>();
		_isHolo = false;
		_isReverse = false;
	}
	
	public void setVisible(boolean b){
		_visible=b;
	}
	public boolean getVisible(){
		return _visible;
	}
	
	public void setRetraite(int i){
		_retraite = i;
	}
	public void addRetraite(int i){
		_retraite+= i;
	}
	public int getRetraite(){
		return _retraite;
	}
	public String getDescription(){
		return _description;
	}
	public void setDescription(String description){
		_description=description;
	}
	public void setPokeBody(PokeBody pokebody){
		_pokebody=pokebody;
	}
	public PokeBody getPokeBody(){
		return _pokebody;
	}
	public void setPokePower(PokePower pokepower){
		_pokepower=pokepower;
	}
	public PokePower getPokePower(){
		return _pokepower;
	}
	
	public void addAttaque(Attaque at){
		_attaques.add(at);
	}
	public int getNbAttaques(){
		return _attaques.size();
	}
	public Attaque getAttaque(int i){
		return _attaques.get(i);
	}
	public void setId(int id){
		_id=id;
	}
	public int getId(){
		return _id;
	}
	
	public void setPV(int pv){
		_pv=pv;
	}
	public int getPV(){
		return _pv;
	}
	
	public void setNumero(String numero){
		_numero=numero;
	}
	public String getNumero(){
		return (_numero==null || _numero.length()==0)?"#"+Integer.toString(_carteId):_numero;
	}
	
	public void setType(String t){
		if(t.split(",").length>0){
			_type = null;
			_type = t.split(",");
		}
	}
	public void addFaiblesse(String t){
		_faiblesses=((_faiblesses.length()>0)?",":"")+
			t;
	}
	public void addResistance(String t){
		_resistances=((_resistances.length()>0)?",":"")+
			t;
	}
	
	public String [] getResistances(){
		return _resistances.split(",");
	}
	public String getResistance(){
		return _resistances;
	}
	public String [] getFaiblesses(){
		return _faiblesses.split(",");
	}
	public String getFaiblesse(){
		return _faiblesses;
	}
	public String [] getTypes(){
		return _type;
	}
	
	public void setSpecialId(String spid){
		_specialId=spid;
	}
	public void setCarteId(int id){
		_carteId=id;
	}

	public String getCarteId(){
		return _specialId == null || _specialId.length() == 0 ? 
				Integer.toString(_carteId) : _specialId;
	}
	
	/**
	 * 
	 * @param principal
	 * @param p
	 * @param rarete
	 */
	public void addQuantite(Context principal, int p, String rarete){
		if(rarete == "normal"){
			_quantite_normal+=p;
			if(_quantite_normal<0)
				_quantite_normal=getQuantite(principal, "normal");
			SGBD dd=new SGBD(principal);
			dd.open();
			dd.updatePossessionCarteExtensionNormal(_extension, _carteId, _quantite_normal);
			dd.close();
		}
		if(rarete == "reverse"){
			_quantite_reverse+=p;
			if(_quantite_reverse<0)
				_quantite_reverse=getQuantite(principal, "revers");
			SGBD dd=new SGBD(principal);
			dd.open();
			dd.updatePossessionCarteExtensionReverse(_extension, _carteId, _quantite_reverse);
			dd.close();
		}
		if(rarete == "holo"){
			_quantite_holo+=p;
			if(_quantite_holo<0)
				_quantite_holo=getQuantite(principal, "holo");
			SGBD dd=new SGBD(principal);
			dd.open();
			dd.updatePossessionCarteExtensionHolo(_extension, _carteId, _quantite_holo);
			dd.close();
		}
	}
	
	public int getQuantite(Context principal, String rarete){
		if(rarete == "normal"){
			if(_quantite_normal==-1){
				SGBD dd=new SGBD(principal);
				dd.open();
				_quantite_normal = dd.getPossessionCarteExtensionNormal(_extension, _carteId);
				dd.close();
			}
			return _quantite_normal;
		}
		if(rarete == "reverse"){
			if(_quantite_reverse==-1){
				SGBD dd=new SGBD(principal);
				dd.open();
				_quantite_reverse = dd.getPossessionCarteExtensionReverse(_extension, _carteId);
				dd.close();
			}
			return _quantite_reverse;
		}
		if(rarete =="holo"){
			if(_quantite_holo==-1){
				SGBD dd=new SGBD(principal);
				dd.open();
				_quantite_holo = dd.getPossessionCarteExtensionHolo(_extension, _carteId);
				dd.close();
			}
			return _quantite_holo;
		}
		return _quantite_reverse;
	}
	
	public void setRarete(String rarete){
		_rarete=rarete;
	}
	public void setNomPkmn(String nompkmn){
		_nomPkmn=nompkmn;
	}
	public String getNomPkmn(){
		return _nomPkmn;
	}
	public void setNom(String nom){
		_nom=nom;
	}

	public String getDrawableString(){
		return "img"+Integer.toString(_extension)+"_"+Integer.toString(_carteId);
	}
	
	public String getDrawableRarete(){
		return "rarete_"+_rarete;
	}
	
	public int getDrawableImage(){
		return _idImage;
	}
	
	public String getNom(){
		return _nom;
	}

	public String getInfos(){
		return getNumero();//((_pv>0)?""+Integer.toString(_pv)+" PV":"");
	}
	
	public boolean getIsHolo(){
		return _isHolo;
	}
	
	public boolean getIsReverse(){
		return _isReverse;
	}
}
