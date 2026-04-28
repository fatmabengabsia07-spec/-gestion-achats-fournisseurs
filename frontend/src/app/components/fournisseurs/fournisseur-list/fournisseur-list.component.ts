import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { FournisseurService } from '../../../core/services/fournisseur.service';
import { Fournisseur } from '../../../models/fournisseur.model';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-fournisseur-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './fournisseur-list.component.html'
})
export class FournisseurListComponent implements OnInit {

  fournisseurs: Fournisseur[] = [];
  searchNom = '';
  message = '';
  isError = false;

  constructor(private fournisseurService: FournisseurService, private cd: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadAll();
  }

 loadAll(): void {
  this.fournisseurService.getAll().subscribe({
    next: data => {
      this.fournisseurs = data;
      this.cd.detectChanges(); // 🔥 FORCE UPDATE
    }
  });
}

  search(): void {
    if (!this.searchNom.trim()) { this.loadAll(); return; }
    this.fournisseurService.search(this.searchNom).subscribe({
      next: data => this.fournisseurs = data
    });
  }

  delete(id: number): void {
    if (!confirm('Supprimer ce fournisseur ?')) return;
    this.fournisseurService.delete(id).subscribe({
      next: () => {
        this.showMessage('Fournisseur supprimé avec succès', false);
        this.loadAll();
      },
      error: () => this.showMessage('Erreur lors de la suppression', true)
    });
  }

  showMessage(msg: string, isError: boolean): void {
    this.message = msg;
    this.isError = isError;
    setTimeout(() => this.message = '', 3000);
  }

  getStars(note: number): string {
    return '★'.repeat(Math.round(note / 2)) + '☆'.repeat(5 - Math.round(note / 2));
  }
}