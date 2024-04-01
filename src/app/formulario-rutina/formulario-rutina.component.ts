import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Rutina } from '../rutina';

@Component({
  selector: 'app-formulario-rutina',
  templateUrl: './formulario-rutina.component.html',
  styleUrls: ['./formulario-rutina.component.css']
})
export class FormularioRutinaComponent {
  accion!: "Añadir" | "Editar";
  rutina: Rutina = {id: 0, nombre: '', ejercicios: [], descripcion: ''};

  constructor(public modal: NgbActiveModal) { }

  guardarRutina(): void {
    this.modal.close(this.rutina);
  }

}
