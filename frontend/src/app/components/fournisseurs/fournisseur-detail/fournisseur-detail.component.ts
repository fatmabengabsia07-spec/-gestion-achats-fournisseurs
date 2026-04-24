import { Component, OnInit, OnDestroy, NgZone, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';

import { FournisseurService } from '../../../core/services/fournisseur.service';
import { CommandeService } from '../../../core/services/commande.service';
import { HistoriqueService } from '../../../core/services/historique.service';

import { Fournisseur } from '../../../models/fournisseur.model';
import { CommandeAchat } from '../../../models/commande.model';
import { HistoriqueAchats } from '../../../models/historique.model';
import { EvaluationDTO } from '../../../models/evaluation.model';

@Component({
  selector: 'app-fournisseur-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './fournisseur-detail.component.html'
})
export class FournisseurDetailComponent implements OnInit, OnDestroy {

  fournisseur: Fournisseur | null = null;
  commandes: CommandeAchat[] = [];
  historiques: HistoriqueAchats[] = [];
  evaluation: EvaluationDTO | null = null;

  id!: number;
  isLoading = true;

  newHistorique: HistoriqueAchats = {
    produit: '',
    quantite: 1,
    delaiLivraison: 0,
    dateAchat: this.getTodayDate()
  };

  showHistoriqueForm = false;
  message = '';
  isError = false;

  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fournisseurService: FournisseurService,
    private commandeService: CommandeService,
    private historiqueService: HistoriqueService,
    private ngZone: NgZone,
    private cdr: ChangeDetectorRef
  ) {}

  // ✅ APPROCHE SIMPLE ET DIRECTE
  ngOnInit(): void {
    console.log('🔴 ngOnInit appelé');
    
    this.route.paramMap
      .pipe(takeUntil(this.destroy$))
      .subscribe(paramMap => {
        console.log('📍 ParamMap changé:', paramMap.get('id'));
        this.id = Number(paramMap.get('id'));
        
        if (this.id) {
          console.log('🆔 Chargement ID:', this.id);
          this.loadDetail();
        }
      });
  }

  loadDetail(): void {
    this.isLoading = true;
    this.fournisseur = null;
    this.commandes = [];
    this.historiques = [];
    this.evaluation = null;
    console.log('♻️ État réinitialisé - fetching data for ID:', this.id);

    // Charger le fournisseur
    this.fournisseurService.getById(this.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (f) => {
          console.log('✅ Fournisseur chargé:', f);
          this.ngZone.run(() => {
            this.fournisseur = f;
            this.isLoading = false;
            console.log('✅ isLoading = false');
            this.cdr.markForCheck();
          });
          // Charger les données supplémentaires
          this.loadCommandes();
          this.loadHistoriques();
          this.loadEvaluation();
        },
        error: (err) => {
          console.error('❌ Erreur - getById:', err);
          this.ngZone.run(() => {
            this.isLoading = false;
            this.fournisseur = {
              id: this.id,
              nom: 'Fournisseur #' + this.id,
              contact: '—',
              qualiteService: 0,
              note: 0
            };
            this.cdr.markForCheck();
          });
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCommandes(): void {
    this.commandeService.getByFournisseur(this.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (c) => {
          this.ngZone.run(() => {
            this.commandes = c;
            this.cdr.markForCheck();
          });
        },
        error: () => {
          this.ngZone.run(() => {
            this.commandes = [];
            this.cdr.markForCheck();
          });
        }
      });
  }

  loadHistoriques(): void {
    this.historiqueService.getByFournisseur(this.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (h) => {
          this.ngZone.run(() => {
            this.historiques = h;
            this.cdr.markForCheck();
          });
        },
        error: () => {
          this.ngZone.run(() => {
            this.historiques = [];
            this.cdr.markForCheck();
          });
        }
      });
  }

  loadEvaluation(): void {
    this.fournisseurService.evaluer(this.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (e) => {
          this.ngZone.run(() => {
            this.evaluation = e;
            this.cdr.markForCheck();
          });
        },
        error: () => {
          this.ngZone.run(() => {
            this.evaluation = null;
            this.cdr.markForCheck();
          });
        }
      });
  }

  addHistorique(): void {
    if (!this.newHistorique.produit.trim()) {
      this.showMessage('Le produit est obligatoire', true);
      return;
    }

    this.historiqueService.create(this.id, this.newHistorique)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.showMessage('Historique ajouté avec succès', false);
            this.showHistoriqueForm = false;
            this.newHistorique = { produit: '', quantite: 1, delaiLivraison: 0, dateAchat: this.getTodayDate() };
            this.cdr.markForCheck();
          });
          this.loadHistoriques();
        },
        error: () => this.showMessage('Erreur lors de l\'ajout', true)
      });
  }

  updateStatut(id: number, statut: string): void {
    this.commandeService.updateStatut(id, statut)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.ngZone.run(() => {
            this.showMessage('Statut mis à jour', false);
            this.cdr.markForCheck();
          });
          this.loadCommandes();
        },
        error: () => this.showMessage('Erreur mise à jour statut', true)
      });
  }

  getStatutClass(statut: string): string {
    const map: any = {
      'EN_ATTENTE': 'warning',
      'EN_COURS': 'info',
      'LIVREE': 'success',
      'ANNULEE': 'danger'
    };
    return `badge bg-${map[statut] || 'secondary'}`;
  }

  getAppreciationClass(a: string): string {
    const map: any = {
      'Excellent': 'badge-excellent',
      'Bon': 'badge-bon',
      'Moyen': 'badge-moyen',
      'Insuffisant': 'badge-insuffisant'
    };
    return `badge ${map[a] || 'bg-secondary'}`;
  }

  showMessage(msg: string, isError: boolean): void {
    this.message = msg;
    this.isError = isError;
    setTimeout(() => this.message = '', 3000);
  }

  getTodayDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}