import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Ejercicio } from '../entities/ejercicio';

@Component({
  selector: 'app-formulario-ejercicio',
  templateUrl: './formulario-ejercicio.component.html',
  styleUrls: ['./formulario-ejercicio.component.css']
})
export class FormularioEjercicioComponent {
  accion!: "Añadir" | "Editar";
  ejercicio: Ejercicio = {id: 0, nombre: '',observaciones: '', tipo: '',musculosTrabajados: '',material: '',dificultad: '', descripcion: '', multimedia: []};


  constructor(public modal: NgbActiveModal) { }

  guardarEjercicio(): void {
    this.modal.close(this.ejercicio);
  }


}
