import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommandeService } from '../../../core/services/commande.service';
import { FournisseurService } from '../../../core/services/fournisseur.service';
import { CommandeAchat } from '../../../models/commande.model';
import { Fournisseur } from '../../../models/fournisseur.model';

@Component({
  selector: 'app-commande-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './commande-form.component.html'
})
export class CommandeFormComponent implements OnInit {

  commande: CommandeAchat = {
    fournisseurId: 0, date: '', statut: 'EN_ATTENTE', montant: 0
  };
  fournisseurs: Fournisseur[] = [];
  isEditMode = false;
  isLoading = false;
  id: number | null = null;
  errors: any = {};

  constructor(
    private commandeService: CommandeService,
    private fournisseurService: FournisseurService,
    private route: ActivatedRoute,
    public router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.fournisseurService.getAll().subscribe({
      next: (f) => {
        this.fournisseurs = f;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Erreur chargement fournisseurs:', err)
    });

    this.id = this.route.snapshot.params['id'];
    if (this.id) {
      this.isEditMode = true;
      this.commandeService.getById(this.id).subscribe({
        next: (c) => {
          this.commande = c;
          this.isLoading = false;
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Erreur chargement commande:', err);
          this.isLoading = false;
          this.cdr.markForCheck();
        }
      });
    } else {
      this.isLoading = false;
    }
  }

  save(): void {
    this.errors = {};
    const action = this.isEditMode
      ? this.commandeService.update(this.id!, this.commande)
      : this.commandeService.create(this.commande);

    action.subscribe({
      next: () => this.router.navigate(['/commandes']),
      error: err => { if (err.error?.errors) this.errors = err.error.errors; }
    });
  }
}