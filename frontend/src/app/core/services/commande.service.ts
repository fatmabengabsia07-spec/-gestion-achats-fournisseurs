import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CommandeAchat } from '../../models/commande.model';

@Injectable({ providedIn: 'root' })
export class CommandeService {

  private url = `${environment.apiUrl}/commandes`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<CommandeAchat[]> {
    return this.http.get<CommandeAchat[]>(this.url);
  }

  getById(id: number): Observable<CommandeAchat> {
    return this.http.get<CommandeAchat>(`${this.url}/${id}`);
  }

  getByFournisseur(fId: number): Observable<CommandeAchat[]> {
    return this.http.get<CommandeAchat[]>(`${this.url}/fournisseur/${fId}`);
  }

  getByStatut(statut: string): Observable<CommandeAchat[]> {
    return this.http.get<CommandeAchat[]>(`${this.url}/statut/${statut}`);
  }

  create(c: CommandeAchat): Observable<CommandeAchat> {
    return this.http.post<CommandeAchat>(this.url, c);
  }

  update(id: number, c: CommandeAchat): Observable<CommandeAchat> {
    return this.http.put<CommandeAchat>(`${this.url}/${id}`, c);
  }

  updateStatut(id: number, statut: string): Observable<CommandeAchat> {
    return this.http.patch<CommandeAchat>(`${this.url}/${id}/statut?statut=${statut}`, {});
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}