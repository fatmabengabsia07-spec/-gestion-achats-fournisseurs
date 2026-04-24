import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FournisseurService } from '../../../core/services/fournisseur.service';
import { Fournisseur } from '../../../models/fournisseur.model';

@Component({
  selector: 'app-fournisseur-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './fournisseur-form.component.html'
})
export class FournisseurFormComponent implements OnInit {

  fournisseur: Fournisseur = { nom: '', contact: '', qualiteService: 3, note: 5 };
  isEditMode = false;
  isLoading = false;
  id: number | null = null;
  errors: any = {};
  message = '';

  constructor(
    private service: FournisseurService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    if (this.id) {
      this.isEditMode = true;
      this.isLoading = true;
      this.service.getById(this.id).subscribe({
        next: (data) => {
          this.fournisseur = data;
          this.isLoading = false;
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Erreur chargement fournisseur:', err);
          this.isLoading = false;
          this.message = 'Erreur lors du chargement des données';
          this.cdr.markForCheck();
        }
      });
    }
  }

  save(): void {
    this.errors = {};
    const action = this.isEditMode
      ? this.service.update(this.id!, this.fournisseur)
      : this.service.create(this.fournisseur);

    action.subscribe({
      next: () => this.router.navigate(['/fournisseurs']),
      error: err => {
        if (err.error?.errors) this.errors = err.error.errors;
        else this.message = 'Erreur lors de la sauvegarde';
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/fournisseurs']);
  }
}