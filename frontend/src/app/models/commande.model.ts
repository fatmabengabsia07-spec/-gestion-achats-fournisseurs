export interface CommandeAchat {
  id?: number;
  fournisseurId: number;
  date: string;
  statut: string;
  montant: number;
}