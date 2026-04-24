export interface HistoriqueAchats {
  id?: number;
  fournisseurId?: number;
  produit: string;
  quantite: number;
  delaiLivraison: number;
  dateAchat?: string;
}