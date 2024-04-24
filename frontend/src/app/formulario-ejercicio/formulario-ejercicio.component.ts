import { Component } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Ejercicio } from '../entities/ejercicio';
import { EjercicioService } from '../services/ejercicio.service';

@Component({
  selector: 'app-formulario-ejercicio',
  templateUrl: './formulario-ejercicio.component.html',
  styleUrls: ['./formulario-ejercicio.component.css']
})
export class FormularioEjercicioComponent {
  accion!: "Añadir" | "Editar";
  ejercicio: Ejercicio = {id: 0, nombre: '',observaciones: '', tipo: '',musculosTrabajados: '',material: '',dificultad: '', descripcion: '', multimedia: []};


  constructor(public modal: NgbActiveModal, private ejercicioService: EjercicioService, private modalService: NgbModal) {
    this.ejercicioService.getEjercicios().subscribe(result => {
      for (let i = 0; i < result.length; i++) {
        this.ejercicio = {...result[i]};
      }
    });
  }

  guardarEjercicio(): void {
    this.modal.close(this.ejercicio);
  }


}
