import {
  Component,
  Input,
  Output,
  EventEmitter,
  SimpleChanges,
} from '@angular/core';

@Component({
  selector: 'app-custom-alert',
  templateUrl: './custom-alert.component.html',
  styleUrls: ['./custom-alert.component.css'],
})
export class CustomAlertComponent {
  @Input() mensagem: string = '';
  @Input() mostrarAlerta: boolean = false;
  @Output() mostrarAlertaChange: EventEmitter<boolean> =
    new EventEmitter<boolean>();

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.mostrarAlerta) {
      this.fecharAlerta();
    }
  }

  fecharAlerta(): void {
    setTimeout(() => {
      this.mostrarAlerta = false;
      this.mostrarAlertaChange.emit(this.mostrarAlerta);
    }, 3000);
  }
}
