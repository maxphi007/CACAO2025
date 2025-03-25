/**
 * @author tidzzz 
 */

package abstraction.eq8Distributeur2;

import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.clients.ClientFinal;

import abstraction.eqXRomu.general.Journal;





public class Distributeur2Vendeur extends Distributeur2Acteur implements IDistributeurChocolatDeMarque {
    
    
    protected double capaciteDeVente;
	protected  HashMap<ChocolatDeMarque, Double> ListPrix;
	protected String[] marques;
	protected Journal journalVente;

	protected HashMap<String,Double> Coefficient;
	protected LinkedList<String> equipe;
	
	protected HashMap<ChocolatDeMarque,Integer> aVendu;


	public Distributeur2Vendeur() {
		super();
		this.capaciteDeVente=120000.0;  //capacite de vente par step
		this.ListPrix = new HashMap<ChocolatDeMarque, Double>();
		this.marques = new String[chocolats.size()];
		this.journalVente= new Journal ("journal des ventes", this);
		
		
		this.equipe = new LinkedList<String>();
		
		this.aVendu = new HashMap<ChocolatDeMarque,Integer>();
	}

	public void initialiser () {
		super.initialiser();
		for (ChocolatDeMarque choco : chocolats) {
			this.setPrix(choco);
		}
		
		
		this.equipe.add("EQ4");
		this.equipe.add("EQ5");
		this.equipe.add("EQ6");
		
		
		for (ChocolatDeMarque choc : chocolats) {
			this.aVendu.putIfAbsent(choc, 0);
		}
	}


public void setPrix(ChocolatDeMarque choco) {

	if (choco.getChocolat() == Chocolat.C_MQ_E) {
		ListPrix.put(choco, (double) 10000);
	}
	

	if (choco.getChocolat() == Chocolat.C_HQ_E) {
		ListPrix.put(choco, (double) 22000);
	}
	if (choco.getChocolat() == Chocolat.C_HQ_BE) {
		ListPrix.put(choco, (double) 30000);
	}

}




    public double prix(ChocolatDeMarque cm){
        if (ListPrix.containsKey(cm)) {
			return ListPrix.get(cm);
		} 
		else { 
			return 0;
		}
    }

    
    public double quantiteEnVente(ChocolatDeMarque choco, int crypto){
        if (crypto!=this.cryptogramme || !chocolats.contains(choco)) {
			journalVente.ajouter("Quelqu'un essaye de me pirater !");
			return 0.0;
		} 
		else {
			
			if (choco.toString().contains("C_MQ_E")) {
				double x = (capaciteDeVente*0.30);
				return Math.max(Math.min(x , this.getQuantiteEnStock(choco,crypto)),0.0);
			}
			
			if (choco.toString().contains("C_HQ_BE")) {
				double x = (capaciteDeVente*0.30);
				return Math.max(Math.min(x , this.getQuantiteEnStock(choco,crypto)),0.0);
			}
			if (choco.toString().contains("C_HQ_E")) {
				double x = (capaciteDeVente*0.40);
				return Math.max(Math.min(x , this.getQuantiteEnStock(choco,crypto)),0.0);
			}
			
		}
		return 0.0;
	}

	public double quantiteEnVenteTotal(){
		double qte = 0;

		for (ChocolatDeMarque cm :this.chocolats){
			qte = qte + this.quantiteEnVente(cm, cryptogramme);
		}
		return qte;
	}


	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto){
        if (crypto!=this.cryptogramme) {
			double capaciteDeVenteTG = this.quantiteEnVenteTotal() * ClientFinal.POURCENTAGE_MAX_EN_TG;
			

			if(choco.getChocolat() == Chocolat.C_HQ_E){
				return (0.3 * capaciteDeVenteTG);
			}

			if(choco.getChocolat() == Chocolat.C_HQ_BE){
				return (0.7 * capaciteDeVenteTG);
			}
			
			return 0.0;
		} 
		else {return 0.0;}
    }

	public double quantiteEnVenteTGTotal(){
		double qte = 0;

		for (ChocolatDeMarque cm :this.chocolats){
			qte = qte + this.quantiteEnVenteTG(cm, cryptogramme);
		}
		return qte;
	}



    public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		int pos = (chocolats.indexOf(choco));
		if (pos>=0) {
			stock_Choco.put(choco, this.getQuantiteEnStock(choco,crypto) - quantite) ;
			stockTotal.retirer(this, quantite, cryptogramme);
			this.aVendu.replace(choco, 1);
		}
		journalVente.ajouter(client.getNom()+" a acheté "+quantite+"kg de "+choco+" pour "+montant+" d'euros ");

	}


    public void notificationRayonVide(ChocolatDeMarque choco, int crypto){
        journalVente.ajouter("J'aurais du mettre davantage de " + choco.getNom() + " en vente");
    }


	public List<Journal> getJournaux() {
		
		List<Journal> jour = super.getJournaux();
		jour.add(this.journalVente);
		return jour;
	}



	public void next() {
		super.next();
		journalVente.ajouter("");
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,"==================== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,"QuantitéEnVenteTotal à l'Etape "+Filiere.LA_FILIERE.getEtape()+" : " +this.quantiteEnVenteTotal());
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,"QuantitéEnVenteTGTotal à l'Etape "+Filiere.LA_FILIERE.getEtape()+" : "+this.quantiteEnVenteTGTotal());
		journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,"=================================");
		journalVente.ajouter("");
		for (ChocolatDeMarque choco : chocolats) {
			journalVente.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_PURPLE,"prix de vente pour le chocolats "+choco+" est de : "+this.prix(choco));
		}
		
		for (int i=0;i<this.ListPrix.size(); i++) {
			this.setPrix(chocolats.get(i));
		}
		
		
	}

}
