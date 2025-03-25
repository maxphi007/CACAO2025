package abstraction.eq1Producteur1;

import abstraction.eq1Producteur1.moyenne_qualite;
import abstraction.eq1Producteur1.basse_qualite;
import abstraction.eq1Producteur1.haute_qualite;

public class Stock {
    private double stockFMQ; // Stock de fève de moyenne qualité
    private double stockFBQ; // Stock de fève de bonne qualité
    private double stockFHQ; // Stock de fève de haute qualité

    // Constructeur qui initialise les stocks à zéro
    public Stock() {
        this.stockFMQ = basse_qualite.nombre_feves_total();
        this.stockFBQ = moyenne_qualite.nombre_feves_total();
        this.stockFHQ = haute_qualite.nombre_feves_total();
    }

    // Méthode pour ajouter des fèves à chaque type de stock
    public void ajouterStock(double ajoutFMQ, double ajoutFBQ, double ajoutFHQ) {
        this.stockFMQ += ajoutFMQ;
        this.stockFBQ += ajoutFBQ;
        this.stockFHQ += ajoutFHQ;
    }

    // Méthode pour retirer des fèves de moyenne qualité (FMQ)
    public boolean vendreStockFMQ(double quantite) {
        if (this.stockFMQ >= quantite) {
            this.stockFMQ -= quantite;
            return true;
        }
        return false;
    }

    

    // Méthode pour calculer et retourner le stock total
    public double getStockTotal() {
        return this.stockFMQ + this.stockFBQ + this.stockFHQ;
    }

    // Getters pour accéder aux stocks de chaque type de fève
    public double getStockFMQ() {
        return stockFMQ;
    }

    public double getStockFBQ() {
        return stockFBQ;
    }

    public double getStockFHQ() {
        return stockFHQ;
    }
}

