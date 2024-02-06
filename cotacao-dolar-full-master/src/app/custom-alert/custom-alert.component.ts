import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-custom-alert',
  templateUrl: './custom-alert.component.html',
  styleUrls: ['./custom-alert.component.css']
})
export class CustomAlertComponent {

  @Input() success: boolean = true;
  @Input() titulo: string = '';
  @Input() mensagem: string = '';
  @Input() mostrarAlerta: boolean = true;
  @Output() fecharAlerta: EventEmitter<void> = new EventEmitter<void>();

}
