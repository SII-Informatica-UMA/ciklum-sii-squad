import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Rutina } from '../rutina';
import { Ejercicio } from '../ejercicio';

@Component({
  selector: 'app-formulario-ejercicio',
  templateUrl: './formulario-ejercicio.component.html',
  styleUrls: ['./formulario-ejercicio.component.css']
})
export class FormularioEjercicioComponent {
  accion!: "Añadir" | "Editar";
  rutina: Rutina = {id: 0, nombre: '', ejercicios: [], descripcion: ''};
  ejercicio: Ejercicio = {id: 0, nombre: '', series: 0, repeticiones: '', descripcion: '', video: '', foto: ''};

  constructor(public modal: NgbActiveModal) { }

  guardarRutina(): void {
    this.modal.close(this.rutina);
  }

  guardarEjercicio(): void {
    this.modal.close(this.ejercicio);
  }
}
