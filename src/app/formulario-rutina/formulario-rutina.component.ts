import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Rutina } from '../rutina';
import { Ejercicio } from '../ejercicio'
import { RutinasService } from '../rutina.service';
import { Ejercicio_rutina } from '../ejercicio_rutina';

@Component({
  selector: 'app-formulario-rutina',
  templateUrl: './formulario-rutina.component.html',
  styleUrls: ['./formulario-rutina.component.css']
})
export class FormularioRutinaComponent {
  accion!: "Añadir" | "Editar";
  rutina: Rutina = {id: 0, nombre: '', ejercicios: [], descripcion: '',observaciones: ''};
  ejercicios: Ejercicio_rutina[] = [];

  constructor(public modal: NgbActiveModal, private rutinaService: RutinasService) {
    this.rutinaService.getEjercicios().subscribe(result => {
      for (let i = 0; i < result.length; i++) {
        this.ejercicios.push({series:0,repeticiones:0,duracionMinutos:0,ejercicio: result[i]})
      }
    });
  }

  guardarRutina(): void {
    this.modal.close(this.rutina);
  }

  modificaEjercicio(ejercicio: Ejercicio_rutina): void {
    let idx = this.rutina.ejercicios.findIndex(x => x.ejercicio.id === ejercicio.ejercicio.id);
    if(idx === -1){
      //Añadir ejercicio
      this.rutina.ejercicios.push(ejercicio);
    }else{
      //Eliminar ejercicio
      this.rutina.ejercicios.splice(idx,1);
    }
  }

  buscaEjercicio(ejercicio: Ejercicio_rutina): boolean {
    var res: boolean = this.rutina.ejercicios.find(x => x.ejercicio.id === ejercicio.ejercicio.id) !== undefined;
    return res;
  }
}
