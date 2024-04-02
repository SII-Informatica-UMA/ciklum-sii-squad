import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Rutina } from '../rutina';
import { RutinasService } from '../rutina.service';
import { Ejercicio } from '../ejercicio';

@Component({
  selector: 'app-formulario-rutina',
  templateUrl: './formulario-rutina.component.html',
  styleUrls: ['./formulario-rutina.component.css']
})
export class FormularioRutinaComponent {
  accion!: "Añadir" | "Editar";
  rutina: Rutina = {id: 0, nombre: '', ejercicios: [], descripcion: '',observaciones: ''};
  ejercicios: Ejercicio [] = this.rutinaService.getEjercicios();
  constructor(public modal: NgbActiveModal, private rutinaService: RutinasService) { }

  guardarRutina(): void {
    this.modal.close(this.rutina);
  }

  modificaEjercicio(ejercicio: Ejercicio): void {
    let idx = this.rutina.ejercicios.findIndex(x => x.id === ejercicio.id);
    if(idx === -1){
      //Añadir ejercicio
      this.rutina.ejercicios.push(ejercicio);
    }else{
      //Eliminar ejercicio
      this.rutina.ejercicios.splice(idx,1);
    }
  }

  buscaEjercicio(ejercicio: Ejercicio): boolean {
    var res: boolean = this.rutina.ejercicios.find(x => x.id === ejercicio.id) !== undefined;
    return res;
  }
}
