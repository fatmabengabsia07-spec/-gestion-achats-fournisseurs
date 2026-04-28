import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommandeService } from '../../../core/services/commande.service';
import { FournisseurService } from '../../../core/services/fournisseur.service';
import { CommandeAchat } from '../../../models/commande.model';
import { Fournisseur } from '../../../models/fournisseur.model';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-commande-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './commande-list.component.html'
})
export class CommandeListComponent implements OnInit {

  commandes: CommandeAchat[] = [];
  toutesLesCommandes: CommandeAchat[] = []; // ← copie complète toujours en mémoire
  fournisseurs: Fournisseur[] = [];
  filtreStatut = 'Tous'; // ← valeur initiale = "Tous"
  message = '';
  isError = false;

  constructor(
    private commandeService: CommandeService,
    private fournisseurService: FournisseurService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.fournisseurService.getAll().subscribe(f => this.fournisseurs = f);
    this.loadAll();
  }

  loadAll(): void {
    this.commandeService.getAll().subscribe({
      next: data => {
        this.toutesLesCommandes = data;
        this.commandes = data; // ← affiche tout dès le départ
        this.cd.detectChanges();
      },
      error: () => this.showMessage('Erreur de chargement', true)
    });
  }

  filtrer(): void {
    if (this.filtreStatut === 'Tous' || !this.filtreStatut) {
      // Afficher toutes les commandes sans rappel HTTP
      this.commandes = this.toutesLesCommandes;
    } else {
      // Filtrer localement depuis la copie complète
      this.commandes = this.toutesLesCommandes.filter(
        c => c.statut === this.filtreStatut
      );
    }
  }

  getNomFournisseur(id: number): string {
    return this.fournisseurs.find(f => f.id === id)?.nom || '—';
  }

  updateStatut(id: number, statut: string): void {
    this.commandeService.updateStatut(id, statut).subscribe({
      next: () => {
        this.showMessage('Statut mis à jour', false);
        this.loadAll();
      }
    });
  }

  delete(id: number): void {
    if (!confirm('Supprimer cette commande ?')) return;
    this.commandeService.delete(id).subscribe({
      next: () => {
        this.showMessage('Commande supprimée', false);
        this.loadAll();
      },
      error: () => this.showMessage('Erreur suppression', true)
    });
  }

  getStatutClass(s: string): string {
    const map: any = {
      'EN_ATTENTE': 'warning',
      'EN_COURS': 'info',
      'LIVREE': 'success',
      'ANNULEE': 'danger'
    };
    return `badge bg-${map[s] || 'secondary'}`;
  }

  showMessage(msg: string, isError: boolean): void {
    this.message = msg;
    this.isError = isError;
    setTimeout(() => this.message = '', 3000);
  }
}