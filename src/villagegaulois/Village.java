package villagegaulois;

import com.sun.net.httpserver.Filter.Chain;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbrEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.marche = new Marche(nbrEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String afficherMarche() {
		StringBuilder chaine = new StringBuilder();
		if (this.marche == null) {
			chaine.append("Le village \"" + this.nom + "\" ne possède pas d'étals : \n");
		} else {
			chaine.append("Le marché du village \"" + this.nom + "\" possède plusieurs étals : \n");
			chaine.append(marche.afficherMarche());
		}
		return chaine.toString();
	}

	public String installerVendeur(Gaulois gaulois, String produit, int nbrProduit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append(gaulois.getNom() + " cherche un endroit pour vendre " + nbrProduit + " " + produit + ".\n");
		int indiceEtalLibre = marche.trouverEtalLibre();
		if (indiceEtalLibre == -1) {
			chaine.append("Il n'y a pas d'étal libre");
		} else {
			marche.utiliserEtal(indiceEtalLibre, gaulois, produit, nbrProduit);
			chaine.append("Le vendeur " + gaulois.getNom() + " vend des " + produit + " à l'étal n°" + (indiceEtalLibre+1) + ".\n");
		}
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] listEtal = marche.trouverEtals(produit);
		if (listEtal == null) {
			chaine.append("Aucun vendeur ne propose de " + produit + ".");
		} else {
			chaine.append("Les vendeurs qui proposent des fleurs sont :\n");
			for (Etal etal : listEtal) {
				if (etal != null) {
					chaine.append("- " + etal.getVendeur().getNom() + "\n");
				}
			}
		}
		return chaine.toString();
	}

	public Etal rechercherEtal(Gaulois gaulois) {
		return marche.trouverVendeur(gaulois);
	}

	public String partirVendeur(Gaulois vendeur) {
		return rechercherEtal(vendeur).libererEtal();
	}
	
	
	class Marche {
		private Etal[] etals;
		
		public Marche(int nbrEtals) {
			etals = new Etal[nbrEtals];
			for (int i=0; i<nbrEtals; i++) {
				etals[i] = new Etal();
			}
		}
		
		public void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbrProduit) {
			this.etals[indiceEtal].occuperEtal(vendeur, produit, nbrProduit);
		}
		
		public int trouverEtalLibre() {
			int i=0;
			for (Etal etal : etals) {
				if (!etal.isEtalOccupe()) {
					return i;
				}
				i++;
			}
			return -1;
		}
		
		public Etal[] trouverEtals(String produit) {
			Etal[] listEtal = new Etal[etals.length];
			int i=0;
			for (Etal etal : etals) {
				if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
					listEtal[i]=etal;
					i++;
				}
			}
			return listEtal;
		}
		
		public Etal trouverVendeur(Gaulois gaulois) {
			for (Etal etal : etals) {
				if (etal.getVendeur() == gaulois) {
					return etal;				
				}
			}
			return null;
		}
		
		public String afficherMarche() {
			int nbrEtalVides = 0;
			StringBuilder chaine = new StringBuilder();
			for (Etal etal : etals) {
				if (!etal.isEtalOccupe()) {
					nbrEtalVides++;
				} else {
					chaine.append(etal.afficherEtal());
				}
			}
			chaine.append("Il reste " + nbrEtalVides + " étals non utilisés dans le marché.\n");
			return chaine.toString();
		}
	}
}