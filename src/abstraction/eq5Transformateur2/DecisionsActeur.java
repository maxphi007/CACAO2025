// Simon
package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import java.util.HashMap;
import java.util.List;
import javax.crypto.spec.OAEPParameterSpec;
import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.produits.Feve;


public class DecisionsActeur extends ProcessChoco{
    
    private HashMap<Feve, Double> proportion; // proportion de chaque chocolat dans la production totale
    private double objectifChangementProportion;
    

    public DecisionsActeur(){
        super();
        this.proportion = new HashMap<Feve, Double>();
        this.proportion.put(Feve.F_MQ, 0.4);
        this.proportion.put(Feve.F_MQ_E, 0.4);
        this.proportion.put(Feve.F_HQ_E, 0.1);
        this.proportion.put(Feve.F_HQ_BE, 0.1);

        this.objectifChangementProportion = 10.0 *10000000000.00; // augmentation de 10% de notre capitale de départ en euros
    }
    public double getProportion(Feve f){
        return this.proportion.get(f);
    }

    public double getProductionTotale(){
        return Math.min(super.getCapaciteProductionMachines() ,super.capaciteProductionEmployes());
    }
    
    public Feve correspondFeve(Chocolat c){
        if(c==Chocolat.C_MQ){
            return Feve.F_MQ;
        }
        if(c==Chocolat.C_MQ_E){
            return Feve.F_MQ_E;
        }
        if(c==Chocolat.C_HQ_E){
            return Feve.F_HQ_E;
        }
        if(c==Chocolat.C_HQ_BE){
            return Feve.F_HQ_BE;
        }
        return Feve.F_MQ; // default return value or throw an exception if appropriate
    }


    public void ChangementProportion(){
        if(this.proportion.get(Chocolat.C_MQ)>1){
            this.proportion.put(Feve.F_MQ, this.proportion.get(Chocolat.C_MQ)-0.05);
            this.proportion.put(Feve.F_MQ_E, this.proportion.get(Chocolat.C_MQ_E)-0.05);
            this.proportion.put(Feve.F_HQ_E, this.proportion.get(Chocolat.C_HQ_E)+0.05);
            this.proportion.put(Feve.F_HQ_BE, this.proportion.get(Chocolat.C_HQ_BE)+0.05);

        }
    }

    public boolean objecifChangementProportion(){
        if(Filiere.LA_FILIERE.getBanque().getSolde(this,super.cryptogramme)>=this.objectifChangementProportion){
            this.objectifChangementProportion=this.objectifChangementProportion*1.1;
            return true;
        }
        return false;
    }

    public double coutProduction(Chocolat c){ //cout de production d'une tonne de chocolat
        Feve f=correspondFeve(c);
        double coutProduction=0;
        BourseCacao bc =  ((BourseCacao) (Filiere.LA_FILIERE.getActeur("BourseCacao")));
		Double prixBourse= bc.getCours(f).getValeur();

        
        coutProduction+=super.getFraisAditionnel()/this.getProductionTotale();
        coutProduction+=super.getSalaireTotal()/this.getProductionTotale();
        coutProduction+=prixBourse;   // pour l'instant on base le prix de la tonne de fève sur le prix de la bourse
        coutProduction+=super.coutIngredientSecondaire;
        coutProduction+=super.getcoutAditionnelParTablette()*10000; // *10000 pour passer de tonne à unité de tablette de chocolat
        
        return coutProduction;
    }

    public double prixVente(Chocolat c){
        return 1.30*this.coutProduction(c);
    }

    public void next(){
        super.next();

        
        
        
        
        if(objecifChangementProportion()){
            ChangementProportion();
        }
    }



    
}
