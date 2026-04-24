import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FournisseurService } from '../../../core/services/fournisseur.service';
import { EvaluationDTO } from '../../../models/evaluation.model';

@Component({
  selector: 'app-evaluation-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './evaluation-list.component.html'
})
export class EvaluationListComponent implements OnInit {

  evaluations: EvaluationDTO[] = [];

  constructor(
    private fournisseurService: FournisseurService,
    private cd: ChangeDetectorRef // 🔥 IMPORTANT
  ) {}

  ngOnInit(): void {
    this.loadEvaluations();
  }

  loadEvaluations(): void {
    this.fournisseurService.comparer().subscribe({
      next: data => {
        console.log("EVALUATIONS:", data); // debug

        this.evaluations = data;

        this.cd.detectChanges(); // 🔥 FORCE UPDATE
      },
      error: err => console.error(err)
    });
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

  getBarColor(note: number): string {
    if (note >= 75) return '#198754';
    if (note >= 50) return '#0d6efd';
    if (note >= 25) return '#ffc107';
    return '#dc3545';
  }
}