import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { HistoriqueAchats } from '../../models/historique.model';

@Injectable({ providedIn: 'root' })
export class HistoriqueService {

  private url = `${environment.apiUrl}/historiques`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<HistoriqueAchats[]> {
    return this.http.get<HistoriqueAchats[]>(this.url);
  }

  getByFournisseur(fId: number): Observable<HistoriqueAchats[]> {
    return this.http.get<HistoriqueAchats[]>(`${this.url}/fournisseur/${fId}`);
  }

  create(fId: number, h: HistoriqueAchats): Observable<HistoriqueAchats> {
    return this.http.post<HistoriqueAchats>(`${this.url}/fournisseur/${fId}`, h);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}