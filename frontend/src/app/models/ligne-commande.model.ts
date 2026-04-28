export interface LigneCommandeAchat {
  id?: number;
  commandeId?: number;
  produit: string;
  quantite: number;
  prixUnitaire: number;
}