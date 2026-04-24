import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Fournisseur } from '../../models/fournisseur.model';
import { EvaluationDTO } from '../../models/evaluation.model';

@Injectable({ providedIn: 'root' })
export class FournisseurService {

  private url = `${environment.apiUrl}/fournisseurs`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Fournisseur[]> {
    return this.http.get<Fournisseur[]>(this.url);
  }

  getById(id: number): Observable<Fournisseur> {
    return this.http.get<Fournisseur>(`${this.url}/${id}`);
  }

  create(f: Fournisseur): Observable<Fournisseur> {
    return this.http.post<Fournisseur>(this.url, f);
  }

  update(id: number, f: Fournisseur): Observable<Fournisseur> {
    return this.http.put<Fournisseur>(`${this.url}/${id}`, f);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  evaluer(id: number): Observable<EvaluationDTO> {
    return this.http.get<EvaluationDTO>(`${this.url}/${id}/evaluation`);
  }

  comparer(): Observable<EvaluationDTO[]> {
    return this.http.get<EvaluationDTO[]>(`${this.url}/comparaison`);
  }

  search(nom: string): Observable<Fournisseur[]> {
    return this.http.get<Fournisseur[]>(`${this.url}/search?nom=${nom}`);
  }
}