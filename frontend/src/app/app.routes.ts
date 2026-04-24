import { Routes } from '@angular/router';
import { FournisseurListComponent } from './components/fournisseurs/fournisseur-list/fournisseur-list.component';
import { FournisseurFormComponent } from './components/fournisseurs/fournisseur-form/fournisseur-form.component';
import { FournisseurDetailComponent } from './components/fournisseurs/fournisseur-detail/fournisseur-detail.component';
import { CommandeListComponent } from './components/commandes/commande-list/commande-list.component';
import { CommandeFormComponent } from './components/commandes/commande-form/commande-form.component';
import { EvaluationListComponent } from './components/evaluation/evaluation-list/evaluation-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/fournisseurs', pathMatch: 'full' },
  { path: 'fournisseurs', component: FournisseurListComponent },
  { path: 'fournisseurs/nouveau', component: FournisseurFormComponent },
  { path: 'fournisseurs/:id/edit', component: FournisseurFormComponent },
  { path: 'fournisseurs/:id/detail', component: FournisseurDetailComponent },
  { path: 'commandes', component: CommandeListComponent },
  { path: 'commandes/nouvelle', component: CommandeFormComponent },
  { path: 'commandes/:id/edit', component: CommandeFormComponent },
  { path: 'evaluation', component: EvaluationListComponent },
  { path: '**', redirectTo: '/fournisseurs' }
];