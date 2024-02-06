import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Cotacao } from './cotacao';
import { CotacaoDolarService } from './cotacaodolar.service';
import {
  trigger,
  style,
  transition,
  animate,
} from '@angular/animations';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger(
      'fadeAnimation', [
        transition(':enter', [
          style({
              opacity: 0
            }
          ),
          animate(
            500,
            style({
              opacity: 1,
            })
          )
        ]),
        transition(':leave', [
          animate(
            500,
            style({
              opacity: 0
            })
          )
        ])
      ]
    )
  ]
})
export class AppComponent implements OnInit {

  startDate: string = this.formatarDataParaString(new Date(new Date().getFullYear(), new Date().getMonth(), 1));;
  endDate: string = this.formatarDataParaString(new Date());

  cotacaoAtual = 0;
  cotacaoPorPeriodoLista: Cotacao[] = [];
  switchValue: boolean = false;
  mostrarAlertaDatasEmBranco: boolean = false;
  mostrarAlertaDataInicialMaior: boolean = false;
  mostrarAlertaDataMaiorAtual: boolean = false;

  constructor(
    private cotacaoDolarService: CotacaoDolarService,
    private dateFormat: DatePipe
  ) {}

  public getCotacaoPorPeriodo(
    dataInicialString: string,
    dataFinalString: string
  ): void {
    this.cotacaoPorPeriodoLista = [];
    const dataInicial = this.dateFormat.transform(dataInicialString, "MM-dd-yyyy") || '';
    const dataFinal = this.dateFormat.transform(dataFinalString, "MM-dd-yyyy") || '';

    if (dataInicial && dataFinal) {
      this.verificarDatasValidas(dataInicial, dataFinal);
    } else {
      this.mostrarAlertaDatasEmBranco = true;
    }
  }

  public verificarDatasValidas(dataInicial: string, dataFinal: string): void {
    var data1 = new Date(dataInicial);
    var data2 = new Date(dataFinal);
    var dataAtual = new Date();

    if (dataAtual < data1 || dataAtual < data2 ) {
      this.mostrarAlertaDataMaiorAtual = true
    }
    else if (data1 > data2) {
      this.mostrarAlertaDataInicialMaior = true;
    } else if (data1 <= data2) {
      if (this.switchValue) {
        this.cotacaoDolarService.getCotacaoMenoresAtualPorPeriodo(dataInicial, dataFinal).subscribe(cotacoes => {
          this.cotacaoPorPeriodoLista = cotacoes;
        })
      }
      else{
        this.cotacaoDolarService.getCotacaoPorPeriodoFront(dataInicial, dataFinal).subscribe(cotacoes => {
          this.cotacaoPorPeriodoLista = cotacoes;
        })
      }
    }

  }

  public calcularDiferenca(valor1: number, valor2: number): number {
    const diferenca = valor1 - valor2;
    const diferencaArredondada = parseFloat(diferenca.toFixed(3)); // Arredonda para três casas decimais

    return diferencaArredondada;
  }

  public formatarDataParaString(data: Date): string {
    const ano = data.getFullYear();
    const mes = (data.getMonth() + 1).toString().padStart(2, '0') // acrescenta 1 pois getMounth inicia com 0;
    const dia = data.getDate().toString().padStart(2, '0');

    return `${ano}-${mes}-${dia}`;
  }

  ngOnInit() {
    this.cotacaoDolarService.getCotacaoAtual().subscribe(cotacao => {
      this.cotacaoAtual = cotacao;
    })
  }

  fecharAlertaDatasEmBranco() {
    this.mostrarAlertaDatasEmBranco = false;
  }

  fecharAlertaDataInicialMaior() {
    this.mostrarAlertaDataInicialMaior = false;
  }

  fecharAlertaDataMaiorAtual() {
    this.mostrarAlertaDataMaiorAtual = false;
  }
}
