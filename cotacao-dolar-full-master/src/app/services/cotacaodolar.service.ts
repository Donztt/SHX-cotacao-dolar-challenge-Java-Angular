import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Cotacao } from '../models/cotacao';

@Injectable({ providedIn: 'root' })
export class CotacaoDolarService {
  private apiServerUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  public getCotacaoAtual(codigoMoeda :string): Observable<number> {
    return this.http.get<any>(`${this.apiServerUrl}/moeda/atual/${codigoMoeda}`);
  }

  public getCotacaoPorPeriodoFront(
    dataInicial: string,
    dataFinal: string,
    codigoMoeda: string
  ): Observable<Cotacao[]> {
    return this.http.get<Cotacao[]>(`${this.apiServerUrl}/moeda/${dataInicial}&${dataFinal}&${codigoMoeda}`);
  }

  public getCotacaoMenoresAtualPorPeriodo(
    dataInicial: string,
    dataFinal: string,
    codigoMoeda: string
  ): Observable<Cotacao[]> {
    return this.http.get<Cotacao[]>(`${this.apiServerUrl}/moeda/cotacoesMenoresAtual/${dataInicial}&${dataFinal}&${codigoMoeda}`);
  }
}
